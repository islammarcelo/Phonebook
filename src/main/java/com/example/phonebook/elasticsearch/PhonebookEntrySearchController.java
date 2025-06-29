package com.example.phonebook.elasticsearch;

import com.example.phonebook.util.InputSanitizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/phonebook/search")
public class PhonebookEntrySearchController {
    @Autowired
    private PhonebookEntrySearchService searchService;

    @Autowired
    private InputSanitizer sanitizer;

    @GetMapping
    public ResponseEntity<?> search(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size) {
        try {
            // Sanitize inputs
            String sanitizedKeyword = sanitizer.sanitizeSearchKeyword(keyword);
            Integer sanitizedPage = sanitizer.sanitizeInteger(String.valueOf(page));
            Integer sanitizedSize = sanitizer.sanitizeInteger(String.valueOf(size));

            if (sanitizedKeyword == null || sanitizedKeyword.isEmpty()) {
                return ResponseEntity.badRequest().body("Search keyword cannot be empty");
            }

            if (sanitizedPage == null)
                sanitizedPage = 0;
            if (sanitizedSize == null)
                sanitizedSize = 10;

            Page<PhonebookEntryDocument> result = searchService.searchByNameOrPhone(sanitizedKeyword, sanitizedPage,
                    sanitizedSize);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid search parameters: " + e.getMessage());
        }
    }
}