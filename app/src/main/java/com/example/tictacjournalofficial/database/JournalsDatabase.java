package com.example.tictacjournalofficial.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tictacjournalofficial.dao.JournalsDao;
import com.example.tictacjournalofficial.entities.Journal;

@Database(entities = Journal.class, version = 2, exportSchema = false)
public abstract class JournalsDatabase extends RoomDatabase {
    private static JournalsDatabase journalsDatabase;

    public static synchronized JournalsDatabase getDatabase(Context context){
        if(journalsDatabase == null){
            journalsDatabase = Room.databaseBuilder(
                    context, JournalsDatabase.class,"journals_db"
            ).fallbackToDestructiveMigration()
                    .build();
        }
        return journalsDatabase;
    }

    public abstract JournalsDao journalDao();
}
