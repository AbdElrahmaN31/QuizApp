package com.abdelrahman.quizapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdelrahman.quizapp.Interface.ItemClickListener;
import com.abdelrahman.quizapp.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mTextViewCategory;
    public ImageView mImageViewCategory;

    private ItemClickListener mItemClickListener;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        mTextViewCategory = itemView.findViewById(R.id.category_item_name);
        mImageViewCategory = itemView.findViewById(R.id.category_item_image);

        itemView.setOnClickListener(this);
    }

    public void setmItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public void onClick(View itemView) {
        mItemClickListener.onClick(itemView, getAdapterPosition(), false);
    }
}
