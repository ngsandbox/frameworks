package org.bookstore.database.repositories;

import org.bookstore.database.entities.BookEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
public interface BookRepository extends CrudRepository<BookEntity, String>,
        JpaSpecificationExecutor<BookEntity> {

    @Query("select q from BookEntity q where "
            + " (q.id = :book) ")
    @EntityGraph(value = "BookEntity.views", type = FETCH)
    Optional<BookEntity> findByIdWithView(@Nullable @Param("book") String book);

    @Query("select q from BookEntity q " +
            " where q.id != :bookId and q.id in ( " +
            "    select v.book from BookViewEntity v where " +
            "        v.user in (select bv.user from BookViewEntity bv where bv.book =:bookId and bv.user != :currentUser) " +
            ")")
    List<BookEntity> findConsumersBooks(@Nullable @Param("currentUser")String currentUser,
                                        @Nullable @Param("bookId") String bookId);
}
