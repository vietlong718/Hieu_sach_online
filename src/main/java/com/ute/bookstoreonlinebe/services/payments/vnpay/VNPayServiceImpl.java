package com.ute.bookstoreonlinebe.services.payments.vnpay;

import com.ute.bookstoreonlinebe.dtos.payment.PaymentDto;
import com.ute.bookstoreonlinebe.dtos.payment.PaymentResDto;
import com.ute.bookstoreonlinebe.dtos.payment.TransactionStatusDto;
import com.ute.bookstoreonlinebe.entities.Order;
import com.ute.bookstoreonlinebe.entities.TransactionsHistory;
import com.ute.bookstoreonlinebe.entities.User;
import com.ute.bookstoreonlinebe.models.VNPay;
import com.ute.bookstoreonlinebe.repositories.TransactionsHistoryRepository;
import com.ute.bookstoreonlinebe.services.order.OrderService;
import com.ute.bookstoreonlinebe.services.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class VNPayServiceImpl implements VNPayService{
    private UserService userService;

    private OrderService orderService;

    private TransactionsHistoryRepository historyRepository;

    public VNPayServiceImpl(UserService userService, OrderService orderService, TransactionsHistoryRepository historyRepository) {
        this.userService = userService;
        this.orderService = orderService;
        this.historyRepository = historyRepository;
    }

    @Override
    public PaymentResDto createPayment(PaymentDto paymentDto, Principal principal) throws IOException {
        User user = userService.getUser(principal);
        Order order = orderService.getOrderById(paymentDto.getOrderId());

        int amount = paymentDto.getAmount() * 100;
        Map<String, String> vnp_params = new HashMap<>();

        Map vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPay.vnp_Version);
        vnp_Params.put("vnp_Command", VNPay.vnp_Command);
        vnp_Params.put("vnp_TmnCode", VNPay.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", VNPay.vnp_CurrCode);
        String bank_code = paymentDto.getBankCode();
        if (bank_code != null && !bank_code.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bank_code);
        }
        vnp_Params.put("vnp_TxnRef", order.getId());
        vnp_Params.put("vnp_OrderInfo", paymentDto.getDescription());
        vnp_Params.put("vnp_OrderType", VNPay.vnp_OrderType);

        vnp_Params.put("vnp_Locale", VNPay.vnp_Locale);

        vnp_Params.put("vnp_ReturnUrl", VNPay.vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", VNPay.vnp_IpAddr);
        //vnp_Params.put("vnp_SecureHash", VNPay.vnp_HashSecret);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        //Add Params of 2.1.0 Version
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        //Build data to hash and querystring
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPay.hmacSHA512(VNPay.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPay.vnp_PayUrl + "?" + queryUrl;

        PaymentResDto result = new PaymentResDto();
        result.setStatus("00");
        result.setMessage("success");
        result.setUrl(paymentUrl);
        return result;
    }

    @Override
    public TransactionStatusDto transactionHandle(String amount, String bankCode, String bankTranNo, String cardType, String orderInfo, String payDate, String responseCode, String tmnCode, String transactionNo, String txnRef, String secureHashType, String secureHash, Principal principal) {
        TransactionStatusDto result = new TransactionStatusDto();
        if (!responseCode.equalsIgnoreCase("00")){
            result.setStatus("02");
            result.setMessage("Failed");
            result.setData(null);
            return result;
        }

        //User user = userService.getUser(principal);
        Order order = orderService.getOrderByIdForPayment(txnRef);
        if (order == null){
            result.setStatus("11");
            result.setMessage("Order does not exist");
            result.setData(null);
            return result;
        }
        TransactionsHistory history = new TransactionsHistory();
        history.setAmount(amount);
        history.setBankCode(bankCode);
        history.setBankTranNo(bankTranNo);
        history.setCardType(cardType);
        history.setOrderInfo(orderInfo);
        history.setPayDate(payDate);
        history.setResponseCode(responseCode);
        history.setTmnCode(tmnCode);
        history.setTransactionNo(transactionNo);
        history.setTxnRef(txnRef);

        history.setSecureHashType(secureHashType);
        history.setSecureHash(secureHash);

        order.setPay(true);
        history.setOrder(order);
        orderService.save(order);
        historyRepository.save(history);
        result.setStatus("00");
        result.setMessage("success");
        result.setData(order.toString());
        return result;
    }
}
