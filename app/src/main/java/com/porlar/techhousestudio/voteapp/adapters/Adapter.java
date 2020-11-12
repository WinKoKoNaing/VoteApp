package com.porlar.techhousestudio.voteapp.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.porlar.techhousestudio.voteapp.R;
import com.porlar.techhousestudio.voteapp.models.Tag;
import com.yalantis.filter.adapter.FilterAdapter;
import com.yalantis.filter.widget.FilterItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Adapter extends FilterAdapter<Tag> {
    Context context;

    public Adapter(Context context,@NotNull List<? extends Tag> items) {
        super(items);
        this.context = context;
    }

    @NotNull
    @Override
    public FilterItem createView(int position, Tag item) {
        FilterItem filterItem = new FilterItem(context);

        filterItem.setStrokeColor(context.getResources().getIntArray(R.array.colors)[0]);
        filterItem.setTextColor(context.getResources().getIntArray(R.array.colors)[0]);
        filterItem.setCheckedTextColor(ContextCompat.getColor(context, android.R.color.white));
        filterItem.setColor(ContextCompat.getColor(context, android.R.color.white));
        filterItem.setCheckedColor(context.getResources().getIntArray(R.array.colors)[position]);
        filterItem.setText(item.getText());
        filterItem.deselect();

        return filterItem;
    }
}