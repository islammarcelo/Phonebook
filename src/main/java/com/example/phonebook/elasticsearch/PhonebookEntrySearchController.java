package com.example.phonebook.elasticsearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/phonebook/search")
public class PhonebookEntrySearchController {
    @Autowired
    private PhonebookEntrySearchService searchService;

    @GetMapping
    public Page<PhonebookEntryDocument> search(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size) {
        return searchService.searchByNameOrPhone(keyword, page, size);
    }

}