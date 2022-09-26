package com.vereskul.library.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class BookEntity {
    private int book_id;
    @NotEmpty(message = "Название книги не может буть пустым!")
    @Size(min = 3, max = 250, message = "Длина названия не меньше трех и не более 250 символов!")
    private String name;
    @NotEmpty(message = "Имя автора книги не может буть пустым!")
    @Size(min = 3, max = 100, message = "Длина названия не меньше трех и не более 100 символов!")
    private String author;
    @Min(value = 1000, message = "Год издания не может быть меньше 1000")
    private int yearOfWriting;

    public BookEntity() {
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfWriting() {
        return yearOfWriting;
    }

    public void setYearOfWriting(int yearOfWriting) {
        this.yearOfWriting = yearOfWriting;
    }
}
