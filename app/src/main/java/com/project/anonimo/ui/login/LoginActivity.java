package com.project.anonimo.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.project.anonimo.ui.signup.SignupActivity;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        Button button = binding.btnLogin;
        TextView forgotPassword = binding.tvForgotPassword;
        EditText name = binding.etUsername;
        EditText password = binding.etPassword;
        LoginActivity loginActivity = this;

        SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        String email = sharedPref.getString("username", "");
        String pass = sharedPref.getString("password", "");

        if (!email.isEmpty() && !pass.isEmpty()) {
            ConnectivityManager cm = (ConnectivityManager) loginActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            if (activeNetwork != null && activeNetwork.isConnected()) {
                loginViewModel.signInUser(email,pass);
            } else {
                Toast.makeText(loginActivity,"No Internet",Toast.LENGTH_LONG).show();
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager cm = (ConnectivityManager) loginActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                if (activeNetwork != null && activeNetwork.isConnected()) {
                    if (!name.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
                        loginViewModel.signInUser(name.getText().toString(),password.getText().toString());
                    }else{
                        Toast.makeText(loginActivity,"Please fill in email & password",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(loginActivity,"No Internet",Toast.LENGTH_LONG).show();
                }




            }
        });

        loginViewModel.getStatus().observe(this, new Observer<ApiCallStatus>() {
            @Override
            public void onChanged(ApiCallStatus apiCallStatus) {
                switch (loginViewModel.getStatus().getValue().getStatus()){
                    case ApiCallStatusValue.PROCESSING:
                        break;
                    case ApiCallStatusValue.FINISHED:

                        if(loginViewModel.getUser()!=null){
                            User user =loginViewModel.getUser();
                            if (!(user.getUserID()==null)){


                                SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("username", user.getUserEmail());
                                editor.putString("password", user.getUserPassword());
                                editor.apply();


                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("id",user.getUserID());
                                intent.putExtra("name",user.getUserName());
                                intent.putExtra("email",user.getUserEmail());
                                intent.putExtra("password",user.getUserPassword());
                                startActivity(intent);
                            }else {
                                Toast.makeText(loginActivity,"Wrong email or password",Toast.LENGTH_LONG).show();
                            }

                        }
                        break;
                    case ApiCallStatusValue.FAILURE:
                        Toast.makeText(loginActivity,"Login Failed",Toast.LENGTH_LONG).show();
                        break;
                    default:

                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

    }
}