package com.facilesales.facilesalesapp.pojos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facilesales.facilesalesapp.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    private TextView productItemView;

    public ProductViewHolder(@NonNull View itemView,RecyclerViewInterface recyclerViewInterface) {
        super(itemView);
        productItemView = itemView.findViewById(R.id.textView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerViewInterface != null)
                {
                    int pos = getAdapterPosition();

                    if(pos != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(pos);
                    }
                }
            }
        });
    }

    public void bind(String text){
        productItemView.setText(text);
    }

    static ProductViewHolder create(ViewGroup parent,RecyclerViewInterface recyclerViewInterface){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ProductViewHolder(view,recyclerViewInterface);
    }
}
