package com.example.weed.config;

import com.example.weed.adapter.W1005_LocalDateTimeTypeAdapter;
import com.example.weed.adapter.W2001_LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class W1005_GsonConfig {

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new W1005_LocalDateTimeTypeAdapter())
                .registerTypeAdapter(LocalDate.class, new W2001_LocalDateAdapter())
                .create();
    }
}