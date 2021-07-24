package ritman.wallet.KYC;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ritman.wallet.BaseActivity;
import ritman.wallet.KYC.fragments.FinalKYCFragment;
import ritman.wallet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DocumentActivity extends BaseActivity {

    @BindView(R.id.capture_layout)
    LinearLayout captureLayout;
    @BindView(R.id.confirm_btn)
    Button confirmBtn;
    @BindView(R.id.capture_img_tv)
    TextView captureImgTv;
    @BindView(R.id.capture_gallery_tv)
    TextView captureGalleryTv;
    @BindView(R.id.capture_image_layout)
    LinearLayout captureImageLayout;

    @Override
    protected int getContentResId() {
        return R.layout.activity_document;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
    }

    @OnClick({R.id.capture_layout, R.id.confirm_btn, R.id.capture_img_tv, R.id.capture_gallery_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.capture_layout:
                startActivity(new Intent(getApplicationContext(), ScanDocumentActivity.class));

                break;
            case R.id.confirm_btn:
                startActivity(new Intent(getApplicationContext(), FinalKYCFragment.class));
                break;
            case R.id.capture_img_tv:
                captureLayout.setVisibility(View.GONE);
                captureImageLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.capture_gallery_tv:
                captureLayout.setVisibility(View.GONE);
                captureImageLayout.setVisibility(View.VISIBLE);
                break;
        }
    }
}
