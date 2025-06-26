package com.example.phonebook.repository;

import com.example.phonebook.entity.PhonebookEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhonebookEntryRepository extends JpaRepository<PhonebookEntry, Long> {
}