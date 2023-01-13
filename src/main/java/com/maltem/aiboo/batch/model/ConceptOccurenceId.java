package com.maltem.aiboo.batch.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Embeddable
public class ConceptOccurenceId implements Serializable {

    private String document_name;
    private String concept_name;


}
