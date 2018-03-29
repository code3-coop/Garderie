package org.code3.garderie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@EnableAutoConfiguration
@SpringBootApplication
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
