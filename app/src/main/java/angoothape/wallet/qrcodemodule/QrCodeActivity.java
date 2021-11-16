package angoothape.wallet.qrcodemodule;


import android.os.Bundle;

import angoothape.wallet.R;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.QrCodeDesignBinding;
import angoothape.wallet.scanqrcodemodule.modals.BarcodeGenerate;
import angoothape.wallet.utils.CaptureScreenShot;
import angoothape.wallet.utils.ShareIntent;

public class QrCodeActivity extends RitmanBaseActivity<QrCodeDesignBinding> {


    @Override
    public int getLayoutId() {
        return R.layout.qr_code_design;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        binding.qrCodeImage.setImageBitmap(BarcodeGenerate.createBarCode(sessionManager.getCustomerNo()));

        binding.toolBar.backBtn.setOnClickListener(v -> {
            super.onBackPressed();
        });


        binding.share.setOnClickListener(v -> {
            ShareIntent intent = new ShareIntent(this);
            intent.shareImage(CaptureScreenShot.getScreenShot(binding.shareView));
        });


        binding.toolBar.crossBtn.setOnClickListener(v->{
            onClose();
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
