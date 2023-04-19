package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(
                        RequestConfig.custom()
                                .setConnectTimeout(5000)
                                .setSocketTimeout(30000)
                                .setRedirectsEnabled(false)
                                .build()
                )
                .build();
        HttpGet request = new HttpGet("https://raw.githubusercontent.com/netology-code" +
                "/jd-homeworks/master/http/task1/cats");
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            ObjectMapper mapper = new ObjectMapper();
            Stream<CatsFacts> stream = mapper.readValue(EntityUtils.toString(response.getEntity()),
                            new TypeReference<List<CatsFacts>>() {})
                    .stream();
            List<CatsFacts> res = stream.filter(f -> f.getUpvotes() != null && f.getUpvotes() != 0).toList();
            for (CatsFacts c: res) {
                System.out.printf("Факт\nId: %s\nText: %s\nType: %s\nUser: %s\nUp votes: %d\n\n",
                        c.getId(), c.getText(), c.getType(), c.getUser(), c.getUpvotes());
            }
        }
        catch (IOException e){
            System.out.println("Произошла ошибка");
            e.printStackTrace();
        }
    }
}