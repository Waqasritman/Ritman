package totipay.wallet.dialogs;

import android.os.Bundle;

import totipay.wallet.R;
import totipay.wallet.base.BaseDialogFragment;
import totipay.wallet.databinding.CancelTransactionDialogBinding;
import totipay.wallet.interfaces.OnCancelInterface;

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
