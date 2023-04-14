package com.example.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class uadapter extends RecyclerView.Adapter<uadapter.ViewHolder>{
    private ArrayList<model> parraylist;
    private Context context;

    // creating constructor for our adapter class
    public uadapter(ArrayList<model> parraylist, Context context) {
        this.parraylist = parraylist;
        this.context = context;
    }
    @NonNull
    @Override
    public uadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new uadapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.uitems, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        model mmodel = parraylist.get(position);
        holder.name.setText(mmodel.getName());
        holder.gender.setText(mmodel.getGender());
        holder.phone.setText(mmodel.getMobileNumber());

    }


    @Override
    public int getItemCount() {
        return parraylist.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView name;
        private final TextView phone;
        private final TextView gender;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            name = itemView.findViewById(R.id.idname);
            phone = itemView.findViewById(R.id.idphone);
            gender=itemView.findViewById(R.id.idgender);


        }
    }


}
