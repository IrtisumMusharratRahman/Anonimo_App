package com.project.anonimo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.anonimo.R;
import com.project.anonimo.data.model.Comment;
import com.project.anonimo.data.model.Post;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder> {
    private List<Comment> comments;

    public CommentRecyclerAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_comment_container, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);

        String time = comment.getCommentTime();
        Date date = new Date(Long.parseLong(time));
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a, MMM dd,yyyy", Locale.ENGLISH);
        String formattedDate = formatter.format(date);

        holder.textPostTime.setText(formattedDate);
        holder.textContent.setText(comment.getCommentContent());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textPostTime;
        public TextView textContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textPostTime = itemView.findViewById(R.id.post_time);
            textContent = itemView.findViewById(R.id.content);
        }
    }
}