package vn.edu.hanu.fitdictionary.verification_code_screen;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import vn.edu.hanu.fitdictionary.R;
import vn.edu.hanu.fitdictionary.data.User;
import vn.edu.hanu.fitdictionary.data.UserViewModel;
import vn.edu.hanu.fitdictionary.helper.CountDownTimer;
import vn.edu.hanu.fitdictionary.helper.JavaMailAPI;
import vn.edu.hanu.fitdictionary.helper.RenderFragment;
import vn.edu.hanu.fitdictionary.login_screen.LoginFragment;


public class VerificationCodeFragment extends Fragment {

    private static final String USER = "user";
    private static final String CODE = "code";

    private User user;
    private ConstraintLayout okConstraint, countConstraint;
    private TextView introTV, alertCodeTV, countTV, alertNewPassTV, alertConfirmPassTV, OKTV;
    private String codeSent, codeEntered, newPassEntered, confirmPassEntered;
    private EditText codeET, newPasswordET, confirmPasswordET;
    private ImageView newPasswordIV, confirmPasswordIV;
    private CountDownTimer countDownTimer;
    private Context context;
    private RenderFragment renderFragment;
    private VerificationCodeViewModel verificationCodeViewModel;
    private UserViewModel userViewModel;

    public VerificationCodeFragment() {
        // Required empty public constructor
    }

