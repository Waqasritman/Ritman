package totipay.wallet.dialogs;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import totipay.wallet.R;

import totipay.wallet.adapters.WRCountryListAdapter;
import totipay.wallet.base.BaseDialogFragment;
import totipay.wallet.databinding.TransferDialogPurposeBinding;
import totipay.wallet.di.ResponseHelper.WRCountryListResponse;
import totipay.wallet.interfaces.OnWRCountryList;


public class WRCountryListDialog extends BaseDialogFragment<TransferDialogPurposeBinding>
        implements OnWRCountryList {


    WRCountryListAdapter adapter;
    List<WRCountryListResponse> countryList;
    OnWRCountryList onWRCountryList;

    @Override
    public int getTheme() {
        return R.style.AppTheme_NoActionBar_FullScreenDialog;
    }

    @Override
    protected void injectView() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    public WRCountryListDialog(List<WRCountryListResponse> countryList, OnWRCountryList onWRCountryList) {
        this.countryList = countryList;
        this.onWRCountryList = onWRCountryList;
    }

    @Override
    public int getLayoutId() {
        return R.layout.transfer_dialog_purpose;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        setSearchView();
        setupRecyclerView();

        binding.titleOfPage.setText(getString(R.string.select_country));

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

        Collections.sort(countryList, (o1, o2) ->
                o1.countryName.compareToIgnoreCase(o2.countryName));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                WRCountryListAdapter(getContext(), countryList, this);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setHasFixedSize(true);
        binding.transferPurposeList.setAdapter(adapter);
    }


    @Override
    public void onWRCountryList(List<WRCountryListResponse> list) {

    }

    @Override
    public void onWRSelectCountry(WRCountryListResponse country) {
        onWRCountryList.onWRSelectCountry(country);
        cancelUpload();
    }

    @Override
    public void onResponseMessage(String message) {

    }
}
