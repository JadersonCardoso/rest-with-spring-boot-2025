package br.com.jadersoncardoso.services;

import br.com.jadersoncardoso.controllers.BookController;
import br.com.jadersoncardoso.data.dto.BookDTO;
import br.com.jadersoncardoso.exception.RequiredObjectIsNullException;
import br.com.jadersoncardoso.exception.ResourceNotFoundException;
import br.com.jadersoncardoso.model.Book;
import br.com.jadersoncardoso.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.jadersoncardoso.mapper.ObjectMapper.parseListObjects;
import static br.com.jadersoncardoso.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {
    private Logger logger = LoggerFactory.getLogger(BookService.class);

    private final BookRepository repository;
    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<BookDTO> findAll() {
        logger.info("Finding all books");
        var books = parseListObjects(repository.findAll(), BookDTO.class);
        books.forEach(this::addHateoasLinks);
       return books;
    }

    public BookDTO findById(Long id) {
        logger.info("Finding one Book.");
        var bookEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found this ID"));
        var dto = parseObject(bookEntity, BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public BookDTO create(BookDTO book) {
        if (book == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one Book.");
        var entity = parseObject(book, Book.class);
        var dto = parseObject(repository.save(entity), BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public BookDTO update(BookDTO book) {
        if (book == null) throw new RequiredObjectIsNullException();
        logger.info("Updating one Book.");
        var entity = repository.findById(book.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found this ID"));
        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setTitle(book.getTitle());
        entity.setPrice(book.getPrice());
        var dto = parseObject(repository.save(entity), BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {
        logger.info("Deleting one Book.");
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found this ID"));
        repository.delete(entity);
    }



    private void addHateoasLinks(BookDTO dto) {
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }

}
