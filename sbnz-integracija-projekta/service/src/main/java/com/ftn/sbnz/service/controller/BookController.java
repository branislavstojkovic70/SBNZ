package com.ftn.sbnz.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.sbnz.model.models.Book;
import com.ftn.sbnz.service.dtos.BookDTO;
import com.ftn.sbnz.service.mapper.BookMapper;
import com.ftn.sbnz.service.repository.BookRepository;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/new")
    public Book addBook(@RequestBody BookDTO bookDto) {
        Book book = BookMapper.INSTANCE.bookDTOToBook(bookDto);
        return bookRepository.save(book);
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable Integer id) {
        return bookRepository.findById(id).orElse(null);
    }
}
