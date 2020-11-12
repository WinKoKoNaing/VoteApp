package com.porlar.techhousestudio.voteapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.porlar.techhousestudio.voteapp.R;
import com.porlar.techhousestudio.voteapp.models.Result;
import com.porlar.techhousestudio.voteapp.models.Student;
import com.porlar.techhousestudio.voteapp.ui.ResultActivity;

import java.util.List;

/**
 * Created by USER on 1/3/2019.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {
    private Context context;
    private List<Result> studentList;
    public ResultAdapter(Context context, List<Result> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public ResultAdapter.ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ResultViewHolder(LayoutInflater.from(context).inflate(R.layout.result_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ResultAdapter.ResultViewHolder holder, int position) {
        holder.tvTitle.setText(studentList.get(position).rollNumber);
        holder.tvResultName.setText(studentList.get(position).studentName);
        holder.tvVoteCount.setText(studentList.get(position).voteCount+"");
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle,tvResultName,tvVoteCount;
        public ResultViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvResultTitle);
            tvResultName = itemView.findViewById(R.id.tvResultName);
            tvVoteCount = itemView.findViewById(R.id.tvVoteCount);
        }
    }
}
