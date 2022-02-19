package vn.edu.hanu.fitdictionary.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FitApi {
    @GET("/user")
    public Call<UsersResponse> fetchUsers();

    @GET("/user/email")
    public Call<User> fetchUserByEmail(@Query("email") String email);
}
