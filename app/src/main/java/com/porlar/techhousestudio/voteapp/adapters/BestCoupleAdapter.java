package com.porlar.techhousestudio.voteapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.porlar.techhousestudio.voteapp.R;
import com.porlar.techhousestudio.voteapp.interfaces.SetOnBestCoupleImageListener;
import com.porlar.techhousestudio.voteapp.interfaces.SetOnBestCoupleListener;
import com.porlar.techhousestudio.voteapp.models.CoupleStudent;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by USER on 12/29/2018.
 */
@SuppressWarnings({"deprecation", "unchecked"})
public class BestCoupleAdapter extends RecyclerView.Adapter<BestCoupleAdapter.BestCoupleViewHolder> {
    private Context context;
    private List<CoupleStudent> coupleStudentList;
    private LayoutInflater inflater;
    private SetOnBestCoupleListener setOnBestCoupleListener;
    private SetOnBestCoupleImageListener setOnBestCoupleImageListener;
    private int selectItem = -1;

    public void setSetOnBestCoupleListener(SetOnBestCoupleListener setOnBestCoupleListener) {
        this.setOnBestCoupleListener = setOnBestCoupleListener;
    }

    public void setSetOnBestCoupleImageListener(SetOnBestCoupleImageListener setOnBestCoupleImageListener) {
        this.setOnBestCoupleImageListener = setOnBestCoupleImageListener;
    }

    public BestCoupleAdapter(Context context, List<CoupleStudent> coupleStudentList) {
        this.context = context;
        this.coupleStudentList = coupleStudentList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BestCoupleAdapter.BestCoupleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BestCoupleViewHolder(inflater.inflate(R.layout.bestcouple_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final BestCoupleAdapter.BestCoupleViewHolder holder, final int position) {
        holder.tvBoyName.setText(coupleStudentList.get(position).boyName);
        holder.tvGirlName.setText(coupleStudentList.get(position).girlName);
        Glide.with(context).load(R.drawable.bestcouple).apply(RequestOptions.circleCropTransform()).into(holder.ivBoy);
        Glide.with(context).load(R.drawable.bestcouple).apply(RequestOptions.circleCropTransform()).into(holder.ivGirl);
        holder.coupleCode.setText(coupleStudentList.get(position).coupleCode+"");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.bestcouple);
        Glide.with(context).load(coupleStudentList.get(position).boyImageUri).apply(requestOptions).into(holder.ivBoy);
        Glide.with(context).load(coupleStudentList.get(position).girlImageUri).apply(requestOptions).into(holder.ivGirl);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectItem = position;
                setOnBestCoupleListener.bestCoupleOnClick(coupleStudentList.get(holder.getAdapterPosition()));
                notifyDataSetChanged();
            }
        });
        holder.ivBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnBestCoupleImageListener.onBestCoupleImageClick(coupleStudentList.get(holder.getAdapterPosition()),true);
            }
        });
        holder.ivGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnBestCoupleImageListener.onBestCoupleImageClick(coupleStudentList.get(holder.getAdapterPosition()),false);
            }
        });
        if (selectItem == position) {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.recycler_select_item));
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return coupleStudentList.size();
    }

    public class BestCoupleViewHolder extends RecyclerView.ViewHolder {
        TextView tvGirlName, tvBoyName, coupleCode;
        ImageView ivGirl, ivBoy;

        public BestCoupleViewHolder(View itemView) {
            super(itemView);
            tvBoyName = itemView.findViewById(R.id.tvCoupleBoyName);
            tvGirlName = itemView.findViewById(R.id.tvCoupleGirlName);
            ivGirl = itemView.findViewById(R.id.ivGirl);
            ivBoy = itemView.findViewById(R.id.ivBoy);
            coupleCode = itemView.findViewById(R.id.tvCoupleCode);
        }
    }
}
