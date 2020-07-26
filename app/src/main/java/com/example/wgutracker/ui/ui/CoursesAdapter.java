package com.example.wgutracker.ui.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wgutracker.CourseDetailActivity;
import com.example.wgutracker.CourseEditorActivity;
import com.example.wgutracker.R;
import com.example.wgutracker.database.CourseEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wgutracker.utilities.Constants.COURSE_ID_KEY;
import static com.example.wgutracker.utilities.Constants.MENTOR_ID_KEY_C;
import static com.example.wgutracker.utilities.Constants.TERM_ID_KEY_C;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {
    private List<CourseEntity> mCourses;
    private final Context mContext;

    public CoursesAdapter(List<CourseEntity> mCourses, Context mContext) {
        this.mCourses = mCourses;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.course_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CourseEntity course = mCourses.get(position);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = course.getCourseStart();
        Date endDate = course.getCourseEnd();
        StringBuilder sb = new StringBuilder();
        sb.append(formatter.format(startDate));
        sb.append(" - ");
        sb.append(formatter.format(endDate));
        holder.mTextView.setText(course.getCourseName());
        holder.tTextView.setText(sb.toString());

        holder.mFab.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, CourseEditorActivity.class);
            intent.putExtra(COURSE_ID_KEY, course.getCourseID());
            intent.putExtra(TERM_ID_KEY_C,course.getTermID());
            intent.putExtra(MENTOR_ID_KEY_C, course.getMentorID());
            mContext.startActivity(intent);
        });

        holder.mTextView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, CourseDetailActivity.class);
            intent.putExtra(COURSE_ID_KEY, course.getCourseID());
            intent.putExtra(TERM_ID_KEY_C,course.getTermID());
            intent.putExtra(MENTOR_ID_KEY_C, course.getMentorID());
            mContext.startActivity(intent);
        });
        holder.tTextView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, CourseDetailActivity.class);
            intent.putExtra(COURSE_ID_KEY, course.getCourseID());
            intent.putExtra(TERM_ID_KEY_C,course.getTermID());
            intent.putExtra(MENTOR_ID_KEY_C, course.getMentorID());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.course_text)
        TextView mTextView;
        @BindView(R.id.course_times)
        TextView tTextView;
        @BindView(R.id.fab_edit)
        FloatingActionButton mFab;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
