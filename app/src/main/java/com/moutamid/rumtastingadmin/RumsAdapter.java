package com.moutamid.rumtastingadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RumsAdapter extends RecyclerView.Adapter<RumsAdapter.RumsVH> {
    Context context;
    ArrayList<RumModel> list;

    public RumsAdapter(Context context, ArrayList<RumModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RumsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RumsVH(LayoutInflater.from(context).inflate(R.layout.rum_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RumsVH holder, int position) {
        RumModel model = list.get(holder.getAdapterPosition());
        holder.name.setText(model.name);
        holder.name.setText(model.description);
        Glide.with(context).load(model.image).placeholder(R.color.background).into(holder.profile);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RumsVH extends RecyclerView.ViewHolder{
        TextView name, description, rating;
        ImageView profile;
        Button edit, delete;
        public RumsVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            rating = itemView.findViewById(R.id.rating);
            profile = itemView.findViewById(R.id.profile);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
        }
    }

}
