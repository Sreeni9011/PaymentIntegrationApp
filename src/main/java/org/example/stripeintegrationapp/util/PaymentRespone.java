package org.example.stripeintegrationapp.util;

import com.paypal.api.payments.Links;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaymentRespone {
    private String paymentId;
    private String status;
    private String message;
    private List<Links> url;
}
