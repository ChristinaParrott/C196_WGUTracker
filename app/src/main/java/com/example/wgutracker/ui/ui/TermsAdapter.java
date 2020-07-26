package com.example.wgutracker.ui.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wgutracker.R;
import com.example.wgutracker.TermDetailActivity;
import com.example.wgutracker.TermEditorActivity;
import com.example.wgutracker.database.TermEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wgutracker.utilities.Constants.TERM_ID_KEY;

public class TermsAdapter extends RecyclerView.Adapter<TermsAdapter.ViewHolder> {
    private List<TermEntity> mTerms = new ArrayList<>();
    private final Context mContext;

    public TermsAdapter(List<TermEntity> mTerms, Context mContext) {
        this.mTerms = mTerms;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.term_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TermEntity term = mTerms.get(position);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = term.getTermStart();
        Date endDate = term.getTermEnd();
        StringBuilder sb = new StringBuilder();
        sb.append(formatter.format(startDate));
        sb.append(" - ");
        sb.append(formatter.format(endDate));
        holder.mTextView.setText(term.getTermName());
        holder.tTextView.setText(sb.toString());

        holder.mFab.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, TermEditorActivity.class);
            intent.putExtra(TERM_ID_KEY, term.getTermID());
            mContext.startActivity(intent);
        });
        holder.mTextView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, TermDetailActivity.class);
            intent.putExtra(TERM_ID_KEY, term.getTermID());
            mContext.startActivity(intent);
        });
        holder.tTextView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, TermDetailActivity.class);
            intent.putExtra(TERM_ID_KEY, term.getTermID());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mTerms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.term_text)
        TextView mTextView;
        @BindView(R.id.term_times)
        TextView tTextView;
        @BindView(R.id.fab_edit)
        FloatingActionButton mFab;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
