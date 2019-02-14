package org.bookstore.database;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bookstore.common.BookstoreDAO;
import org.bookstore.common.entities.Book;
import org.bookstore.common.entities.BookView;
import org.bookstore.database.entities.BookEntity;
import org.bookstore.database.entities.BookViewEntity;
import org.bookstore.database.repositories.BookRepository;
import org.bookstore.database.repositories.BookViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class BookstoreDAOImpl implements BookstoreDAO {
    private final BookRepository bookRepository;
    private final BookViewRepository bookViewRepository;

    @Autowired
    public BookstoreDAOImpl(BookRepository bookRepository, BookViewRepository bookViewRepository) {
        log.info("Initialize database bookstore dao");
        this.bookViewRepository = bookViewRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void saveBooks(@NonNull List<Book> books) {
        log.debug("Save list of books {}", books);
        bookRepository.saveAll(books.stream()
                .map(BookEntity::of)
                .collect(toList()));
    }

    @Override
    public void saveBookViews(@NonNull List<BookView> bookViews) {
        log.debug("Save list of book views {}", bookViews);
        bookViewRepository.saveAll(bookViews.stream()
                .map(BookViewEntity::of)
                .collect(toList()));
    }

    @Override
    public List<Book> getBooks() {
        log.debug("Get all list of books");
        return StreamSupport.stream(bookRepository.findAll().spliterator(), false)
                .map(BookEntity::toBook)
                .collect(toList());
    }

    @Override
    public Optional<Book> trackBook(String bookId, String user) {
        log.debug("Track book {} with user {}", bookId, user);
        Optional<BookEntity> bookEntity = bookRepository.findByIdWithView(bookId);
        if (bookEntity.isPresent()) {
            bookViewRepository.save(new BookViewEntity(bookId, user));

        } else {
            log.debug("Book was not found by id {} ", bookId);
        }
        return bookEntity.map(BookEntity::toBook);
    }

    @Override
    public List<BookView> getBooksView(String user) {
        log.debug("Find book views by user {}", user);
        List<BookViewEntity> entities = StreamSupport.stream(bookViewRepository.findAll().spliterator(), false)
                .map(t -> t)
                .collect(toList());
        List<BookView> result = bookViewRepository.findByUser(user)
                .stream().map(BookViewEntity::toBookView)
                .collect(toList());
        return result;

    }

    @Override
    public List<BookView> getBooksView() {
        return StreamSupport.stream(bookViewRepository.findAll().spliterator(), false)
                .map(BookViewEntity::toBookView)
                .collect(toList());
    }

    @Override
    public List<Book> findBooksThrowViewers(String user, String bookId) {
        log.debug("Find same book views by user {} from source book id ", user, bookId);
        return bookRepository.findConsumersBooks(user, bookId)
                .stream().map(BookEntity::toBook)
                .collect(toList());
    }
}
