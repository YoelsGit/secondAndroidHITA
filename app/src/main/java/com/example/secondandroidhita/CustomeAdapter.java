package com.example.secondandroidhita;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomeAdapter extends RecyclerView.Adapter<CustomeAdapter.MyViewHolder> implements Filterable {

    private ArrayList<DataModel> dataSet; // Original dataset
    private ArrayList<DataModel> filteredList; // Filtered dataset
    private Context context; // Context for creating the dialog

    public CustomeAdapter(ArrayList<DataModel> dataSet, Context context) {
        this.dataSet = dataSet;
        this.filteredList = new ArrayList<>(dataSet); // Initialize filteredList with the original dataset
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewVersion;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textView);
            textViewVersion = itemView.findViewById(R.id.textView2);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    @NonNull
    @Override
    public CustomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardrow, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomeAdapter.MyViewHolder holder, int position) {
        DataModel item = filteredList.get(position);
        holder.textViewName.setText(item.getName());
        holder.textViewVersion.setText(item.getVersion());
        holder.imageView.setImageResource(item.getImage());

        holder.itemView.setOnClickListener(v -> {
            // Create the dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate custom layout
            View dialogView = inflater.inflate(R.layout.dialog_layout, null);
            builder.setView(dialogView);

            // Set data to the dialog
            TextView nameTextView = dialogView.findViewById(R.id.dialogName);
            TextView versionTextView = dialogView.findViewById(R.id.dialogVersion);
            ImageView imageView = dialogView.findViewById(R.id.dialogImage);

            nameTextView.setText(item.getName());
            versionTextView.setText(item.getVersion());
            imageView.setImageResource(item.getImage());

            builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

            // Show the dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }


    private void showItemDetailsDialog(DataModel item) {
        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Inflate the custom layout for the dialog
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(dialogView);

        // Set up the dialog views
        TextView nameTextView = dialogView.findViewById(R.id.dialogName);
        TextView versionTextView = dialogView.findViewById(R.id.dialogVersion);
        ImageView imageView = dialogView.findViewById(R.id.dialogImage);

        // Populate the dialog with data
        nameTextView.setText(item.getName());
        versionTextView.setText(item.getVersion());
        imageView.setImageResource(item.getImage());

        // Add an OK button
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<DataModel> tempList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    tempList.addAll(dataSet); // No filter applied, use the original dataset
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (DataModel item : dataSet) {
                        if (item.getName().toLowerCase().contains(filterPattern)) {
                            tempList.add(item); // Add matching items
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = tempList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList.clear();
                filteredList.addAll((List<DataModel>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
