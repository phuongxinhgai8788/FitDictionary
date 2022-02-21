package vn.edu.hanu.fitdictionary.verification_code_screen;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import vn.edu.hanu.fitdictionary.MainActivity;
import vn.edu.hanu.fitdictionary.R;
import vn.edu.hanu.fitdictionary.UserHomeFragment;
import vn.edu.hanu.fitdictionary.confirm_email_screen.ConfirmEmailFragment;
import vn.edu.hanu.fitdictionary.data.User;
import vn.edu.hanu.fitdictionary.data.UserViewModel;
import vn.edu.hanu.fitdictionary.helper.CountDownTimer;
import vn.edu.hanu.fitdictionary.helper.JavaMailAPI;
import vn.edu.hanu.fitdictionary.helper.RenderFragment;
import vn.edu.hanu.fitdictionary.introduce_screen.IntroduceFragment;


public class VerificationCodeFragment extends Fragment {

    private static final String USER = "user";
    private static final String CODE = "code";

    private User user;
    private ConstraintLayout backConstraint, okConstraint, countConstraint;
    private TextView introTV, alertCodeTV, countTV, alertNewPassTV, alertConfirmPassTV;
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

    public static VerificationCodeFragment newInstance() {
        VerificationCodeFragment fragment = new VerificationCodeFragment();

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
        View view =  inflater.inflate(R.layout.fragment_verification_code, container, false);
        backConstraint = view.findViewById(R.id.back_constraint_confirm_pass);
        okConstraint = view.findViewById(R.id.ok_bar_confirm_email);
        introTV = view.findViewById(R.id.verification_code_was_sent);
        codeET = view.findViewById(R.id.confirm_code_enter);
        newPasswordET = view.findViewById(R.id.new_password_et);
        confirmPasswordET = view.findViewById(R.id.confirm_password_et);
        newPasswordIV = view.findViewById(R.id.see_new_password);
        confirmPasswordIV = view.findViewById(R.id.see_confirm_new_pass);
        alertCodeTV = view.findViewById(R.id.alert_confirm_code);
        alertNewPassTV = view.findViewById(R.id.alert_new_password);
        alertConfirmPassTV = view.findViewById(R.id.alert_confirm_password);
        countConstraint = view.findViewById(R.id.count_constraint_confirm_code);
        countTV = view.findViewById(R.id.count_tv_confirm_code);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        okConstraint.setEnabled(false);
        introTV.setText("Verification code was sent to "+user.getEmail()+".Resend request after " +
                "60s if you have not received the code yet. Remember to check on Spam!");
        verificationCodeViewModel.isCodeValidate.observe(getViewLifecycleOwner(), data -> {
            if(!data){
                alertCodeTV.setText("Code must have 6 digits");
                alertCodeTV.setTextColor(Color.RED);
            }
        });
        verificationCodeViewModel.isNewPasswordValidate.observe(getViewLifecycleOwner(), data -> {
            if(!data){
                alertNewPassTV.setText("Password must have at least 6 characters");
                alertNewPassTV.setTextColor(Color.RED);
            }
        });
        verificationCodeViewModel.isConfirmPasswordValidate.observe(getViewLifecycleOwner(), data -> {
            if(!data){
                alertNewPassTV.setText("Password must have at least 6 characters");
                alertNewPassTV.setTextColor(Color.RED);
            }
        });
        verificationCodeViewModel.isBtnOkValidate.observe(getViewLifecycleOwner(), data -> {
            okConstraint.setEnabled(data);
        });
        countDownTimer = new CountDownTimer(60*1000, 1000);
        countDownTimer.setHostFragment(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        countDownTimer.start();
        codeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                codeEntered = s.toString();
                verificationCodeViewModel.onCodeChange(s,start, before, count);
                getView().findViewById(R.id.constraint_reset_password).animate().alpha(1f);
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
            if(!codeEntered.equals(codeSent)){
                alertCodeTV.setText("Code does not match");
                alertCodeTV.setTextColor(Color.RED);
            } else if(!newPassEntered.equals(confirmPassEntered)) {
                alertConfirmPassTV.setText("Confirm password does not match");
                alertConfirmPassTV.setTextColor(Color.RED);
            } else {
                user.setPassword(newPassEntered);
                userViewModel.updateUser(user);
                UserHomeFragment userHomeFragment = UserHomeFragment.newInstance(user);
                    RenderFragment mainActivity = (RenderFragment) context;
                    mainActivity.openFragment(userHomeFragment, true);
                }
        });
        getView().findViewById(R.id.cancel_confirm_code).setOnClickListener(v -> {
            ConfirmEmailFragment confirmEmailFragment = ConfirmEmailFragment.newInstance();
            renderFragment.openFragment(confirmEmailFragment, true);
        });
    }

    public void onCountDownTimerTickEvent(long millisUntilFinished) {
        long leftSeconds = millisUntilFinished / 1000;
        String countBtnText = "Resend after "+leftSeconds+" s";
        countTV.setText(countBtnText);
        countTV.setBackground(this.getResources().getDrawable(R.drawable.rounded_corner));
        countTV.setEnabled(false);
    }

    public void onCountDownTimerFinishEvent() {
        countTV.setEnabled(true);
        countTV.setText("Resend");
        countTV.setBackground(this.getResources().getDrawable(R.drawable.background_timer));
        this.countTV.setOnClickListener(v -> {
            codeSent = "";
            for(int i=0; i<6;i++){
                int randomDigit = (int) ( 10*Math.random());
                codeSent+=randomDigit;
            }
            String emailBody = "Dear "+user.getEmail()+",\n We got a request to reset your FIT Dictionary " +
                    "password. Please copy and paste the Verification Code below on your FIT App.\n"+codeSent;
            String subject = "FIT verification code";
            JavaMailAPI javaMailAPI = new JavaMailAPI(context, user.getEmail(), subject, emailBody);
            javaMailAPI.execute();
            Toast.makeText(context, "Send email successfully", Toast.LENGTH_SHORT).show();
            countDownTimer.start();
        });
    }
}