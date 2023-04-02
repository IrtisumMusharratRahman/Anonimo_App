package com.project.anonimo.ui.signup;

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
import retrofit2.http.Body;

public class SignupViewModel extends ViewModel {

    private ApiInterface apiInterface;
    private MutableLiveData<ApiCallStatus> status;

    public SignupViewModel() {
        status = new MutableLiveData<>();
        status.setValue(new ApiCallStatus(ApiCallStatusValue.PROCESSING));
    }

    public MutableLiveData<ApiCallStatus> getStatus() {
        return status;
    }

    public void signUpUser(User user){
        this.apiInterface = ApiClient.getInstance().getApiInterface();
        Call<String> call = this.apiInterface.signUpUser(user);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    status.setValue(new ApiCallStatus(ApiCallStatusValue.FINISHED));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                status.setValue(new ApiCallStatus(ApiCallStatusValue.FAILURE));
            }
        });
    }


}
