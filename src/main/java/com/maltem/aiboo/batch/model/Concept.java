package com.maltem.aiboo.batch.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
public class Concept implements Serializable {
    @Id
    private String name;

    private String babelSynsetId;

    /*@ManyToMany(mappedBy="concepts")
    private Set<Document> documents;*/

    @OneToMany(
            mappedBy = "concept",
            cascade = CascadeType.ALL
    )
    Set<ConceptOccurence> conceptOccurences;

}
