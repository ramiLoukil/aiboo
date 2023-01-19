package com.maltem.aiboo.batch.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
public class Document implements Serializable {

    @Id
    private String name;

    private String type;

    private String content;

    /*@ManyToMany
    @JoinTable(
            name = "document_concept",
            joinColumns = @JoinColumn(name = "document_name"),
            inverseJoinColumns = @JoinColumn(name = "concept_name"))
    private Set<Concept> concepts;*/

    @OneToMany(
            mappedBy = "document",
            cascade = CascadeType.ALL
    )
    Set<ConceptOccurence> conceptOccurences;

    private LocalDate extractionDate;

    private Boolean valid;

    private String language;

    @Transient
    private Map<String,Long> tokensWithOccurence;

}
