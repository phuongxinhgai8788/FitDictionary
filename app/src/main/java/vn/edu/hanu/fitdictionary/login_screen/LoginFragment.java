package vn.edu.hanu.fitdictionary.login_screen;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import vn.edu.hanu.fitdictionary.MainActivity;
import vn.edu.hanu.fitdictionary.R;
import vn.edu.hanu.fitdictionary.UserHomeFragment;
import vn.edu.hanu.fitdictionary.confirm_email_screen.ConfirmEmailFragment;
import vn.edu.hanu.fitdictionary.data.User;
import vn.edu.hanu.fitdictionary.data.UserViewModel;
import vn.edu.hanu.fitdictionary.data.Users;
import vn.edu.hanu.fitdictionary.databinding.FragmentForgotPasswordBinding;
import vn.edu.hanu.fitdictionary.helper.RenderFragment;
import vn.edu.hanu.fitdictionary.register_screen.RegisterFragment;


public class LoginFragment extends Fragment {


    private ConstraintLayout loginConstraint;
    private EditText emailET, passwordET;
    private TextView forgotPassTV, alertEmailTV, alertPasswordTV, signUpTV;
    private ImageView passwordIV;
    private Context context;
    private RenderFragment renderFragment;
    private String emailEntered,  passwordEntered;
    private Users users;
    private LoginViewModel loginViewModel;
    private static final String USERS = "users";

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(Users users) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putSerializable(USERS, users);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            users = (Users)getArguments().getSerializable(USERS);
        }
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.renderFragment = (RenderFragment) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        emailET = view.findViewById(R.id.email_address_et_login);
        passwordET = view.findViewById(R.id.password_et_login);
        forgotPassTV = view.findViewById(R.id.forgot_pass_login);
        loginConstraint = view.findViewById(R.id.login_btn_login);
        alertEmailTV = view.findViewById(R.id.alert_login);
        passwordIV = view.findViewById(R.id.see_pass_login);
        signUpTV = view.findViewById(R.id.signup_switch);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginConstraint.setEnabled(false);
        loginViewModel.isEmailValidate.observe(getViewLifecycleOwner(), data -> {
            if (!data) {
                alertEmailTV.setVisibility(View.VISIBLE);
                alertEmailTV.setText("Hanu email format @s.hanu.edu.vn is required");
            } else{
                alertEmailTV.setVisibility(View.INVISIBLE);
            }
        });

        loginViewModel.isPasswordValidate.observe(getViewLifecycleOwner(), data -> {
            if (!data) {
                alertEmailTV.setVisibility(View.VISIBLE);
                alertEmailTV.setText("Password must have more than 5 characters");
            }else{
                alertEmailTV.setVisibility(View.INVISIBLE);
            }
        });

        loginViewModel.isBtnLoginValidate.observe(getViewLifecycleOwner(), data -> {
            loginConstraint.setEnabled(data);
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        emailET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailEntered = s.toString();
                loginViewModel.onEmailChanged(s, start, before, count);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordIV.setImageResource(R.mipmap.ic_hide_pass_foreground);
                passwordEntered = s.toString();
                loginViewModel.onPasswordChanged(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        passwordIV.setOnClickListener( v -> {
            TransformationMethod showOrHide = passwordET.getTransformationMethod();
            if (showOrHide == null || showOrHide == PasswordTransformationMethod.getInstance()) {
                passwordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                passwordIV.setImageResource(R.drawable.ic_see_pass);
            } else {
                passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                passwordIV.setImageResource(R.mipmap.ic_hide_pass_foreground);

            }
        });
        forgotPassTV.setOnClickListener(v -> {
            ConfirmEmailFragment confirmEmailFragment = ConfirmEmailFragment.newInstance();
            renderFragment.openFragment(confirmEmailFragment, true);
        });

        loginConstraint.setOnClickListener(v -> {
                confirmUser();

        });

        //sign up
        signUpTV.setOnClickListener(v -> {
            RegisterFragment registerFragment = RegisterFragment.newInstance(users);
            renderFragment.openFragment(registerFragment, true);
        });
    }

    private void confirmUser() {

//        userViewModel.fetchUserByEmail(emailEntered).observe(getViewLifecycleOwner(), user -> {
//            if(user==null){
//                Toast.makeText(context,"User does not exist", Toast.LENGTH_SHORT).show();
//            }else{
//                String password = user.getPassword();
//                if(!password.equals(passwordEntered)){
//                    Toast.makeText(context,"Password does not match", Toast.LENGTH_SHORT).show();
//                }else{
//                    UserHomeFragment userHomeFragment = UserHomeFragment.newInstance(user);
//                    renderFragment.openFragment(userHomeFragment, true);
//                }
//                Log.d("LoginFragment",user.toString()+"");
//            }
//        });
        boolean hasUser = false;
        User user = new User();
        for(User user1:users.getUsers()){
            if(user1.getEmail().equals(emailEntered)){
                user = user1;
                hasUser = true;
            }
        }
        if(hasUser){
            if(!passwordEntered.equals(user.getPassword())){
                Toast.makeText(context, user.getFullName()+" : Wrong password", Toast.LENGTH_SHORT).show();
            }else {
                UserHomeFragment userHomeFragment = UserHomeFragment.newInstance(user);
                renderFragment.openFragment(userHomeFragment, true);
            }
        }else{
            Toast.makeText(context, "User does not exist",Toast.LENGTH_SHORT).show();
        }
    }
}