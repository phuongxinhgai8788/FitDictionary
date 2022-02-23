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

import java.util.List;

import vn.edu.hanu.fitdictionary.R;
import vn.edu.hanu.fitdictionary.data.User;
import vn.edu.hanu.fitdictionary.data.UserViewModel;
import vn.edu.hanu.fitdictionary.helper.JavaMailAPI;
import vn.edu.hanu.fitdictionary.helper.RenderFragment;
import vn.edu.hanu.fitdictionary.verification_code_screen.VerificationCodeFragment;


public class ConfirmEmailFragment extends Fragment {

    private EditText emailET;
    private TextView alertTV, cancelTV;
    private ConstraintLayout sendConstraint;
    private String emailEntered, code;
    private Context context;
    private ConfirmEmailViewModel confirmEmailViewModel;
    private RenderFragment renderFragment;
    private UserViewModel userViewModel;
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
        renderFragment = (RenderFragment) context;
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
        sendConstraint = view.findViewById(R.id.reset_pass_bar);
        alertTV = view.findViewById(R.id.alert_forgot_pass);
        cancelTV = view.findViewById(R.id.cancel_reset_password);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sendConstraint.setEnabled(false);
        confirmEmailViewModel.isEmailValidate.observe(getViewLifecycleOwner(), data -> {
            if(!data){
                alertTV.setVisibility(View.VISIBLE);
                alertTV.setText("Hanu email format @s.hanu.edu.vn is required");
                alertTV.setTextColor(Color.RED);
            }else{
                alertTV.setVisibility(View.INVISIBLE);
            }
        });
        confirmEmailViewModel.isEmailValidate.observe(getViewLifecycleOwner(), data -> {
            sendConstraint.setEnabled(data);
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        cancelTV.setOnClickListener(v -> {
            getActivity().onBackPressed();
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
        sendConstraint.setOnClickListener(v -> {

                confirmUser();

        });
    }

    private void confirmUser() {
        userViewModel.getUserByEmail(emailEntered).observe(getViewLifecycleOwner(), user -> {

            if (user == null) {

                Toast.makeText(context, "Account does not exist", Toast.LENGTH_SHORT).show();
            } else {
                this.user = user.get(0);
                sendEmail();
                VerificationCodeFragment verificationCodeFragment = VerificationCodeFragment.newInstance(this.user, code);
                RenderFragment renderFragment = (RenderFragment) context;
                renderFragment.openFragment(verificationCodeFragment, true);
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
