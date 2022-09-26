package com.vereskul.library.validator;

import com.vereskul.library.dao.BookDAO;
import com.vereskul.library.model.BookEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookOwnerValidator implements Validator {
    private final BookDAO bookDAO;

    public BookOwnerValidator(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return BookEntity.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        BookEntity b = (BookEntity) o;

        if(bookDAO.showBookOwner(b.getBook_id()).isPresent()){
            errors.rejectValue("book","","Эта книга находится у читателя");
        }

    }
}
