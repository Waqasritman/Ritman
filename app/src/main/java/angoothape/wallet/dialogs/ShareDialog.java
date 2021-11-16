package angoothape.wallet.dialogs;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import angoothape.wallet.R;
import angoothape.wallet.base.BaseDialogFragment;
import angoothape.wallet.databinding.LayoutDialogShareBinding;
import angoothape.wallet.interfaces.OnReceiptGenerator;

public class ShareDialog extends BaseDialogFragment<LayoutDialogShareBinding> {


    OnReceiptGenerator onReceiptGenerator;

    public ShareDialog(OnReceiptGenerator onReceiptGenerator) {
        this.onReceiptGenerator = onReceiptGenerator;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_dialog_share_;
    }

    @Override
    protected void injectView() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.saveAsPdf.setOnClickListener(v -> {
            cancelUpload();
            onReceiptGenerator.onSaveAsPdf();
        });

        binding.sendAsOthers.setOnClickListener(v -> {
            cancelUpload();
            onReceiptGenerator.onSentAsOthers();
        });


        binding.sendAsWhatspp.setOnClickListener(v -> {
            cancelUpload();
            onReceiptGenerator.onSentAsWhatsApp();
        });


        binding.sendAsImage.setOnClickListener(v -> {
            cancelUpload();
            onReceiptGenerator.onSaveAsImage();
        });
    }


    @Override
    public int getTheme() {
        return R.style.AppTheme_NoActionBar_FullScreenDialog;
    }
}
