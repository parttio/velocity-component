package org.vaadin.addons.usageexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestServer {

    /**
     * This main method runs the test server, configure to custom port
     * 9998 in application.properties (so that it doesn't conflict with
     * potential actual application developed concurrently).
     *
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(TestServer.class, args);
    }

}
