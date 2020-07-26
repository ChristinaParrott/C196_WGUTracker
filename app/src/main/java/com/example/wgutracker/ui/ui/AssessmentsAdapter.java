package com.example.wgutracker.ui.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wgutracker.AssessDetailActivity;
import com.example.wgutracker.AssessEditorActivity;
import com.example.wgutracker.R;
import com.example.wgutracker.database.AssessmentEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wgutracker.utilities.Constants.ASSESS_ID_KEY;
import static com.example.wgutracker.utilities.Constants.A_COURSE_ID_KEY;

public class AssessmentsAdapter extends RecyclerView.Adapter<AssessmentsAdapter.ViewHolder> {
    private List<AssessmentEntity> mAssess;
    private final Context mContext;

    public AssessmentsAdapter(List<AssessmentEntity> mAssess, Context mContext) {
        this.mAssess = mAssess;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.assessment_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AssessmentEntity assess = mAssess.get(position);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = assess.getAssessDate();
        holder.mTextView.setText(assess.getAssessName());
        holder.pTextView.setText(assess.getAssessType());
        holder.tTextView.setText(formatter.format(date));

        holder.mFab.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, AssessEditorActivity.class);
            intent.putExtra(ASSESS_ID_KEY, assess.getAssessID());
            intent.putExtra(A_COURSE_ID_KEY, assess.getCourseID());
            mContext.startActivity(intent);
        });

        holder.mTextView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, AssessDetailActivity.class);
            intent.putExtra(ASSESS_ID_KEY, assess.getAssessID());
            intent.putExtra(A_COURSE_ID_KEY, assess.getCourseID());
            mContext.startActivity(intent);
        });

        holder.pTextView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, AssessDetailActivity.class);
            intent.putExtra(ASSESS_ID_KEY, assess.getAssessID());
            intent.putExtra(A_COURSE_ID_KEY, assess.getCourseID());
            mContext.startActivity(intent);
        });
        holder.tTextView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, AssessDetailActivity.class);
            intent.putExtra(ASSESS_ID_KEY, assess.getAssessID());
            intent.putExtra(A_COURSE_ID_KEY, assess.getCourseID());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mAssess.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.assess_text)
        TextView mTextView;
        @BindView(R.id.assess_type)
        TextView pTextView;
        @BindView(R.id.assess_times)
        TextView tTextView;
        @BindView(R.id.fab_edit)
        FloatingActionButton mFab;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
