package com.example.phonebook.controller;

import com.example.phonebook.entity.PhonebookEntry;
import com.example.phonebook.service.PhonebookEntryService;
import com.example.phonebook.util.InputSanitizer;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/phonebook")
public class PhonebookEntryController {
    @Autowired
    private PhonebookEntryService service;

    @Autowired
    private InputSanitizer sanitizer;

    @PostMapping
    public ResponseEntity<?> createEntry(@Valid @RequestBody PhonebookEntry entry) {
        try {
            // Sanitize entry data
            if (entry.getName() != null) {
                entry.setName(sanitizer.sanitizeName(entry.getName()));
            }
            if (entry.getPhone() != null) {
                entry.setPhone(sanitizer.sanitizePhone(entry.getPhone()));
            }

            PhonebookEntry saved = service.createEntry(entry);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        }
    }

    @GetMapping
    public Page<PhonebookEntry> getAllEntries(Pageable pageable) {
        return service.getAllEntries(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhonebookEntry> getEntryById(@PathVariable String id) {
        try {
            Long sanitizedId = sanitizer.sanitizeLong(id);
            if (sanitizedId == null) {
                return ResponseEntity.badRequest().build();
            }

            return service.getEntryById(sanitizedId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEntry(@PathVariable String id, @Valid @RequestBody PhonebookEntry entry) {
        try {
            Long sanitizedId = sanitizer.sanitizeLong(id);
            if (sanitizedId == null) {
                return ResponseEntity.badRequest().body("Invalid entry ID");
            }

            if (!service.getEntryById(sanitizedId).isPresent()) {
                return ResponseEntity.notFound().build();
            }

            // Sanitize entry data
            if (entry.getName() != null) {
                entry.setName(sanitizer.sanitizeName(entry.getName()));
            }
            if (entry.getPhone() != null) {
                entry.setPhone(sanitizer.sanitizePhone(entry.getPhone()));
            }

            PhonebookEntry updated = service.updateEntry(sanitizedId, entry);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid entry ID");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEntry(@PathVariable String id) {
        try {
            Long sanitizedId = sanitizer.sanitizeLong(id);
            if (sanitizedId == null) {
                return ResponseEntity.badRequest().body("Invalid entry ID");
            }

            if (!service.getEntryById(sanitizedId).isPresent()) {
                return ResponseEntity.notFound().build();
            }

            service.deleteEntry(sanitizedId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid entry ID");
        }
    }
}