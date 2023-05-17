package com.example.tictacjournalofficial.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tictacjournalofficial.entities.Journal;

import java.util.List;

@Dao
public interface JournalsDao {


    @Query("Select * FROM journals ORDER by id DESC")

    List<Journal> getAllJournals();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertJournal(Journal journal);

    @Delete
    void deleteJournal(Journal journal);


}
