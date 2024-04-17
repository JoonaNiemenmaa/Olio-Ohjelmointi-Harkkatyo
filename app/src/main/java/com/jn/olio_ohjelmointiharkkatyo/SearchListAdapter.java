package com.jn.olio_ohjelmointiharkkatyo;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchListAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    private Context context;
    private EditText edit_municipality_name;
    private ArrayList<String> searches = new ArrayList<>();

    public SearchListAdapter(Context context, ArrayList<String> searches, EditText edit_municipality_name) {
        this.context = context;
        this.searches = searches;
        this.edit_municipality_name = edit_municipality_name;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(context).inflate(R.layout.search_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.text_previous_search_holder.setText(searches.get(position));
        holder.cl.setOnClickListener(view -> {
            edit_municipality_name.setText(holder.text_previous_search_holder.getText().toString());
        });
    }

    @Override
    public int getItemCount() {
        return searches.size();
    }
}


