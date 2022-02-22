package vn.edu.hanu.fitdictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import vn.edu.hanu.fitdictionary.data.User;
import vn.edu.hanu.fitdictionary.data.UserViewModel;
import vn.edu.hanu.fitdictionary.data.Users;
import vn.edu.hanu.fitdictionary.helper.RenderFragment;
import vn.edu.hanu.fitdictionary.login_screen.LoginFragment;

public class MainActivity extends AppCompatActivity implements RenderFragment {

    private User user;
    private Users users;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.fetchUsers().observe(this, users -> {
            if(users!=null){
                this.users = new Users(users);
                LoginFragment loginFragment = LoginFragment.newInstance(this.users);
                openFragment(loginFragment, false);
            }
        });

    }

    @Override
    public void openFragment(Fragment fragment, Boolean addToBackStack){
        final String TAG = fragment.getClass().getSimpleName();

        Fragment sameTagFragment = getSupportFragmentManager().findFragmentByTag(TAG);

        if(sameTagFragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(sameTagFragment)
                    .commit();
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, TAG);

        if(addToBackStack) fragmentTransaction.addToBackStack(TAG);

        try {
            fragmentTransaction.commit();
        }catch(IllegalStateException ise){
            ise.printStackTrace();
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void updateUsers() {
        userViewModel.fetchUsers().observe(this, users -> {
            this.users = new Users(users);
        });
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Users getUsers() {
        return this.users;
    }

}

