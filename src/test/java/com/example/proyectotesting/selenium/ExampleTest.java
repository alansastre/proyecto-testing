package com.example.proyectotesting.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ExampleTest {

    // Navegador
    WebDriver driver;

    @BeforeEach
    void setUp() {
//        String dir = System.getProperty("user.dir"); // ruta del proyecto
//        String driverUrl = "/drivers/chromedriver.exe";
//        String url = dir + driverUrl;
//        System.setProperty("webdriver.chrome.driver", url);
//        driver = new ChromeDriver(); // Google Chrome

        System.getenv().forEach((key, value) -> System.out.println(key + " " + value));
        System.getProperties().forEach((key, value) -> System.out.println(key + " " + value));

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);

    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void tagNameSelector(){
//        driver.get("https://github.com/mozilla");
        driver.get("https://proyecto-testing.herokuapp.com");
//        driver.get("http://localhost:8080/");

        WebElement h1 = driver.findElement(By.tagName("h1"));
        String h1Text = h1.getText();
        assertEquals("Products Directory", h1Text);
    }

}
