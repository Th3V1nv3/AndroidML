package com.facilesales.facilesalesapp.pojos;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.facilesales.facilesalesapp.database.AvailProduct;


public class ProductListAdapter extends ListAdapter<AvailProduct,ProductViewHolder>{
    private final RecyclerViewInterface recyclerViewInterface;
    public ProductListAdapter(@NonNull DiffUtil.ItemCallback<AvailProduct> diffCallback, RecyclerViewInterface recyclerViewInterface) {
        super(diffCallback);
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ProductViewHolder.create(parent,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        AvailProduct current = getItem(position);
        holder.bind("Name : "+current.getName()+"\nID : "+current.getId()+"\nQuantity : "+current.getQuantity()+"\nCost : R"+current.getCost()+"\nSelling Price : R "+current.getSellingPrice());
    }

    public AvailProduct getProductAt(int position){
         return getItem(position);
    }

    public static class ProductDiff extends DiffUtil.ItemCallback<AvailProduct>{

        @Override
        public boolean areItemsTheSame(@NonNull AvailProduct oldItem, @NonNull AvailProduct newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull AvailProduct oldItem, @NonNull AvailProduct newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }
}
