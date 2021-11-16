package angoothape.wallet;


import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityFundTransferToMerchantBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.FundTransferToMerchantRequest;
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequest;
import angoothape.wallet.di.JSONdi.restResponse.DistributorAgents;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.dialogs.DistributorAgentDialog;
import angoothape.wallet.dialogs.SingleButtonMessageDialog;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.utils.Utils;
import angoothape.wallet.viewmodels.FundTransferViewModel;
import okhttp3.internal.Util;

public class FundTransferToMerchantActivity extends RitmanBaseActivity<ActivityFundTransferToMerchantBinding> implements OnDecisionMade {

    FundTransferViewModel viewModel;
    FundTransferToMerchantRequest request;

    public boolean isValidate() {
        if (request.Merchant_Agent_ID.isEmpty()) {
            onMessage("Please select Merchant");
            return false;
        } else if (binding.amountTxt.getText().toString().isEmpty()) {
            onMessage("Please enter amount you wish to send");
            return false;
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fund_transfer_to_merchant;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(FundTransferViewModel.class);
        request = new FundTransferToMerchantRequest();
        binding.selectMerchantTxt.setOnClickListener(v -> {
            getMerchants();
        });

        binding.toolBar.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.tranferBtn.setOnClickListener(v -> {
            if (isValidate()) {
                binding.tranferBtn.setEnabled(false);
                tranferFund();
            }
        });
    }


    public void getMerchants() {
        viewModel.getDistributorMerchants(new SimpleRequest(), sessionManager.getMerchantName().trim()).observe(this
                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            if (response.resource.data.getDistributorAgents().size() > 0) {
                                showDialog(response.resource.data.getDistributorAgents());
                            } else {
                                showSucces("No Agent found", "Sorry", true);
                            }
                        } else {
                            onMessage(response.resource.description);
                        }
                    }
                });
    }


    private void showSucces(String message, String title, boolean isError) {
        SingleButtonMessageDialog dialog = new
                SingleButtonMessageDialog(title
                , message, this,
                false, isError);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    public void tranferFund() {
        Utils.showCustomProgressDialog(this , false);
        request.TransferAmount = binding.amountTxt.getText().toString();
        viewModel.fundTransferToMerchant(request, sessionManager.getMerchantName()).observe(this
                , response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            SingleButtonMessageDialog dialog = new
                                    SingleButtonMessageDialog(getString(R.string.successfully_tranfared)
                                    , getString(R.string.fund_successfully), this,
                                    false);
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            dialog.show(transaction, "");
                        } else {
                            showSucces(response.resource.description , "Error", true);
                        }
                    }
                });
    }


    public void selectAgent(DistributorAgents agents) {
        binding.merhant.setText(agents.agentName);
        request.Distributor_Balance = agents.balance;
        request.Merchant_Agent_ID = agents.subAgentID;
    }


    public void showDialog(List<DistributorAgents> agents) {
        DistributorAgentDialog distributorAgentDialog = new DistributorAgentDialog(agents, this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        distributorAgentDialog.show(transaction, "");
    }

    @Override
    public void onProceed() {
        finish();
    }

    @Override
    public void onCancel(boolean goBack)  {
        finish();
    }
}