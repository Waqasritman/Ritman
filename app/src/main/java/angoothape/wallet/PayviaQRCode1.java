package angoothape.wallet;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PayviaQRCode1 extends AppCompatActivity {

    LinearLayout proceed_pay_button;
    TextView view_qr_code,back_press;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payvia_qrcode1);
        proceed_pay_button = findViewById(R.id.proceed_pay_button);
        back_press = findViewById(R.id.back_press);

        proceed_pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PayviaQRCode1.this, PayviaQRCode2.class);
                startActivity(intent);
            }
        });

        back_press.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
