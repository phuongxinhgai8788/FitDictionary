package vn.edu.hanu.fitdictionary.register_screen;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import vn.edu.hanu.fitdictionary.R;
import vn.edu.hanu.fitdictionary.data.User;
import vn.edu.hanu.fitdictionary.data.UserViewModel;
import vn.edu.hanu.fitdictionary.helper.RenderFragment;


public class RegisterFragment extends Fragment {
    private ConstraintLayout signUpConstraint;
    private EditText emailET, passwordET, fullNameET;
    private TextView alertEmailTV, alertPasswordTV, logInTV, signUpTV;
    private ImageView seePassIV;
    private String emailEntered, passwordEntered, fullNameEntered;
    private RegisterViewModel registerViewModel;
    private UserViewModel userViewModel;
    private Context context;
    private int maxID;

    public RegisterFragment() {
        // Required empty public constructor
    }


    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
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
        seePassIV = view.findViewById(R.id.see_pass_login);
        signUpConstraint = view.findViewById(R.id.signup_btn);
        emailET = view.findViewById(R.id.email_address_et_signup);
        passwordET = view.findViewById(R.id.password_et_signup);
        fullNameET = view.findViewById(R.id.full_name_et_signup);
        alertEmailTV = view.findViewById(R.id.alert_email_signup);
        alertPasswordTV = view.findViewById(R.id.alert_password_signup);
        logInTV = view.findViewById(R.id.login_switch);
        signUpTV = view.findViewById(R.id.signup_tv);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signUpConstraint.setEnabled(false);
        registerViewModel.isEmailValidate.observe(getViewLifecycleOwner(), data -> {
            if(!data){
                alertEmailTV.setText("Hanu email format @s.hanu.edu.vn is required");
            }else{
                alertEmailTV.setVisibility(View.INVISIBLE);
                registerViewModel.validateBtnSignUp();
            }
        });
        registerViewModel.isPasswordValidate.observe(getViewLifecycleOwner(), data -> {
            if(!data){
                alertPasswordTV.setVisibility(View.VISIBLE);
                alertPasswordTV.setText("Password must have more than 5 characters");
            }else {
                alertPasswordTV.setVisibility(View.INVISIBLE);
                registerViewModel.validateBtnSignUp();
            }
        });
        registerViewModel.isFullNameValidate.observe(getViewLifecycleOwner(), data -> {
            if(data){
                registerViewModel.validateBtnSignUp();
            }
        });
        registerViewModel.isBtnSignUpValidate.observe(getViewLifecycleOwner(), data -> {
           if(data){
               signUpConstraint.setEnabled(true);
               signUpConstraint.setBackgroundResource(R.drawable.background_gradient);
               signUpTV.setTextColor(getResources().getColor(R.color.white));
           } else {
               signUpConstraint.setEnabled(false);
               signUpConstraint.setBackgroundResource(R.drawable.background_button);
               signUpTV.setTextColor(getResources().getColor(R.color.gray));
           }

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
        
        seePassIV.setOnClickListener( v -> {
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

        logInTV.setOnClickListener(v -> {
           getActivity().onBackPressed();
        });
    }

    private void confirmUser() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_background);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        userViewModel.getUserByEmail(emailEntered).observe(getViewLifecycleOwner(), user -> {
            progressDialog.dismiss();
            if(user!=null && user.size()>0){
            Toast.makeText(context, "User exists. Login or reset password", Toast.LENGTH_SHORT).show();

            }else{
            User newUser = new User();
                newUser.setEmail(emailEntered);
                newUser.setFullName(fullNameEntered);
                newUser.setPassword(passwordEntered);
                userViewModel.getLastId().observe(getViewLifecycleOwner(), max -> {
                    if (max != null) {
                        maxID = max.getMax();
                        newUser.setId(maxID + 1);
                        userViewModel.saveUser(newUser).observe(getViewLifecycleOwner(), savedUser -> {
                            if (savedUser != null) {
                                getActivity().onBackPressed();
                            }
                        });
                    }

                });
        }
        });
    }
}

