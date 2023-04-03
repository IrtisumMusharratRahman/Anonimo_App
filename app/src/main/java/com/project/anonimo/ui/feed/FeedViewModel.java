package com.project.anonimo.ui.feed;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.anonimo.data.ApiCallStatus;
import com.project.anonimo.data.ApiCallStatusValue;
import com.project.anonimo.data.model.Post;
import com.project.anonimo.repository.API.ApiClient.ApiClient;
import com.project.anonimo.repository.API.ApiInterface.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedViewModel extends ViewModel {

    private ApiInterface apiInterface;
    private MutableLiveData<ApiCallStatus> status;
    private List<Post> posts;

    public FeedViewModel() {
        status = new MutableLiveData<>();
        status.setValue(new ApiCallStatus(ApiCallStatusValue.PROCESSING));
    }

    public void setStatus(ApiCallStatus status) {
        this.status.setValue(status);
    }

    public MutableLiveData<ApiCallStatus> getStatus() {
        return status;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void getAllPosts(){
        this.apiInterface = ApiClient.getInstance().getApiInterface();
        Call<List<Post>> call = this.apiInterface.getAllPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()){
                    posts=response.body();
                    status.setValue(new ApiCallStatus(ApiCallStatusValue.FINISHED));
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                status.setValue(new ApiCallStatus(ApiCallStatusValue.FAILURE));
            }
        });
    }
}