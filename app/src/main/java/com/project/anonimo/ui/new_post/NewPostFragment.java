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

import com.project.anonimo.MainActivity;
import com.project.anonimo.R;
import com.project.anonimo.data.ApiCallStatus;
import com.project.anonimo.data.ApiCallStatusValue;
import com.project.anonimo.data.Tags;
import com.project.anonimo.data.model.Comment;
import com.project.anonimo.data.model.Post;
import com.project.anonimo.data.model.User;
import com.project.anonimo.databinding.FragmentNewPostBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class NewPostFragment extends Fragment {

    private FragmentNewPostBinding binding;
    private NewPostViewModel newPostViewModel;

    private EditText content;
    private Button btnPost;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newPostViewModel =
                new ViewModelProvider(this).get(NewPostViewModel.class);

        binding = FragmentNewPostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        content = binding.etContent;
        btnPost = binding.btnPost;
        NewPostFragment newPostFragment = this;

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity= (MainActivity) getActivity();

                Post post = new Post(
                        UUID.randomUUID().toString(),
                        mainActivity.getUser().getUserID(),
                        content.getText().toString(),
                        "Testing",
                        String.valueOf(Calendar.getInstance().getTimeInMillis()),
                        new ArrayList<Comment>()
                        );
                newPostViewModel.createPost(post);
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
}