package com.cryptopriceinfo.priceserver.controller;

import com.cryptopriceinfo.priceserver.domain.PriceInfo;
import com.cryptopriceinfo.priceserver.domain.PriceRequest;
import com.cryptopriceinfo.priceserver.domain.PriceResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PriceController {

    @Autowired
    ObjectMapper mapper;

    @PostMapping("/price")
    public @ResponseBody PriceResponse getPrice(@RequestBody PriceRequest priceRequest) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://min-api.cryptocompare.com/data/price";
        String fsym = "fsym=" + priceRequest.from;
        String tsyms = "tsyms=" + String.join(", ", String.join(",", priceRequest.to));
        ResponseEntity<String> response = restTemplate.getForEntity(url + "?" + fsym + "&" + tsyms, String.class);

        JsonNode jsonResponse;
        List<PriceInfo> prices = new ArrayList<>();
        try {
            jsonResponse = mapper.readTree(response.getBody());
            for(String name : priceRequest.to) {
                if (jsonResponse.has(name)) {
                    prices.add(new PriceInfo(name, jsonResponse.get(name).asText()));
                }
            }
        } catch (IOException e) {
            return null;
        }

        return new PriceResponse(prices);
    }
}
