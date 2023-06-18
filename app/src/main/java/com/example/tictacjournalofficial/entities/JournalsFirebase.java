package com.example.tictacjournalofficial.entities;

public class JournalsFirebase {
    private String title;
    private String content;
    private String date;

    public JournalsFirebase() {
    }

    public JournalsFirebase(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }
}
