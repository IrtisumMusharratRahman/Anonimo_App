package com.project.anonimo.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.project.anonimo.R;
import com.project.anonimo.data.model.Post;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.ViewHolder> {
    private List<Post> posts;
    private Fragment fragment;
    private View view;

    public PostRecyclerAdapter(List<Post> blogPosts,Fragment fragment) {

        this.posts = blogPosts;
        this.fragment=fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_post_container, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post blogPost = posts.get(position);
        String time = blogPost.getPostTime();
        Date date = new Date(Long.parseLong(time));
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a, MMM dd,yyyy", Locale.ENGLISH);
        String formattedDate = formatter.format(date);
        holder.textPostTime.setText(formattedDate);
        holder.textContent.setText(blogPost.getPostContent());
        holder.textTag.setText(blogPost.getPostTag());

        LinearLayout layout = view.findViewById(R.id.rv_post_container);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("postID", blogPost.getPostID());

                NavHostFragment.findNavController(fragment).navigate(R.id.navigation_single_post, bundle);

            }
        });

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
