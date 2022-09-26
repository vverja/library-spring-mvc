package com.vereskul.library.model;



import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class PersonEntity{
    private int person_id;
    @NotEmpty(message = "Имя читателя не может буть пустым!")
    @Size(min = 3, max = 250, message = "Длина названия не меньше трех и не более 45 символов!")
    private String fullName;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate birthday;

    public PersonEntity() {
    }

    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}
