package com.SmartContactManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class SmartContactManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartContactManagerApplication.class, args);
        System.out.println("Hello spring boot .....");
    }

}
// Note - 
// Your all folder must be under the com.SmartContactManager package , this package we get by default durring project making
// if you create folder structure out of the above mentioned package then you face some error package not found etc..
// 
