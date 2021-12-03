package com.example.signupform;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpForm extends AppCompatActivity {
    TextInputLayout regName,regEmail,regPassword,cPassword;
    Button regBtn;


    private  Boolean validateFullName() {
        String val =regName.getEditText().getText().toString();

        if (val.isEmpty()) {
            regName.setError("Field cannot be empty");
            return false;
        }else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }

    }

    private  Boolean validateEmail() {
        String val =regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        }else if (!val.matches(emailPattern)) {
            regEmail.setError("Invalid email address");
            return false;
        }

        else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_form);

        regName = findViewById(R.id.reg_name);
        regEmail = findViewById(R.id.reg_email);
        regPassword = findViewById(R.id.reg_password);
        cPassword=findViewById(R.id.c_password);
        regBtn = findViewById(R.id.btn_register);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateFullName()  | !validateEmail()  ) {

                    String message = "All field Required";
                    Toast.makeText(SignUpForm.this,message,Toast.LENGTH_LONG).show();
                }
                SignUpRequest signUpRequest = new SignUpRequest();
                signUpRequest.setEmail(regEmail.getEditText().getText().toString());
                signUpRequest.setPassword(regPassword.getEditText().getText().toString());
                signUpRequest.setFirst_name(regName.getEditText().getText().toString());
                registerUser(signUpRequest);
            }
        });


    }
    public void registerUser(SignUpRequest signUpRequest){

        Call<SignUpResponse> signUpResponseCall = ApiClient.getService().registerUsers(signUpRequest);
        signUpResponseCall.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if(response.isSuccessful()){
                    String message = "Registration Successful....";
                    Toast.makeText(SignUpForm.this,message,Toast.LENGTH_LONG).show();

                    final AlertDialog.Builder alert = new AlertDialog.Builder(SignUpForm.this);
                    View mview = getLayoutInflater().inflate(R.layout.custom_dialog,null);

                    final TextView dialog_text = mview.findViewById(R.id.dialog_textView);
                    Button cancle_btn = mview.findViewById(R.id.dialog_btn);

                    alert.setView(mview);

                    final AlertDialog alertDialog = alert.create();
                    alertDialog.setCanceledOnTouchOutside(false);

                    cancle_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                }else{
                    String message = "An error occurred, please try again";
                    Toast.makeText(SignUpForm.this,message,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {

                String message = t.getLocalizedMessage();
                Toast.makeText(SignUpForm.this,message,Toast.LENGTH_LONG).show();

            }
        });
    }



}