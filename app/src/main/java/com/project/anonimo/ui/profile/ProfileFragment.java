package com.project.anonimo.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import com.project.anonimo.adapter.PostRecyclerAdapter;
import com.project.anonimo.data.ApiCallStatus;
import com.project.anonimo.data.ApiCallStatusValue;
import com.project.anonimo.data.model.Post;
import com.project.anonimo.data.model.User;
import com.project.anonimo.databinding.FragmentProfileBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;
    private ProgressBar progressBar;
    private EditText user_name;
    private ImageButton imageButton;
    private EditText user_email;
    private TextView noData;
    private RecyclerView myPostRV;
    private ProfileFragment fragment;
    private PostRecyclerAdapter adapter;
    private MainActivity mainActivity;
    private User newUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        progressBar = binding.profileProgressBar.getRoot();
        user_name = binding.userName;
        imageButton = binding.editName;
        user_email = binding.userEmail;
        noData = binding.profileNoData.getRoot();
        myPostRV = binding.recyclerViewMyposts;

        fragment=this;



        return root;
    }

    @Override
    public void onStart() {

        mainActivity = (MainActivity) getActivity();

        User user = mainActivity.getUser();

        user_name.setText(user.getUserName());
        user_email.setText(user.getUserEmail());

        profileViewModel.setUpdateStatus(new ApiCallStatus(ApiCallStatusValue.PROCESSING));
        profileViewModel.setDeleteStatus(new ApiCallStatus(ApiCallStatusValue.PROCESSING));


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newUser = new User(user.getUserID(),user_name.getText().toString(),user.getUserEmail(),user.getUserPassword());
                profileViewModel.updateUserName(newUser);
            }
        });

        profileViewModel.getUpdateStatus().observe(getViewLifecycleOwner(), new Observer<ApiCallStatus>() {
            @Override
            public void onChanged(ApiCallStatus apiCallStatus) {
                switch (profileViewModel.getUpdateStatus().getValue().getStatus()){
                    case ApiCallStatusValue.PROCESSING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case ApiCallStatusValue.FINISHED:
                        progressBar.setVisibility(View.GONE);
                        mainActivity.setUser(newUser);
                        NavHostFragment.findNavController(fragment).navigate(R.id.navigation_Profile);

                        break;
                    case ApiCallStatusValue.FAILURE:
                        Toast.makeText(getActivity(),"Name Changing Process Failed",Toast.LENGTH_LONG).show();
                        break;
                    default:

                }
            }
        });

        profileViewModel.getAllPosts();

        profileViewModel.getPostStatus().observe(getViewLifecycleOwner(), new Observer<ApiCallStatus>() {
            @Override
            public void onChanged(ApiCallStatus apiCallStatus) {
                switch (profileViewModel.getPostStatus().getValue().getStatus()){
                    case ApiCallStatusValue.PROCESSING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case ApiCallStatusValue.FINISHED:
                        progressBar.setVisibility(View.GONE);
                        if(profileViewModel.getPosts() != null){
                            if (profileViewModel.getPosts().isEmpty()){
                                noData.setVisibility(View.VISIBLE);
                                myPostRV.setVisibility(View.GONE);

                            }else {

                                List<Post> postList = profileViewModel.getPosts();
                                List<Post> myPosts = new ArrayList<>();
                                for (int i=0;i<postList.size();i++){
                                    if (mainActivity.getUser().getUserID().equals(postList.get(i).getUserID())){
                                        myPosts.add(postList.get(i));
                                    }
                                }

                                Collections.sort(myPosts, new Comparator<Post>() {
                                    @Override
                                    public int compare(Post post1, Post post2) {
                                        return post2.getPostTime().compareTo(post1.getPostTime());
                                    }
                                });


                                adapter = new PostRecyclerAdapter(myPosts,fragment);
                                myPostRV.setHasFixedSize(true);
                                myPostRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                                myPostRV.setAdapter(adapter);
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