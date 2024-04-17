package com.jn.olio_ohjelmointiharkkatyo;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchViewHolder extends RecyclerView.ViewHolder {
    TextView text_previous_search_holder;
    ConstraintLayout cl;
    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);
        text_previous_search_holder = itemView.findViewById(R.id.textPreviousSearchHolder);
        cl = itemView.findViewById(R.id.constraint_layout);
    }
}

