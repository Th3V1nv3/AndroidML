package com.facilesales.facilesalesapp.pojos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facilesales.facilesalesapp.R;

import java.text.DecimalFormat;
import java.util.List;

public class SellingViewAdapter extends RecyclerView.Adapter<SellingViewAdapter.MyViewHolder> {
    List<AvailProduct> productList;
    Context context;
    private final RecyclerViewInterface recyclerViewInterface;

    public SellingViewAdapter(List<AvailProduct> productList, Context context, RecyclerViewInterface recyclerViewInterface) {
        this.productList = productList;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public SellingViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_row, parent,false);

        return new SellingViewAdapter.MyViewHolder(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull SellingViewAdapter.MyViewHolder holder, int position) {
        holder.rowId.setText("ID : "+String.valueOf(productList.get(position).getId()));
        holder.rowName.setText(productList.get(position).getName());
        holder.rowQuantity.setText("No. items : "+String.valueOf(productList.get(position).getQuantity()));
        holder.rowPrice.setText("Cost : R"+String.valueOf(new DecimalFormat("##.##").format(productList.get(position).getSellingPrice())));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
         TextView rowPrice,rowQuantity,rowId,rowName;
         public MyViewHolder(@NonNull View itemView,RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            rowId = (TextView)itemView.findViewById(R.id.row_id);
            rowName = (TextView) itemView.findViewById(R.id.row_name);
            rowQuantity = (TextView) itemView.findViewById(R.id.row_quantity);
            rowPrice = (TextView) itemView.findViewById(R.id.row_price);

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
    }
}
