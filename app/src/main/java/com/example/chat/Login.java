package com.example.chat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chat.Retrofit.INodeJs;
import com.example.chat.Retrofit.LoginResult;
import com.example.chat.Retrofit.RetrofitClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    //declarar variables para la conexión con el api

    private Retrofit retrofit;
    private com.example.chat.Retrofit.INodeJs INodeJs;
    //private String BASE_URL = "http://192.168.1.4:3000";


    @Override
    protected void onStop() {
        //cuando se detiene el activity se debe tambien la conexión.

        super.onStop();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //initialization the var of frontens objects



        /*retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();*/
        retrofit = RetrofitClient.getInstance();

        INodeJs = retrofit.create(INodeJs.class);





    }

    public void EventLogin(View View){
        email= findViewById(R.id.editTextEmail);
        password= findViewById(R.id.editTextPassword);

        String semail= email.getText().toString();
        String spassword= password.getText().toString();

        LoginUser( semail,  spassword);
    }

    private void LoginUser(String email, String password){
        HashMap<String, String> map = new HashMap<>();

        map.put("email", email);
        map.put("password", password);

        Call<LoginResult> call = INodeJs.executeLogin(map);

        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {

                if (response.code() == 200) {

                    LoginResult result = response.body();

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Login.this);
                    builder1.setTitle(result.getName());
                    builder1.setMessage(result.getEmail());

                    builder1.show();


                    Toast.makeText(Login.this, "ya "+ result.getName(), Toast.LENGTH_SHORT).show();

                    openChat( result.getName());

                } else if (response.code() == 404) {


                    LoginResult result = response.body();


                    Toast.makeText(Login.this, "Error usuario o contraseña erronea", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Toast.makeText(Login.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    public void eventOptenSignup(View view){
        Intent Register= new Intent(this, SignUp.class);
        startActivity(Register);
    }

    private void openChat(String name){
        Intent intent = new Intent(Login.this, MainActivity.class);
        intent.putExtra("username", name);
        startActivity(intent);
    }
}
