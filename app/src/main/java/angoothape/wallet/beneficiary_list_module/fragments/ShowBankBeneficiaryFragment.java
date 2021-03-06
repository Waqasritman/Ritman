package angoothape.wallet.beneficiary_list_module.fragments;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.View;

import angoothape.wallet.R;
import angoothape.wallet.beneficiary_list_module.BeneficiaryListActivity;
import angoothape.wallet.databinding.FragmentEditBeneficiaryBinding;
import angoothape.wallet.di.XMLdi.RequestHelper.DeleteBeneficiaryRequest;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;
import angoothape.wallet.di.XMLdi.apicaller.DeleteBeneficiaryTask;
import angoothape.wallet.dialogs.AlertDialog;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.interfaces.OnSuccessMessage;
import angoothape.wallet.utils.IsNetworkConnection;

public class ShowBankBeneficiaryFragment extends BaseFragment<FragmentEditBeneficiaryBinding>
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

        binding.nextLayout.setOnClickListener(v -> {

        });

        binding.transferNow.setVisibility(View.GONE);

    }


    public void showBeneficiary() {
        binding.beneName.setText(beneficiaryDetails.firstName
                .concat(" ").concat(beneficiaryDetails.lastName));
        binding.bankname.setText(beneficiaryDetails.bankName);
        binding.branchName.setText(beneficiaryDetails.branchName);
        binding.accountName.setText(beneficiaryDetails.accountNumber);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_edit_beneficiary;
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

    /**
     * ondelete bene confirmed
     */
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
    public void onCancel(boolean goBack)  {

    }
}