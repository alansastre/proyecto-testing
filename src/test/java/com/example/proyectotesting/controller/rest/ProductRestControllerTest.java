package com.example.proyectotesting.controller.rest;

import com.example.proyectotesting.entities.Product;
import com.example.proyectotesting.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductRestControllerTest {

    private TestRestTemplate testRestTemplate;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:" + port);
        testRestTemplate = new TestRestTemplate(restTemplateBuilder);
    }

    @Test
    void findAll() {
        // Creamos 2 productos demo
        createDemoProduct();
        createDemoProduct();

        ResponseEntity<Product[]> response = testRestTemplate.getForEntity("/api/products", Product[].class);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertNotNull(response.getBody());

        List<Product> products = List.of(response.getBody());
        assertNotNull(products);
        assertTrue(products.size() >= 2);
    }

    @Test
    void findOneOk() {
        Product product = createDemoProduct();
        ResponseEntity<Product> response =
                testRestTemplate.getForEntity("/api/products/" + product.getId(), Product.class);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());

        Product responseProduct = response.getBody();
        assertNotNull(responseProduct);
        assertNotNull(responseProduct.getId());
        assertEquals(product.getId(), responseProduct.getId());

    }

    @Test
    void findOneNotFound() {
        ResponseEntity<Product> response =
                testRestTemplate.getForEntity("/api/products/99999", Product.class);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.hasBody());
    }

    private Product createDemoProduct(){
        String json = """
                {
                    "name": "Product creado desde JUnit",
                    "description": "description example",
                    "quantity": 6,
                    "price": 4.99
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<Product> response =
                testRestTemplate.postForEntity("/api/products", request, Product.class);
        return response.getBody();
    }

    @Test
    void create() {

        // Preparar el JSON a enviar al controlador
        String json = """
                {
                    "name": "Product creado desde JUnit",
                    "description": "description example",
                    "quantity": 6,
                    "price": 4.99
                }
                """;

        // Cabeceras para indicar al servidor que los datos
        // enviados son en JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        // Crear petición HTTP con el json y las cabeceras
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        // Ejecutar petición HTTP POST
        ResponseEntity<Product> response =
                testRestTemplate.postForEntity("/api/products", request, Product.class);

        // Procesar respuesta y aserciones
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.hasBody());

        Product product = response.getBody();
        assertNotNull(product);
        assertEquals("Product creado desde JUnit", product.getName());
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void deleteAll() {
    }
}