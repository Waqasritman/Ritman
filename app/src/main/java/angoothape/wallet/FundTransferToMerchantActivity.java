package angoothape.wallet;


import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityFundTransferToMerchantBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.FundTransferToMerchantRequest;
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequest;
import angoothape.wallet.di.JSONdi.restResponse.DistributorAgents;
import angoothape.wallet.di.JSONdi.restResponse.DistributorDetailsResponse;
import angoothape.wallet.di.JSONdi.restResponse.FundTransferToMerchantResponse;
import angoothape.wallet.di.JSONdi.restResponse.FundingBankingDetailsResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
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
                transferFund();
            }
        });
    }


    public void getMerchants() {
        Utils.showCustomProgressDialog(this, false);
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();
        SimpleRequest simpleRequest = new SimpleRequest();
        String body = RestClient.makeGSONString(simpleRequest);
        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body, gKey.trim());


        viewModel.getDistributorMerchants(aeRequest
                , KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())).trim()).observe(this, response -> {
            Utils.hideCustomProgressDialog();
            if (response.status == Status.ERROR) {
                onError(getString(response.messageResourceId));
            } else {
                assert response.resource != null;
                if (response.resource.responseCode.equals(101)) {

                    String bodyy = AESHelper.decrypt(response.resource.data.body
                            , gKey);
                    Log.e("getMerchants: ", bodyy);
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<DistributorDetailsResponse>() {
                        }.getType();
                        DistributorDetailsResponse data = gson.fromJson(bodyy, type);
                        if (data.getDistributorAgents().size() > 0) {
                            showDialog(data.getDistributorAgents());
                        } else {
                            showSucces("No Agent found", "Sorry", true);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Utils.hideCustomProgressDialog();
                    if (response.resource.data != null) {
                        String bodyy = AESHelper.decrypt(response.resource.data.body
                                , gKey);
                        Log.e("getBillDetails: ", bodyy);
                        if (!body.isEmpty()) {
                            onError(bodyy);
                        } else {
                            onError(response.resource.description);
                        }
                    } else {
                        onError(response.resource.description);
                    }
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

    public void transferFund() {
        Utils.showCustomProgressDialog(this, false);
        request.TransferAmount = binding.amountTxt.getText().toString();

        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();

        String body = RestClient.makeGSONString(request);
        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body, gKey.trim());


        viewModel.fundTransferToMerchant(aeRequest
                , KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())).trim()).observe(this, response -> {
            Utils.hideCustomProgressDialog();
            if (response.status == Status.ERROR) {
                onError(getString(response.messageResourceId));
            } else {

                if (response.resource.responseCode.equals(101)) {

                    String bodyy = AESHelper.decrypt(response.resource.data.body
                            , gKey);
                    Log.e("getMerchants: ", bodyy);
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<FundTransferToMerchantResponse>() {
                        }.getType();
                        FundTransferToMerchantResponse data = gson.fromJson(bodyy, type);
                        SingleButtonMessageDialog dialog = new
                                SingleButtonMessageDialog(getString(R.string.successfully_tranfared)
                                , data.status, this,
                                false);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        dialog.show(transaction, "");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Utils.hideCustomProgressDialog();
                    if (response.resource.data != null) {
                        String bodyy = AESHelper.decrypt(response.resource.data.body
                                , gKey);
                        Log.e("getBillDetails: ", bodyy);
                        if (!body.isEmpty()) {
                            onError(bodyy);
                        } else {
                            onError(response.resource.description);
                        }
                    } else {
                        onError(response.resource.description);
                    }
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
    public void onCancel(boolean goBack) {
        finish();
    }
}