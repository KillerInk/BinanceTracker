package com.binancetracker.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.binancetracker.R;
import com.binancetracker.databinding.TextRowItemBinding;
import com.binancetracker.repo.room.entity.AssetModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<AssetModel> assetModels;
    private View.OnClickListener onClickListener;

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
        assetModels = new ArrayList<>();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public synchronized void setLocalDataSet(Collection<AssetModel> dataSet)
    {
        assetModels.clear();
        assetModels.addAll(dataSet);
        Collections.sort(assetModels, new Comparator<AssetModel>() {
            @Override
            public int compare(AssetModel lhs, AssetModel rhs) {
                return lhs.getTotalValuePrice() > rhs.getTotalValuePrice() ? -1 : (lhs.getTotalValuePrice() < rhs.getTotalValuePrice() ) ? 1 : 0;
            }
        });
        notifyItemRangeChanged(0, assetModels.size());
        //notifyDataSetChanged();
    }

    public AssetModel getAssetModel(int pos)
    {
        return assetModels.get(pos);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item

        TextRowItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.text_row_item, viewGroup, false);
        binding.framelayoutRecycleritem.setOnClickListener(onClickListener);
        return new ViewHolder(binding);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextRowItemBinding().setAssetmodel(assetModels.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (assetModels == null)
            return 0;
        return assetModels.size();
    }
}

