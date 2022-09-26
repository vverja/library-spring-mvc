package com.vereskul.library.controller;

import com.vereskul.library.dao.BookDAO;
import com.vereskul.library.dao.PersonDAO;
import com.vereskul.library.model.BookEntity;
import com.vereskul.library.model.PersonEntity;
import com.vereskul.library.validator.BookOwnerValidator;
import com.vereskul.library.validator.BookValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final PersonDAO personDAO;
    private final BookDAO bookDAO;
    private final BookOwnerValidator bookOwnerValidator;
    private final BookValidator bookValidator;

    public BooksController(PersonDAO personDAO, BookDAO bookDAO,
                           BookOwnerValidator bookOwnerValidator, BookValidator bookValidator) {
        this.personDAO = personDAO;
        this.bookDAO = bookDAO;
        this.bookOwnerValidator = bookOwnerValidator;
        this.bookValidator = bookValidator;
    }
    @GetMapping
    public String index(Model model){
        model.addAttribute("list_of_books", bookDAO.index());
        return "books/index";
    }

    @GetMapping("/new")
    public String sendToAdd(@ModelAttribute("book") BookEntity book){
        return "books/new";
    }

    @PostMapping("/new")
    public String add(@ModelAttribute("book") @Valid BookEntity book, BindingResult bindingResult){
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        bookDAO.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable int id, Model model){
        model.addAttribute("book", bookDAO.show(id).orElseGet(BookEntity::new));
        model.addAttribute("book_owner", bookDAO.showBookOwner(id).orElseGet(PersonEntity::new));
        model.addAttribute("person_list", personDAO.index());
        model.addAttribute("owner", new PersonEntity());
        return "books/show";
    }

    @PatchMapping("/{id}")
    String giveBook(@ModelAttribute("book") @Valid BookEntity book,
                    @PathVariable("id") int bookId,
                    BindingResult bindingResult){
        bookValidator.validate(book, bindingResult);
        if(bindingResult.hasErrors()){
            return "redirect:/books/"+bookId;
        }
        bookDAO.update(book);
        return "redirect:/books/"+bookId;
    }

    @DeleteMapping("/{id}")
    String deleteBook(@PathVariable int id){
        bookDAO.delete(id);
        return "redirect:/books";
    }

    @DeleteMapping("/owner/{id}")
    String freeBook(@PathVariable("id") int bookId){
        bookDAO.returnBookFromPerson(new Integer[]{bookId});
        return "redirect:/books/"+bookId;
    }
    @PatchMapping("/owner/{id}")
    String giveBook(@ModelAttribute("book_owner") PersonEntity person,
                    @PathVariable("id") int bookId,
                    BindingResult bindingResult){
        bookOwnerValidator.validate(bookDAO.show(bookId).orElseGet(BookEntity::new),bindingResult);
        if(bindingResult.hasErrors()){
            return "redirect:/books/"+bookId;
        }
        personDAO.giveBookToPerson(person.getPerson_id(), bookId);
        return "redirect:/books/"+bookId;
    }
}
