package com.example.phonebook.service;

import com.example.phonebook.elasticsearch.PhonebookEntryDocument;
import com.example.phonebook.elasticsearch.PhonebookEntrySearchRepository;
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
    @Autowired
    private PhonebookEntrySearchRepository searchRepository;

    public PhonebookEntry createEntry(PhonebookEntry entry) {
        PhonebookEntry saved = repository.save(entry);
        searchRepository.save(toDocument(saved));
        return saved;

    }

    public List<PhonebookEntry> getAllEntries() {
        return repository.findAll();
    }

    public Optional<PhonebookEntry> getEntryById(Long id) {
        return repository.findById(id);
    }

    public PhonebookEntry updateEntry(Long id, PhonebookEntry entry) {
        entry.setId(id);
        PhonebookEntry saved = repository.save(entry);
        searchRepository.save(toDocument(saved));
        return saved;
    }

    public void deleteEntry(Long id) {
        repository.deleteById(id);
        searchRepository.deleteById(id.toString());

    }

    public Page<PhonebookEntry> getAllEntries(Pageable pageable) {
        return repository.findAll(pageable);
    }

    private PhonebookEntryDocument toDocument(PhonebookEntry phonebookEntry) {
        PhonebookEntryDocument doc = new PhonebookEntryDocument();
        doc.setId(phonebookEntry.getId().toString());
        doc.setName(phonebookEntry.getName());
        doc.setPhone(phonebookEntry.getPhone());
        return doc;
    }
}