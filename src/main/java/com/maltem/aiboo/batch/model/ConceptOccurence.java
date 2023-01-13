package com.maltem.aiboo.batch.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name="document_concept")
public class ConceptOccurence implements Serializable {

    @EmbeddedId
    private ConceptOccurenceId conceptOccurenceId;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("document_name")
    @JoinColumn(name = "document_name")
    private Document document;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("concept_name")
    @JoinColumn(name = "concept_name")
    private Concept concept;

    private int occurence;

    /*public ConceptOccurence(Concept concept, int occurence) {
        this.concept = concept;
        this.occurence = occurence;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConceptOccurence)) return false;
        ConceptOccurence that = (ConceptOccurence) o;
        return Objects.equals(document.getName(), that.document.getName()) &&
                Objects.equals(concept.getName(), that.concept.getName()); //&&
                //Objects.equals(occurence, that.occurence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(document.getName(), concept.getName());//, occurence);
    }

}
