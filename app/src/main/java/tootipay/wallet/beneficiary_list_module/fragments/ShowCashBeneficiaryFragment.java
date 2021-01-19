package tootipay.wallet.beneficiary_list_module.fragments;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.View;

import tootipay.wallet.R;
import tootipay.wallet.beneficiary_list_module.BeneficiaryListActivity;
import tootipay.wallet.databinding.FragmentShowCashBeneficairyBinding;
import tootipay.wallet.di.RequestHelper.DeleteBeneficiaryRequest;
import tootipay.wallet.di.ResponseHelper.GetBeneficiaryListResponse;
import tootipay.wallet.di.apicaller.DeleteBeneficiaryTask;
import tootipay.wallet.dialogs.AlertDialog;
import tootipay.wallet.fragments.BaseFragment;
import tootipay.wallet.interfaces.OnDecisionMade;
import tootipay.wallet.interfaces.OnSuccessMessage;
import tootipay.wallet.utils.IsNetworkConnection;

public class ShowCashBeneficiaryFragment extends BaseFragment<FragmentShowCashBeneficairyBinding>
        implements OnSuccessMessage, OnDecisionMade {


    GetBeneficiaryListResponse beneficiaryDetails;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        if (getArguments() != null) {
            beneficiaryDetails = getArguments().getParcelable("bene_details");
            showBeneficiary();
        }

        binding.deleteBeneficiary.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {

                AlertDialog alertDialog = new AlertDialog(getString(R.string.delete_beneficary)
                        , getString(R.string.sure_to_delete_bene), this);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                alertDialog.show(transaction, "");


            } else {
                onMessage(getString(R.string.no_internet));
            }
        });
        binding.transferNow.setVisibility(View.GONE);


        binding.nextLayout.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("bene_details", beneficiaryDetails);
            Navigation.findNavController(getView())
                    .navigate(R.id.action_showCashBeneficiaryFragment_to_updateCashBeneficiaryFragment
                            , bundle);
        });
    }


    public void showBeneficiary() {
        binding.beneName.setText(beneficiaryDetails.firstName
                .concat(" ").concat(beneficiaryDetails.lastName));
        binding.mobileNumber.setText(beneficiaryDetails.telephone);
        binding.payoutCurrency.setText(beneficiaryDetails.payOutCurrency);
        binding.payoutBranch.setText(beneficiaryDetails.payoutBranchName);
        binding.payoutCountry.setText(beneficiaryDetails.payOutCountryCode);
        binding.address.setText(beneficiaryDetails.address);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_show_cash_beneficairy;
    }

    @Override
    public void onProceed() {
        DeleteBeneficiaryRequest request = new DeleteBeneficiaryRequest();
        request.customerNo = ((BeneficiaryListActivity) getBaseActivity()).sessionManager.getCustomerNo();
        request.beneficiaryNo = beneficiaryDetails.beneficiaryNo;
        request.languageId = getSessionManager().getlanguageselection();

        DeleteBeneficiaryTask task = new DeleteBeneficiaryTask(getContext(), this);
        task.execute(request);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onSuccess(String s) {
        onMessage(getString(R.string.successfully_deleted_bene));
        Navigation.findNavController(getView())
                .navigateUp();
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }
}