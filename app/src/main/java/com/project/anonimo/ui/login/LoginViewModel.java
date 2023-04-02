package com.project.anonimo.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.anonimo.data.ApiCallStatus;
import com.project.anonimo.data.ApiCallStatusValue;
import com.project.anonimo.data.model.Post;
import com.project.anonimo.data.model.User;
import com.project.anonimo.repository.API.ApiClient.ApiClient;
import com.project.anonimo.repository.API.ApiInterface.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;

public class LoginViewModel extends ViewModel {
    private ApiInterface apiInterface;
    private MutableLiveData<ApiCallStatus> status;
    private User user;

    public LoginViewModel() {
        status = new MutableLiveData<>();
        status.setValue(new ApiCallStatus(ApiCallStatusValue.PROCESSING));
    }

    public MutableLiveData<ApiCallStatus> getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public void signInUser(String userEmail, String userPassword){

        this.apiInterface = ApiClient.getInstance().getApiInterface();
        Call<User> call = this.apiInterface.signInUser(userEmail,userPassword);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    user=response.body();
                    status.setValue(new ApiCallStatus(ApiCallStatusValue.FINISHED));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                status.setValue(new ApiCallStatus(ApiCallStatusValue.FAILURE));
            }
        });

    }


}