    public static VerificationCodeFragment newInstance(User user, String code) {
        VerificationCodeFragment fragment = new VerificationCodeFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER, user);
        args.putString(CODE, code);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(USER);
            codeSent = getArguments().getString(CODE);
        }
        verificationCodeViewModel = ViewModelProviders.of(this).get(VerificationCodeViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        countDownTimer = new CountDownTimer(60 * 1000, 1000);
        countDownTimer.setHostFragment(this);
        countDownTimer.start();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        renderFragment = (RenderFragment) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verification_code, container, false);
        okConstraint = view.findViewById(R.id.ok_bar_confirm_code);
        introTV = view.findViewById(R.id.verification_code_was_sent);
        codeET = view.findViewById(R.id.verification_code_et);
        newPasswordET = view.findViewById(R.id.new_password_et_confirm_code);
        confirmPasswordET = view.findViewById(R.id.confirm_password_et_confirm_code);
        newPasswordIV = view.findViewById(R.id.see_new_pass_confirm_code);
        confirmPasswordIV = view.findViewById(R.id.see_confirm_pass_confirm_code);
        alertCodeTV = view.findViewById(R.id.alert_confirm_code);
        alertNewPassTV = view.findViewById(R.id.alert_new_password);
        alertConfirmPassTV = view.findViewById(R.id.alert_confirm_password);
        countConstraint = view.findViewById(R.id.count_constraint_confirm_code);
        countTV = view.findViewById(R.id.count_tv_confirm_code);
        OKTV = view.findViewById(R.id.ok_tv);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        okConstraint.setEnabled(false);

        introTV.setText("Verification code was sent to " + user.getEmail() + ".Resend request after " +
                "60s if you have not received the code yet. Remember to check on Spam!");
        verificationCodeViewModel.isCodeValidate.observe(getViewLifecycleOwner(), data -> {
            if (!data) {
                alertCodeTV.setVisibility(View.VISIBLE);
                alertCodeTV.setText("Code must have 6 digits");
            } else {
                alertCodeTV.setVisibility(View.INVISIBLE);
                verificationCodeViewModel.validateBtnOk();

            }
        });
        verificationCodeViewModel.isNewPasswordValidate.observe(getViewLifecycleOwner(), data -> {
            if (!data) {
                alertNewPassTV.setVisibility(View.VISIBLE);
                alertNewPassTV.setText("Password must have at least 6 characters");
            } else {
                alertNewPassTV.setVisibility(View.INVISIBLE);
                verificationCodeViewModel.validateBtnOk();
            }
        });
        verificationCodeViewModel.isConfirmPasswordValidate.observe(getViewLifecycleOwner(), data -> {
            if (!data) {
                alertConfirmPassTV.setVisibility(View.VISIBLE);
                alertConfirmPassTV.setText("Password must have at least 6 characters");
            } else {
                alertConfirmPassTV.setVisibility(View.INVISIBLE);
                verificationCodeViewModel.validateBtnOk();

            }
        });
        verificationCodeViewModel.isBtnOkValidate.observe(getViewLifecycleOwner(), data -> {
            if(data){
                okConstraint.setEnabled(true);
                okConstraint.setBackgroundResource(R.drawable.background_gradient);
                OKTV.setTextColor(getResources().getColor(R.color.white));
            }else{
                okConstraint.setEnabled(false);
                okConstraint.setBackgroundResource(R.drawable.background_button);
                OKTV.setTextColor(getResources().getColor(R.color.gray));
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        codeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                codeEntered = s.toString();
                verificationCodeViewModel.onCodeChange(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newPasswordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newPasswordIV.setImageResource(R.mipmap.ic_hide_pass_foreground);
                newPassEntered = s.toString();
                verificationCodeViewModel.onNewPasswordChange(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmPasswordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirmPasswordIV.setImageResource(R.mipmap.ic_hide_pass_foreground);
                confirmPassEntered = s.toString();
                verificationCodeViewModel.onConfirmNewPasswordChange(s, start, before, count);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newPasswordIV.setOnClickListener(v -> {
            TransformationMethod showOrHide = newPasswordET.getTransformationMethod();
            if (showOrHide == null || showOrHide == PasswordTransformationMethod.getInstance()) {
                newPasswordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                newPasswordIV.setImageResource(R.drawable.ic_see_pass);
            } else {
                newPasswordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                newPasswordIV.setImageResource(R.mipmap.ic_hide_pass_foreground);

            }
        });
        confirmPasswordIV.setOnClickListener(v -> {
            TransformationMethod showOrHide = confirmPasswordET.getTransformationMethod();
            if (showOrHide == null || showOrHide == PasswordTransformationMethod.getInstance()) {
                confirmPasswordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                confirmPasswordIV.setImageResource(R.drawable.ic_see_pass);
            } else {
                confirmPasswordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                confirmPasswordIV.setImageResource(R.mipmap.ic_hide_pass_foreground);

            }
        });

        okConstraint.setOnClickListener(v -> {
            if (!codeEntered.equals(codeSent)) {
                alertCodeTV.setVisibility(View.VISIBLE);
                alertCodeTV.setText("Code does not match");
            } else if (!newPassEntered.equals(confirmPassEntered)) {
                alertConfirmPassTV.setVisibility(View.VISIBLE);
                alertConfirmPassTV.setText("Confirm password does not match");
            } else {
                user.setPassword(newPassEntered);
                ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_background);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                userViewModel.updateUser(user.getId(), user).observe(getViewLifecycleOwner(), user -> {
                    progressDialog.dismiss();
                    if (user != null) {
                        LoginFragment loginFragment = LoginFragment.newInstance();
                        renderFragment.openFragment(loginFragment, false);

                    }
                });
            }
        });
    }

        public void onCountDownTimerTickEvent ( long millisUntilFinished){
            long leftSeconds = millisUntilFinished / 1000;
            String countBtnText = "Resend after " + leftSeconds + " s";
            countConstraint.setEnabled(false);
            countTV.setText(countBtnText);
            countTV.setTextColor(Color.BLACK);
        }

        public void onCountDownTimerFinishEvent () {
            countTV.setEnabled(true);
            countTV.setText("Resend");
            this.countTV.setOnClickListener(v -> {
                codeSent = "";
                for (int i = 0; i < 6; i++) {
                    int randomDigit = (int) (10 * Math.random());
                    codeSent += randomDigit;
                }
                String emailBody = "Dear " + user.getEmail() + ",\n We got a request to reset your FIT Dictionary " +
                        "password. Please copy and paste the Verification Code below on your FIT App.\n" + codeSent;
                String subject = "FIT Dictionary verification code";
                JavaMailAPI javaMailAPI = new JavaMailAPI(context, user.getEmail(), subject, emailBody);
                javaMailAPI.execute();
                Toast.makeText(context, "Send email successfully", Toast.LENGTH_SHORT).show();
                countDownTimer.start();
            });
        }
    }
