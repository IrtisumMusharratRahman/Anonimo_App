package com.project.anonimo.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.anonimo.data.ApiCallStatus;
import com.project.anonimo.data.ApiCallStatusValue;
import com.project.anonimo.data.model.User;
import com.project.anonimo.repository.API.ApiClient.ApiClient;
import com.project.anonimo.repository.API.ApiInterface.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Path;

public class ProfileViewModel extends ViewModel {

    private ApiInterface apiInterface;
    private MutableLiveData<ApiCallStatus> updateStatus;
    private MutableLiveData<ApiCallStatus> deleteStatus;

    public ProfileViewModel() {
        updateStatus = new MutableLiveData<>();
        updateStatus.setValue(new ApiCallStatus(ApiCallStatusValue.PROCESSING));

        deleteStatus = new MutableLiveData<>();
        deleteStatus.setValue(new ApiCallStatus(ApiCallStatusValue.PROCESSING));
    }

    public MutableLiveData<ApiCallStatus> getUpdateStatus() {
        return updateStatus;
    }

    public MutableLiveData<ApiCallStatus> getDeleteStatus() {
        return deleteStatus;
    }

    public void updateUserName(User user) {
        this.apiInterface = ApiClient.getInstance().getApiInterface();
        Call<String> call = this.apiInterface.updateUserName(user);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    updateStatus.setValue(new ApiCallStatus(ApiCallStatusValue.FINISHED));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                updateStatus.setValue(new ApiCallStatus(ApiCallStatusValue.FAILURE));
            }
        });
    }

    public void deletePost(String postID) {
        this.apiInterface = ApiClient.getInstance().getApiInterface();
        Call<String> call = this.apiInterface.deletePost(postID);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    deleteStatus.setValue(new ApiCallStatus(ApiCallStatusValue.FINISHED));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                deleteStatus.setValue(new ApiCallStatus(ApiCallStatusValue.FAILURE));
            }
        });
    }
}