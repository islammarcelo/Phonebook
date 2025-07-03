package com.example.phonebook.controller;

import com.example.phonebook.service.PhonebookEntryService;
import com.example.phonebook.elasticsearch.PhonebookEntrySearchService;
import com.example.phonebook.util.InputSanitizer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PhonebookWebController.class)
@TestPropertySource(properties = {
                "app.title=Phonebook - Test"
})
class PhonebookWebControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private PhonebookEntryService service;

        @MockBean
        private PhonebookEntrySearchService searchService;

        @MockBean
        private InputSanitizer sanitizer;

        @Test
        void testListEntries_WithValidSearch() throws Exception {
                when(service.getAllEntries(any(PageRequest.class)))
                                .thenReturn(new PageImpl<>(Arrays.asList()));

                mockMvc.perform(get("/phonebook")
                                .param("keyword", "john"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("phonebook_list"));
        }

        @Test
        void testListEntries_WithInvalidSearch_ShowsError() throws Exception {
                // Mock sanitizer to throw exception for invalid input
                when(sanitizer.sanitizeSearchKeyword("john@doe"))
                                .thenThrow(new IllegalArgumentException(
                                                "Search keyword contains invalid character: '@'. Only letters, numbers, spaces, hyphens, and apostrophes are allowed."));

                when(service.getAllEntries(any(PageRequest.class)))
                                .thenReturn(new PageImpl<>(Arrays.asList()));

                mockMvc.perform(get("/phonebook")
                                .param("keyword", "john@doe"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("phonebook_list"))
                                .andExpect(model().attributeExists("error"))
                                .andExpect(model().attribute("error",
                                                "Invalid search input: Search keyword contains invalid character: '@'. Only letters, numbers, spaces, hyphens, and apostrophes are allowed."));
        }

        @Test
        void testListEntries_WithDangerousSearch_ShowsError() throws Exception {
                // Mock sanitizer to throw exception for dangerous content
                when(sanitizer.sanitizeSearchKeyword("<script>alert('xss')</script>"))
                                .thenThrow(new IllegalArgumentException(
                                                "Search keyword contains potentially dangerous content"));

                when(service.getAllEntries(any(PageRequest.class)))
                                .thenReturn(new PageImpl<>(Arrays.asList()));

                mockMvc.perform(get("/phonebook")
                                .param("keyword", "<script>alert('xss')</script>"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("phonebook_list"))
                                .andExpect(model().attributeExists("error"))
                                .andExpect(model().attribute("error",
                                                "Invalid search input: Search keyword contains potentially dangerous content"));
        }

        @Test
        void testListEntries_IncludesAppTitle() throws Exception {
                when(service.getAllEntries(any(PageRequest.class)))
                                .thenReturn(new PageImpl<>(Arrays.asList()));

                mockMvc.perform(get("/phonebook"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("phonebook_list"))
                                .andExpect(model().attribute("appTitle", "Phonebook - Test"));
        }
}