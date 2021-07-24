package ritman.wallet.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import ritman.wallet.R;
import ritman.wallet.adapters.WRBillerOperatorAdapter;
import ritman.wallet.base.BaseDialogFragment;
import ritman.wallet.billpayment.BillPaymentMainActivity;
import ritman.wallet.billpayment.fragments.UtilityCategoryBFragment;
import ritman.wallet.databinding.TransferDialogPurposeBinding;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillerNamesResponseC;
import ritman.wallet.di.XMLdi.ResponseHelper.WRBillerNamesResponse;
import ritman.wallet.interfaces.OnWRBillerNames;

public class WRBillerOperatorsDialog extends BaseDialogFragment<TransferDialogPurposeBinding>
        implements OnWRBillerNames {


    List<GetWRBillerNamesResponseC> billerTypeResponseList;
    OnWRBillerNames onWRBillerNames;
    WRBillerOperatorAdapter adapter;


    public WRBillerOperatorsDialog(List<GetWRBillerNamesResponseC> billerTypeResponseList, OnWRBillerNames onWRBillerNames) {
        this.billerTypeResponseList = billerTypeResponseList;
        this.onWRBillerNames = onWRBillerNames;
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
        setSearchView();
        setupRecyclerView();

        binding.titleOfPage.setText(getString(R.string.select_operator));

        binding.closeDialog.setOnClickListener(v -> {
          cancelUpload();


        });
        binding.searchView.setVisibility(View.VISIBLE);
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

        Collections.sort(billerTypeResponseList, (o1, o2) ->
                o1.Biller_Name.compareToIgnoreCase(o2.Biller_Name));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                WRBillerOperatorAdapter(getContext() , billerTypeResponseList, this);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setHasFixedSize(true);
        binding.transferPurposeList.setAdapter(adapter);
    }


    @Override
    public void onBillerNamesList(List<GetWRBillerNamesResponseC> responses) {

    }

    @Override
    public void onSelectBillerName(GetWRBillerNamesResponseC billerName) {
        onWRBillerNames.onSelectBillerName(billerName);
        cancelUpload();

    }

    @Override
    public void onResponseMessage(String message) {

    }




}
