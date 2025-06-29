package com.example.phonebook.controller;

import com.example.phonebook.entity.PhonebookEntry;
import com.example.phonebook.service.PhonebookEntryService;
import com.example.phonebook.util.InputSanitizer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PhonebookEntryController.class)
class PhonebookEntryControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private PhonebookEntryService service;

        @MockBean
        private InputSanitizer sanitizer;

        @Test
        void testCreateEntry() throws Exception {
                PhonebookEntry entry = new PhonebookEntry(null, "12345678", "Alice");
                PhonebookEntry saved = new PhonebookEntry(1L, "12345678", "Alice");
                when(service.createEntry(any(PhonebookEntry.class))).thenReturn(saved);
                when(sanitizer.sanitizeName("Alice")).thenReturn("Alice");
                when(sanitizer.sanitizePhone("12345678")).thenReturn("12345678");

                mockMvc.perform(post("/api/phonebook")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"phone\":\"12345678\",\"name\":\"Alice\"}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.name").value("Alice"));
        }

        @Test
        void testGetAllEntriesWithPagination() throws Exception {
                when(service.getAllEntries(any(PageRequest.class)))
                                .thenReturn(new PageImpl<>(Arrays.asList(new PhonebookEntry(1L, "12345678", "Alice"))));

                mockMvc.perform(get("/api/phonebook?page=0&size=1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].name").value("Alice"));
        }

        @Test
        void testGetEntryById() throws Exception {
                when(service.getEntryById(1L)).thenReturn(Optional.of(new PhonebookEntry(1L, "12345678", "Alice")));
                when(sanitizer.sanitizeLong("1")).thenReturn(1L);

                mockMvc.perform(get("/api/phonebook/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Alice"));
        }

        @Test
        void testUpdateEntry() throws Exception {
                when(service.getEntryById(1L)).thenReturn(Optional.of(new PhonebookEntry(1L, "12345678", "Alice")));
                when(service.updateEntry(Mockito.eq(1L), any(PhonebookEntry.class)))
                                .thenReturn(new PhonebookEntry(1L, "12345678", "Alice Updated"));
                when(sanitizer.sanitizeLong("1")).thenReturn(1L);
                when(sanitizer.sanitizeName("Alice Updated")).thenReturn("Alice Updated");
                when(sanitizer.sanitizePhone("12345678")).thenReturn("12345678");

                mockMvc.perform(put("/api/phonebook/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"phone\":\"12345678\",\"name\":\"Alice Updated\"}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Alice Updated"));
        }

        @Test
        void testDeleteEntry() throws Exception {
                when(service.getEntryById(1L)).thenReturn(Optional.of(new PhonebookEntry(1L, "12345678", "Alice")));
                when(sanitizer.sanitizeLong("1")).thenReturn(1L);

                mockMvc.perform(delete("/api/phonebook/1"))
                                .andExpect(status().isNoContent());
        }
}