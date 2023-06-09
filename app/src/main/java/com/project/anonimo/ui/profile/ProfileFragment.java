package com.project.anonimo.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.anonimo.MainActivity;
import com.project.anonimo.R;
import com.project.anonimo.adapter.OnItemClickListener;
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
    private String deletePostID;
    private List<Post> myPosts;

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

        profileViewModel.setPostStatus(new ApiCallStatus(ApiCallStatusValue.PROCESSING));
        profileViewModel.setUpdateStatus(new ApiCallStatus(ApiCallStatusValue.PROCESSING));
        profileViewModel.setDeleteStatus(new ApiCallStatus(ApiCallStatusValue.PROCESSING));


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!user_name.getText().toString().isEmpty()){
                    newUser = new User(user.getUserID(),user_name.getText().toString(),user.getUserEmail(),user.getUserPassword());
                    profileViewModel.updateUserName(newUser);
                }else{
                    Toast.makeText(getActivity(),"Please fill in new user name",Toast.LENGTH_LONG).show();
                }

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
                        User user = mainActivity.getUser();

                        user_name.setText(user.getUserName());
                        user_email.setText(user.getUserEmail());

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
                        List<Post> postList = profileViewModel.getPosts();
                        myPosts = new ArrayList<>();
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

                        if(myPosts != null){
                            Log.e("TAG", "onChanged: inside null" );
                            if (myPosts.isEmpty()){
                                Log.e("TAG", "onChanged: inside empty" );
                                noData.setVisibility(View.VISIBLE);
                                myPostRV.setVisibility(View.GONE);

                            }else {
                                adapter = new PostRecyclerAdapter(myPosts,fragment);
                                myPostRV.setHasFixedSize(true);
                                myPostRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                                myPostRV.setAdapter(adapter);
                                new ItemTouchHelper(new SwipeToDeleteCallback(adapter)).attachToRecyclerView(myPostRV);
                            }
                        }else{
                            Log.e("TAG", "onChanged: outside null" );
                            noData.setVisibility(View.VISIBLE);
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

        profileViewModel.getDeleteStatus().observe(getViewLifecycleOwner(), new Observer<ApiCallStatus>() {
            @Override
            public void onChanged(ApiCallStatus apiCallStatus) {
                switch (profileViewModel.getDeleteStatus().getValue().getStatus()){
                    case ApiCallStatusValue.PROCESSING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case ApiCallStatusValue.FINISHED:
                        progressBar.setVisibility(View.GONE);
                        profileViewModel.setPostStatus(new ApiCallStatus(ApiCallStatusValue.PROCESSING));
                        profileViewModel.getAllPosts();
                        break;
                    case ApiCallStatusValue.FAILURE:
                        Toast.makeText(getActivity(),"Delete My Post Failed",Toast.LENGTH_LONG).show();
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


    public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

        public SwipeToDeleteCallback(PostRecyclerAdapter adapter) {
            super(0, ItemTouchHelper.LEFT);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            if (myPosts!=null && !myPosts.isEmpty()){
            deletePostID=myPosts.get(position).getPostID();
            profileViewModel.deletePost(deletePostID);
        }
    }
}
}