package ru.sberbank.main.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomConfiguration {

    @Bean
    public Random random() {
        return new Random();
    }

}
