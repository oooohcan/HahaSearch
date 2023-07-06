package edu.zuel.hahasearch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("edu.zuel.hahasearch.mapper")
public class HahaSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(HahaSearchApplication.class, args);
    }

}
