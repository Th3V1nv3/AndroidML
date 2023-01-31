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

public class HistoryViewAdapter extends RecyclerView.Adapter<HistoryViewAdapter.MyViewHolder> {
    List<HistoryRow> productList;
    Context context;
    private final RecyclerViewInterface recyclerViewInterface;

    public HistoryViewAdapter(List<HistoryRow> productList, Context context, RecyclerViewInterface recyclerViewInterface) {
        this.productList = productList;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public HistoryViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_row, parent,false);
        return new HistoryViewAdapter.MyViewHolder(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewAdapter.MyViewHolder holder, int position) {
        holder.rowId.setText("Invoice ID : "+String.valueOf(productList.get(position).getInvoiceId()));
        holder.rowName.setText(productList.get(position).getDate().toString());
        holder.rowQuantity.setText("Total amount made : R"+String.valueOf(productList.get(position).getTotalMountMade()));
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
