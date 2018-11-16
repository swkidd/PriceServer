package com.cryptopriceinfo.priceserver.domain;

public class PriceInfo {
    public String name;
    public String value;
    public PriceInfo() {}
    public PriceInfo(String name, String value) {
        this.name = name;
        this.value = value;
    }

}
