package com.vereskul.library.validator;

import com.vereskul.library.dao.BookDAO;
import com.vereskul.library.model.BookEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {
    private final BookDAO bookDAO;
    public BookValidator(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return BookEntity.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        BookEntity book = (BookEntity) o;
        if(bookDAO.show(book.getName()).isPresent()) {
            errors.rejectValue("name","", "Такая книга уже есть в библиотеке");
        }
    }
}
