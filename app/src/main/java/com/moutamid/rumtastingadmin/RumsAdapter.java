package com.moutamid.rumtastingadmin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Collection;

public class RumsAdapter extends RecyclerView.Adapter<RumsAdapter.RumsVH> implements Filterable {
    Context context;
    ArrayList<RumModel> list;
    ArrayList<RumModel> listAll;

    public RumsAdapter(Context context, ArrayList<RumModel> list) {
        this.context = context;
        this.list = list;
        this.listAll = new ArrayList<>(list);
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
        holder.description.setText(model.description);
        Glide.with(context).load(model.image).placeholder(R.color.background).into(holder.profile);

        if (model.rating != null) {
            double rate = model.rating.star1 + model.rating.star2 + model.rating.star3 + model.rating.star4 + model.rating.star5;
            rate = rate / 5;
            holder.rating.setText(String.format("%.2f", rate));
        }

        holder.edit.setOnClickListener(v -> context.startActivity(new Intent(context, UpdateActivity.class).putExtra(Constants.ID, model.id)));

        holder.delete.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(context)
                    .setTitle("Delete")
                    .setMessage("Do you really want to delete this rum?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dialog.dismiss();
                        Constants.databaseReference().child(Constants.RUMS).child(model.id).removeValue();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<RumModel> filterList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filterList.addAll(listAll);
            } else {
                for (RumModel listModel : listAll) {
                    if (listModel.name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filterList.add(listModel);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((Collection<? extends RumModel>) results.values);
            notifyDataSetChanged();
        }
    };

    public static class RumsVH extends RecyclerView.ViewHolder {
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
