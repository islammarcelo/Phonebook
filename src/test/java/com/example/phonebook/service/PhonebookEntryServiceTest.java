package com.example.phonebook.service;

import com.example.phonebook.elasticsearch.PhonebookEntryDocument;
import com.example.phonebook.elasticsearch.PhonebookEntrySearchRepository;
import com.example.phonebook.entity.PhonebookEntry;
import com.example.phonebook.repository.PhonebookEntryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PhonebookEntryServiceTest {

    @Mock
    private PhonebookEntryRepository repository;

    @Mock
    private PhonebookEntrySearchRepository searchRepository;

    @InjectMocks
    private PhonebookEntryService service;

    public PhonebookEntryServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEntry() {
        PhonebookEntry entry = new PhonebookEntry(null, "12345678", "Alice");
        PhonebookEntry saved = new PhonebookEntry(1L, "12345678", "Alice");
        PhonebookEntryDocument document = new PhonebookEntryDocument("1", "12345678", "Alice");

        when(repository.save(entry)).thenReturn(saved);
        when(searchRepository.save(any(PhonebookEntryDocument.class))).thenReturn(document);

        PhonebookEntry result = service.createEntry(entry);

        assertEquals("Alice", result.getName());
        assertEquals("12345678", result.getPhone());
        verify(repository).save(entry);
        verify(searchRepository).save(any(PhonebookEntryDocument.class));
    }

    @Test
    void testGetAllEntries() {
        List<PhonebookEntry> entries = Arrays.asList(
                new PhonebookEntry(1L, "12345678", "Alice"),
                new PhonebookEntry(2L, "87654321", "Bob"));
        when(repository.findAll()).thenReturn(entries);
        List<PhonebookEntry> result = service.getAllEntries();
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllEntriesWithPageable() {
        Page<PhonebookEntry> page = new PageImpl<>(Arrays.asList(
                new PhonebookEntry(1L, "12345678", "Alice")));
        when(repository.findAll(any(PageRequest.class))).thenReturn(page);
        Page<PhonebookEntry> result = service.getAllEntries(PageRequest.of(0, 1));
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetEntryById() {
        PhonebookEntry entry = new PhonebookEntry(1L, "12345678", "Alice");
        when(repository.findById(1L)).thenReturn(Optional.of(entry));
        Optional<PhonebookEntry> found = service.getEntryById(1L);
        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getName());
    }

    @Test
    void testUpdateEntry() {
        PhonebookEntry entry = new PhonebookEntry(1L, "12345678", "Alice");
        PhonebookEntryDocument document = new PhonebookEntryDocument("1", "12345678", "Alice");

        when(repository.save(entry)).thenReturn(entry);
        when(searchRepository.save(any(PhonebookEntryDocument.class))).thenReturn(document);

        PhonebookEntry updated = service.updateEntry(1L, entry);
        assertEquals(1L, updated.getId());
        verify(repository).save(entry);
        verify(searchRepository).save(any(PhonebookEntryDocument.class));
    }

    @Test
    void testDeleteEntry() {
        doNothing().when(repository).deleteById(1L);
        doNothing().when(searchRepository).deleteById("1");

        service.deleteEntry(1L);

        verify(repository).deleteById(1L);
        verify(searchRepository).deleteById("1");
    }
}
