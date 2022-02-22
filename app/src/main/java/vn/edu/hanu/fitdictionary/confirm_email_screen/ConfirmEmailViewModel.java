package vn.edu.hanu.fitdictionary.confirm_email_screen;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConfirmEmailViewModel extends ViewModel {

    final String EMAIL_FORMAT = "^[a-zA-Z0-9._-]+@s.hanu.edu.vn";

    private MutableLiveData<Boolean> _isEmailValidate = new MutableLiveData<>();

    public LiveData<Boolean> isEmailValidate = _isEmailValidate;

    public void onEmailChanged(CharSequence s, int start, int before, int count){
        String email = s.toString();
        _isEmailValidate.postValue(email.matches(EMAIL_FORMAT));
        _isEmailValidate.setValue(_isEmailValidate.getValue()!=null && _isEmailValidate.getValue());
    }
}
