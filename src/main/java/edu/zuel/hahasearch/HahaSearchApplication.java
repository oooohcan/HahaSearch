package edu.zuel.hahasearch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
@MapperScan("edu.zuel.hahasearch.mapper")
public class HahaSearchApplication {

    public static void main(String[] args) {
        try{
            String currentDir = System.getProperty("user.dir");
            String batchFile = currentDir + "\\support.bat";
            Runtime.getRuntime().exec(batchFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SpringApplication.run(HahaSearchApplication.class, args);
    }

}
