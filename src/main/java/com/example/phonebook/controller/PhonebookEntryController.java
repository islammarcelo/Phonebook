package com.example.phonebook.controller;

import com.example.phonebook.entity.PhonebookEntry;
import com.example.phonebook.service.PhonebookEntryService;

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

    @PostMapping
    public ResponseEntity<PhonebookEntry> createEntry(@Valid @RequestBody PhonebookEntry entry) {
        return ResponseEntity.ok(service.createEntry(entry));
    }

    @GetMapping
    public Page<PhonebookEntry> getAllEntries(Pageable pageable) {
        return service.getAllEntries(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhonebookEntry> getEntryById(@PathVariable Long id) {
        return service.getEntryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhonebookEntry> updateEntry(@PathVariable Long id, @Valid @RequestBody PhonebookEntry entry) {
        if (!service.getEntryById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.updateEntry(id, entry));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long id) {
        if (!service.getEntryById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }
}