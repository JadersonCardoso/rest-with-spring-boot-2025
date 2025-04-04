package br.com.jadersoncardoso.controllers;

import br.com.jadersoncardoso.controllers.docs.BookControllerDocs;
import br.com.jadersoncardoso.data.dto.BookDTO;
import br.com.jadersoncardoso.services.BookService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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
    public ResponseEntity<PagedModel<EntityModel<BookDTO>>> findAll(Integer page, Integer size, String direction) {
        var sortDiretion = "des".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDiretion, "author"));
        return ResponseEntity.ok(service.findAll(pageable));
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
