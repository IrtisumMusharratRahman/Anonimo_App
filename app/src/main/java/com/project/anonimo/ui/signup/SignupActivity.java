package com.project.anonimo.ui.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.anonimo.MainActivity;
import com.project.anonimo.R;
import com.project.anonimo.data.ApiCallStatus;
import com.project.anonimo.data.ApiCallStatusValue;
import com.project.anonimo.data.model.User;
import com.project.anonimo.databinding.ActivityLoginBinding;
import com.project.anonimo.databinding.ActivitySignupBinding;
import com.project.anonimo.ui.feed.FeedViewModel;
import com.project.anonimo.ui.login.LoginActivity;

import java.util.UUID;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        EditText name = binding.etUsername;
        EditText email = binding.etEmail;
        EditText pass = binding.etPassword;
        Button button = binding.btnSignup;
        SignupActivity signupActivity = this;


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!name.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !pass.getText().toString().isEmpty()){
                    User user = new User(UUID.randomUUID().toString(),name.getText().toString(),email.getText().toString(),pass.getText().toString());
                    signupViewModel.signUpUser(user);
                }else{
                    Toast.makeText(signupActivity,"Please fill in name,email & password",Toast.LENGTH_LONG).show();
                }




            }
        });


        signupViewModel.getStatus().observe(this, new Observer<ApiCallStatus>() {
            @Override
            public void onChanged(ApiCallStatus apiCallStatus) {
                switch (signupViewModel.getStatus().getValue().getStatus()){
                    case ApiCallStatusValue.PROCESSING:
                        break;
                    case ApiCallStatusValue.FINISHED:
                        Toast.makeText(signupActivity,"SignUp Successful",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    case ApiCallStatusValue.UNSUCCESSFUL:
                        Toast.makeText(signupActivity,"This user already exist",Toast.LENGTH_LONG).show();
                        break;
                    case ApiCallStatusValue.FAILURE:
                        Toast.makeText(signupActivity,"SignUp Failed",Toast.LENGTH_LONG).show();
                        break;
                    default:

                }

            }
        });

    }
}