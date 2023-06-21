package com.example.tictacjournalofficial.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tictacjournalofficial.entities.ColorCount;
import com.example.tictacjournalofficial.entities.Journal;

import java.util.List;

@Dao
public interface JournalsDao {

    @Query("Select * FROM journals ORDER by id DESC")
    List<Journal> getAllJournals();

    @Query("SELECT color, COUNT(*) as count FROM journals GROUP BY color")
    LiveData<List<ColorCount>> getColorCounts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertJournal(Journal journal);

    @Query("SELECT * FROM journals WHERE title LIKE :searchQuery OR subtitle LIKE :searchQuery OR journal_text LIKE :searchQuery")
    List<Journal> searchJournals(String searchQuery);

    @Delete
    void deleteJournal(Journal journal);
}
