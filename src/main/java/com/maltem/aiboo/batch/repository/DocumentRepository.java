package com.maltem.aiboo.batch.repository;

import com.maltem.aiboo.batch.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document,String> {
    void save(String documentName);
}
