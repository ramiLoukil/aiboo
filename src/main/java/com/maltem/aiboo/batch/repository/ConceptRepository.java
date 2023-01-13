package com.maltem.aiboo.batch.repository;

import com.maltem.aiboo.batch.model.Concept;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConceptRepository extends JpaRepository<Concept,String> {
}
