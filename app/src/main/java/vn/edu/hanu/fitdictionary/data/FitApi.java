package vn.edu.hanu.fitdictionary.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FitApi {
    @GET("/users")
    public Call<List<User>> fetchUsers();

    @GET("/users/id/{id}")
    public Call<User> fetchUserById(@Path("id") int id);

    @GET("/users/email/{email}")
    public Call<User> fetchUserByEmail(@Path("String") String email);

    @POST("/users")
    public Call<User> register(@Body User user);

    @PUT("update/{id}")
    public Call<User> updateProfile(@Path("id") int id, @Body User user);

}
