package com.nineleaps.authentication.jwt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
@SpringBootTest
class ApplicationTests {
	@Autowired
	private ApplicationContext context;
	@Test
	void contextLoads() {
		assertNotNull(context);
	}

	  @Test
  void testMainMethod() {
     Application.main(new String[]{});
     Assertions.assertTrue(true, "Main method tested");
  }

}