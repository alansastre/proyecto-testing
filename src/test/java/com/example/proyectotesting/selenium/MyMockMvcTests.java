package com.example.proyectotesting.selenium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @see <a href="https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.testing.spring-boot-applications">Spring Boot Docs</a>
 */
@SpringBootTest
@AutoConfigureMockMvc
class MyMockMvcTests {

    @Autowired
    MockMvc mvc;

    @Disabled("Disabled until check lambdatest")
    @Test
    void obtenerListaTest() throws Exception {

        mvc.perform(get("/products")) // url a testear: http://localhost:8080/products
                .andExpect(status().isOk()) // estado HTTP de la respuesta 200
                .andExpect(model().attributeExists("products")) // comprobar los atributos cargados en el modelo
                .andExpect(view().name("product-list")) // comprobar los atributos cargados en el modelo
                .andExpect( forwardedUrl("/WEB-INF/views/product-list.jsp")); // vista que se mostrará


    }

    @Disabled("Disabled until check lambdatest")
    @Test
    void crearProductoTest() throws Exception {
        mvc.perform(
                    post("/products")
                            .param("name", "producto prueba")
                            .param("description", "Descripción producto")
                            .param("price", "88.77")
                            .param("quantity", "8")
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }


    @Test
    void viewProductNotFound() throws Exception {

        mvc.perform(get("/products/1/view"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void viewProductNull() throws Exception {

        mvc.perform(get("/products/null/view"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void viewProductOK() throws Exception {

        mvc.perform(get("/products/9/view"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("product"))
                .andExpect(view().name("product-view"));
    }

    @Test
    void editProductOK() throws Exception {

        mvc.perform(get("/products/9/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("manufacturers"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(view().name("product-edit"));
    }
}
