package com.example.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class padapter extends RecyclerView.Adapter<padapter.ViewHolder> {
    // creating variables for our ArrayList and context
    private ArrayList<walletpass> parraylist;
    private Context context;

    // creating constructor for our adapter class
    public padapter(ArrayList<walletpass> parraylist, Context context) {
        this.parraylist = parraylist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.witems, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        walletpass walletpass = parraylist.get(position);
        holder.amount.setText(walletpass.getAmount());
        holder.dAndt.setText(walletpass.getdt());

    }

    @Override
    public int getItemCount() {
        return parraylist.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView amount;
        private final TextView dAndt;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            amount = itemView.findViewById(R.id.idamount);
            dAndt = itemView.findViewById(R.id.iddt);

        }
    }

}

