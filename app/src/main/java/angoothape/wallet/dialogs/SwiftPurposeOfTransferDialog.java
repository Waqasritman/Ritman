package angoothape.wallet.dialogs;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import angoothape.wallet.R;
import angoothape.wallet.base.BaseDialogFragment;
import angoothape.wallet.databinding.CustomPurposeOfTransferBinding;
import angoothape.wallet.interfaces.OnSwiftPurposeTransferSelection;

public class SwiftPurposeOfTransferDialog extends BaseDialogFragment<CustomPurposeOfTransferBinding> {

    OnSwiftPurposeTransferSelection onSwiftPurposeTransferSelection;

    public SwiftPurposeOfTransferDialog(OnSwiftPurposeTransferSelection onSwiftPurposeTransferSelection) {
        this.onSwiftPurposeTransferSelection = onSwiftPurposeTransferSelection;
    }

    @Override
    public int getTheme() {
        return R.style.AppTheme_NoActionBar_FullScreenDialog;
    }

    @Override
    protected void injectView() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.custom_purpose_of_transfer;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.titleOfPage.setText(getString(R.string.select_the_purpose_txt));


        binding.trade.setOnClickListener(v -> {
            onSwiftPurposeTransferSelection.onSwiftPurpose(
                    binding.trade.getText().toString(), 0
            );
            cancelUpload();
        });

        binding.otherTitle.setOnClickListener(v -> {
            onSwiftPurposeTransferSelection.onSwiftPurpose(
                    binding.otherTitle.getText().toString(), 1
            );
            cancelUpload();
        });

    }
}
