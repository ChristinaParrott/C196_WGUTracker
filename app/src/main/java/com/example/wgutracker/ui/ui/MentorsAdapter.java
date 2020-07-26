package com.example.wgutracker.ui.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wgutracker.MentorDetailActivity;
import com.example.wgutracker.MentorEditorActivity;
import com.example.wgutracker.R;
import com.example.wgutracker.database.MentorEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wgutracker.utilities.Constants.MENTOR_ID_KEY;

public class MentorsAdapter extends RecyclerView.Adapter<MentorsAdapter.ViewHolder> {
    private List<MentorEntity> mMentor = new ArrayList<>();
    private final Context mContext;

    public MentorsAdapter(List<MentorEntity> mMentor, Context mContext) {
        this.mMentor = mMentor;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.mentor_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MentorEntity mentor = mMentor.get(position);
        holder.mTextView.setText(mentor.getMentorName());
        holder.tTextView.setText(mentor.getMentorEmail());

        holder.mFab.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, MentorEditorActivity.class);
            intent.putExtra(MENTOR_ID_KEY, mentor.getMentorID());
            mContext.startActivity(intent);
        });
        holder.mTextView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, MentorDetailActivity.class);
            intent.putExtra(MENTOR_ID_KEY, mentor.getMentorID());
            mContext.startActivity(intent);
        });
        holder.tTextView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, MentorDetailActivity.class);
            intent.putExtra(MENTOR_ID_KEY, mentor.getMentorID());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mMentor.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mentor_text)
        TextView mTextView;
        @BindView(R.id.mentor_email)
        TextView tTextView;
        @BindView(R.id.fab_edit)
        FloatingActionButton mFab;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
