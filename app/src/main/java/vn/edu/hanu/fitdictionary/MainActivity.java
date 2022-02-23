package vn.edu.hanu.fitdictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import vn.edu.hanu.fitdictionary.data.User;
import vn.edu.hanu.fitdictionary.helper.RenderFragment;
import vn.edu.hanu.fitdictionary.login_screen.LoginFragment;

public class MainActivity extends AppCompatActivity implements RenderFragment {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

                LoginFragment loginFragment = LoginFragment.newInstance();
                openFragment(loginFragment, false);
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
    public void setUser(User user) {
        this.user = user;
    }


}

