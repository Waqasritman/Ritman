package angoothape.wallet.beneficairyRegistration.fragments;

import android.os.Bundle;

import androidx.navigation.Navigation;

import angoothape.wallet.R;
import angoothape.wallet.databinding.BeneficiaryConfirmationLayoutBinding;
import angoothape.wallet.di.JSONdi.restResponse.VerifyBeneficiary;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;
import angoothape.wallet.fragments.BaseFragment;

public class ConfirmBeneficiary extends BaseFragment<BeneficiaryConfirmationLayoutBinding> {


    GetBeneficiaryListResponse benedetails;
    VerifyBeneficiary.BeneficiaryDetails beneficiaryDetails;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        beneficiaryDetails = getArguments().getParcelable("verified_bene");
        benedetails = getArguments().getParcelable("bene");

        binding.name.setText(beneficiaryDetails.name);
        binding.accountNo.setText(beneficiaryDetails.bankAccountNumber);
        binding.ifscCode.setText(beneficiaryDetails.ifscCode);
        binding.bankName.setText(beneficiaryDetails.bankName);


        binding.confirmBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("customer_no", benedetails.customerNo);
            bundle.putParcelable("bene", benedetails); //bene add
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_confirmBeneficiary_to_bank_tranfer_navigation
                            , bundle);
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.beneficiary_confirmation_layout;
    }
}
