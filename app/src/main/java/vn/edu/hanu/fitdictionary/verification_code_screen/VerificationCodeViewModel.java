package vn.edu.hanu.fitdictionary.verification_code_screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VerificationCodeViewModel extends ViewModel {
    private MutableLiveData<Boolean> _isCodeValidate = new MutableLiveData<>();
    private MutableLiveData<Boolean> _isNewPasswordValidate = new MutableLiveData<>();
    private MutableLiveData<Boolean> _isConfirmPasswordValidate = new MutableLiveData<>();
    private MutableLiveData<Boolean> _isBtnOkValidate = new MutableLiveData<>();

    public LiveData<Boolean> isCodeValidate = _isCodeValidate;
    public LiveData<Boolean> isBtnOkValidate = _isBtnOkValidate;
    public LiveData<Boolean> isNewPasswordValidate = _isNewPasswordValidate;
    public LiveData<Boolean> isConfirmPasswordValidate = _isConfirmPasswordValidate;


    public void onCodeChange(CharSequence s, int start, int before, int count){
        String code = s.toString();
        _isCodeValidate.postValue(code.length()==6 && code.matches("[0-9]+"));
        validateBtnOk();
    }
    public void onNewPasswordChange(CharSequence s, int start, int before, int count){
        String newPassword = s.toString();
        _isNewPasswordValidate.postValue(newPassword.length() >= 6);
        validateBtnOk();
    }
    public void onConfirmNewPasswordChange(CharSequence s, int start, int before, int count){
        String password = s.toString();
        _isConfirmPasswordValidate.postValue(password.length()>=6);
        validateBtnOk();
    }
    public void validateBtnOk(){
        _isBtnOkValidate.setValue(_isCodeValidate.getValue() !=null && _isCodeValidate.getValue() && _isNewPasswordValidate.getValue() !=null && _isNewPasswordValidate.getValue() && _isConfirmPasswordValidate.getValue()!=null && _isConfirmPasswordValidate.getValue());

    }
}
