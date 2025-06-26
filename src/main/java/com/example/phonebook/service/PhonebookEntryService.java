package com.example.phonebook.service;

import com.example.phonebook.entity.PhonebookEntry;
import com.example.phonebook.repository.PhonebookEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhonebookEntryService {
    @Autowired
    private PhonebookEntryRepository repository;

    public PhonebookEntry createEntry(PhonebookEntry entry) {
        return repository.save(entry);
    }

    public List<PhonebookEntry> getAllEntries() {
        return repository.findAll();
    }

    public Optional<PhonebookEntry> getEntryById(Long id) {
        return repository.findById(id);
    }

    public PhonebookEntry updateEntry(Long id, PhonebookEntry entry) {
        entry.setId(id);
        return repository.save(entry);
    }

    public void deleteEntry(Long id) {
        repository.deleteById(id);
    }

    public Page<PhonebookEntry> getAllEntries(Pageable pageable) {
        return repository.findAll(pageable);
    }
}