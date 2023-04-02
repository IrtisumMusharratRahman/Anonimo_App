package com.project.anonimo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.anonimo.R;
import com.project.anonimo.data.model.Post;

import java.util.List;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.ViewHolder> {
    private List<Post> posts;

    public PostRecyclerAdapter(List<Post> blogPosts) {
        this.posts = blogPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_post_container, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post blogPost = posts.get(position);
        holder.textPostTime.setText(blogPost.getPostTime());
        holder.textContent.setText(blogPost.getPostContent());
        holder.textTag.setText(blogPost.getPostTag());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textPostTime;
        public TextView textContent;
        public TextView textTag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textPostTime = itemView.findViewById(R.id.text_post_time);
            textContent = itemView.findViewById(R.id.text_content);
            textTag = itemView.findViewById(R.id.text_tag);
        }
    }
}
