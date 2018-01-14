package com.sheygam.java_17_14_01_18;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gregorysheygam on 14/01/2018.
 */

public class HttpProvider {
    private static final HttpProvider ourInstance = new HttpProvider();
    private static final String BASE_URL = "https://telranstudentsproject.appspot.com/_ah/api/contactsApi/v1";

    private OkHttpClient client;
    private MediaType JSON;
    private Gson gson;

    public static HttpProvider getInstance() {
        return ourInstance;
    }

    private HttpProvider() {
//        Manager m = new Manager.Builder()
//                .name("")
//                .email("")
//                .salary(26)
//                .build();
        client = new OkHttpClient();
        JSON = MediaType.parse("application/json; charset=utf-8");
        gson = new Gson();
    }


    public String registration(String email, String password) throws Exception {
        Auth auth = new Auth(email,password);
        String jsonRequest = gson.toJson(auth);

        RequestBody body = RequestBody.create(JSON,jsonRequest);
        Request request = new Request.Builder()
                .url(BASE_URL + "/registration")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        if(response.code() < 400){
            String responseJson = response.body().string();
            AuthToken token = gson.fromJson(responseJson,AuthToken.class);
            return token.getToken();
        }else if(response.code() == 409){
            throw new Exception("User already exist!");
        }else{
            String error = response.body().string();
            Log.d("MY_TAG", "registration: error:" + error);
            throw new Exception("Server error! Call to support!");
        }
    }

}
