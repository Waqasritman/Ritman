package tootipay.wallet.Mobile_Top_Up.fragments;

import android.os.Bundle;

import tootipay.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import tootipay.wallet.R;
import tootipay.wallet.databinding.FragmentUtilityBillerDetailsBinding;
import tootipay.wallet.di.RequestHelper.WRBillDetailRequest;
import tootipay.wallet.di.ResponseHelper.WRBillDetailsResponse;
import tootipay.wallet.di.apicaller.WRBillDetailsTask;
import tootipay.wallet.fragments.BaseFragment;
import tootipay.wallet.interfaces.OnWRBillDetail;
import tootipay.wallet.utils.IsNetworkConnection;

public class MobileTopupBillDetailsFragment extends BaseFragment<FragmentUtilityBillerDetailsBinding>
        implements OnWRBillDetail {


    @Override
    protected void injectView() {

    }


    @Override
    protected void setUp(Bundle savedInstanceState) {
        getBillDetails();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_utility_biller_details;
    }


    void getBillDetails() {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            WRBillDetailRequest request = new WRBillDetailRequest();
            request.field1 = ((MobileTopUpMainActivity) getBaseActivity())
                    .payBillRequest.mobileAccount;
            request.field2 = ((MobileTopUpMainActivity) getBaseActivity())
                    .payBillRequest.mobileAccount2;
            // request.skuID = billerPlan.billerSKUId.toString();
            request.customerNo = getSessionManager().getCustomerNo();
            request.billerID = ((MobileTopUpMainActivity) getBaseActivity()).payBillRequest.billerID;
            request.countryCode = ((MobileTopUpMainActivity) getBaseActivity())
                    .payBillRequest.countryCode;
            request.languageID = getSessionManager().getlanguageselection();
            request.skuID = ((MobileTopUpMainActivity) getBaseActivity()).payBillRequest.skuID;

            WRBillDetailsTask task = new WRBillDetailsTask(getContext(), this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }
    }

    @Override
    public void onBillDetails(WRBillDetailsResponse response) {
        binding.customerName.setText(response.customerName);
        binding.dueBalance.setText(response.balanceORDue);
        binding.dueDate.setText(response.billDueDate);
    }
}