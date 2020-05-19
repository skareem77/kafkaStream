package com.kafka.exchange.streams.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.kafka.exchange.streams.model.Exchange;
import com.kafka.exchange.streams.model.ExchangeRate;
import com.kafka.exchange.streams.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class ExchangeResource {

    @Autowired
    RestTemplate exchangeRestTemplate;

    @Autowired
    KafkaTemplate<String, Exchange> transactionTemplate;

    @RequestMapping(value = "/v1/convert", method = RequestMethod.POST)
    public Response exchangeAmount(@RequestBody Exchange exchange) {
        StringBuilder sb = new StringBuilder("https://api.exchangeratesapi.io/latest");
        sb.append("?base=" + exchange.getFrom()).append("&symbols=" + exchange.getTo());
        ExchangeRate response = exchangeRestTemplate.getForObject(sb.toString(), ExchangeRate.class);
        Map<String, Double> map = (LinkedHashMap) response.getRate();
        exchange.setAmount(map.get(exchange.getTo()) * exchange.getAmount());
        Response result = new Response();
        sendMessage(exchange, result);
        return result;
    }

    private void sendMessage(Exchange exchange, Response response) {

        try {
            ListenableFuture<SendResult<String, Exchange>> future = transactionTemplate.send("transaction_topic", exchange.getFrom(), exchange);

            future.addCallback(new ListenableFutureCallback<SendResult<String, Exchange>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    System.out.println(throwable.getMessage());
                    response.setMessage(throwable.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, Exchange> stringExchangeSendResult) {
                    System.out.println("Success");
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
