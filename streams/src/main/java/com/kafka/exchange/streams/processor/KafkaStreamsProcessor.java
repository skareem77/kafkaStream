package com.kafka.exchange.streams.processor;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

public interface KafkaStreamsProcessor {

    @Input("transaction_topic")
    KStream<?, ?> input();

    @Output("processing_topic")
    KStream<?, ?> processOutput();

    @Output("address_topic")
    KStream<?, ?> addressOutput();

    @Output("payment_topic")
    KStream<?, ?> cardOutput();
}
