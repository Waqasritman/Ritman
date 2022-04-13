package angoothape.wallet.settlementaeps.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;

import angoothape.wallet.MoneyTransferModuleV.TransactionReceiptActivity;
import angoothape.wallet.R;
import angoothape.wallet.databinding.FragmentAepsSettlementBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.aepsSettlement.AEPSSettlementCommission;
import angoothape.wallet.di.JSONdi.restRequest.aepsSettlement.AEPSSettlementTransactionRequest;
import angoothape.wallet.di.JSONdi.restResponse.aepssattlement.AEPSSettlementCommissionResponse;
import angoothape.wallet.di.JSONdi.restResponse.aepssattlement.AEPSSettlementTransaction;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.settlementaeps.AEPSSettlementTransactionActivity;
import angoothape.wallet.utils.Utils;

public class AEPSSettlementSummaryFragment extends BaseFragment<FragmentAepsSettlementBinding> {

    AEPSSettlementCommission commissionRequest;
    AEPSSettlementTransactionRequest settlementTransactionRequest;


    @Override
    protected void injectView() {

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AEPSSettlementTransactionActivity) getBaseActivity()).binding.toolBar.titleTxt
                .setText(getString(R.string.aeps_settlement));
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        settlementTransactionRequest = new AEPSSettlementTransactionRequest();
        commissionRequest = getArguments().getParcelable("request");
        binding.paymentMode.setText(getArguments().getString("paymentModename"));

        settlementTransactionRequest.Beneficiary_ID = getArguments().getString("beneid");
        settlementTransactionRequest.Payment_ID = commissionRequest.Payment_Id;
        settlementTransactionRequest.amount_ = commissionRequest.Transfer_Amount;

        getCommission();


        binding.confirmBtn.setOnClickListener(v -> aepsSettlement());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_aeps_settlement;
    }


    void getCommission() {
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();
        String body = RestClient.makeGSONString(commissionRequest);

        AERequest request = new AERequest();
        request.body = AESHelper.encrypt(body.trim(), gKey.trim());

        ((AEPSSettlementTransactionActivity) getBaseActivity()).viewModel.getCommission(request,
                KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())).trim()).observe(getViewLifecycleOwner()
                , response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {

                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            //    onMessage(response.resource.description);

                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<AEPSSettlementCommissionResponse>() {
                                }.getType();

                                AEPSSettlementCommissionResponse balance = gson.fromJson(bodyy, type);
                                binding.transferAmount.setText("INR " + balance.transferAmount);
                                binding.commission.setText("INR " + balance.commission);
                                binding.totalPayableAmount.setText("INR " + balance.totalPayable);
                                binding.mainView.setVisibility(View.VISIBLE);

                                settlementTransactionRequest.commission_ = balance.commission;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {

                            Utils.hideCustomProgressDialog();
                            if (response.resource.data != null) {
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , gKey);
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


    void aepsSettlement() {
        Utils.showCustomProgressDialog(getContext(), false);


        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();
        String body = RestClient.makeGSONString(settlementTransactionRequest);
        Log.e("aepsSettlement: ", body);
        AERequest request = new AERequest();
        request.body = AESHelper.encrypt(body.trim(), gKey.trim());

        ((AEPSSettlementTransactionActivity) getBaseActivity()).viewModel.settlementTransaction(request,
                KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())).trim()).observe(getViewLifecycleOwner()
                , response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        binding.confirmBtn.setEnabled(true);
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            onMessage(response.resource.description);

                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<AEPSSettlementTransaction>() {
                                }.getType();

                                AEPSSettlementTransaction balance = gson.fromJson(bodyy, type);
                                Handler mHandler;
                                mHandler = new Handler();
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mHandler.removeCallbacks(this);
                                        binding.confirmBtn.setEnabled(true);
                                        Intent i = new Intent(getActivity(), TransactionReceiptActivity.class);
                                        i.putExtra("TransactionNumber", balance.txnNo);
                                        startActivity(i);
                                        getBaseActivity().finish();
                                    }
                                }, 200);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            binding.confirmBtn.setEnabled(true);
                            Utils.hideCustomProgressDialog();
                            if (response.resource.data != null) {
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , gKey);
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
}
