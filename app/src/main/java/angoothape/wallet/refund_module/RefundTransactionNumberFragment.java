package angoothape.wallet.refund_module;

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
                Log.e("body", body);
                AERequest request = new AERequest();
                request.body = AESHelper.encrypt(body.trim(), gKey.trim());


                viewModel.otpRefund(request
                        , KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                                .getKey(getSessionManager().getMerchantName())).trim())
                        .observe(getViewLifecycleOwner(), response -> {
                            Utils.hideCustomProgressDialog();
                            if (response.status == Status.ERROR) {
                                onMessage(getString(response.messageResourceId));
                            } else if (response.status == Status.SUCCESS) {
                                if (response.resource.responseCode.equals(101)) {
                                    String bodyy = AESHelper.decrypt(response.resource.data.body
                                            , gKey);
                                    try {
                                        Gson gson = new Gson();
                                        Type type = new TypeToken<RefundOTPResponse>() {
                                        }.getType();
                                        RefundOTPResponse data = gson.fromJson(bodyy, type);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    onMessage(response.resource.description);
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
