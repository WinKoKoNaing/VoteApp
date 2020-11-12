package com.porlar.techhousestudio.voteapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.porlar.techhousestudio.voteapp.R;

import java.util.List;

/**
  Created by USER on 12/25/2018.
 */

public class ImageSlideAdapter extends PagerAdapter {
    private Context context;
    private List<String> imageList;
    private LayoutInflater inflater;

    public ImageSlideAdapter(Context context, List<String> imageList) {
        this.context = context;
        this.imageList = imageList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @SuppressLint("CheckResult")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.image_slide_row, container, false);
        ImageView ivSlide = view.findViewById(R.id.ivSlide);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.default_image);
        Glide.with(context).load(imageList.get(position)).apply(requestOptions).into(ivSlide);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == o);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);

    }
}
