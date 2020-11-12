package com.porlar.techhousestudio.voteapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.ChildEventListener;
import com.porlar.techhousestudio.voteapp.R;
import com.porlar.techhousestudio.voteapp.interfaces.SetOnImageClickListener;
import com.porlar.techhousestudio.voteapp.interfaces.SetOnStudentClickListener;
import com.porlar.techhousestudio.voteapp.models.SelectionStudent;
import com.porlar.techhousestudio.voteapp.models.Student;

import java.util.List;

import es.dmoral.toasty.Toasty;
import me.myatminsoe.mdetect.MDetect;

@SuppressWarnings({"deprecation", "unchecked"})
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private List<Student> studentList;
    private Context context;
    private LayoutInflater inflater;
    private SetOnStudentClickListener onStudentClickListener;
    private SetOnImageClickListener onImageClickListener;
    private int selectItem = -1;
    private int default_photo = -1;

    public void setOnImageClickListener(SetOnImageClickListener setOnImageClickListener) {
        this.onImageClickListener = setOnImageClickListener;
    }

    public void setOnStudentClickListener(SetOnStudentClickListener onStudentClickListener) {
        this.onStudentClickListener = onStudentClickListener;
    }

    public void setDefault_photo(int default_photo) {
        this.default_photo = default_photo;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public StudentAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentViewHolder(inflater.inflate(R.layout.students_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentViewHolder holder, final int position) {
        holder.tvSection.setText(studentList.get(position).sectionName.substring(8));

        holder.layout_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectItem = position;
                onStudentClickListener.studentOnClick(studentList.get(holder.getAdapterPosition()));
                notifyDataSetChanged();
            }
        });
        holder.tvRollNumber.setText(studentList.get(position).rollNumber);
        if (studentList.get(position).studentImageUri != null) {
            Glide.with(context).load(studentList.get(position).studentImageUri).into(holder.ivImageUri);
        } else if (default_photo != -1) {
            holder.ivImageUri.setImageResource(default_photo);
        }
        @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if (studentList.get(position).allKings.containsKey(androidId)
                || studentList.get(position).allQueens.containsKey(androidId)
                || studentList.get(position).jockers.containsKey(androidId)
                ) {
            holder.tvStudentName.setText(studentList.get(position).studentName + " voted");
            holder.layout_student.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toasty.info(context,"You Have Voted",Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            holder.tvStudentName.setText(studentList.get(position).studentName);
        }

        if (studentList.get(position).isSelection) {
            holder.ivImageUri.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onImageClickListener.studentOnClick(studentList.get(holder.getAdapterPosition()));
                }
            });
        } else {
            holder.ivImageUri.setOnClickListener(null);
        }



        if (selectItem == position) {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.recycler_select_item));
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }


    public class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvSection, tvStudentName, tvRollNumber, tvFbUri;
        ImageView ivImageUri;
        LinearLayout layout_student;


        StudentViewHolder(View itemView) {
            super(itemView);
            tvSection = itemView.findViewById(R.id.tvSection);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvRollNumber = itemView.findViewById(R.id.tvRollNumber);
            ivImageUri = itemView.findViewById(R.id.ivImageUri);
            layout_student = itemView.findViewById(R.id.layout_student);

        }
    }
}
