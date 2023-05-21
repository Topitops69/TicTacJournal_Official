package com.example.tictacjournalofficial.quotes;

import java.util.ArrayList;
import java.util.List;

public class QuotesData {
    public static List<QuotesList> getLifeQuotes(){
        final List<QuotesList> lifeQuotesList = new ArrayList<>();

        QuotesList q1 = new QuotesList("I am the master of my mind. I am the captain of my soul.", "Anonymous");
        lifeQuotesList.add(q1);
        QuotesList q2 = new QuotesList("Hey you, everything's gonna be alright. ;) ", "Topitops");
        lifeQuotesList.add(q2);
        QuotesList q3 = new QuotesList("Keep going and never give up!", "Your future self.");
        lifeQuotesList.add(q3);

        return lifeQuotesList;
    }

    //you can add more here same ras igbaw ang format
}
