package vn.edu.hanu.fitdictionary.confirm_email_screen;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import vn.edu.hanu.fitdictionary.MainActivity;
import vn.edu.hanu.fitdictionary.R;
import vn.edu.hanu.fitdictionary.data.User;
import vn.edu.hanu.fitdictionary.data.UserViewModel;
import vn.edu.hanu.fitdictionary.helper.JavaMailAPI;


public class ConfirmEmailFragment extends Fragment {

    private EditText emailET;
    private TextView alertTV;
    private ConstraintLayout resetPasswordConstraint, backConstraint;
    private String emailEntered, code;
    private Context context;
    private ConfirmEmailViewModel confirmEmailViewModel;
    private UserViewModel userViewModel;
    private MainActivity mainActivity;
    private User user;

    public ConfirmEmailFragment() {
        // Required empty public constructor
    }


    public static ConfirmEmailFragment newInstance() {
        ConfirmEmailFragment fragment = new ConfirmEmailFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        confirmEmailViewModel = ViewModelProviders.of(this).get(ConfirmEmailViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        emailET = view.findViewById(R.id.email_address_et_forgot_pass);
        resetPasswordConstraint = view.findViewById(R.id.reset_pass_bar);
        alertTV = view.findViewById(R.id.alert_forgot_pass);
        backConstraint = view.findViewById(R.id.back_constraint_forgot_pass);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resetPasswordConstraint.setEnabled(false);
        confirmEmailViewModel.isEmailValidate.observe(getViewLifecycleOwner(), data -> {
            if(!data){
                alertTV.setText("Wrong Hanu email format");
                alertTV.setTextColor(Color.RED);
            }else{
                alertTV.setText("Correct Hanu email format");
                alertTV.setTextColor(Color.BLACK);
            }
        });
        confirmEmailViewModel.isBtnResetPasswordValidate.observe(getViewLifecycleOwner(), data -> {
            resetPasswordConstraint.setEnabled(data);
            Log.d("ConfirmEmailFragment", resetPasswordConstraint.isEnabled()+"");
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        backConstraint.setOnClickListener(v -> {
            mainActivity.onBackPressed();
        });
        emailET.addTextChangedListener( new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailEntered = s.toString();
                confirmEmailViewModel.onEmailChanged(s, start, before,count);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        resetPasswordConstraint.setOnClickListener(v -> {

                confirmUser();

        });
    }

    private void confirmUser() {
        userViewModel.fetchUserByEmail(emailEntered).observe(getViewLifecycleOwner(), user -> {

            if (user == null) {

                Toast.makeText(context, "Account does not exist", Toast.LENGTH_SHORT).show();
            } else {
                this.user = user;
                sendEmail();
                mainActivity.setUser(user);
                mainActivity.renderVerificationCodeFragment(code);
            }
        });
    }

    private void sendEmail() {
        code = "";
        for(int i=0; i<6;i++){
            int randomDigit = (int) ( 10*Math.random());
            code+=randomDigit;
        }
        String emailBody = "Dear "+user.getFullName()+",\n We got a request to reset your FIT Dictionary " +
                "password. Please copy and paste the Verification Code below on your FIT App.\n"+code;
        String subject = "FIT Dictionary verification code";

        JavaMailAPI javaMailAPI = new JavaMailAPI(context, user.getEmail(), subject, emailBody);

        javaMailAPI.execute();
    }
    }
