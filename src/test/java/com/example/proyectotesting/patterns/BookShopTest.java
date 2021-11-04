package com.example.proyectotesting.patterns;

import com.example.proyectotesting.patterns.behavioral.iterator.Book;
import com.example.proyectotesting.patterns.behavioral.iterator.BookShop;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class BookShopTest {

    @Test
    void addBook() {

        // 1. creacion de datos
        Book libro1 = new Book("2233", "Author1", 1990);
        // 2. creacion objeto a testear
        BookShop shop = new BookShop();

        // 4. para comprobar si se ha añadido correctamente el libro dentro de BookShop
        // entonces comprobamos el iterador
        Iterator<Book> iterator = shop.iterator();

        assertFalse(iterator.hasNext()); // false

        // 3. Ejecutar método a testear
        shop.addBook(libro1);

        // 5. compruebo si hay libro a iterar, si debe haber porque hemos añadido 1 libro
        assertTrue(iterator.hasNext()); // true

    }

    @Test
    void iterator() {

        // 1. creacion de datos
        Book libro1 = new Book("2233", "Author1", 1990);
        Book libro2 = new Book("2233", "Author2", 1990);

        // 2. creacion objeto a testear
        BookShop shop = new BookShop();

        shop.addBook(libro1);
        shop.addBook(libro2);

        Iterator<Book> iterator = shop.iterator();

        // Iterar los libros:

            // 1. Iterar el primer libro
        assertTrue(iterator.hasNext());

        Book book1 = iterator.next();
        assertEquals("Author1", book1.getAuthor());

            // 2. Iterar el segundo libro
        assertTrue(iterator.hasNext());

        Book book2 = iterator.next();
        assertEquals("Author2", book2.getAuthor());

            // 3. Comprobar que no quedan más libros para iterar
        assertFalse(iterator.hasNext());

    }
}