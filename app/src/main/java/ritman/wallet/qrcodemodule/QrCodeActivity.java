package ritman.wallet.qrcodemodule;


import android.os.Bundle;

import ritman.wallet.R;
import ritman.wallet.base.RitmanBaseActivity;
import ritman.wallet.databinding.QrCodeDesignBinding;
import ritman.wallet.scanqrcodemodule.modals.BarcodeGenerate;
import ritman.wallet.utils.CaptureScreenShot;
import ritman.wallet.utils.ShareIntent;

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
