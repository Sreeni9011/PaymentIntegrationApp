package org.example.stripeintegrationapp.controller;
/*
import com.paypal.base.rest.PayPalRESTException;
import org.example.stripeintegrationapp.service.PaypalService;
import org.example.stripeintegrationapp.util.PayPalRequest;
import org.example.stripeintegrationapp.util.PaymentRespone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PayPalController {

    @Autowired
    PaypalService service;

    @PostMapping("payment/create")
    public ResponseEntity<PaymentRespone> getPayment(@RequestParam("method") String method,
                                                     @RequestParam("amount") String amount,
                                                     @RequestParam("currency") String currency,
                                                     @RequestParam("description") String description) throws PayPalRESTException {

        String cancelUrl = "http://localhost:8080/payment/cancel";
        String successUrl = "http://localhost:8080/payment/success";
        PayPalRequest payPalRequest = new PayPalRequest();
        payPalRequest.setCurrency(currency);
        payPalRequest.setDescription(description);
        payPalRequest.setMethod(method);
        payPalRequest.setIntent("sale");
        payPalRequest.setTotal(Double.valueOf(amount));
        payPalRequest.setCancelUrl(cancelUrl);
        payPalRequest.setSuccessUrl(successUrl);
        return ResponseEntity.ok(service.getPayment(payPalRequest));

    }


}*/

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stripeintegrationapp.service.PaypalService;
import org.example.stripeintegrationapp.util.PayPalRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaypalController {

    private final PaypalService paypalService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/payment/create")
    public RedirectView createPayment(
            @RequestParam("method") String method,
            @RequestParam("amount") String amount,
            @RequestParam("currency") String currency,
            @RequestParam("description") String description
    ) {
        try {
            String cancelUrl = "http://localhost:8080/payment/cancel";
            String successUrl = "http://localhost:8080/payment/success";
            PayPalRequest payPalRequest = new PayPalRequest();
            payPalRequest.setCurrency(currency);
            payPalRequest.setDescription(description);
            payPalRequest.setMethod(method);
            payPalRequest.setIntent("sale");
            payPalRequest.setTotal(Double.valueOf(amount));
            payPalRequest.setCancelUrl(cancelUrl);
            payPalRequest.setSuccessUrl(successUrl);
            Payment payment = paypalService.createPayment(payPalRequest);

            for (Links links: payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return new RedirectView(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
        }
        return new RedirectView("/payment/error");
    }

    @GetMapping("/payment/success")
    public String paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ) {
        try {
            Payment payment = paypalService.excutePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "paymentSuccess";
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
        }
        return "paymentSuccess";
    }

    @GetMapping("/payment/cancel")
    public String paymentCancel() {
        return "paymentCancel";
    }

    @GetMapping("/payment/error")
    public String paymentError() {
        return "paymentError";
    }
}
