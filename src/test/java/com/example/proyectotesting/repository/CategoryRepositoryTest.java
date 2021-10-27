package com.example.proyectotesting.repository;

import com.example.proyectotesting.entities.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
//@TestPropertySource(properties = {
//        ""
//})
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository repository;

    @Test
    void findAll() {
        List<Category> categories = repository.findAll();
        assertNotNull(categories);
        assertTrue(categories.size() >= 2);
    }

    @Test
    void create() {

        Category category = new Category("Categoria JUnit", "verde");
        Category categoryDB = repository.save(category);
        assertNotNull(categoryDB);
        assertNotNull(categoryDB.getId());
        assertTrue(repository.existsById(categoryDB.getId()));

    }
}