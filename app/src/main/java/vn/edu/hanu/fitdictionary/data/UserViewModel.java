package vn.edu.hanu.fitdictionary.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class UserViewModel extends ViewModel {

    private FetchUser fetchUser = FetchUser.get();

    public UserViewModel(){
    }

    public LiveData<User> fetchUserByEmail(String email){
        return fetchUser.fetchUserByEmail(email);
    }

    public LiveData<List<User>> fetchUsers(){
        return fetchUser.fetchUsers();
    }

    public void updateUser(User user) {
    }
}
