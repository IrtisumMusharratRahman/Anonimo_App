package com.project.anonimo.ui.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.project.anonimo.MainActivity;
import com.project.anonimo.databinding.ActivityLoginBinding;
import com.project.anonimo.databinding.ActivitySignupBinding;
import com.project.anonimo.ui.feed.FeedViewModel;
import com.project.anonimo.ui.login.LoginActivity;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private SignupViewModel signupViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button button = binding.btnSignup;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}