package com.sheygam.java_17_14_01_18;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.regBtn)
                .setOnClickListener(v -> new RegTask().execute());
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
