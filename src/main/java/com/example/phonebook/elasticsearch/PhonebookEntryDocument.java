package com.example.phonebook.elasticsearch;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "phonebook")
public class PhonebookEntryDocument {
    @Id
    private String id;
    private String phone;
    private String name;
}