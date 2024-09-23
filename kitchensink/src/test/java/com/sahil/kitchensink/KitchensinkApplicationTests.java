package com.sahil.kitchensink;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class KitchensinkApplicationTests {

	//bean made to avoid event listener in testing
	@MockBean
	AdminCreater adminCreater;
	@Test
	//test to check if context loads
	void contextLoads() {
	}

}
