package angoothape.wallet.refund_module;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;

import angoothape.wallet.R;
import angoothape.wallet.databinding.RefundTransactionNumberLayoutBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.GenerateOTPRefund;
import angoothape.wallet.di.JSONdi.restResponse.RefundOTPResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.utils.Utils;

public class RefundTransactionNumberFragment extends BaseFragment<RefundTransactionNumberLayoutBinding> {

    RefundViewModel viewModel;

    @Override
    protected void injectView() {

    }


    @Override
    public boolean isValidate() {
        if (binding.transactionNo.getText().toString().isEmpty()) {
            onMessage(getString(R.string.enter_transaction_no_txt));
            return false;
        } else if (binding.transactionNo.getText().toString().length() < 13 || binding.transactionNo.getText().toString().length() > 16) {
            onMessage("Transaction no should be between 13 and 16 digits");
            return false;
        }
        return true;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(RefundViewModel.class);

        binding.btnNext.setOnClickListener(v -> {
            if (isValidate()) {
                Utils.showCustomProgressDialog(getContext(), false);
                String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())).trim();

                GenerateOTPRefund otpRequest = new GenerateOTPRefund();
                otpRequest.Txn_no = binding.transactionNo.getText().toString();
                String body = RestClient.makeGSONString(otpRequest);

                AERequest request = new AERequest();
                request.body = AESHelper.encrypt(body.trim(), gKey.trim());


                viewModel.otpRefund(request
                        , KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                                .getKey(getSessionManager().getMerchantName())).trim())
                        .observe(getViewLifecycleOwner(), response -> {
                            Utils.hideCustomProgressDialog();
                            if (response.status == Status.ERROR) {
                                onError(getString(response.messageResourceId));
                            } else if (response.status == Status.SUCCESS) {
                                assert response.resource != null;
                                if (response.resource.responseCode.equals(101)) {

                                    try {

                                        Intent intent = new Intent(getBaseActivity()
                                                , RefundOTPFragment.class);
                                        intent.putExtra("txn_no", binding.transactionNo.getText().toString());
                                        getBaseActivity().startActivity(intent);
                                        getBaseActivity().finish();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Utils.hideCustomProgressDialog();
                                    if (response.resource.data != null) {
                                        String bodyy = AESHelper.decrypt(response.resource.data.body
                                                , gKey);
                                        String error;
                                        if (!body.isEmpty()) {
                                            error = bodyy.replace("Txn_no", "Transaction number");
                                            error = error.replace("The field", "");
                                        } else {
                                            error = response.resource.description.replace("Txn_no", "Transaction number");
                                            error = error.replace("The field", "The");
                                        }
                                        onError(error.replace(" a string or array type", ""));
                                    } else {
                                        String error = response.resource.description.replace("Txn_no", "Transaction number");
                                        error = error.replace("The field", "The");

                                        onError(error.replace(" a string or array type", ""));
                                    }
                                }

                            }

                        });
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.refund_transaction_number_layout;
    }
}
