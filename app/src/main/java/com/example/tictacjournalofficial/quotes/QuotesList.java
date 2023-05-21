package com.example.tictacjournalofficial.quotes;

public class QuotesList {
    private final String quote;
    private final String writer;


    public QuotesList(String quote, String writer) {
        this.quote = quote;
        this.writer = writer;
    }


    public String getQoute(){
        return quote;
    }

    public String getWriter(){
        return writer;
    }

}
