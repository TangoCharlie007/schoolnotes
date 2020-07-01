package com.sam.schoolnotes.database;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.sam.schoolnotes.utils.SampleDataProvider;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.content.ContentValues.TAG;

//Singleton class only one instance of class is created
public class AppRepository {
    private static AppRepository ourInstance;
    public LiveData<List<NoteEntity>> mNotesList;
    private Executor mExecutor = Executors.newSingleThreadExecutor();
    private AppDatabase mDatabase;

    public static AppRepository getInstance(Context context){
        return ourInstance = new AppRepository(context);
    }

    private AppRepository(Context context)
    {
        mDatabase = AppDatabase.getInstance(context);
        mNotesList = getAllNotes();
    }

    public void addSampleData() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.notesDao().insertAll(SampleDataProvider.getSampleData());

            }
        });

    }

    private LiveData<List<NoteEntity>> getAllNotes()
    {
       return mDatabase.notesDao().getALLNotes();
    }

    public void deleteAllData() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                int notes = mDatabase.notesDao().deleteALLNotes();
                Log.d("MyTag","run: notes deleted : "+notes);
            }
        });
    }

    public NoteEntity loadNote(int noteId) {
        return mDatabase.notesDao().getNoteById(noteId);
    }

    public void insertNote(final NoteEntity noteEntity) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.notesDao().insertNote(noteEntity);
            }
        });

    }

    public void deleteNote(final NoteEntity noteEntity) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.notesDao().deleteNote(noteEntity);
            }
        });
    }
}
