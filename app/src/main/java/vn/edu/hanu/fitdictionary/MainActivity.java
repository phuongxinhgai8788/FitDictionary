package vn.edu.hanu.fitdictionary;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;

import java.time.LocalTime;

public class MainActivity extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment = FitDictionaryFragment.newInstance();
        Fragment fragmentIntro = IntroduceFragment.newInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (currentFragment == null) {
            fragmentTransaction.add(R.id.fragment_container, fragment).commit();
        }
            LocalTime now1 = LocalTime.now();
            LocalTime now2 = now1.plusSeconds(2);

            while(!now1.equals(now2)){
                now1 = LocalTime.now();
            }
                fragmentTransaction.replace(R.id.fragment_container, fragmentIntro);



    }
}

