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

public class ExampleTest {

    // Navegador
    WebDriver driver;

    @BeforeEach
    void setUp() {

        System.getenv().forEach((key, value) -> System.out.println(key + " " + value));
        System.getProperties().forEach((key, value) -> System.out.println(key + " " + value));

        if(System.getProperties().get("os.name").equals("Linux")){
            System.out.println("Configurando Navegador Chrome Headless para CI");
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--headless");
            driver = new ChromeDriver(options);
        }else{
            System.out.println("Configurando Navegador Chrome desde carpeta drivers para testing en desarrollo");
            String dir = System.getProperty("user.dir"); // ruta del proyecto
            String driverUrl = "/drivers/chromedriver.exe";
            String url = dir + driverUrl;
            System.setProperty("webdriver.chrome.driver", url);
            driver = new ChromeDriver(); // Google Chrome
        }


    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void tagNameSelector(){
////        driver.get("https://github.com/mozilla");
//        driver.get("https://testing-alansastre.herokuapp.com/products");
////        driver.get("http://localhost:8080/");
//
//        WebElement h1 = driver.findElement(By.tagName("h1"));
//        String h1Text = h1.getText();
//        assertEquals("Products Directory", h1Text);

        driver.get("https://testing-alansastre.herokuapp.com/products");

        assertEquals("Products Directory", driver.findElement(By.tagName("h1")).getText());
    }

}
