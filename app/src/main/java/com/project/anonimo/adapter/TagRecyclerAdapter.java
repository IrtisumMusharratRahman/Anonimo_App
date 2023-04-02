package com.project.anonimo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.anonimo.R;

import java.util.List;

public class TagRecyclerAdapter extends RecyclerView.Adapter<TagRecyclerAdapter.ViewHolder> {

    private List<String> tagList;

    public TagRecyclerAdapter(List<String> tagList) {
        this.tagList = tagList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_tag_container, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String tag = tagList.get(position);
        holder.tagName.setText(tag);
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tagName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tagName = itemView.findViewById(R.id.tag_name);
        }
    }
}