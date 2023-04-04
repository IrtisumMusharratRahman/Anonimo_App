package com.project.anonimo.ui.feed;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.anonimo.MainActivity;
import com.project.anonimo.adapter.PostRecyclerAdapter;
import com.project.anonimo.adapter.TagRecyclerAdapter;
import com.project.anonimo.data.ApiCallStatus;
import com.project.anonimo.data.ApiCallStatusValue;

import com.project.anonimo.data.model.Post;
import com.project.anonimo.databinding.FragmentFeedBinding;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FeedFragment extends Fragment {

    private FragmentFeedBinding binding;
    private PostRecyclerAdapter adapter;
//    private TagRecyclerAdapter tagRecyclerAdapter;
    private FeedViewModel feedViewModel;

    ProgressBar progressBar;
    TextView noData;
    RecyclerView tagRV;
    RecyclerView postRv;
    private FeedFragment fragment;
//    private Tags tags;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        feedViewModel =
                new ViewModelProvider(this).get(FeedViewModel.class);

        binding = FragmentFeedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        progressBar = binding.feedProgressBar.getRoot();
        noData = binding.feedNoData.getRoot();
        tagRV = binding.recyclerViewFeedTag;
        postRv = binding.recyclerViewPosts;
//        tags =Tags.getInstance();

        fragment=this;



        return root;
    }

    @Override
    public void onStart() {

        feedViewModel.setStatus(new ApiCallStatus(ApiCallStatusValue.PROCESSING));

        feedViewModel.getAllPosts();

        feedViewModel.getStatus().observe(getViewLifecycleOwner(), new Observer<ApiCallStatus>() {
            @Override
            public void onChanged(ApiCallStatus apiCallStatus) {
                switch (feedViewModel.getStatus().getValue().getStatus()){
                    case ApiCallStatusValue.PROCESSING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case ApiCallStatusValue.FINISHED:
                        progressBar.setVisibility(View.GONE);
                        if(feedViewModel.getPosts() != null){
                            if (feedViewModel.getPosts().isEmpty()){
                                noData.setVisibility(View.VISIBLE);
                                postRv.setVisibility(View.GONE);

                            }else {

                                List<Post> temp = feedViewModel.getPosts();
                                Collections.sort(temp, new Comparator<Post>() {
                                    @Override
                                    public int compare(Post post1, Post post2) {
                                        return post2.getPostTime().compareTo(post1.getPostTime());
                                    }
                                });

                                adapter = new PostRecyclerAdapter(temp,fragment);
                                postRv.setHasFixedSize(true);
                                postRv.setLayoutManager(new LinearLayoutManager(getActivity()));
                                postRv.setAdapter(adapter);

//                                tagRecyclerAdapter = new TagRecyclerAdapter(tags.getMyList());
//                                tagRecyclerAdapter.setHasFixedSize(true);
//                                tagRecyclerAdapter.setLayoutManager(new LinearLayoutManager(getActivity()));
//                                tagRecyclerAdapter.setAdapter(adapter);
                            }
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
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}