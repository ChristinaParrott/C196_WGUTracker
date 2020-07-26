package com.example.wgutracker.ui.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wgutracker.NoteDetailActivity;
import com.example.wgutracker.NoteEditorActivity;
import com.example.wgutracker.R;
import com.example.wgutracker.database.NoteEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wgutracker.utilities.Constants.COURSE_ID_KEY;
import static com.example.wgutracker.utilities.Constants.NOTE_ID_KEY;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    private List<NoteEntity> mNotes = new ArrayList<>();
    private final Context mContext;

    public NotesAdapter(List<NoteEntity> mNotes, Context mContext) {
        this.mNotes = mNotes;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.note_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final NoteEntity note = mNotes.get(position);
        holder.mTextView.setText(note.getNoteName());
        holder.tTextView.setText(note.getNoteText());

        holder.mFab.setOnClickListener(v -> {
           Intent intent = new Intent(mContext, NoteEditorActivity.class);
           intent.putExtra(NOTE_ID_KEY, note.getNoteID());
           intent.putExtra(COURSE_ID_KEY, note.getCourseID());
           mContext.startActivity(intent);
        });

        holder.mTextView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, NoteDetailActivity.class);
            intent.putExtra(NOTE_ID_KEY, note.getNoteID());
            intent.putExtra(COURSE_ID_KEY, note.getCourseID());
            mContext.startActivity(intent);
        });
        holder.tTextView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, NoteDetailActivity.class);
            intent.putExtra(NOTE_ID_KEY, note.getNoteID());
            intent.putExtra(COURSE_ID_KEY, note.getCourseID());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.note_title)
        TextView mTextView;
        @BindView(R.id.note_text)
        TextView tTextView;
        @BindView(R.id.fab_edit)
        FloatingActionButton mFab;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
