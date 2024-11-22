//package com.individual_s7.user_service.config;
//
//import io.github.cdimascio.dotenv.Dotenv;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
//@Configuration
//public class DotenvConfig {
////    static {
////        Dotenv dotenv = Dotenv.configure().load();
////        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
////    }
//
//    @Bean
//    @Profile("!test") // Only load this bean if the profile is NOT "test"
//    public Dotenv dotenv() {
//        Dotenv dotenv = Dotenv.configure().load();
//        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
//        return dotenv;
//    }
//}
