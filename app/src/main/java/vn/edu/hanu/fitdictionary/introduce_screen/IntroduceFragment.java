package vn.edu.hanu.fitdictionary.introduce_screen;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.edu.hanu.fitdictionary.MainActivity;
import vn.edu.hanu.fitdictionary.R;


public class IntroduceFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ConstraintLayout loginConstraint;
    private ConstraintLayout signUpConstraint;
    private Context context;

    public IntroduceFragment() {
        // Required empty public constructor
    }

       public static IntroduceFragment newInstance() {
        IntroduceFragment fragment = new IntroduceFragment();

        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_introduce, container, false);
        loginConstraint = view.findViewById(R.id.login_bar_login);
        signUpConstraint = view.findViewById(R.id.signup_bar_login);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity mainActivity = (MainActivity) context;
        loginConstraint.setOnClickListener(v -> {
            mainActivity.renderLoginFragment();
        });
        signUpConstraint.setOnClickListener(v -> {
            mainActivity.renderSignUpFragment();
        });
    }
}