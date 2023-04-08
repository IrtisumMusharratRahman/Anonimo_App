package com.project.anonimo.ui.new_post;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.anonimo.MainActivity;
import com.project.anonimo.R;
import com.project.anonimo.adapter.OnItemClickListener;
import com.project.anonimo.adapter.TagRecyclerAdapter;
import com.project.anonimo.data.ApiCallStatus;
import com.project.anonimo.data.ApiCallStatusValue;
import com.project.anonimo.data.Tags;
import com.project.anonimo.data.model.Comment;
import com.project.anonimo.data.model.Post;
import com.project.anonimo.data.model.User;
import com.project.anonimo.databinding.FragmentNewPostBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class NewPostFragment extends Fragment implements OnItemClickListener {

    private FragmentNewPostBinding binding;
    private NewPostViewModel newPostViewModel;

    private EditText content;
    private Button btnPost;
    private String postTag;
    private TagRecyclerAdapter adapter;
    private RecyclerView tagRV;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newPostViewModel =
                new ViewModelProvider(this).get(NewPostViewModel.class);

        binding = FragmentNewPostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        content = binding.etContent;
        btnPost = binding.btnPost;
        tagRV=binding.recyclerViewNewPostTag;
        Tags tags = Tags.getInstance();
        NewPostFragment newPostFragment = this;

        adapter = new TagRecyclerAdapter(tags.getMyList());
        adapter.setOnItemClickListener(this);
        tagRV.setHasFixedSize(true);
        tagRV.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
        tagRV.setAdapter(adapter);



        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity= (MainActivity) getActivity();

                if (postTag != null){
                    Post post = new Post(
                            UUID.randomUUID().toString(),
                            mainActivity.getUser().getUserID(),
                            content.getText().toString(),
                            postTag,
                            String.valueOf(Calendar.getInstance().getTimeInMillis()),
                            new ArrayList<Comment>()
                    );
                    newPostViewModel.createPost(post);
                }else {
                    Toast.makeText(getActivity(),"Please Choose A Tag",Toast.LENGTH_LONG).show();
                }


            }
        });

        newPostViewModel.getStatus().observe(getViewLifecycleOwner(), new Observer<ApiCallStatus>() {
            @Override
            public void onChanged(ApiCallStatus apiCallStatus) {
                switch (newPostViewModel.getStatus().getValue().getStatus()){
                    case ApiCallStatusValue.PROCESSING:
                        break;
                    case ApiCallStatusValue.FINISHED:
                        Toast.makeText(getActivity(),"New Post Created",Toast.LENGTH_LONG).show();
                        NavHostFragment.findNavController(newPostFragment).navigate(R.id.navigation_feed);
                        break;
                    case ApiCallStatusValue.FAILURE:
                        Toast.makeText(getActivity(),"Login Failed",Toast.LENGTH_LONG).show();
                        break;
                    default:

                }
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(Object data) {
        postTag=(String) data;
    }
}