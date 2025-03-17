package br.com.jadersoncardoso.mapper.mocks;

import br.com.jadersoncardoso.data.dto.BookDTO;
import br.com.jadersoncardoso.model.Book;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

public class MockBook {

    public Book mockEntity() {
        return mockEntity(0);
    }
    public BookDTO mockDTO() {
        return mockDTO(0);
    }

    public List<Book> mockEntityList() {
        return IntStream.range(0,14)
                .mapToObj(this::mockEntity).toList();
    }
    public List<BookDTO> mockDTOList() {
        return IntStream.range(0,14)
                .mapToObj(this::mockDTO).toList();
    }
    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setId(number.longValue());
        book.setTitle("Arte do Jiu-Jitsu para negócios");
        book.setAuthor("Jaderson Brandão");
        book.setPrice(new BigDecimal("50.00"));
        book.setLaunchDate(new Date(2023, 3, 10));
        return book;
    }

    public BookDTO mockDTO(Integer number) {
        BookDTO book = new BookDTO();
        book.setId(number.longValue());
        book.setTitle("Arte do Jiu-Jitsu para negócios");
        book.setAuthor("Jaderson Brandão");
        book.setPrice(new BigDecimal("50.00"));
        book.setLaunchDate(new Date(2023, 3, 10));
        return book;
    }
}
