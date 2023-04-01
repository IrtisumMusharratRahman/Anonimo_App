package com.project.anonimo.ui.new_post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewPostViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public NewPostViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is NewPost fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}