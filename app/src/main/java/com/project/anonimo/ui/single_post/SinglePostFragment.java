package com.project.anonimo.ui.single_post;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.anonimo.R;
import com.project.anonimo.adapter.CommentRecyclerAdapter;
import com.project.anonimo.adapter.PostRecyclerAdapter;
import com.project.anonimo.data.ApiCallStatus;
import com.project.anonimo.data.ApiCallStatusValue;
import com.project.anonimo.data.model.Comment;
import com.project.anonimo.data.model.Post;
import com.project.anonimo.databinding.FragmentProfileBinding;
import com.project.anonimo.databinding.FragmentSinglePostBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class SinglePostFragment extends Fragment {

    private SinglePostViewModel mViewModel;
    private TextView content;
    private TextView time;
    private TextView tag;
    private EditText doComment;
    private ImageButton commentBtn;
    private RecyclerView commentRv;
    private String postID;
    private FragmentSinglePostBinding binding;
    private ProgressBar progressBar;
    private TextView noData;
    private List<Comment> comments;
    private CommentRecyclerAdapter adapter;
    private SinglePostFragment fragment;
    private Post singlePost;

    public static SinglePostFragment newInstance() {
        return new SinglePostFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(SinglePostViewModel.class);

        binding = FragmentSinglePostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle args = getArguments();

        if (args != null && args.containsKey("postID")) {
            String id = args.getString("postID");
            postID=id;
        }else{
            postID="";
        }

        content=binding.getRoot().findViewById(R.id.text_content);
        time=binding.getRoot().findViewById(R.id.text_post_time);
        tag=binding.getRoot().findViewById(R.id.text_tag);
        doComment=binding.doComment;
        commentBtn=binding.commentBtn;
        commentRv=binding.recyclerViewComments;
        progressBar=binding.singelePostProgressBar.getRoot();
        noData=binding.singlePostNoData.getRoot();

        fragment=this;


        return root;
    }

    @Override
    public void onStart() {

        mViewModel.getPost(postID);

        mViewModel.getPostStatus().observe(getViewLifecycleOwner(), new Observer<ApiCallStatus>() {
            @Override
            public void onChanged(ApiCallStatus apiCallStatus) {
                switch (mViewModel.getPostStatus().getValue().getStatus()){
                    case ApiCallStatusValue.PROCESSING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case ApiCallStatusValue.FINISHED:
                        progressBar.setVisibility(View.GONE);

                        singlePost = mViewModel.getPost();
                        content.setText(singlePost.getPostContent());
                        String t = singlePost.getPostTime();
                        Date date = new Date(Long.parseLong(t));
                        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a, MMM dd,yyyy", Locale.ENGLISH);
                        String formattedDate = formatter.format(date);
                        time.setText(formattedDate);
                        tag.setText(singlePost.getPostTag());
                        comments=singlePost.getComments();

                        if(comments!=null && !comments.isEmpty()){
                            Collections.sort(comments, new Comparator<Comment>() {
                                @Override
                                public int compare(Comment comment1, Comment comment2) {
                                    return comment2.getCommentTime().compareTo(comment1.getCommentTime());
                                }
                            });
                            adapter = new CommentRecyclerAdapter(comments);
                            commentRv.setHasFixedSize(true);
                            commentRv.setLayoutManager(new LinearLayoutManager(getActivity()));
                            commentRv.setAdapter(adapter);
                        }


                        break;
                    case ApiCallStatusValue.FAILURE:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(),"Data Fetching Failed",Toast.LENGTH_LONG).show();
                        break;
                    default:

                }
            }
        });

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!doComment.getText().toString().isEmpty()){
                    Comment comment = new Comment(UUID.randomUUID().toString(),postID,doComment.getText().toString(),String.valueOf(Calendar.getInstance().getTimeInMillis()));
                    List<Comment> newCommentList = singlePost.getComments();
                    newCommentList.add(comment);
                    singlePost.setComments(newCommentList);
                    mViewModel.addComment(singlePost);
                }else{
                    Toast.makeText(getActivity(),"Please fill in comment section",Toast.LENGTH_LONG).show();
                }


            }
        });

        mViewModel.getCommentStatus().observe(getViewLifecycleOwner(), new Observer<ApiCallStatus>() {
            @Override
            public void onChanged(ApiCallStatus apiCallStatus) {
                switch (mViewModel.getCommentStatus().getValue().getStatus()){
                    case ApiCallStatusValue.PROCESSING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case ApiCallStatusValue.FINISHED:
                        progressBar.setVisibility(View.GONE);
                        Bundle bundle = new Bundle();
                        bundle.putString("postID", postID);

//                        NavHostFragment.findNavController(fragment).navigate(R.id.navigation_single_post, bundle);
                        mViewModel.getPost(postID);
                        doComment.setText("");

                        break;
                    case ApiCallStatusValue.FAILURE:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(),"Data Fetching Failed",Toast.LENGTH_LONG).show();
                        break;
                    default:

                }
            }
        });



        super.onStart();
    }
}