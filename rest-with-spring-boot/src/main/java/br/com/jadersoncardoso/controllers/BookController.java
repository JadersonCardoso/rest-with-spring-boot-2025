package br.com.jadersoncardoso.controllers;

import br.com.jadersoncardoso.controllers.docs.BookControllerDocs;
import br.com.jadersoncardoso.data.dto.BookDTO;
import br.com.jadersoncardoso.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books/v1")
public class BookController implements BookControllerDocs {

    private final BookService service;
    public BookController(BookService service) {
        this.service = service;
    }

    @Override
    public List<BookDTO> findAll() {
        return service.findAll();
    }

    @Override
    public BookDTO findById(Long id) {
        return service.findById(id);
    }

    @Override
    public BookDTO create(BookDTO book) {
        return service.create(book);
    }

    @Override
    public BookDTO update(BookDTO book) {
        return service.update(book);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
