package vn.edu.hanu.fitdictionary.register_screen;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import vn.edu.hanu.fitdictionary.MainActivity;
import vn.edu.hanu.fitdictionary.R;
import vn.edu.hanu.fitdictionary.data.User;
import vn.edu.hanu.fitdictionary.data.UserViewModel;
import vn.edu.hanu.fitdictionary.helper.RenderFragment;
import vn.edu.hanu.fitdictionary.login_screen.LoginFragment;


public class RegisterFragment extends Fragment {
    private ConstraintLayout backConstraint, signUpConstraint;
    private EditText emailET, passwordET, fullNameET;
    private TextView alertEmailTV, alertPasswordTV;
    private CheckBox checkBoxEmail, checkBoxTerms;
    private ImageView seePassIV;
    private String emailEntered, passwordEntered, fullNameEntered;
    private RegisterViewModel registerViewModel;
    private UserViewModel userViewModel;
    private Context context;

    public RegisterFragment() {
        // Required empty public constructor
    }


    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();

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
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        backConstraint = view.findViewById(R.id.back_constraint_sign_up);
        signUpConstraint = view.findViewById(R.id.signup_btn);
        emailET = view.findViewById(R.id.email_address_et_signup);
        passwordET = view.findViewById(R.id.password_et_signup);
        fullNameET = view.findViewById(R.id.full_name_et_signup);
        alertEmailTV = view.findViewById(R.id.alert_email_signup);
        alertPasswordTV = view.findViewById(R.id.alert_password_signup);
        checkBoxEmail = view.findViewById(R.id.checkbox_email_updates_sign_up);
        checkBoxTerms = view.findViewById(R.id.checkbox_agreement_sign_up);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        signUpConstraint.setEnabled(false);
        registerViewModel.isEmailValidate.observe(getViewLifecycleOwner(), data -> {
            if(!data){
                alertEmailTV.setText("Hanu email format @s.hanu.edu.vn is required");
                alertEmailTV.setTextColor(Color.RED);
            }else{
                alertEmailTV.setText("Correct Hanu email format");
                alertEmailTV.setTextColor(Color.BLACK);
            }
        });
        registerViewModel.isPasswordValidate.observe(getViewLifecycleOwner(), data -> {
            if(!data){
                alertPasswordTV.setText("Password must have more than 5 characters");
                alertPasswordTV.setTextColor(Color.RED);
            }else {
                alertPasswordTV.setVisibility(View.INVISIBLE);
            }
        });
        registerViewModel.isBtnSignUpValidate.observe(getViewLifecycleOwner(), data -> {
            signUpConstraint.setEnabled(data);
            
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
                registerViewModel.onEmailChanged(s, start, before, count);
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
                seePassIV.setImageResource(R.mipmap.ic_hide_pass_foreground);
                passwordEntered = s.toString();
                registerViewModel.onPasswordChanged(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fullNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fullNameEntered = s.toString();
                registerViewModel.onFullNameChange(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        
        alertPasswordTV.setOnClickListener( v -> {
            TransformationMethod showOrHide = passwordET.getTransformationMethod();
            if(showOrHide == null || showOrHide == PasswordTransformationMethod.getInstance()){
                passwordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                seePassIV.setImageResource(R.drawable.ic_see_pass);
            }else{
                passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                seePassIV.setImageResource(R.mipmap.ic_hide_pass_foreground);
            }
        });
        
        signUpConstraint.setOnClickListener(v -> {
            confirmUser();
        });
        
        backConstraint.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }

    private void confirmUser() {

        userViewModel.fetchUserByEmail(emailEntered).observe(getViewLifecycleOwner(), user -> {
            if(user.equals("User not found")){
                User savedUser = new User();
                savedUser.setId(0);
                savedUser.setEmail(emailEntered);
                savedUser.setFullName(fullNameEntered);
                savedUser.setRole("STUDENT");
                savedUser.setPassword(passwordEntered);
                userViewModel.saveUser(savedUser);
                RenderFragment renderFragment = (RenderFragment) context;
                LoginFragment loginFragment = LoginFragment.newInstance();
                renderFragment.openFragment(loginFragment, true);
            }else{
                Toast.makeText(context, "User already exists", Toast.LENGTH_SHORT).show();
            }
        });
    }
}