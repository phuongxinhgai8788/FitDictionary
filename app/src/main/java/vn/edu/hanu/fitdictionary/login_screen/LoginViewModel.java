package vn.edu.hanu.fitdictionary.login_screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    final String EMAIL_FORMAT = "^[a-zA-Z0-9._-]+@s.hanu.edu.vn";
    private MutableLiveData<Boolean> _isEmailValidate = new MutableLiveData<>();
    private MutableLiveData<Boolean> _isPasswordValidate = new MutableLiveData<>();
    private MutableLiveData<Boolean> _isBtnLoginValidate = new MutableLiveData<>();
    private String emailEntered;
    private String passwordEntered;
    public LiveData<Boolean> isEmailValidate = _isEmailValidate;
    public LiveData<Boolean> isPasswordValidate = _isPasswordValidate;
    public LiveData<Boolean> isBtnLoginValidate = _isBtnLoginValidate;

    public void onEmailChanged(CharSequence s, int start, int before, int count) {
         emailEntered = s.toString();
        _isEmailValidate.postValue(emailEntered.matches(EMAIL_FORMAT));
        validateBtnLogin();
    }

    public void onPasswordChanged(CharSequence s, int start, int before, int count) {
         passwordEntered = s.toString();
        _isPasswordValidate.postValue(passwordEntered.length() >= 6);
        validateBtnLogin();
    }
    public void validateBtnLogin(){
        _isBtnLoginValidate.setValue(_isEmailValidate.getValue() != null && _isEmailValidate.getValue() && _isPasswordValidate.getValue() != null && _isPasswordValidate.getValue());
        }
}
