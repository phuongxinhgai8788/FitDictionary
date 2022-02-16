package vn.edu.hanu.fitdictionary.helper;

import vn.edu.hanu.fitdictionary.verification_code_screen.VerificationCodeFragment;

public class CountDownTimer extends android.os.CountDownTimer {
    private VerificationCodeFragment hostFragment;

    public CountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public void setHostFragment(VerificationCodeFragment hostFragment){
        this.hostFragment = hostFragment;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(hostFragment!=null){
            hostFragment.onCountDownTimerTickEvent(millisUntilFinished);
        }
    }

    @Override
    public void onFinish() {
        if(hostFragment!=null){
            hostFragment.onCountDownTimerFinishEvent();
        }
    }
}
