package com.example.phonebook.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InputSanitizerTest {

    private InputSanitizer sanitizer;

    @BeforeEach
    void setUp() {
        sanitizer = new InputSanitizer();
    }

    @Test
    void testSanitizePhone_ValidInput() {
        assertEquals("12345678", sanitizer.sanitizePhone("12345678"));
        assertEquals("12345678", sanitizer.sanitizePhone("123-456-78"));
        assertEquals("12345678", sanitizer.sanitizePhone("123 456 78"));
        assertEquals("12345678", sanitizer.sanitizePhone("(123) 456-78"));
    }

    @Test
    void testSanitizePhone_InvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> sanitizer.sanitizePhone("1234567"));
        assertThrows(IllegalArgumentException.class, () -> sanitizer.sanitizePhone("123456789"));
        assertThrows(IllegalArgumentException.class, () -> sanitizer.sanitizePhone("1234567a"));
    }

    @Test
    void testSanitizePhone_NullInput() {
        assertNull(sanitizer.sanitizePhone(null));
    }

    @Test
    void testSanitizeName_ValidInput() {
        assertEquals("John Doe", sanitizer.sanitizeName("john doe"));
        assertEquals("Mary-Jane", sanitizer.sanitizeName("mary-jane"));
        assertEquals("O'Connor", sanitizer.sanitizeName("o'connor"));
        assertEquals("Jean-Pierre", sanitizer.sanitizeName("jean-pierre"));
    }

    @Test
    void testSanitizeName_InvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> sanitizer.sanitizeName("John123"));
        assertThrows(IllegalArgumentException.class, () -> sanitizer.sanitizeName("John@Doe"));
        assertThrows(IllegalArgumentException.class, () -> sanitizer.sanitizeName(""));
        assertThrows(IllegalArgumentException.class, () -> sanitizer.sanitizeName("   "));
    }

    @Test
    void testSanitizeName_NullInput() {
        assertNull(sanitizer.sanitizeName(null));
    }

    @Test
    void testSanitizeSearchKeyword_ValidInput() {
        assertEquals("john", sanitizer.sanitizeSearchKeyword("john"));
        assertEquals("12345678", sanitizer.sanitizeSearchKeyword("12345678"));
        assertEquals("john-doe", sanitizer.sanitizeSearchKeyword("john-doe"));
        assertEquals("john doe", sanitizer.sanitizeSearchKeyword("john doe"));
    }

    @Test
    void testSanitizeSearchKeyword_InvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> sanitizer.sanitizeSearchKeyword("john@doe"));
        assertThrows(IllegalArgumentException.class, () -> sanitizer.sanitizeSearchKeyword("john<script>"));
        assertThrows(IllegalArgumentException.class, () -> sanitizer.sanitizeSearchKeyword("john&doe"));
        assertThrows(IllegalArgumentException.class, () -> sanitizer.sanitizeSearchKeyword("john!doe"));
        assertThrows(IllegalArgumentException.class, () -> sanitizer.sanitizeSearchKeyword("john#doe"));
    }

    @Test
    void testSanitizeSearchKeyword_EmptyInput() {
        assertNull(sanitizer.sanitizeSearchKeyword(""));
        assertNull(sanitizer.sanitizeSearchKeyword("   "));
    }

    @Test
    void testSanitizeSearchKeyword_NullInput() {
        assertNull(sanitizer.sanitizeSearchKeyword(null));
    }

    @Test
    void testEscapeHtml() {
        assertEquals("&lt;script&gt;alert(&#x27;xss&#x27;)&lt;&#x2F;script&gt;",
                sanitizer.escapeHtml("<script>alert('xss')</script>"));
        assertEquals("&amp;&lt;&gt;&quot;&#x27;&#x2F;",
                sanitizer.escapeHtml("&<>\"'/"));
        assertEquals("Hello World", sanitizer.escapeHtml("Hello World"));
    }

    @Test
    void testEscapeHtml_NullInput() {
        assertNull(sanitizer.escapeHtml(null));
    }

    @Test
    void testSanitizeText() {
        assertEquals("Hello World", sanitizer.sanitizeText("  Hello World  "));
        assertEquals("&lt;script&gt;", sanitizer.sanitizeText("<script>"));
        assertEquals("", sanitizer.sanitizeText("   "));
    }

    @Test
    void testSanitizeText_NullInput() {
        assertNull(sanitizer.sanitizeText(null));
    }

    @Test
    void testSanitizeInteger_ValidInput() {
        assertEquals(123, sanitizer.sanitizeInteger("123"));
        assertEquals(0, sanitizer.sanitizeInteger("0"));
        assertEquals(-123, sanitizer.sanitizeInteger("-123"));
        assertEquals(123, sanitizer.sanitizeInteger("  123  "));
    }

    @Test
    void testSanitizeInteger_InvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> sanitizer.sanitizeInteger("abc"));
        assertThrows(IllegalArgumentException.class, () -> sanitizer.sanitizeInteger("123.45"));
    }

    @Test
    void testSanitizeInteger_EmptyInput() {
        assertNull(sanitizer.sanitizeInteger(""));
        assertNull(sanitizer.sanitizeInteger("   "));
    }

    @Test
    void testSanitizeInteger_NullInput() {
        assertNull(sanitizer.sanitizeInteger(null));
    }

    @Test
    void testSanitizeLong_ValidInput() {
        assertEquals(123L, sanitizer.sanitizeLong("123"));
        assertEquals(0L, sanitizer.sanitizeLong("0"));
        assertEquals(-123L, sanitizer.sanitizeLong("-123"));
        assertEquals(123L, sanitizer.sanitizeLong("  123  "));
    }

    @Test
    void testSanitizeLong_InvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> sanitizer.sanitizeLong("abc"));
        assertThrows(IllegalArgumentException.class, () -> sanitizer.sanitizeLong("123.45"));
    }

    @Test
    void testSanitizeLong_EmptyInput() {
        assertNull(sanitizer.sanitizeLong(""));
        assertNull(sanitizer.sanitizeLong("   "));
    }

    @Test
    void testSanitizeLong_NullInput() {
        assertNull(sanitizer.sanitizeLong(null));
    }

    @Test
    void testContainsDangerousContent() {
        assertTrue(sanitizer.containsDangerousContent("<script>alert('xss')</script>"));
        assertTrue(sanitizer.containsDangerousContent("javascript:alert('xss')"));
        assertTrue(sanitizer.containsDangerousContent("SELECT * FROM users"));
        assertTrue(sanitizer.containsDangerousContent("DROP TABLE users"));
        assertTrue(sanitizer.containsDangerousContent("../etc/passwd"));
        assertTrue(sanitizer.containsDangerousContent("..\\windows\\system32"));

        assertFalse(sanitizer.containsDangerousContent("Hello World"));
        assertFalse(sanitizer.containsDangerousContent("john doe"));
        assertFalse(sanitizer.containsDangerousContent("12345678"));
    }

    @Test
    void testContainsDangerousContent_NullInput() {
        assertFalse(sanitizer.containsDangerousContent(null));
    }
}