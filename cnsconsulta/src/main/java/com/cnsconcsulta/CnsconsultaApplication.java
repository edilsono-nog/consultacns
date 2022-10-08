package com.cnsconcsulta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EntityScan(basePackages = {"com.cnsconcsulta.model"})
@ComponentScan(basePackages = {"com.*"})
@EnableJpaRepositories(basePackages = {"com.cnsconcsulta.repository"})
@EnableTransactionManagement
@RestController
@EnableAutoConfiguration
@EnableCaching
public class CnsconsultaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CnsconsultaApplication.class, args);
	}

}
