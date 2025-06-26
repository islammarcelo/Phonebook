package com.example.phonebook.elasticsearch;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PhonebookEntrySearchRepository extends ElasticsearchRepository<PhonebookEntryDocument, String> {

    @Query("""
            {
              "bool": {
                "should": [
                  { "match_phrase_prefix": { "name": "?0" }},
                  { "match_phrase_prefix": { "phone": "?0" }}
                ]
              }
            }
            """)
    Page<PhonebookEntryDocument> searchByNameOrPhone(String keyword, Pageable pageable);
}