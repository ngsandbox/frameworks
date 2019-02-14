package org.bookstore.web.services;

import lombok.extern.slf4j.Slf4j;
import org.bookstore.common.BookstoreDAO;
import org.bookstore.common.MathUtils;
import org.bookstore.common.entities.Book;
import org.bookstore.common.entities.BookDetails;
import org.bookstore.common.entities.BookView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookstoreService {

    private final BookstoreDAO bookstoreDAO;
    private final CsvBookSourceService bookSourceService;

    @Autowired
    public BookstoreService(BookstoreDAO bookstoreDAO,
                            CsvBookSourceService bookSourceService) {
        this.bookstoreDAO = bookstoreDAO;
        this.bookSourceService = bookSourceService;
    }


    @PostConstruct
    protected void uploadData() {
        log.info("Start upload date from book source to database");
        List<Book> books = bookSourceService.getBooks();
        bookstoreDAO.saveBooks(books);

        List<BookView> bookViews = bookSourceService.getBookViews();
        bookstoreDAO.saveBookViews(bookViews);
    }

    public List<Book> getBooks() {
        log.debug("Get full list of books");
        return bookstoreDAO.getBooks();
    }

    public Optional<BookDetails> trackBookAndGetDetails(String bookId, String user) {
        log.info("Track book view {} for user {}", bookId, user);
        Optional<Book> book = bookstoreDAO.trackBook(bookId, user);
        return book.flatMap(b -> addSimilar(user, b));
    }

    private Optional<BookDetails> addSimilar(String user, Book book) {
        log.info("Try to find similar books fo book id {}", book.getId());
        List<Book> similar = bookstoreDAO.findBooksThrowViewers(user, book.getId());
        char[] bookChars = book.getName().toCharArray();
        similar.sort(Comparator.comparing(b -> MathUtils.cosineSimilarity(bookChars, b.getName().toCharArray()), Comparator.reverseOrder()));
        return Optional.of(new BookDetails(user, book, similar));
    }

    public List<BookView> getBooksViews(String user) {
        log.debug("Get book views for user {}", user);
        return bookstoreDAO.getBooksView(user);
    }

    public List<BookView> getBookViews() {
        return bookstoreDAO.getBooksView();
    }
}
