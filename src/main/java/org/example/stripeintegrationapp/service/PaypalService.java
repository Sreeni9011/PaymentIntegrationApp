package org.example.stripeintegrationapp.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.example.stripeintegrationapp.util.PayPalRequest;
import org.example.stripeintegrationapp.util.PaymentRespone;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PaypalService {
    private final APIContext apiContext;

    public Payment createPayment(PayPalRequest request) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(request.getCurrency());
        amount.setTotal(String.format(Locale.forLanguageTag(request.getCurrency()), "%.2f", request.getTotal()));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(request.getDescription());
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(request.getMethod());
        Payment payment = new Payment();
        payment.setIntent(request.getIntent());
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(request.getCancelUrl());
        redirectUrls.setReturnUrl(request.getSuccessUrl());
        payment.setRedirectUrls(redirectUrls);
        return payment.create(apiContext);
    }

    public Payment excutePayment(String paymentId, String payerId ) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution execution = new PaymentExecution();
        execution.setPayerId(payerId);

        return payment.execute(apiContext, execution);
    }

    public PaymentRespone getPayment(PayPalRequest request) throws PayPalRESTException {

        Payment payment = createPayment(request);
        System.out.println("PAYMENT: "+payment);

        return  PaymentRespone.builder()
                .url(payment.getLinks())
                .build();


    }
}
