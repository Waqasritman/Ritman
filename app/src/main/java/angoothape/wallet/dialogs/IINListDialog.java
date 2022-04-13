package angoothape.wallet.dialogs;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.adapters.IINTypeAdapter;
import angoothape.wallet.base.BaseDialogFragment;
import angoothape.wallet.databinding.TransferDialogPurposeBinding;
import angoothape.wallet.di.JSONdi.restResponse.IINListResponse;
import angoothape.wallet.interfaces.InterfaceIINList;


public class IINListDialog extends BaseDialogFragment<TransferDialogPurposeBinding>
        implements InterfaceIINList {


    List<IINListResponse> IINListResponseList;
    InterfaceIINList interfaceIINList;
    IINTypeAdapter adapter;


    public IINListDialog(List<IINListResponse> IINListResponseList, InterfaceIINList interfaceIINList) {
        this.IINListResponseList = IINListResponseList;
        this.interfaceIINList = interfaceIINList;
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

        binding.titleOfPage.setText(getString(R.string.select_bank));

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

        Collections.sort(IINListResponseList, (o1, o2) ->
                o1.Issuer_Bank_Name.compareToIgnoreCase(o2.Issuer_Bank_Name));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                IINTypeAdapter(getContext(), IINListResponseList, this);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setHasFixedSize(true);
        binding.transferPurposeList.setAdapter(adapter);
    }

    @Override
    public void onResponseMessage(String message) {

    }

    @Override
    public void IINList(List<IINListResponse> IINlist) {

    }

    @Override
    public void onSelectIINList(IINListResponse SelectIIN) {
        interfaceIINList.onSelectIINList(SelectIIN);
        cancelUpload();
    }
}
