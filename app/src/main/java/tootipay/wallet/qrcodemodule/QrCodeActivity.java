package tootipay.wallet.qrcodemodule;


import android.os.Bundle;

import tootipay.wallet.R;
import tootipay.wallet.base.TootiBaseActivity;
import tootipay.wallet.databinding.QrCodeDesignBinding;
import tootipay.wallet.scanqrcodemodule.modals.BarcodeGenerate;
import tootipay.wallet.utils.CaptureScreenShot;
import tootipay.wallet.utils.ShareIntent;

public class QrCodeActivity extends TootiBaseActivity<QrCodeDesignBinding> {


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
