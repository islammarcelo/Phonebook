package com.example.phonebook.controller;

import com.example.phonebook.elasticsearch.PhonebookEntryDocument;
import com.example.phonebook.elasticsearch.PhonebookEntrySearchService;
import com.example.phonebook.entity.PhonebookEntry;
import com.example.phonebook.service.PhonebookEntryService;
import com.example.phonebook.util.InputSanitizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.core.env.Environment;

@Controller
@RequestMapping("/phonebook")
public class PhonebookWebController {
    @Autowired
    private PhonebookEntryService service;

    @Autowired
    private PhonebookEntrySearchService phonebookEntrySearchService;

    @Autowired
    private InputSanitizer sanitizer;

    @Autowired
    private Environment env;

    @GetMapping
    public String listEntries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keyword,
            Model model) {

        try {
            // Sanitize inputs
            Integer sanitizedPage = sanitizer.sanitizeInteger(String.valueOf(page));
            Integer sanitizedSize = sanitizer.sanitizeInteger(String.valueOf(size));
            String sanitizedKeyword = sanitizer.sanitizeSearchKeyword(keyword);

            if (sanitizedPage == null)
                sanitizedPage = 0;
            if (sanitizedSize == null)
                sanitizedSize = 5;

            Pageable pageable = PageRequest.of(sanitizedPage, sanitizedSize);

            if (sanitizedKeyword != null && !sanitizedKeyword.isEmpty()) {
                Page<PhonebookEntryDocument> entryPage = phonebookEntrySearchService
                        .searchByNameOrPhone(sanitizedKeyword, sanitizedPage, sanitizedSize);
                model.addAttribute("entryPage", entryPage);
            } else {
                Page<PhonebookEntry> entryPage = service.getAllEntries(pageable);
                model.addAttribute("entryPage", entryPage);
            }

            model.addAttribute("currentPage", sanitizedPage);
            model.addAttribute("pageSize", sanitizedSize);
            model.addAttribute("keyword", sanitizedKeyword);

            // Add active profile to the model
            String[] profiles = env.getActiveProfiles();
            String activeProfile = profiles.length > 0 ? profiles[0] : "default";
            model.addAttribute("activeProfile", activeProfile);

        } catch (IllegalArgumentException e) {
            // Handle sanitization errors gracefully
            model.addAttribute("error", "Invalid search input: " + e.getMessage());

            // Show empty results but keep the original keyword for display
            Page<PhonebookEntry> emptyPage = service.getAllEntries(PageRequest.of(0, 5));
            model.addAttribute("entryPage", emptyPage);
            model.addAttribute("currentPage", 0);
            model.addAttribute("pageSize", 5);
            model.addAttribute("keyword", keyword); // Keep original keyword for display
            // Add active profile to the model in error case too
            String[] profiles = env.getActiveProfiles();
            String activeProfile = profiles.length > 0 ? profiles[0] : "default";
            model.addAttribute("activeProfile", activeProfile);
        }

        return "phonebook_list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("entry", new PhonebookEntry());
        return "phonebook_form";
    }

    @PostMapping("/save")
    public String saveEntry(@ModelAttribute PhonebookEntry entry, RedirectAttributes redirectAttributes) {
        try {
            // Sanitize entry data
            if (entry.getName() != null) {
                entry.setName(sanitizer.sanitizeName(entry.getName()));
            }
            if (entry.getPhone() != null) {
                entry.setPhone(sanitizer.sanitizePhone(entry.getPhone()));
            }

            service.createEntry(entry);
            redirectAttributes.addFlashAttribute("success", "Entry saved successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/phonebook/new";
        }

        return "redirect:/phonebook";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Long sanitizedId = sanitizer.sanitizeLong(id);
            if (sanitizedId == null) {
                redirectAttributes.addFlashAttribute("error", "Invalid entry ID");
                return "redirect:/phonebook";
            }

            PhonebookEntry entry = service.getEntryById(sanitizedId).orElseThrow();
            model.addAttribute("entry", entry);
            return "phonebook_form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Entry not found");
            return "redirect:/phonebook";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteEntry(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            Long sanitizedId = sanitizer.sanitizeLong(id);
            if (sanitizedId == null) {
                redirectAttributes.addFlashAttribute("error", "Invalid entry ID");
                return "redirect:/phonebook";
            }

            service.deleteEntry(sanitizedId);
            redirectAttributes.addFlashAttribute("success", "Entry deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete entry");
        }

        return "redirect:/phonebook";
    }
}
