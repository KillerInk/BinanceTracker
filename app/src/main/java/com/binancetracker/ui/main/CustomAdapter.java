package com.binancetracker.ui.main;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.binancetracker.R;
import com.binancetracker.databinding.TextRowItemBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<AssetModel> assetModels;

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

    public synchronized void setLocalDataSet(Collection<AssetModel> dataSet)
    {
        assetModels.clear();
        assetModels.addAll(dataSet);
        assetModels.sort(new ListSorter());
        notifyDataSetChanged();
    }

    public class ListSorter implements Comparator<AssetModel> {
        @Override
        public int compare(AssetModel lhs, AssetModel rhs) {
            Double distance = lhs.getTotalValuePrice();
            Double distance1 = rhs.getTotalValuePrice();
            if (distance.compareTo(distance1) > 0) {
                return -1;
            } else if (distance.compareTo(distance1) < 0) {
                return 1;
            } else {
                return 0;
            }
        }
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

