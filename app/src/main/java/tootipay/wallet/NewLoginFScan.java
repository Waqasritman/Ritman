package tootipay.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import tootipay.wallet.home_module.NewDashboardActivity;
import tootipay.wallet.login_signup_module.fragments_sign_up.NewLoginOption;

public class NewLoginFScan extends Activity {

    ImageView scanfinger;
    TextView login_another_way;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scan_finger_login);

            scanfinger=findViewById(R.id.scanfinger);
        login_another_way=findViewById(R.id.login_another_way);

        scanfinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), NewDashboardActivity.class);
                startActivity(intent);

            }
        });

        login_another_way.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), NewLoginOption.class);
                startActivity(intent);

            }
        });





    }
}
