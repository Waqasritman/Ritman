package totipay.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import totipay.wallet.login_signup_module.MainActivityLoginSignUp;

public class NewSplash extends Activity {


    //    code for language screen

    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_login);
        startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.removeCallbacks(this);
                startActivity(new Intent(getApplicationContext(), MainActivityLoginSignUp.class));
               /* startActivity(new Intent(getApplicationContext(),NewDashboardActivity.class));*/
                finish();
            }
        }, 2000);


    }






}
