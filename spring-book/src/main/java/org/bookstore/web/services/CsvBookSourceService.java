package org.bookstore.web.services;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bookstore.common.BookSourceService;
import org.bookstore.common.entities.Book;
import org.bookstore.common.entities.BookView;
import org.bookstore.common.errors.UploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class CsvBookSourceService implements BookSourceService {

    @Value("${bs.skip.csv.delimiter:;}")
    private String csvDelimiter;

    @Value("${bs.skip.books.csv.lines:1}")
    private Integer skipBooksLines;

    @Value("${bs.skip.books.view.csv.lines:1}")
    private Integer skipBooksViewLines;

    private Book extractBook(List<String> cells) {
        log.debug("Exctract book from cells {}", cells);
        return new Book(cells.get(0),
                cells.get(1),
                cells.get(2),
                cells.get(3),
                cells.get(4),
                Collections.emptyList());
    }

    private BookView extractBookView(List<String> cells) {
        log.debug("Exctract book's view from cells {}", cells);
        return new BookView(cells.get(0),
                cells.get(1));
    }

    @Override
    public List<Book> getBooks() {
        String PATH_TO_BOOK_CSV = "/data/books.csv";
        ConverterInfo<Book> converterInfo = new ConverterInfo<>(PATH_TO_BOOK_CSV, csvDelimiter, skipBooksLines, 5,
                this::extractBook);
        return converCsvContent(converterInfo, this::processRow);
    }

    @Override
    public List<BookView> getBookViews() {
        String PATH_TO_BOOKS_VIEW_CSV = "/data/books-views.csv";
        ConverterInfo<BookView> converterInfo = new ConverterInfo<>(PATH_TO_BOOKS_VIEW_CSV, csvDelimiter, skipBooksViewLines, 2,
                this::extractBookView);
        return converCsvContent(converterInfo, this::processRow);
    }

    private <T> List<T> converCsvContent(ConverterInfo<T> converterInfo, BiFunction<ConverterInfo<T>, String, T> converter) {
        log.info("Start read data {}", converterInfo);
        URL resource = getClass().getResource(converterInfo.bookPath);
        if (resource == null) {
            log.error("Csv file was not found by path {}", converterInfo.bookPath);
            throw new UploadException("The resource was not found " + converterInfo.bookPath);
        }

        Path path = Paths.get(resource.getPath());

        try (Stream<String> lines = Files.lines(path)) {
            Stream<String> stream = lines;
            if (skipBooksLines != null && skipBooksLines > 0) {
                log.info("Skip lines count {}", converterInfo.skipLinesCount);
                stream = stream.skip(skipBooksLines);
            }

            return stream.map(row -> converter.apply(converterInfo, row))
                    .filter(Objects::nonNull)
                    .collect(toList());

        } catch (IOException ex) {
            log.error("Error to process file {}", converterInfo.bookPath, ex);
            throw new UploadException("Error to process internal book's csv file", ex);
        } catch (Exception ex) {
            log.error("Internal error to process file {}", converterInfo.bookPath, ex);
            throw new UploadException("Error to process internal book's csv file", ex);
        }
    }

    private <T> T processRow(ConverterInfo<T> info, String row) {
        String data = StringUtils.trim(row);
        log.debug("Convert row's  data {}", data);
        if (StringUtils.isEmpty(data)) {
            log.warn("Data is empty. Skip the row", data);
            return null;
        }

        List<String> cells = Arrays.stream(row.split(info.delimiter))
                .map(StringUtils::trim)
                .map(c -> c.replace("\"", ""))
                .collect(toList());
        if (cells.size() < info.columnsSize) {
            log.warn("Error convert row. Cells count less than {}. Skip the row", info.columnsSize);
            return null;
        }

        return info.converter.apply(cells);
    }

    @AllArgsConstructor
    @ToString
    private static class ConverterInfo<T> {
        String bookPath;
        String delimiter;
        Integer skipLinesCount;
        int columnsSize;
        Function<List<String>, T> converter;
    }
}
