package com.vereskul.library.validator;

import com.vereskul.library.dao.PersonDAO;
import com.vereskul.library.model.BookEntity;
import com.vereskul.library.model.PersonEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class PersonValidator implements Validator {
    private final PersonDAO personDAO;
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> clazz){
        return BookEntity.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PersonEntity person = (PersonEntity) o;
        if(personDAO.show(person.getFullName()).isPresent()) {
            errors.rejectValue("fullName", "","Такой читатель уже есть");
        }
    }
}
