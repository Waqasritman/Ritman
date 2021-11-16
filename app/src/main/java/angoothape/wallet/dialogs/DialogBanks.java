package angoothape.wallet.dialogs;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.adapters.BanksAdapter;
import angoothape.wallet.base.BaseDialogFragment;
import angoothape.wallet.beneficairyRegistration.BeneficiaryRegistrationActivity;
import angoothape.wallet.databinding.TransferDialogPurposeBinding;
import angoothape.wallet.di.JSONdi.restResponse.BankListResponse;
import angoothape.wallet.di.JSONdi.restResponse.BanksList;
import angoothape.wallet.interfaces.OnSelectBank;
import angoothape.wallet.personal_loan.fragment.PLActivity;

public class DialogBanks extends BaseDialogFragment<TransferDialogPurposeBinding>
        implements OnSelectBank {

    OnSelectBank onSelectBank;
    List<BanksList> responseList;
    BanksAdapter adapter;

    @Override
    public int getTheme() {
        return R.style.AppTheme_NoActionBar_FullScreenDialog;
    }

    @Override
    protected void injectView() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }


    public DialogBanks(List<BanksList> responseList, OnSelectBank onSelectBank) {
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
        } else if (getBaseActivity() instanceof PLActivity) {
            binding.toolBarLayout.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.posBlue));
        } else {
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
                BanksAdapter(responseList, this);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setHasFixedSize(true);
        binding.transferPurposeList.setAdapter(adapter);
        binding.searchView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSelectBank(BankListResponse bankDetails) {

    }

    @Override
    public void onSelectBank(BanksList bankDetails) {
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
