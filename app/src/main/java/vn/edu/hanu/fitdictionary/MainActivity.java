package vn.edu.hanu.fitdictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import vn.edu.hanu.fitdictionary.data.User;
import vn.edu.hanu.fitdictionary.confirm_email_screen.ConfirmEmailFragment;
import vn.edu.hanu.fitdictionary.helper.RenderFragment;
import vn.edu.hanu.fitdictionary.verification_code_screen.VerificationCodeFragment;
import vn.edu.hanu.fitdictionary.introduce_screen.IntroduceFragment;
import vn.edu.hanu.fitdictionary.login_screen.LoginFragment;
import vn.edu.hanu.fitdictionary.register_screen.RegisterFragment;

public class MainActivity extends AppCompatActivity implements RenderFragment {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginFragment introduceFragment = LoginFragment.newInstance();
        openFragment(introduceFragment, false);

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

//    public void setUser(User user){
//        this.user = user;
//    }
//
//    public void renderIntroduceFragment(){
//        Fragment fragment = IntroduceFragment.newInstance();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
//        if (currentFragment == null) {
//            fragmentTransaction.add(R.id.fragment_container, fragment)
//                    .commit();
//        }
//    }
//
//    public void renderLoginFragment(){
//        Fragment fragment = LoginFragment.newInstance();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.addToBackStack(null)
//                .replace(R.id.fragment_container, fragment)
//                .commit();
//    }
//
//    public void renderSignUpFragment() {
//        Fragment fragment = RegisterFragment.newInstance();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.addToBackStack(null)
//                .replace(R.id.fragment_container, fragment)
//                .commit();
//    }
//
//    public void renderForgotPasswordFragment() {
//        Fragment fragment = ConfirmEmailFragment.newInstance();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.addToBackStack(null)
//                .replace(R.id.fragment_container, fragment)
//                .commit();
//    }
//
//    public void renderVerificationCodeFragment(String code){
//        Fragment fragment = VerificationCodeFragment.newInstance(user,code);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.addToBackStack(null)
//                .replace(R.id.fragment_container, fragment)
//                .commit();
//
//    }
//
//    public void renderUserHomeFragment() {
//    }

}

