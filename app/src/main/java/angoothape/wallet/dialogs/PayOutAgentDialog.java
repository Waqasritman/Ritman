package angoothape.wallet.dialogs;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.adapters.PayoutAgentAdapter;
import angoothape.wallet.base.BaseDialogFragment;
import angoothape.wallet.databinding.TransferDialogPurposeBinding;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetCashNetworkListResponse;
import angoothape.wallet.interfaces.OnSelectPayoutAgent;

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
