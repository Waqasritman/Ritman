package ritman.wallet.dialogs;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;


import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ritman.wallet.adapters.BankListAdapter;
import ritman.wallet.R;
import ritman.wallet.base.BaseDialogFragment;
import ritman.wallet.beneficairyRegistration.BeneficiaryRegistrationActivity;
import ritman.wallet.databinding.TransferDialogPurposeBinding;
import ritman.wallet.di.JSONdi.restResponse.BankListResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetBankNetworkListResponse;
import ritman.wallet.interfaces.OnSelectBank;
import ritman.wallet.personal_loan.fragment.DataFragment;
import ritman.wallet.personal_loan.fragment.PLActivity;

import java.util.List;

public class DialogBankNetwork extends BaseDialogFragment<TransferDialogPurposeBinding>
        implements OnSelectBank {

    OnSelectBank onSelectBank;
    List<BankListResponse> responseList;
    BankListAdapter adapter;

    @Override
    public int getTheme() {
        return R.style.AppTheme_NoActionBar_FullScreenDialog;
    }

    @Override
    protected void injectView() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }


    public DialogBankNetwork(List<BankListResponse> responseList, OnSelectBank onSelectBank) {
        this.responseList = responseList;
        this.onSelectBank = onSelectBank;
    }

    @Override
    public int getLayoutId() {
        return R.layout.transfer_dialog_purpose;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        setSearchView();
        setupRecyclerView();

        binding.closeDialog.setOnClickListener(v -> {
            cancelUpload();
        });

        binding.titleOfPage.setText(getString(R.string.select_bank));

        if (getBaseActivity() instanceof BeneficiaryRegistrationActivity) {
            binding.toolBarLayout.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.posBlue));
        }
        else if (getBaseActivity() instanceof PLActivity){
            binding.toolBarLayout.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.posBlue));
        }

        else {
            binding.toolBarLayout.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
        }
    }

    /**
     * method will init the search box
     */
    private void setSearchView() {
        binding.searchView.setMaxWidth(Integer.MAX_VALUE);
        binding.searchView.requestFocus();
        binding.searchView.setFocusable(true);
        binding.searchView.setHint(getString(R.string.search));


        binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.getFilter().filter(s);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                BankListAdapter(responseList, this);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setHasFixedSize(true);
        binding.transferPurposeList.setAdapter(adapter);
        binding.searchView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSelectBank(BankListResponse bankDetails) {
        onSelectBank.onSelectBank(bankDetails);
        cancelUpload();
    }

    @Override
    public void onSelectBankName(String bankName) {

    }

    @Override
    public void onSelectBranch(BankListResponse branchName) {
        onSelectBank.onSelectBranch(branchName);
        cancelUpload();
    }
}
