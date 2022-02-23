package vn.edu.hanu.fitdictionary.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Collection;
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

        retrofit = new Retrofit.Builder().baseUrl("https://fit-dic-api-5th.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        fitApi = retrofit.create(FitApi.class);
    }

    public LiveData<List<User>> fetchUsers(){
        MutableLiveData<List<User>> responseLiveData = new MutableLiveData<>();
        Call<List<User>> userRequest = fitApi.fetchUsers();
        userRequest.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d(TAG, "Users response received");
                List<User> users = response.body();
                responseLiveData.setValue(users);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

                Log.d(TAG, "Failed to fetch users", t);
            }
        });
        return responseLiveData;
    }

    public LiveData<User> fetchUserByID(int id){
        MutableLiveData<User> responseLiveData = new MutableLiveData<>();
        Call<User> userRequest = fitApi.fetchUserById(id);
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
    public LiveData<User> register (User user){
        MutableLiveData<User> responseLiveData = new MutableLiveData<>();
        Call<User> userRequest = fitApi.register(user);
        userRequest.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "Register user response received: "+response);
                User user = response.body();
                responseLiveData.setValue(user);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Log.d(TAG, "Failed to register user", t);
            }
        });
        return responseLiveData;
    }
    public LiveData<User> updateProfile (int id, User user){
        MutableLiveData<User> responseLiveData = new MutableLiveData<>();
        Call<User> userRequest = fitApi.updateProfile(id, user);
        userRequest.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "Update User response received: "+response.body());
                User user = response.body();
                responseLiveData.setValue(user);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Log.d(TAG, "Failed to update user", t);
            }
        });
        return responseLiveData;
    }

    public LiveData<Max> fetchMaxId (){
        MutableLiveData<Max> responseLiveData = new MutableLiveData<>();
        Call<Max> userRequest = fitApi.fetchMaxId();
        userRequest.enqueue(new Callback<Max>() {
            @Override
            public void onResponse(Call<Max> call, Response<Max> response) {
                Log.d(TAG, "Get last id response received: "+response.body());
                Max max = response.body();
                responseLiveData.setValue(max);
            }

            @Override
            public void onFailure(Call<Max> call, Throwable t) {

                Log.d(TAG, "Failed to get last user", t);
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
