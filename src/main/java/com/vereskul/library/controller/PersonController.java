package com.vereskul.library.controller;

import com.vereskul.library.dao.BookDAO;
import com.vereskul.library.dao.PersonDAO;
import com.vereskul.library.model.BookEntity;
import com.vereskul.library.model.PersonEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/person")
public class PersonController {
    private final PersonDAO personDAO;
    private final BookDAO bookDAO;

    public PersonController(PersonDAO personDAO, BookDAO bookDAO) {
        this.personDAO = personDAO;
        this.bookDAO = bookDAO;
    }
    @GetMapping
    public String index(Model model){
        model.addAttribute("person_list", personDAO.index());
        return "person/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable int id, Model model){
        model.addAttribute("person", personDAO.show(id).orElseGet(PersonEntity::new));
        model.addAttribute("book", new BookEntity());
        model.addAttribute("person_books", personDAO.getListOfPersonBooks(id));
        model.addAttribute("list_of_free_books", bookDAO.getListOfFreeBooks());
        return "person/show";
    }

    @GetMapping("/new")
    public String addPerson(@ModelAttribute("person") PersonEntity person, Model model){
        return "person/new";
    }

    @PostMapping("/owned_books/{id}")
    public String addBookToPerson(@ModelAttribute("book")BookEntity book, @PathVariable int id){
        personDAO.giveBookToPerson(id, book.getBook_id());
        return "redirect:/person/"+id;
    }

    @PostMapping("/new")
    public String add(@ModelAttribute @Valid PersonEntity person, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "person/new";
        }
        personDAO.save(person);
        return "redirect:/person";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id){
        personDAO.delete(id);
        return "redirect:/person";
    }

    @DeleteMapping("/owned_books/{id}")
    public String deletePersonBook(@RequestParam("return") Integer[] books, @PathVariable int id){
        bookDAO.returnBookFromPerson(books);
        return "redirect:/person/"+id;
    }
}
