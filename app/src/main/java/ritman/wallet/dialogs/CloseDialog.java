package ritman.wallet.dialogs;

import android.os.Bundle;

import androidx.core.content.ContextCompat;

import ritman.wallet.AEPSActivity;
import ritman.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import ritman.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import ritman.wallet.R;
import ritman.wallet.TransactionHistory.TransactionHistoryActivity;
import ritman.wallet.base.BaseDialogFragment;
import ritman.wallet.beneficairyRegistration.BeneficiaryRegistrationActivity;
import ritman.wallet.billpayment.BillPaymentMainActivity;
import ritman.wallet.billpayment.fragments.MoneyTransferActivity;
import ritman.wallet.databinding.CancelTransactionDialogBinding;
import ritman.wallet.interfaces.OnCancelInterface;
import ritman.wallet.personal_loan.fragment.PLActivity;

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
        if (getBaseActivity() instanceof BeneficiaryRegistrationActivity) {
            binding.confirmNumber.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.posBlue));
            binding.notNumber.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_beneficiary_registeration));
            binding.title.setTextColor(getResources().getColor(R.color.posBlue));
        }
        else if (getBaseActivity() instanceof MoneyTransferMainLayout){
            binding.confirmNumber.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
            binding.notNumber.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_money_transfer));
            binding.title.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

        else if (getBaseActivity() instanceof MobileTopUpMainActivity){
            binding.confirmNumber.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.posPurple));
            binding.notNumber.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_mobile_top_up));
            binding.title.setTextColor(getResources().getColor(R.color.posPurple));

        }

        else if (getBaseActivity() instanceof BillPaymentMainActivity){
            binding.confirmNumber.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.posDarkBlue));
            binding.notNumber.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_bill_payment));
            binding.title.setTextColor(getResources().getColor(R.color.posDarkBlue));

        }

        else if (getBaseActivity() instanceof AEPSActivity){
            binding.confirmNumber.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.poslightred));
            binding.notNumber.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_aeps_orange));
            binding.title.setTextColor(getResources().getColor(R.color.poslightred));

        }

        if (getBaseActivity() instanceof PLActivity) {
            binding.confirmNumber.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.posBlue));
            binding.notNumber.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_beneficiary_registeration));
            binding.title.setTextColor(getResources().getColor(R.color.posBlue));
        }

        binding.notNumber.setOnClickListener(v -> {
            cancelUpload();
        });

        binding.confirmNumber.setOnClickListener(v -> {
            cancelUpload();
            onCancelInterface.onCancel();
        });
    }
}
