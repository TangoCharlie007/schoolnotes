package com.sam.schoolnotes;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sam.schoolnotes.database.NoteEntity;
import com.sam.schoolnotes.utils.Constants;
import com.sam.schoolnotes.viewmodels.EditorViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sam.schoolnotes.utils.Constants.EDITING_KEY;

public class EditorActivity extends AppCompatActivity {
    private EditorViewModel mViewModel;
    private boolean mNewNote;
    private boolean isEditing;

    @BindView(R.id.edit_note_text)
    TextView mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        if (savedInstanceState != null)
        {
            isEditing= savedInstanceState.getBoolean(EDITING_KEY);
        }

        initViewModel();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putBoolean(EDITING_KEY,true);
        super.onSaveInstanceState(outState);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(EditorViewModel.class);

        mViewModel.mLiveNote.observe(this, new Observer<NoteEntity>() {
            @Override
            public void onChanged(NoteEntity noteEntity) {
                if(noteEntity != null && !isEditing)
                {
                    mEditText.setText(noteEntity.getText());

                }
            }
        });

        Bundle bundle =getIntent().getExtras();

        if (bundle==null)
        {
            setTitle("New Note");
            mNewNote = true;
        }
        else
        {
            setTitle("Edit Note");
            int noteId = bundle.getInt(Constants.NOTE_ID_KEY);
            mViewModel.loadNote(noteId);
            mNewNote = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!mNewNote)
        {
            getMenuInflater().inflate(R.menu.menu_editor,menu);
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            saveAndExit();
            finish();
            return true;
        }
        else if(item.getItemId() == R.id.action_delete_note)
        {
            deleteNote();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteNote() {
        mViewModel.deleteNote();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveAndExit();
    }

    private void saveAndExit() {
        mViewModel.saveAndExit(mEditText.getText().toString());
    }
}
