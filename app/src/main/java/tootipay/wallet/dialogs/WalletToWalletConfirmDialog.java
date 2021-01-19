package tootipay.wallet.dialogs;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;

import tootipay.wallet.R;
import tootipay.wallet.base.BaseDialogFragment;
import tootipay.wallet.databinding.WalletToWalletSummaryDialogBinding;
import tootipay.wallet.di.RequestHelper.WalletToWalletTransferRequest;
import tootipay.wallet.interfaces.OnWalletTransferConfirmation;

public class WalletToWalletConfirmDialog extends BaseDialogFragment<WalletToWalletSummaryDialogBinding> {

    OnWalletTransferConfirmation onWalletTransferConfirmation;
    WalletToWalletTransferRequest request;
    String name;

    public WalletToWalletConfirmDialog(WalletToWalletTransferRequest request, String name
            , OnWalletTransferConfirmation onWalletTransferConfirmation) {
        this.request = request;
        this.name = name;
        this.onWalletTransferConfirmation = onWalletTransferConfirmation;
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
        return R.layout.wallet_to_wallet_summary_dialog;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.receiverNumber.setText(request.receiptMobileNo);
        binding.receiverName.setText(name);
        binding.sendingAmountField.setText(request.transferAmount);
        binding.description.setText(request.description);

        binding.sendingCurrency.setText(request.payInCurrency);
        binding.receivingCurrency.setText(request.receiptCurrency);

        if (binding.description.getText().toString().isEmpty()) {
            binding.description.setVisibility(View.GONE);
            binding.descriptionTitle.setVisibility(View.GONE);
        }


        binding.toolbar.backBtn.setOnClickListener(v -> {
            cancelUpload();
        });

        binding.editButton.setOnClickListener(v -> {
            cancelUpload();
        });

        binding.confirmBtn.setOnClickListener(v -> {
            cancelUpload();
            onWalletTransferConfirmation.onConfirmed();
        });
    }
}