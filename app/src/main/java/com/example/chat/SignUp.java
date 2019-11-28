package com.example.chat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chat.Retrofit.INodeJs;
import com.example.chat.Retrofit.RetrofitClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUp extends AppCompatActivity {
    private Retrofit retrofit;
    private com.example.chat.Retrofit.INodeJs INodeJs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        retrofit = RetrofitClient.getInstance();
        INodeJs = retrofit.create(com.example.chat.Retrofit.INodeJs.class);


    }
    public void eventRegister(View view){
        EditText name, email, password;
        name = findViewById(R.id.editName);
        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);

        String sname= name.getText().toString();
        String semail= email.getText().toString();
        String spassword= password.getText().toString();

        sendData(sname,semail,spassword);
    }

    private void sendData(String name, String  email, String password){

                HashMap<String, String> map = new HashMap<>();

                map.put("name", name);
                map.put("email", email);
                map.put("password", password);

                Call<Void> call = INodeJs.executeSignup(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() == 200) {
                            Toast.makeText(SignUp.this,
                                    "Signed up successfully", Toast.LENGTH_LONG).show();
                        } else if (response.code() == 400) {
                            Toast.makeText(SignUp.this,
                                    "Already registered", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(SignUp.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }


}
