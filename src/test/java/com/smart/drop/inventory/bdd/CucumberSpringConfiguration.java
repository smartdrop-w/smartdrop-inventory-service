package com.smart.drop.inventory.bdd;

import com.smart.drop.InventoryServiceApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = InventoryServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CucumberSpringConfiguration {
}
