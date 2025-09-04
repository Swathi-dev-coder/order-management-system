package com.oms.order_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@SpringBootTest
class OrderServiceApplicationTests {

//	void contextLoads() {
//	}

	@Test
	void t() {
//	    public static void main(String[] args) {
	        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	        String hashed = encoder.encode("xyzabc");
	        System.out.println(hashed);
//	    }
	}

}
