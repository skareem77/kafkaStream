package com.kafka.exchange.streams.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExchangeRate {

    @JsonProperty("rates")
    private Object rate;
    @JsonProperty("base")
    private String base;
    @JsonProperty("date")
    private String date;

    public Object getRate() {
        return rate;
    }

    public void setRate(Object rate) {
        this.rate = rate;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
