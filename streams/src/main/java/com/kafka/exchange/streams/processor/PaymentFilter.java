package com.kafka.exchange.streams.processor;

import com.kafka.exchange.streams.model.Exchange;
import com.kafka.exchange.streams.utils.CreditCardValidator;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;

@EnableBinding(KafkaStreamsProcessor.class)
public class PaymentFilter {

    @StreamListener("transaction_topic")
    @SendTo({"processing_topic","address_topic","payment_topic"})
    public KStream<String, Exchange>[] paymentProcess(KStream<String, Exchange> input) {
        Predicate<String, Exchange> isValid = (String, Exchange) ->  isValidCard(Exchange) && isValidAddress(Exchange);
        Predicate<String, Exchange> isNotValidAddress = (String, Exchange) -> isNotValidAddress(Exchange);
        Predicate<String, Exchange> isNotValidCard = (String, Exchange) -> isNotValidCard(Exchange);
        return input.branch(isValid, isNotValidAddress, isNotValidCard);
    }

    private boolean isValidCard(Exchange exchange) {
        boolean isValid = false;
        try {
            Long num = Long.parseLong(exchange.getCard().getCardNo());
            isValid = CreditCardValidator.isValid(num);
            System.out.println("Credit card validation " + isValid);
        } catch (Exception e) {
            e.getMessage();
        }
        return isValid;
    }

    private boolean isValidAddress(Exchange exchange) {
        boolean isValid = false;
        String country = exchange.getAddress().getCountry();
        if ("USA".equals(country))
            isValid = true;
        return isValid;

    }

    private boolean isNotValidAddress(Exchange exchange) {
        return !isValidAddress(exchange);
    }

    private boolean isNotValidCard(Exchange exchange) {
        return !isValidCard(exchange);
    }
}
