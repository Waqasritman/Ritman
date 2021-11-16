package angoothape.wallet.dialogs;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import angoothape.wallet.FundTransferToMerchantActivity;
import angoothape.wallet.R;
import angoothape.wallet.adapters.DistributorListAdapter;
import angoothape.wallet.base.BaseDialogFragment;
import angoothape.wallet.databinding.TransferDialogPurposeBinding;
import angoothape.wallet.di.JSONdi.restResponse.DistributorAgents;

public class DistributorAgentDialog  extends BaseDialogFragment<TransferDialogPurposeBinding> {


    List<DistributorAgents> agentsList;
    FundTransferToMerchantActivity activity;
    DistributorListAdapter adapter;


    public DistributorAgentDialog(List<DistributorAgents> agentsList, FundTransferToMerchantActivity activity) {
        this.agentsList = agentsList;
        this.activity = activity;
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

        binding.titleOfPage.setText("Select Merchant");
        binding.searchView.setVisibility(View.GONE);
        binding.closeDialog.setOnClickListener(v -> {
            cancelUpload();
        });
    }

    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                DistributorListAdapter(agentsList, this);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setHasFixedSize(true);
        binding.transferPurposeList.setAdapter(adapter);
        binding.searchView.setVisibility(View.VISIBLE);
    }

    public void selectAgent(DistributorAgents agents) {
        activity.selectAgent(agents);
        getDialog().dismiss();
    }
}
