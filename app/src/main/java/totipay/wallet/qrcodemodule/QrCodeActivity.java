package totipay.wallet.qrcodemodule;


import android.os.Bundle;

import totipay.wallet.R;
import totipay.wallet.base.TootiBaseActivity;
import totipay.wallet.databinding.QrCodeDesignBinding;
import totipay.wallet.scanqrcodemodule.modals.BarcodeGenerate;
import totipay.wallet.utils.CaptureScreenShot;
import totipay.wallet.utils.ShareIntent;

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
