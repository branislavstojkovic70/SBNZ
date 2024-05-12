package com.ftn.sbnz.service.repository;

import com.ftn.sbnz.model.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
