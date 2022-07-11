package com.ute.bookstoreonlinebe.controllers;

import com.ute.bookstoreonlinebe.dtos.payment.PaymentDto;
import com.ute.bookstoreonlinebe.dtos.payment.PaymentResDto;
import com.ute.bookstoreonlinebe.dtos.payment.TransactionStatusDto;
import com.ute.bookstoreonlinebe.repositories.TransactionsHistoryRepository;
import com.ute.bookstoreonlinebe.services.order.OrderService;
import com.ute.bookstoreonlinebe.services.payments.vnpay.VNPayService;
import com.ute.bookstoreonlinebe.services.user.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/rest/paymnet")
public class PaymentController {
    private UserService userService;

    private OrderService orderService;

    private TransactionsHistoryRepository historyRepository;

    private VNPayService vnPayService;

    public PaymentController(UserService userService, OrderService orderService
            , TransactionsHistoryRepository historyRepository, VNPayService vnPayService) {
        this.userService = userService;
        this.orderService = orderService;
        this.historyRepository = historyRepository;
        this.vnPayService = vnPayService;
    }

    @ApiOperation(value = "Tạo thanh toán với vnpay")
    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("/create-paymnet/vnpay")
    public ResponseEntity<?> createPayment(
            @RequestBody PaymentDto paymentDto, Principal principal) throws IOException {
        PaymentResDto result = vnPayService.createPayment(paymentDto, principal);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    @ApiOperation(value = "Thông tin thanh toán sau khi thanh toán thành công")
    @GetMapping("/thong-tin-thanh-toan")
    public ResponseEntity<?> transactionHandle(
            @RequestParam(value = "vnp_Amount", required = false) String amount,
            @RequestParam(value = "vnp_BankCode", required = false) String bankCode,
            @RequestParam(value = "vnp_BankTranNo", required = false) String bankTranNo,
            @RequestParam(value = "vnp_CardType", required = false) String cardType,
            @RequestParam(value = "vnp_OrderInfo", required = false) String orderInfo,
            @RequestParam(value = "vnp_PayDate", required = false) String payDate,
            @RequestParam(value = "vnp_ResponseCode", required = false) String responseCode,
            @RequestParam(value = "vnp_TmnCode", required = false) String tmnCode,
            @RequestParam(value = "vnp_TransactionNo", required = false) String transactionNo,
            @RequestParam(value = "vnp_TxnRef", required = false) String txnRef,
            @RequestParam(value = "vnp_SecureHashType", required = false) String secureHashType,
            @RequestParam(value = "vnp_SecureHash", required = false) String secureHash,
            Principal principal

    ) throws MessagingException {
        TransactionStatusDto result = vnPayService
                .transactionHandle(amount, bankCode, bankTranNo, cardType, orderInfo, payDate, responseCode
                                , tmnCode, transactionNo, txnRef, secureHashType, secureHash, principal);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

//    @ApiOperation(value = "Tạo thanh toán với vnpay")
//    @PreAuthorize("hasRole('MEMBER')")
//    @PostMapping("/create-paymnet/vnpay")
//    public ResponseEntity<?> createPayment(
//            @RequestBody PaymentDto paymentDto, Principal principal) throws IOException {
//        User user = userService.getUser(principal);
//        Order order = orderService.getOrderById(paymentDto.getOrderId());
//
//        int amount = paymentDto.getAmount() * 100;
//        Map<String, String> vnp_params = new HashMap<>();
//
//        Map vnp_Params = new HashMap<>();
//        vnp_Params.put("vnp_Version", VNPay.vnp_Version);
//        vnp_Params.put("vnp_Command", VNPay.vnp_Command);
//        vnp_Params.put("vnp_TmnCode", VNPay.vnp_TmnCode);
//        vnp_Params.put("vnp_Amount", String.valueOf(amount));
//        vnp_Params.put("vnp_CurrCode", VNPay.vnp_CurrCode);
//        String bank_code = paymentDto.getBankCode();
//        if (bank_code != null && !bank_code.isEmpty()) {
//            vnp_Params.put("vnp_BankCode", bank_code);
//        }
//        vnp_Params.put("vnp_TxnRef", order.getId());
//        vnp_Params.put("vnp_OrderInfo", paymentDto.getDescription());
//        vnp_Params.put("vnp_OrderType", VNPay.vnp_OrderType);
//
//        vnp_Params.put("vnp_Locale", VNPay.vnp_Locale);
//
//        vnp_Params.put("vnp_ReturnUrl", VNPay.vnp_Returnurl);
//        vnp_Params.put("vnp_IpAddr", VNPay.vnp_IpAddr);
//        //vnp_Params.put("vnp_SecureHash", VNPay.vnp_HashSecret);
//        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        String vnp_CreateDate = formatter.format(cld.getTime());
//
//        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
//        cld.add(Calendar.MINUTE, 15);
//        String vnp_ExpireDate = formatter.format(cld.getTime());
//        //Add Params of 2.1.0 Version
//        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
//
//        //Build data to hash and querystring
//        List fieldNames = new ArrayList(vnp_Params.keySet());
//        Collections.sort(fieldNames);
//        StringBuilder hashData = new StringBuilder();
//        StringBuilder query = new StringBuilder();
//        Iterator itr = fieldNames.iterator();
//        while (itr.hasNext()) {
//            String fieldName = (String) itr.next();
//            String fieldValue = (String) vnp_Params.get(fieldName);
//            if ((fieldValue != null) && (fieldValue.length() > 0)) {
//                //Build hash data
//                hashData.append(fieldName);
//                hashData.append('=');
//                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                //Build query
//                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
//                query.append('=');
//                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                if (itr.hasNext()) {
//                    query.append('&');
//                    hashData.append('&');
//                }
//            }
//        }
//        String queryUrl = query.toString();
//        String vnp_SecureHash = VNPay.hmacSHA512(VNPay.vnp_HashSecret, hashData.toString());
//        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
//        String paymentUrl = VNPay.vnp_PayUrl + "?" + queryUrl;
//
//        PaymentResDto result = new PaymentResDto();
//        result.setStatus("00");
//        result.setMessage("success");
//        result.setUrl(paymentUrl);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
//    }
//    @ApiOperation(value = "Thông tin thanh toán sau khi thanh toán thành công")
//    @GetMapping("/thong-tin-thanh-toan")
//    public ResponseEntity<?> transactionHandle(
//          @RequestParam(value = "vnp_Amount", required = false) String amount,
//          @RequestParam(value = "vnp_BankCode", required = false) String bankCode,
//          @RequestParam(value = "vnp_BankTranNo", required = false) String bankTranNo,
//          @RequestParam(value = "vnp_CardType", required = false) String cardType,
//          @RequestParam(value = "vnp_OrderInfo", required = false) String orderInfo,
//          @RequestParam(value = "vnp_PayDate", required = false) String payDate,
//          @RequestParam(value = "vnp_ResponseCode", required = false) String responseCode,
//          @RequestParam(value = "vnp_TmnCode", required = false) String tmnCode,
//          @RequestParam(value = "vnp_TransactionNo", required = false) String transactionNo,
//          @RequestParam(value = "vnp_TxnRef", required = false) String txnRef,
//          @RequestParam(value = "vnp_SecureHashType", required = false) String secureHashType,
//          @RequestParam(value = "vnp_SecureHash", required = false) String secureHash,
//          Principal principal
//
//    ) throws MessagingException {
//        TransactionStatusDto result = new TransactionStatusDto();
//        if (!responseCode.equalsIgnoreCase("00")){
//            result.setStatus("02");
//            result.setMessage("Failed");
//            result.setData(null);
//            return ResponseEntity.status(HttpStatus.OK).body(result);
//        }
//
//        //User user = userService.getUser(principal);
//        Order order = orderService.getOrderByIdForPayment(txnRef);
//        if (order == null){
//            result.setStatus("11");
//            result.setMessage("Order does not exist");
//            result.setData(null);
//            return ResponseEntity.status(HttpStatus.OK).body(result);
//        }
//        TransactionsHistory history = new TransactionsHistory();
//        history.setAmount(amount);
//        history.setBankCode(bankCode);
//        history.setBankTranNo(bankTranNo);
//        history.setCardType(cardType);
//        history.setOrderInfo(orderInfo);
//        history.setPayDate(payDate);
//        history.setResponseCode(responseCode);
//        history.setTmnCode(tmnCode);
//        history.setTransactionNo(transactionNo);
//        history.setTxnRef(txnRef);
//
//        history.setSecureHashType(secureHashType);
//        history.setSecureHash(secureHash);
//
//        order.setPay(true);
//        history.setOrder(order);
//        orderService.save(order);
//        historyRepository.save(history);
//        result.setStatus("00");
//        result.setMessage("success");
//        result.setData(order.toString());
//
//        return new ResponseEntity<>(order, HttpStatus.OK);
//    }
}

//vnp_Amount=1000000
//
//&vnp_BankCode=NCB
//
//&vnp_BankTranNo=20170829152730
//&vnp_CardType=ATM
//&vnp_OrderInfo=Thanh+toan+don+hang+thoi+gian%3A+2017-08-29+15%3A27%3A02
//&vnp_PayDate=20170829153052
//&vnp_ResponseCode=00
//&vnp_TmnCode=2QXUI4J4
//&vnp_TransactionNo=12996460
//&vnp_TxnRef=23597
//&vnp_SecureHashType=SHA256
//&vnp_SecureHash=20081f0ee1cc6b524e273b6d4050fefd
