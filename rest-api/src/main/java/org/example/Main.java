package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Board Game Library Management System!");
        SpringApplication.run(Main.class, args);
    }
}