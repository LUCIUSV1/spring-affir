package org.lucius;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //开启事务注解
@MapperScan("org.lucius.mapper")
public class SpringAffairApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAffairApplication.class, args);
    }

}
