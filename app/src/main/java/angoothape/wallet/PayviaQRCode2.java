package angoothape.wallet;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import angoothape.wallet.home_module.NewDashboardActivity;

public class PayviaQRCode2 extends AppCompatActivity {
    TextView view_qr_code,back_press,backToHome;
    LinearLayout linear_layout_reciept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payvia_qrcode2);

        back_press = findViewById(R.id.back_press);
        backToHome = findViewById(R.id.backToHome);
        linear_layout_reciept = findViewById(R.id.linear_layout_reciept);
        back_press.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        linear_layout_reciept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PayviaQRCode2.this, NewDashboardActivity.class);
                startActivity(intent);
            }
        });
    }
}
