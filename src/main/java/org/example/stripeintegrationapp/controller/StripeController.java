package org.example.stripeintegrationapp.controller;

import org.example.stripeintegrationapp.util.ProductRequest;
import org.example.stripeintegrationapp.util.StripeResponse;
import org.example.stripeintegrationapp.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product/v1")
public class StripeController {
    private StripeService stripeService;

    @Autowired
    public  StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> checkoutProduct(@RequestBody ProductRequest productRequest) {
        StripeResponse stripeResponse = stripeService.checkoutProducts(productRequest);
        return ResponseEntity.ok(stripeResponse);

    }
}
