package vn.edu.hanu.fitdictionary.register_screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {
    final String EMAIL_FORMAT = "^[a-zA-Z0-9._-]+@s.hanu.edu.vn";
    private MutableLiveData<Boolean> _isEmailValidate = new MutableLiveData<>();
    private MutableLiveData<Boolean> _isPasswordValidate = new MutableLiveData<>();
    private MutableLiveData<Boolean> _isFullNameValidate = new MutableLiveData<>();
    private MutableLiveData<Boolean> _isBtnSignUpValidate = new MutableLiveData<>();


    public LiveData<Boolean> isEmailValidate = _isEmailValidate;
    public LiveData<Boolean> isPasswordValidate = _isPasswordValidate;
    public LiveData<Boolean> isBtnSignUpValidate = _isBtnSignUpValidate;
    public LiveData<Boolean> isFullNameValidate = _isFullNameValidate;

    public void onEmailChanged(CharSequence s, int start, int before, int count) {
        String email = s.toString();
        _isEmailValidate.postValue(email.matches(EMAIL_FORMAT));
        _isBtnSignUpValidate.setValue(_isEmailValidate.getValue() != null && _isEmailValidate.getValue() && _isPasswordValidate.getValue() != null && _isPasswordValidate.getValue() && _isFullNameValidate.getValue() && _isFullNameValidate.getValue()!=null);

    }

    public void onPasswordChanged(CharSequence s, int start, int before, int count) {
        String password = s.toString();
        _isPasswordValidate.postValue(password.length() >= 6);
        _isBtnSignUpValidate.setValue(_isEmailValidate.getValue() != null && _isEmailValidate.getValue() && _isPasswordValidate.getValue() != null && _isPasswordValidate.getValue()&& _isFullNameValidate.getValue() && _isFullNameValidate.getValue()!=null);
    }

    public void onFullNameChange(CharSequence s, int start, int before, int count) {
        String fullName = s.toString();
        _isFullNameValidate.postValue(fullName.length()>0);
        _isBtnSignUpValidate.setValue(_isEmailValidate.getValue() != null && _isEmailValidate.getValue() && _isPasswordValidate.getValue() != null && _isPasswordValidate.getValue() && _isFullNameValidate.getValue() && _isFullNameValidate.getValue()!=null);

    }
}
