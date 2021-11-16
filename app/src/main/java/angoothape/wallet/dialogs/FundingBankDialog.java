package angoothape.wallet.dialogs;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.UploadCashDetailsActivity;
import angoothape.wallet.adapters.BankFundingDetailsAdapter;
import angoothape.wallet.base.BaseDialogFragment;
import angoothape.wallet.beneficairyRegistration.BeneficiaryRegistrationActivity;
import angoothape.wallet.databinding.TransferDialogPurposeBinding;
import angoothape.wallet.di.JSONdi.restResponse.FundingBankingDetailsResponse;
import angoothape.wallet.personal_loan.fragment.PLActivity;


public class FundingBankDialog extends BaseDialogFragment<TransferDialogPurposeBinding> {


    List<FundingBankingDetailsResponse> detailsResponseList;
    BankFundingDetailsAdapter adapter;
    UploadCashDetailsActivity activity;


    public FundingBankDialog(List<FundingBankingDetailsResponse> detailsResponseList, UploadCashDetailsActivity activity) {
        this.detailsResponseList = detailsResponseList;
        this.activity = activity;
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
        return R.layout.transfer_dialog_purpose;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        setAccountsRecyclerView();
        binding.closeDialog.setOnClickListener(v -> {
            cancelUpload();
        });

        binding.titleOfPage.setText(getString(R.string.select_bank));

        if (getBaseActivity() instanceof BeneficiaryRegistrationActivity) {
            binding.toolBarLayout.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.posBlue));
        } else if (getBaseActivity() instanceof PLActivity) {
            binding.toolBarLayout.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.posBlue));
        } else {
            binding.toolBarLayout.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
        }
    }

    private void setAccountsRecyclerView() {
        adapter = new
                BankFundingDetailsAdapter(detailsResponseList , this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setAdapter(adapter);
    }


    public void onSelectBank(FundingBankingDetailsResponse response) {
        activity.onSelectBank(response);
        cancelUpload();
    }
}
