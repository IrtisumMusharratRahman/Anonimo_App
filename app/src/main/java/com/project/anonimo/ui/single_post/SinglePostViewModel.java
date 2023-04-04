package com.project.anonimo.ui.single_post;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.anonimo.data.ApiCallStatus;
import com.project.anonimo.data.ApiCallStatusValue;
import com.project.anonimo.data.model.Comment;
import com.project.anonimo.data.model.Post;
import com.project.anonimo.data.model.User;
import com.project.anonimo.repository.API.ApiClient.ApiClient;
import com.project.anonimo.repository.API.ApiInterface.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class SinglePostViewModel extends ViewModel {
    private ApiInterface apiInterface;
    private MutableLiveData<ApiCallStatus> postStatus;
    private MutableLiveData<ApiCallStatus> commentStatus;
    private Post post;

    public SinglePostViewModel(){
        postStatus = new MutableLiveData<>();
        postStatus.setValue(new ApiCallStatus(ApiCallStatusValue.PROCESSING));

        commentStatus = new MutableLiveData<>();
        commentStatus.setValue(new ApiCallStatus(ApiCallStatusValue.PROCESSING));
    }

    public MutableLiveData<ApiCallStatus> getPostStatus() {
        return postStatus;
    }

    public MutableLiveData<ApiCallStatus> getCommentStatus() {
        return commentStatus;
    }

    public Post getPost() {
        return post;
    }

    public void getPost(String postID){
        this.apiInterface = ApiClient.getInstance().getApiInterface();
        Call<Post> call = this.apiInterface.getPost(postID);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()){
                    post=response.body();
                    postStatus.setValue(new ApiCallStatus(ApiCallStatusValue.FINISHED));
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                postStatus.setValue(new ApiCallStatus(ApiCallStatusValue.FAILURE));
            }
        });
    }

    public void addComment(Post post){
        this.apiInterface = ApiClient.getInstance().getApiInterface();
        Call<String> call = this.apiInterface.addComment(post);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    commentStatus.setValue(new ApiCallStatus(ApiCallStatusValue.FINISHED));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                commentStatus.setValue(new ApiCallStatus(ApiCallStatusValue.FAILURE));
            }
        });
    }
}