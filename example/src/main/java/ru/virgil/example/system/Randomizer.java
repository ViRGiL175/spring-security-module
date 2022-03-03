package ru.virgil.example.system;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class Randomizer {

    @Bean
    public Random provideRandom() {
        return new Random();
    }

    @Bean
    public Faker provideFaker() {
        return new Faker();
    }

}
