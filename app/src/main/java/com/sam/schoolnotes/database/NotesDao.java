package com.sam.schoolnotes.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotesDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(NoteEntity noteEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<NoteEntity> noteEntities);

    @Delete
    void deleteNote(NoteEntity noteEntity);

    @Query("SELECT * FROM  notes WHERE id= :id")
    NoteEntity getNoteById(int id);

    //When we return LiveData the Room will handle Multithreading by itself
    @Query("SELECT * FROM notes ORDER BY date DESC")
    LiveData<List<NoteEntity>> getALLNotes();

    @Query("DELETE FROM notes")
    int deleteALLNotes();

    @Query("SELECT COUNT(*) FROM notes")
    int getCount();
}
