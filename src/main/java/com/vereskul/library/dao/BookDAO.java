package com.vereskul.library.dao;

import com.vereskul.library.model.BookEntity;
import com.vereskul.library.model.PersonEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<BookEntity> index(){
        String query = "SELECT * FROM library.book ORDER BY name";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(BookEntity.class));
    }

    public Optional<BookEntity> show(int id){
        String query = "SELECT * FROM library.book WHERE book_id=?";
        List<BookEntity> entities = jdbcTemplate.query(query,
                new Object[]{id},
                new BeanPropertyRowMapper<>(BookEntity.class));
        return entities.stream().findAny();
    }
    public Optional<BookEntity> show(String name){
        String query = "SELECT * FROM library.book WHERE name=?";
        List<BookEntity> entities = jdbcTemplate.query(query,
                new Object[]{name},
                new BeanPropertyRowMapper<>(BookEntity.class));
        return entities.stream().findAny();
    }

    public void save (BookEntity book){
        String query = "insert into library.book(name, author, year_of_writing) values(?,?,?)";
        jdbcTemplate.update(query, book.getName(), book.getAuthor(), book.getYearOfWriting());
    }

    public void update(BookEntity book){
        String query = "update library.book set name=?, author=?, year_of_writing=? where book_id=?";
        jdbcTemplate.update(query, book.getName(), book.getAuthor(), book.getYearOfWriting(), book.getBook_id());
    }
    public void delete(int id){
        String query = "delete from library.book where book_id=?";
        jdbcTemplate.update(query, id);
    }
    public Optional<PersonEntity> showBookOwner(int id){
        String query = "select p.* from library.person p" +
                " join library.book_statement bs on p.person_id = bs.person_id" +
                " join library.book b on bs.book_id = b.book_id" +
                " where b.book_id=?";
        return jdbcTemplate.query(query, new Object[]{id},
                new BeanPropertyRowMapper<>(PersonEntity.class))
                    .stream().findAny();
    }

    public void returnBookFromPerson(Integer[] book_id){
        String inSql = String.join(",", Collections.nCopies(book_id.length, "?"));

        String query = String.format("delete from library.book_statement where book_id in (%s)", inSql);
        jdbcTemplate.update(query,book_id);
    }

    public List<BookEntity> getListOfFreeBooks(){
        String query = "SELECT * from library.book " +
                "WHERE book.book_id not in (" +
                "SELECT book_statement.book_id from library.book_statement" +
                ") ORDER BY book.name";
        return jdbcTemplate.query(query,new BeanPropertyRowMapper<>(BookEntity.class));
    }
}
