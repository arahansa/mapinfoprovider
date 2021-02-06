package com.arahansa.mapinfoprovider.controller;

import lombok.Data;
import org.apache.tomcat.jni.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class MapInfoController {

    @Autowired
    RestTemplate restTemplate;

    @Value( "${naverClientId}" )
    String naverClientId;

    @Value( "${naverClientSecret}" )
    String naverClientSecret;

    @Data
    static class AddressInfo{
        String roadAddress;
        String jibunAddress;
        String englishAddress;
        String x;
        String y;
        Double distance;
    }

    @Data
    static class GeoCodeResp{
        String status;
        String errorMessage;
        List<AddressInfo> addresses;
    }

    @GetMapping("/info")
    public String info(){
        return "clientId:"+naverClientId+",clientSecret :"+naverClientSecret;
    }

    @GetMapping("/address")
    public GeoCodeResp getAddressInfo(String query){
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", naverClientId);
        headers.set("X-NCP-APIGW-API-KEY", naverClientSecret);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<GeoCodeResp> exchange = restTemplate.exchange("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + query, HttpMethod.GET, request, GeoCodeResp.class);
        if(exchange.getStatusCode() == HttpStatus.OK){
            return exchange.getBody();
        }else{
            return null;
        }
    }

}
