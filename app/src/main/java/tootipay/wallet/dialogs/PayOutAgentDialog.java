package tootipay.wallet.dialogs;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import tootipay.wallet.R;
import tootipay.wallet.adapters.PayoutAgentAdapter;
import tootipay.wallet.adapters.WRBillerTypeAdapter;
import tootipay.wallet.base.BaseDialogFragment;
import tootipay.wallet.databinding.TransferDialogPurposeBinding;
import tootipay.wallet.di.ResponseHelper.GetCashNetworkListResponse;
import tootipay.wallet.interfaces.OnSelectPayoutAgent;

public class PayOutAgentDialog extends BaseDialogFragment<TransferDialogPurposeBinding>
 implements OnSelectPayoutAgent {


    PayoutAgentAdapter agentAdapter;
    List<GetCashNetworkListResponse> networkListResponses;
    OnSelectPayoutAgent onSelectPayoutAgent;


    public PayOutAgentDialog(List<GetCashNetworkListResponse> networkListResponses,
                             OnSelectPayoutAgent onSelectPayoutAgent) {
        this.networkListResponses = networkListResponses;
        this.onSelectPayoutAgent = onSelectPayoutAgent;
    }

    @Override
    public int getTheme() {
        return R.style.AppTheme_NoActionBar_FullScreenDialog;
    }

    @Override
    protected void injectView() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }



    @Override
    public int getLayoutId() {
        return R.layout.transfer_dialog_purpose;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        setupRecyclerView();

        binding.closeDialog.setOnClickListener(v -> cancelUpload());
    }

    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        agentAdapter = new
                PayoutAgentAdapter(networkListResponses, this);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setHasFixedSize(true);
        binding.transferPurposeList.setAdapter(agentAdapter);
    }

    @Override
    public void onSelectPayoutAgent(GetCashNetworkListResponse response) {
        onSelectPayoutAgent.onSelectPayoutAgent(response);
        cancelUpload();
    }
}
