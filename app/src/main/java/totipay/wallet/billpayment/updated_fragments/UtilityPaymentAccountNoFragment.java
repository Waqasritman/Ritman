package totipay.wallet.billpayment.updated_fragments;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import totipay.wallet.R;
import totipay.wallet.billpayment.BillPaymentMainActivity;
import totipay.wallet.billpayment.helpers.BillerInputTypes;
import totipay.wallet.databinding.FragmentUtilityPaymentAccountBinding;
import totipay.wallet.di.RequestHelper.GetWRBillerFieldsRequest;
import totipay.wallet.di.RequestHelper.WRBillDetailRequest;
import totipay.wallet.di.ResponseHelper.WRBillDetailsResponse;
import totipay.wallet.di.ResponseHelper.WRBillerFieldsResponse;
import totipay.wallet.di.apicaller.GetWRBillerFieldsTask;
import totipay.wallet.di.apicaller.WRBillDetailsTask;
import totipay.wallet.fragments.BaseFragment;
import totipay.wallet.interfaces.OnWRBillDetail;
import totipay.wallet.interfaces.OnWRBillerFields;
import totipay.wallet.utils.BillerTypeParser;
import totipay.wallet.utils.IsNetworkConnection;

public class UtilityPaymentAccountNoFragment extends BaseFragment<FragmentUtilityPaymentAccountBinding>
        implements OnWRBillerFields, OnWRBillDetail {


    List<WRBillerFieldsResponse> wrBillerList;

    @Override
    protected void injectView() {

    }

    public boolean isValidateOne() {
        if (TextUtils.isEmpty(binding.accountNo.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(wrBillerList.get(0).labelName));
            return false;
        } else if (binding.accountNo.getText().length() < wrBillerList.get(0).maxLength
                ||
                binding.accountNo.getText().length() > wrBillerList.get(0).minLength) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(wrBillerList.get(0).labelName));

            return false;
        }
        return true;
    }

    public boolean isValidateTwe() {
        if (TextUtils.isEmpty(binding.accountNo.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(wrBillerList.get(0).labelName));
            return false;
        } else if (binding.accountNo.getText().length() < wrBillerList.get(0).minLength
                ||
                binding.accountNo.getText().length() > wrBillerList.get(0).maxLength) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(0).labelName));

            return false;
        } else if (TextUtils.isEmpty(binding.accountPolicyNo.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(wrBillerList.get(1).labelName));
            return false;
        } else if (binding.accountPolicyNo.getText().length() < wrBillerList.get(1).minLength
                ||
                binding.accountPolicyNo.getText().length() > wrBillerList.get(1).maxLength) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(wrBillerList.get(1).labelName));
            return false;
        }
        return true;
    }

    public boolean isValidateThree() {
        if (TextUtils.isEmpty(binding.accountNo.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(wrBillerList.get(0).labelName));
            return false;
        } else if (binding.accountNo.getText().length() < wrBillerList.get(0).minLength
                ||
                binding.accountNo.getText().length() > wrBillerList.get(0).maxLength) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(wrBillerList.get(0).labelName));

            return false;
        } else if (TextUtils.isEmpty(binding.accountPolicyNo.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(wrBillerList.get(1).labelName));
            return false;
        } else if (binding.accountPolicyNo.getText().length() < wrBillerList.get(1).minLength
                ||
                binding.accountPolicyNo.getText().length() > wrBillerList.get(1).maxLength) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(wrBillerList.get(1).labelName));

            return false;
        } else if (TextUtils.isEmpty(binding.reEnterAccountPolicy.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(wrBillerList.get(2).labelName));
            return false;
        } else if (binding.reEnterAccountPolicy.getText().length() < wrBillerList.get(2).minLength
                ||
                binding.reEnterAccountPolicy.getText().length() > wrBillerList.get(2).maxLength) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(wrBillerList.get(2).labelName));
            return false;
        }
        return true;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            GetWRBillerFieldsRequest request = new GetWRBillerFieldsRequest();
            request.languageId = getSessionManager().getlanguageselection();
            request.billerID = ((BillPaymentMainActivity) getBaseActivity())
                    .payBillRequest.billerID;
            request.countryCode = ((BillPaymentMainActivity) getBaseActivity()).payBillRequest.countryCode;
            request.skuID = ((BillPaymentMainActivity) getBaseActivity()).payBillRequest.skuID;

            GetWRBillerFieldsTask task = new GetWRBillerFieldsTask(getContext(), this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }


        binding.nextLayout.setOnClickListener(v -> {
            if (wrBillerList.size() == 1) {
                if (isValidateOne()) {
                    fillData();
                }
            } else if (wrBillerList.size() == 2) {
                if (isValidateTwe()) {
                    fillData();
                }
            } else if (wrBillerList.size() == 3) {
                if (isValidateThree()) {
                    fillData();
                }
            }
        });
    }

    public void fillData() {
        ((BillPaymentMainActivity) getBaseActivity())
                .payBillRequest.mobileAccount = binding.accountNo.getText().toString();
        ((BillPaymentMainActivity) getBaseActivity())
                .payBillRequest.mobileAccount2 = binding.accountPolicyNo.getText().toString();
        ((BillPaymentMainActivity) getBaseActivity())
                .payBillRequest.mobileAccount3 = binding.reEnterAccountPolicy.getText().toString();
        getBillDetails();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_utility_payment_account;
    }

    @Override
    public void onWRBillerField(List<WRBillerFieldsResponse> response) {
        wrBillerList = new ArrayList<>();
        wrBillerList.addAll(response);
        if (response.size() == 1) {
            onSizeOne(response);
        } else if (response.size() == 2) {
            onSizeTwo(response);
        } else if (response.size() == 3) {
            onSizeThree(response);
        }

    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onBillDetails(WRBillDetailsResponse response) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("bill_details", response);
        Navigation.findNavController(getView())
                .navigate(R.id.action_utilityPaymentAccountNoFragment_to_utilityBillerDetailsFragment
                        , bundle);
    }

    void getBillDetails() {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            WRBillDetailRequest request = new WRBillDetailRequest();
            request.field1 = ((BillPaymentMainActivity) getBaseActivity())
                    .payBillRequest.mobileAccount;
            request.field2 = ((BillPaymentMainActivity) getBaseActivity())
                    .payBillRequest.mobileAccount2;
            // request.skuID = billerPlan.billerSKUId.toString();
            request.customerNo = getSessionManager().getCustomerNo();
            request.billerID = ((BillPaymentMainActivity) getBaseActivity()).payBillRequest.billerID;
            request.countryCode = ((BillPaymentMainActivity) getBaseActivity())
                    .payBillRequest.countryCode;
            request.languageID = getSessionManager().getlanguageselection();
            request.skuID = ((BillPaymentMainActivity) getBaseActivity()).payBillRequest.skuID;

            WRBillDetailsTask task = new WRBillDetailsTask(getContext(), this);
            ;
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }
    }

    void onSizeOne(List<WRBillerFieldsResponse> response) {

        binding.accountNoField.setHint(response.get(0).labelName);
        binding.accountNo.setHint(response.get(0).description);
        if (response.get(0).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        binding.accountPolicyNoField.setVisibility(View.GONE);
        binding.accountPolicyNo.setVisibility(View.GONE);

        binding.descriptionField.setVisibility(View.GONE);
        binding.reEnterAccountPolicy.setVisibility(View.GONE);

    }

    void onSizeTwo(List<WRBillerFieldsResponse> response) {
        binding.accountNoField.setHint(response.get(0).labelName);
        binding.accountNo.setHint(response.get(0).description);
        if (response.get(0).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        binding.accountPolicyNoField.setHint(response.get(1).labelName);
        binding.accountPolicyNo.setHint(response.get(1).description);
        if (response.get(1).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountPolicyNo.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        binding.descriptionField.setVisibility(View.GONE);
        binding.reEnterAccountPolicy.setVisibility(View.GONE);
    }

    void onSizeThree(List<WRBillerFieldsResponse> response) {
        binding.accountNoField.setHint(response.get(0).labelName);
        binding.accountNo.setHint(response.get(0).description);
        if (response.get(0).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        binding.accountPolicyNoField.setHint(response.get(1).labelName);
        binding.accountPolicyNo.setHint(response.get(1).description);
        if (response.get(1).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountPolicyNo.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        binding.descriptionField.setHint(response.get(2).labelName);
        binding.reEnterAccountPolicy.setHint(response.get(2).description);
        if (response.get(3).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.reEnterAccountPolicy.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }
}
