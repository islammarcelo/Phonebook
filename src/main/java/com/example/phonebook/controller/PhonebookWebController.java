package com.example.phonebook.controller;

import com.example.phonebook.entity.PhonebookEntry;
import com.example.phonebook.service.PhonebookEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Controller
@RequestMapping("/phonebook")
public class PhonebookWebController {
    @Autowired
    private PhonebookEntryService service;

    @GetMapping
    public String listEntries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PhonebookEntry> entryPage = service.getAllEntries(pageable);
        model.addAttribute("entryPage", entryPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "phonebook_list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("entry", new PhonebookEntry());
        return "phonebook_form";
    }

    @PostMapping("/save")
    public String saveEntry(@ModelAttribute PhonebookEntry entry) {
        service.createEntry(entry);
        return "redirect:/phonebook";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        PhonebookEntry entry = service.getEntryById(id).orElseThrow();
        model.addAttribute("entry", entry);
        return "phonebook_form";
    }

    @GetMapping("/delete/{id}")
    public String deleteEntry(@PathVariable Long id) {
        service.deleteEntry(id);
        return "redirect:/phonebook";
    }
}
