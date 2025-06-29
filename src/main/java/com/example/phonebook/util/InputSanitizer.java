package com.example.phonebook.util;

import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class InputSanitizer {

    // Pattern for phone numbers (8 digits only)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{8}$");

    // Pattern for names (letters, spaces, hyphens, apostrophes only)
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s\\-']+$");

    // Pattern for search keywords (alphanumeric, spaces, basic punctuation)
    private static final Pattern SEARCH_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s\\-']+$");

    /**
     * Escape HTML entities to prevent XSS
     */
    public String escapeHtml(String input) {
        if (input == null) {
            return null;
        }

        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;")
                .replace("/", "&#x2F;");
    }

    /**
     * Sanitize phone number input
     */
    public String sanitizePhone(String phone) {
        if (phone == null) {
            return null;
        }

        // Remove all non-digit characters
        String cleaned = phone.replaceAll("[^\\d]", "");

        // Validate format
        if (!PHONE_PATTERN.matcher(cleaned).matches()) {
            throw new IllegalArgumentException("Phone number must be exactly 8 digits");
        }

        return cleaned;
    }

    /**
     * Sanitize name input
     */
    public String sanitizeName(String name) {
        if (name == null) {
            return null;
        }

        // Trim whitespace
        String trimmed = name.trim();

        // Check for empty string
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        // Validate format
        if (!NAME_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("Name contains invalid characters");
        }

        // Capitalize first letter of each word
        return capitalizeWords(trimmed);
    }

    /**
     * Sanitize search keyword
     */
    public String sanitizeSearchKeyword(String keyword) {
        if (keyword == null) {
            return null;
        }

        // Trim whitespace
        String trimmed = keyword.trim();

        // If empty, return null
        if (trimmed.isEmpty()) {
            return null;
        }

        // Check for dangerous content first
        if (containsDangerousContent(trimmed)) {
            throw new IllegalArgumentException("Search keyword contains potentially dangerous content");
        }

        // Validate format - be more specific about what's allowed
        if (!SEARCH_PATTERN.matcher(trimmed).matches()) {
            // Find the first invalid character to provide a helpful error message
            for (int i = 0; i < trimmed.length(); i++) {
                char c = trimmed.charAt(i);
                if (!Character.isLetterOrDigit(c) && c != ' ' && c != '-' && c != '\'') {
                    throw new IllegalArgumentException("Search keyword contains invalid character: '" + c
                            + "'. Only letters, numbers, spaces, hyphens, and apostrophes are allowed.");
                }
            }
            throw new IllegalArgumentException("Search keyword contains invalid characters");
        }

        // Escape HTML entities to prevent XSS
        return escapeHtml(trimmed);
    }

    /**
     * Sanitize general text input
     */
    public String sanitizeText(String text) {
        if (text == null) {
            return null;
        }

        // Trim whitespace
        String trimmed = text.trim();

        // Escape HTML entities
        return escapeHtml(trimmed);
    }

    /**
     * Sanitize numeric input
     */
    public Integer sanitizeInteger(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric input");
        }
    }

    /**
     * Sanitize Long input
     */
    public Long sanitizeLong(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        try {
            return Long.parseLong(input.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric input");
        }
    }

    /**
     * Capitalize first letter of each word
     */
    private String capitalizeWords(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        // Split by whitespace, but preserve hyphens and apostrophes
        String[] words = text.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                result.append(" ");
            }
            if (!words[i].isEmpty()) {
                // Handle hyphenated words
                if (words[i].contains("-")) {
                    String[] hyphenParts = words[i].split("-");
                    for (int j = 0; j < hyphenParts.length; j++) {
                        if (j > 0)
                            result.append("-");
                        if (!hyphenParts[j].isEmpty()) {
                            result.append(Character.toUpperCase(hyphenParts[j].charAt(0)))
                                    .append(hyphenParts[j].substring(1).toLowerCase());
                        }
                    }
                } else {
                    // Handle words with apostrophes (like O'Connor)
                    if (words[i].contains("'")) {
                        String[] apostropheParts = words[i].split("'");
                        for (int j = 0; j < apostropheParts.length; j++) {
                            if (j > 0)
                                result.append("'");
                            if (!apostropheParts[j].isEmpty()) {
                                result.append(Character.toUpperCase(apostropheParts[j].charAt(0)))
                                        .append(apostropheParts[j].substring(1).toLowerCase());
                            }
                        }
                    } else {
                        result.append(Character.toUpperCase(words[i].charAt(0)))
                                .append(words[i].substring(1).toLowerCase());
                    }
                }
            }
        }

        return result.toString();
    }

    /**
     * Check if input contains potentially dangerous content
     */
    public boolean containsDangerousContent(String input) {
        if (input == null) {
            return false;
        }

        String lowerInput = input.toLowerCase();

        // Check for script tags
        if (lowerInput.contains("<script") || lowerInput.contains("javascript:")) {
            return true;
        }

        // Check for SQL injection patterns
        if (lowerInput.contains("union") || lowerInput.contains("select") ||
                lowerInput.contains("drop") || lowerInput.contains("delete") ||
                lowerInput.contains("insert") || lowerInput.contains("update")) {
            return true;
        }

        // Check for path traversal
        if (lowerInput.contains("../") || lowerInput.contains("..\\")) {
            return true;
        }

        return false;
    }
}