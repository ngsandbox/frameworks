package org.bookstore.database.repositories;

import org.bookstore.database.entities.BookViewEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookViewRepository extends CrudRepository<BookViewEntity, String>,
        JpaSpecificationExecutor<BookViewEntity> {

    @Query("select v from BookViewEntity v where "
            + " v.user = :user ")
    List<BookViewEntity> findByUser(@Nullable @Param("user") String user);
}
