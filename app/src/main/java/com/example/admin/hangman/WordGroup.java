package com.example.admin.hangman;

import java.util.List;

public class WordGroup {

    private Integer level;
    private List<String> words;

    public WordGroup() {
    }

    public WordGroup(Integer level, List<String> words) {
        this.level = level;
        this.words = words;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }
}
