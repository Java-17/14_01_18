package com.sheygam.java_17_14_01_18;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputEmail, inputPassword;
    private ProgressBar myProgress;
    private Button loginBtn, regBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        myProgress = findViewById(R.id.myProgress);
        loginBtn = findViewById(R.id.loginBtn);
        regBtn = findViewById(R.id.regBtn);
        regBtn.setOnClickListener(v -> new RegTask().execute());
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.loginBtn){
            regBtn.setEnabled(false);
            loginBtn.setEnabled(false);
            myProgress.setVisibility(View.VISIBLE);
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();

            HttpProvider.getInstance().login(email, password, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "Connection error!", Toast.LENGTH_SHORT).show();
                        myProgress.setVisibility(View.INVISIBLE);
                        regBtn.setEnabled(true);
                        loginBtn.setEnabled(true);

                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()){
                        Gson gson = new Gson();
                        String responseJson = response.body().string();
                        AuthToken authToken = gson.fromJson(responseJson,AuthToken.class);
                        Log.d("MY_TAG", "onResponse: " + authToken.getToken());
                        runOnUiThread(() -> {
                            regBtn.setEnabled(true);
                            loginBtn.setEnabled(true);
                            myProgress.setVisibility(View.INVISIBLE);
                        });
                    }else{
                        Log.d("MY_TAG", "onResponse: error:" + response.body().string());
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, "Http error!", Toast.LENGTH_SHORT).show();
                            regBtn.setEnabled(true);
                            loginBtn.setEnabled(true);
                            myProgress.setVisibility(View.INVISIBLE);
                        });
                    }
                }
            });
        }
    }

    class RegTask extends AsyncTask<Void,Void,String>{
        String email, password;
        boolean isSuccess = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            email = ((EditText)findViewById(R.id.inputEmail)).getText().toString();
            password = ((EditText)findViewById(R.id.inputPassword)).getText().toString();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String res = "Registration OK!";
            try {
                String token = HttpProvider.getInstance().registration(email,password);
                Log.d("MY_TAG", "doInBackground: " + token);
            } catch (IOException e){
                res = "Connection error!";
                isSuccess = false;
            }catch (Exception e) {
                e.printStackTrace();
                res = e.getMessage();
                isSuccess = false;
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            if(isSuccess){
                // Start next activity
            }
        }
    }
}
