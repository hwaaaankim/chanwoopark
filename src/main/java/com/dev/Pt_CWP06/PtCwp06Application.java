package com.dev.Pt_CWP06;

import javax.servlet.http.HttpSessionListener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.dev.Pt_CWP06.config.SessionListener;

@SpringBootApplication
public class PtCwp06Application {

	public static void main(String[] args) {
		SpringApplication.run(PtCwp06Application.class, args);
	}

	@Bean
	public HttpSessionListener httpSessionListener() {
		return new SessionListener();
	}
}
