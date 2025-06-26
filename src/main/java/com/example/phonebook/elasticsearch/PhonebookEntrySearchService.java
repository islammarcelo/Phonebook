package com.example.phonebook.elasticsearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Service
public class PhonebookEntrySearchService {
    @Autowired
    private PhonebookEntrySearchRepository searchRepository;

    public Page<PhonebookEntryDocument> searchByNameOrPhone(String keyword, int page, int size) {
        return searchRepository.searchByNameOrPhone(keyword, PageRequest.of(page, size));
    }
}