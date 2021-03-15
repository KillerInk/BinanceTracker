package com.binancetracker.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.binancetracker.R;
import com.binancetracker.databinding.TextRowItemBinding;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private AssetModel[] localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextRowItemBinding textRowItemBinding;

        public ViewHolder(TextRowItemBinding view) {
            super(view.getRoot());
            textRowItemBinding = view;
        }

        public TextRowItemBinding getTextRowItemBinding() {
            return textRowItemBinding;
        }
    }

    public CustomAdapter() {
    }

    public void setLocalDataSet(AssetModel[] dataSet)
    {
        localDataSet = dataSet;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item

        TextRowItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.text_row_item, viewGroup, false);
        return new ViewHolder(binding);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextRowItemBinding().setAssetmodel(localDataSet[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (localDataSet == null)
            return 0;
        return localDataSet.length;
    }
}

