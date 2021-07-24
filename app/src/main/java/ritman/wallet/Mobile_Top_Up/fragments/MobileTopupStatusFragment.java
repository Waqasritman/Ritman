package ritman.wallet.Mobile_Top_Up.fragments;

import android.os.Bundle;

import android.view.View;

import ritman.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import ritman.wallet.R;
import ritman.wallet.databinding.FragmentMobileTopupStatusBinding;
import ritman.wallet.di.XMLdi.RequestHelper.PrepaidStatusRequest;
import ritman.wallet.di.XMLdi.apicaller.PrepaidStatusTask;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.interfaces.OnSuccessMessage;
import ritman.wallet.utils.IsNetworkConnection;

public class MobileTopupStatusFragment extends BaseFragment<FragmentMobileTopupStatusBinding>
        implements OnSuccessMessage {
    String requestID = "";


    @Override
    public void onResume() {
        super.onResume();
        ((MobileTopUpMainActivity)getBaseActivity()).binding.toolBar.toolBar.setVisibility(View.GONE);
    }

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        if (getArguments() != null) {
            requestID = getArguments().getString("request_id");
            binding.operatorName.setText(getArguments().getString("operator_name"));
            binding.transferAmount.setText(getArguments().getString("transfer_amount"));
        }


        if (!requestID.isEmpty()) {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                PrepaidStatusRequest request = new PrepaidStatusRequest();
                request.languageId = getSessionManager().getlanguageselection();
                request.requestId = requestID;


                PrepaidStatusTask task = new PrepaidStatusTask(getContext(), this);
                task.execute(request);
            } else {
                onMessage(getString(R.string.no_internet));
                getBaseActivity().finish();
            }
        }

        binding.backBtn.setOnClickListener(v -> {
            getBaseActivity().finish();
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mobile_topup_status;
    }


    @Override
    public void onSuccess(String s) {
        binding.status.setText(s);
    }


    @Override
    public void onResponseMessage(String message) {
        onMessage(message);

    }
}