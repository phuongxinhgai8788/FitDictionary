package vn.edu.hanu.fitdictionary.login_screen;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    final String EMAIL_FORMAT = "^[a-zA-Z0-9._-]+@s.hanu.edu.vn";
    private MutableLiveData<Boolean> _isEmailValidate = new MutableLiveData<>();

    private MutableLiveData<Boolean> _isPasswordValidate = new MutableLiveData<>();
    private MutableLiveData<Boolean> _isBtnLoginValidate = new MutableLiveData<>();


    public LiveData<Boolean> isEmailValidate = _isEmailValidate;
    public LiveData<Boolean> isPasswordValidate = _isPasswordValidate;
    public LiveData<Boolean> isBtnLoginValidate = _isBtnLoginValidate;

    public void onEmailChanged(CharSequence s, int start, int before, int count) {
        String email = s.toString();
        _isEmailValidate.postValue(email.matches(EMAIL_FORMAT));
        _isBtnLoginValidate.setValue(_isEmailValidate.getValue() != null && _isEmailValidate.getValue() && _isPasswordValidate.getValue() != null && _isPasswordValidate.getValue());

    }

    public void onPasswordChanged(CharSequence s, int start, int before, int count) {
        String password = s.toString();
        _isPasswordValidate.postValue(password.length() >= 6);
        _isBtnLoginValidate.setValue(_isEmailValidate.getValue() != null && _isEmailValidate.getValue() && _isPasswordValidate.getValue() != null && _isPasswordValidate.getValue());
    }

}
