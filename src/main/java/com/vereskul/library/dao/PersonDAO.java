package com.vereskul.library.dao;

import com.vereskul.library.model.BookEntity;
import com.vereskul.library.model.PersonEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PersonEntity> index(){
        String query = "SELECT * FROM library.person";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(PersonEntity.class));
    }

    public Optional<PersonEntity> show(int id){
        String query = "SELECT * FROM library.person WHERE person_id=?";
        List<PersonEntity> entities = jdbcTemplate.query(query,
                new Object[]{id},
                new BeanPropertyRowMapper<>(PersonEntity.class));
        return entities.stream().findAny();
    }
     public Optional<PersonEntity> show(String name){
        String query = "SELECT * FROM library.person WHERE full_name=?";
        List<PersonEntity> entities = jdbcTemplate.query(query,
                new Object[]{name},
                new BeanPropertyRowMapper<>(PersonEntity.class));
        return entities.stream().findAny();
    }

    public void save (PersonEntity person){
        String query = "insert into library.person(full_name, birthday) values(?,?)";
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        jdbcTemplate.update(query, person.getFullName(), person.getBirthday().format(format));
    }

    public void update(PersonEntity person){
        Optional<PersonEntity> oldPerson = show(person.getPerson_id());
        oldPerson.ifPresent(entity -> BeanUtils.copyProperties(person, entity));
    }

    public void delete(int id){
        String query = "delete from library.person where person_id=?";
        jdbcTemplate.update(query,id);
    }

    public List<BookEntity> getListOfPersonBooks(int id){
        String query = "select b.* from library.book b" +
                " join library.book_statement bs on b.book_id = bs.book_id " +
                " join library.person p on bs.person_id = p.person_id " +
                " where p.person_id=?";
        return jdbcTemplate.query(query, new Object[]{id},
                                        new BeanPropertyRowMapper<>(BookEntity.class));

    }

    public void giveBookToPerson(int person_id, int book_id){
        String query = "insert into library.book_statement(person_id, book_id) values (?,?)";
        jdbcTemplate.update(query, person_id, book_id);
    }
}
