package ritman.wallet;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.FrameLayout;
import android.widget.TextView;

public class ScanQRCode3 extends AppCompatActivity {

    FrameLayout content_frame;
    TextView view_qr_code,back_press;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode3);

        content_frame = findViewById(R.id.content_frame);
        view_qr_code = findViewById(R.id.view_qr_code);
        back_press = findViewById(R.id.back_press);

        content_frame.setOnClickListener(view -> {
            Intent intent = new Intent(ScanQRCode3.this, PayviaQRCode1.class);
            startActivity(intent);

        });

        view_qr_code.setOnClickListener(view -> {
            Intent intent = new Intent(ScanQRCode3.this, PayviaQRCode1.class);
            startActivity(intent);
        });

        back_press.setOnClickListener(view -> onBackPressed());
    }
}
