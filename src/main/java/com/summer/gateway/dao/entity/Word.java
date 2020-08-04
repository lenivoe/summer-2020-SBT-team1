package com.summer.gateway.dao.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int index;
    private String value;

    public Word() {
    }

    public Word(String value, int index) {
        this.value = value;
        this.index = index;
    }

    public boolean compareWord(String word) {
        if (this.getValue().equals("{}"))
            return true;
        else {
            return this.getValue().equals(word);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", index=" + index +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return index == word.index &&
                Objects.equals(id, word.id) &&
                Objects.equals(value, word.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, index);
    }
}
