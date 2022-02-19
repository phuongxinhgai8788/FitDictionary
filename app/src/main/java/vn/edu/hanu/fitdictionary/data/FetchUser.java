package vn.edu.hanu.fitdictionary.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FetchUser {
    private Retrofit retrofit;
    private FitApi fitApi;
    private static FetchUser instance;
    private final String TAG = "FetchUser";

    private FetchUser(){

        retrofit = new Retrofit.Builder().baseUrl("http://192.168.2.100:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        fitApi = retrofit.create(FitApi.class);
    }

    public LiveData<List<User>> fetchUsers(){
        MutableLiveData<List<User>> responseLiveData = new MutableLiveData<>();
        Call<UsersResponse> userRequest = fitApi.fetchUsers();
        userRequest.enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                Log.d(TAG, "Users response received");
                UsersResponse usersResponse = response.body();
                List<User> users = usersResponse.users;
                responseLiveData.setValue(users);
            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {

                Log.d(TAG, "Failed to fetch users", t);
            }
        });
        return responseLiveData;
    }

    public LiveData<User> fetchUserByEmail(String email){
        MutableLiveData<User> responseLiveData = new MutableLiveData<>();
        Call<User> userRequest = fitApi.fetchUserByEmail(email);
        userRequest.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "User response received: "+response.body());
                User user = response.body();
                responseLiveData.setValue(user);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Log.d(TAG, "Failed to fetch user", t);
            }
        });
        return responseLiveData;
    }
    public static void initialize(){
        if(instance == null){
            instance = new FetchUser();
        }
    }
    public static FetchUser get(){
        if(instance == null){
            throw new IllegalStateException("FetchUser must be initialized!");
        }
        return instance;
    }
}
