package angoothape.wallet.Mobile_Top_Up.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

import angoothape.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import angoothape.wallet.Mobile_Top_Up.viewmodels.MobileTopUpViewModel;
import angoothape.wallet.R;

import angoothape.wallet.databinding.ActivityMobileTopUpFirstBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.PrepaidOperatorRequest;

import angoothape.wallet.di.JSONdi.restResponse.PayBillPaymentResponse;
import angoothape.wallet.di.JSONdi.restResponse.PrepaidOperatorResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.utils.CheckValidation;
import angoothape.wallet.utils.IsNetworkConnection;
import angoothape.wallet.utils.Utils;

public class MobileTopUpFirstActivity extends BaseFragment<ActivityMobileTopUpFirstBinding> {

    MobileTopUpViewModel viewModel;
    String billerid, circle_name, MobileNumber, Field1Name, Field2Name, Field3Name, billerlogo;
    ArrayList<PrepaidOperatorResponse.BillerField> billerFields;

    @Override
    protected void injectView() {

    }

    public boolean isNumberValidate() {
        if (TextUtils.isEmpty(binding.numberLayout.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.plz_enter_mobile_no));
            return false;
        } else if (!CheckValidation.isPhoneNumberValidate(binding.numberLayout.mobilesignupb.getText().toString(),
                binding.numberLayout.countryCodeTextView.getText().toString())) {
            onMessage(getString(R.string.invalid_number));

            return false;
        }
        return true;
    }

    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.numberLayout.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.plz_enter_mobile_no));
            return false;
        } else if (!CheckValidation.isPhoneNumberValidate(binding.numberLayout.mobilesignupb.getText().toString(),
                binding.numberLayout.countryCodeTextView.getText().toString())) {
            onMessage(getString(R.string.invalid_number));
            return false;
        } else if (TextUtils.isEmpty(binding.operator.getText())) {
            onMessage(getString(R.string.plz_select_top_up_type));
            return false;
        } else if (TextUtils.isEmpty(binding.selectCategory.getText())) {
            onMessage(getString(R.string.plz_select_category));
            return false;
        } else if (TextUtils.isEmpty(binding.selectOperator.getText())) {
            onMessage(getString(R.string.plz_select_operator));
            return false;
        }
        return true;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        billerFields = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(MobileTopUpViewModel.class);
        binding.nextLayout.setOnClickListener(v -> {
            if (isNumberValidate()) {
                MobileNumber = binding.numberLayout.mobilesignupb.getText().toString();
                getPrepaidOperator();
            }
        });
    }

    void getPrepaidOperator() {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            Utils.showCustomProgressDialog(getContext(), false);
            String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                    .getKey(getSessionManager().getMerchantName())).trim();
            PrepaidOperatorRequest request = new PrepaidOperatorRequest();
            request.MobileNo = binding.numberLayout.mobilesignupb.getText().toString();
            request.CountryCode = "IN";
            String body = RestClient.makeGSONString(request);

            AERequest aeRequest = new AERequest();
            aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());
            viewModel.getOperator(aeRequest, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                    KeyHelper.getSKey(KeyHelper
                            .getKey(getSessionManager().getMerchantName())))
                    .observe(getViewLifecycleOwner(), response -> {
                        Utils.hideCustomProgressDialog();
                        if (response.status == Status.ERROR) {
                            onError(getString(response.messageResourceId));
                        } else {
                            assert response.resource != null;
                            if (response.resource.responseCode.equals(101)) {
                                Utils.hideCustomProgressDialog();
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , gKey);
                                Log.e("getPrepaidOperator: ", bodyy);
                                try {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<PrepaidOperatorResponse>() {
                                    }.getType();
                                    PrepaidOperatorResponse data = gson.fromJson(bodyy, type);

                                    billerid = data.getBillerid();
                                    circle_name = data.getCircleName();
                                    billerlogo = data.getBillerLogo();
                                    billerFields.addAll(data.getBillerFields());
                                    for (int i = 0; i < billerFields.size(); i++) {
                                        if (billerFields.size() == 1) {
                                            Field1Name = billerFields.get(0).getParameterName();
                                        }
                                        if (billerFields.size() == 2) {
                                            Field1Name = billerFields.get(0).getParameterName();
                                            Field2Name = billerFields.get(1).getParameterName();
                                        }
                                        if (billerFields.size() == 3) {
                                            Field1Name = billerFields.get(0).getParameterName();
                                            Field2Name = billerFields.get(1).getParameterName();
                                            Field3Name = billerFields.get(2).getParameterName();
                                        }
                                    }
                                    Bundle bundle = new Bundle();
                                    bundle.putString("billerid", billerid);
                                    bundle.putString("circle_name", circle_name);
                                    bundle.putString("MobileNumber", MobileNumber);
                                    bundle.putString("Field1Name", Field1Name);
                                    bundle.putString("Field2Name", Field2Name);
                                    bundle.putString("Field3Name", Field3Name);
                                    bundle.putString("billerlogo", billerlogo);

                                    Navigation.findNavController(binding.getRoot()).navigate(R.id
                                            .action_mobileTopUpFirstActivity_to_planNameFragment, bundle);
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


        } else {
            onMessage(getString(R.string.no_internet));
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mobile_top_up_first;
    }

}
