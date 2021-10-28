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

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductRestControllerTest {

    private static final String PRODUCTS_URL = "/api/products";
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

        ResponseEntity<Product[]> response = testRestTemplate.getForEntity(PRODUCTS_URL, Product[].class);

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
                testRestTemplate.getForEntity(PRODUCTS_URL + "/" + product.getId(), Product.class);

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
                testRestTemplate.getForEntity(PRODUCTS_URL + "/99999", Product.class);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.hasBody());
    }

    @Test
    void createOK() {
        // Preparar el JSON a enviar al controlador
        String json = """
                {
                    "name": "Product creado desde JUnit",
                    "description": "description example",
                    "quantity": 6,
                    "price": 4.99
                }
                """;

        // Ejecutar petición HTTP POST
        ResponseEntity<Product> response =
                testRestTemplate.postForEntity(PRODUCTS_URL, createHttpRequest(json), Product.class);

        // Procesar respuesta y aserciones
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.hasBody());

        Product product = response.getBody();
        assertNotNull(product);
        assertEquals("Product creado desde JUnit", product.getName());
    }

    @Test
    void createBadRequest() {
        String json = """
                {
                    "id": 5,
                    "name": "Product creado desde JUnit",
                    "description": "description example",
                    "quantity": 6,
                    "price": 4.99
                }
                """;

        ResponseEntity<Product> response =
                testRestTemplate.postForEntity(PRODUCTS_URL, createHttpRequest(json), Product.class);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.hasBody());
    }


    @Test
    void stringFormatTest(){
        String result = String.format("Hola %s %s", "Alan", "Sastre");
        System.out.println(result);
        String result2 = "Hola " + "Alan " + "Sastre";

    }
    @Test
    void update() {
        Product product = createDemoProduct();
        String json = String.format("""
                {
                    "id": %d,
                    "name": "Producto modificado desde JUnit",
                    "description": "description example",
                    "quantity": 6,
                    "price": 4.99
                }
                """, product.getId());
        System.out.println(json);

        // ejecutar petición PUT
        ResponseEntity<Product> response =
                testRestTemplate.exchange(PRODUCTS_URL, HttpMethod.PUT, createHttpRequest(json), Product.class);

        // Aserciones estado código HTTP 200 OK
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Comprobar que hay datos en la respuesta
        assertTrue(response.hasBody());
        assertNotNull(response.getBody());

        // Aserciones cuerpo de la respuesta, esperamos un objeto de la clase Product
        Product responseProduct = response.getBody();
        // Comprobar que se mantiene el mismo id clave primaria
        assertEquals(product.getId(), responseProduct.getId());

        // Comprobar los nombres
        assertEquals("Producto modificado desde JUnit", responseProduct.getName());

        // aqui comprobamos que el producto modificado no tiene el mismo nombre que el producto antes de ser modificado
        assertNotEquals(responseProduct.getName(), product.getName());

    }

    @Test
    void deleteById() {
        // Crear producto
        Product product = createDemoProduct();
        String url = PRODUCTS_URL + "/" + product.getId();

        // GET y comprobar que sí existe
        ResponseEntity<Product> response = testRestTemplate.getForEntity(url, Product.class);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product.getId(), response.getBody().getId());

        // ejecutar petición DELETE
        testRestTemplate.delete(url);

        // GET y comprobar que no existe
        ResponseEntity<Product> response2 = testRestTemplate.getForEntity(url, Product.class);
        assertEquals(404, response2.getStatusCodeValue());
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
        assertFalse(response2.hasBody());
    }

    @Test
    void deleteById2() {
        // Crear producto
        Product product = createDemoProduct();
        String url = PRODUCTS_URL + "/" + product.getId();

        // GET y comprobar que sí existe
        ResponseEntity<Product> response = testRestTemplate.getForEntity(url, Product.class);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product.getId(), response.getBody().getId());

        // ejecutar petición DELETE
        response = testRestTemplate.exchange(url,
                                            HttpMethod.DELETE,
                                            createHttpRequest(""),
                                            Product.class);

        assertEquals(204, response.getStatusCodeValue());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // GET y comprobar que no existe
        ResponseEntity<Product> response2 = testRestTemplate.getForEntity(url, Product.class);
        assertEquals(404, response2.getStatusCodeValue());
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
        assertFalse(response2.hasBody());

    }

    @Test
    void deleteAll() {
        // crear 2 productos
        createDemoProduct();
        createDemoProduct();

        // findAll comprobar que existen productos
        ResponseEntity<Product[]> response = testRestTemplate.getForEntity(PRODUCTS_URL, Product[].class);
        assertNotNull(response.getBody());
        List<Product> products = List.of(response.getBody());
        assertTrue(products.size() >= 2);

        // deleteAll
        testRestTemplate.delete(PRODUCTS_URL); // borra todos

        // findAll comprobar que no existen productos
        response = testRestTemplate.getForEntity(PRODUCTS_URL, Product[].class);
        assertNotNull(response.getBody());
        products = List.of(response.getBody());
        assertEquals(0, products.size());
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
                testRestTemplate.postForEntity(PRODUCTS_URL, request, Product.class);
        return response.getBody();
    }

    private HttpEntity<String> createHttpRequest(String json){
        // Cabeceras para indicar al servidor que los datos
        // enviados son en JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        // Crear petición HTTP con el json y las cabeceras
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        return request;
    }
}