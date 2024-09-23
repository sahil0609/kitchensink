package com.sahil.kitchensink;

import org.junit.Test;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MongoDBContainer;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(initializers ={BaseIntegrationTest.dbinitializer.class, ConfigDataApplicationContextInitializer.class})
public class BaseIntegrationTest {

    private static final MongoDBContainer testcontainer = new MongoDBContainer("mongo:4.0.10");

    static {
        testcontainer.start();
    }

    @Test
    public void testContainerStart() {
        assertTrue(testcontainer.isRunning());
    }

    public static class dbinitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "spring.data.mongodb.uri=" + testcontainer.getReplicaSetUrl()
            ).applyTo(applicationContext.getEnvironment());
        }
    }

}
