package tootipay.wallet.dialogs;

import android.os.Bundle;

import tootipay.wallet.R;
import tootipay.wallet.base.BaseDialogFragment;
import tootipay.wallet.databinding.CancelTransactionDialogBinding;
import tootipay.wallet.interfaces.OnCancelInterface;

public class CloseDialog extends BaseDialogFragment<CancelTransactionDialogBinding> {

    OnCancelInterface onCancelInterface;

    public CloseDialog(OnCancelInterface onCancelInterface) {
        this.onCancelInterface = onCancelInterface;
    }

    @Override
    public int getLayoutId() {
        return R.layout.cancel_transaction_dialog;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        binding.notNumber.setOnClickListener(v -> {
            cancelUpload();
        });

        binding.confirmNumber.setOnClickListener(v -> {
            cancelUpload();
            onCancelInterface.onCancel();
        });
    }
}
