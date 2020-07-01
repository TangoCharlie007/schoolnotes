package com.sam.schoolnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sam.schoolnotes.database.NoteEntity;
import com.sam.schoolnotes.model.NotesAdapter;
import com.sam.schoolnotes.utils.SampleDataProvider;
import com.sam.schoolnotes.viewmodels.ListActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private ListActivityViewModel mViewModel;
    private List<NoteEntity> mNoteList = new ArrayList<>();
    private Toolbar toolbar;
    NotesAdapter mNotesAdapter;

    @BindView(R.id.notes_recyclerview)
    RecyclerView mRecyclerView;

    @OnClick(R.id.fab_add_note)
    void onFabClicked(){
        Intent intent = new Intent(MainActivity.this,EditorActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        initViewModel();

        ButterKnife.bind(this);
        initRecyclerView();


    }

    private void initViewModel() {

        Observer<List<NoteEntity>>  notesObserver = new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(List<NoteEntity> noteEntities) {
                mNoteList.clear();
                mNoteList.addAll(noteEntities);

                if(mNotesAdapter == null)
                {
                    mNotesAdapter = new NotesAdapter(MainActivity.this,mNoteList);
                    mRecyclerView.setAdapter(mNotesAdapter);
                }
                else
                {
                    mNotesAdapter.notifyDataSetChanged();
                }

            }
        };
        mViewModel = ViewModelProviders.of(this)
                .get(ListActivityViewModel.class);
        mViewModel.mNotesList.observe(MainActivity.this,notesObserver);
    }


    private void initRecyclerView() {
        mRecyclerView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                delteNote(mNotesAdapter.getNoteAtPosition(viewHolder.getAdapterPosition()));

            }
        });
        //Register TouchHelper with recycler view to work
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void delteNote(NoteEntity noteEntity) {
        mViewModel.deleteNote(noteEntity);
        Toast.makeText(this,"Note Deleted",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.add_simple_data:
                addSampleData();
                return true;

            case R.id.delete_all_data:
                deleteAllData();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void deleteAllData() {
        mViewModel.deleteAllData();

    }

    private void addSampleData() {
        mViewModel.addSampleData();
    }
}
