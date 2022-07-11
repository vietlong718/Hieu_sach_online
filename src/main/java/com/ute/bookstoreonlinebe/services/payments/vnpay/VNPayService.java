package com.ute.bookstoreonlinebe.services.payments.vnpay;

import com.ute.bookstoreonlinebe.dtos.payment.PaymentDto;
import com.ute.bookstoreonlinebe.dtos.payment.PaymentResDto;
import com.ute.bookstoreonlinebe.dtos.payment.TransactionStatusDto;

import java.io.IOException;
import java.security.Principal;

public interface VNPayService {
    PaymentResDto createPayment(PaymentDto paymentDto, Principal principal) throws IOException;
    TransactionStatusDto transactionHandle (
            String amount, String bankCode, String bankTranNo,
            String cardType, String orderInfo, String payDate,
            String responseCode, String tmnCode, String transactionNo,
            String txnRef, String secureHashType, String secureHash, Principal principal);
}
