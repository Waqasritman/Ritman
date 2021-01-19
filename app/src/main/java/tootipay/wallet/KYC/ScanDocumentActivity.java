package tootipay.wallet.KYC;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import tootipay.wallet.KYC.fragments.FinalKYCFragment;
import tootipay.wallet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanDocumentActivity extends AppCompatActivity {

    @BindView(R.id.capture_btn)
    Button captureBtn;
    @BindView(R.id.back_tv)
    TextView backTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_document);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.capture_btn, R.id.back_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.capture_btn:
                startActivity(new Intent(getApplicationContext(), FinalKYCFragment.class));
                break;
            case R.id.back_tv:
                finish();
                break;
        }
    }
}
