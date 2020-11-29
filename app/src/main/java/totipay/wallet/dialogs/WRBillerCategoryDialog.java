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
import totipay.wallet.adapters.WRBillerCategoryListAdapter;
import totipay.wallet.base.BaseDialogFragment;
import totipay.wallet.databinding.TransferDialogPurposeBinding;
import totipay.wallet.di.ResponseHelper.WRBillerCategoryResponse;
import totipay.wallet.interfaces.OnWRBillerCategoryResponse;

public class WRBillerCategoryDialog extends BaseDialogFragment<TransferDialogPurposeBinding>
        implements OnWRBillerCategoryResponse {


    WRBillerCategoryListAdapter adapter;
    List<WRBillerCategoryResponse> categoryResponseList;
    OnWRBillerCategoryResponse delegate;

    public WRBillerCategoryDialog(List<WRBillerCategoryResponse> categoryResponseList, OnWRBillerCategoryResponse delegate) {
        this.categoryResponseList = categoryResponseList;
        this.delegate = delegate;
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

        binding.titleOfPage.setText(getString(R.string.select_type));

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

        Collections.sort(categoryResponseList, (o1, o2) ->
                o1.name.compareToIgnoreCase(o2.name));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                WRBillerCategoryListAdapter(getContext() , categoryResponseList, this);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setHasFixedSize(true);
        binding.transferPurposeList.setAdapter(adapter);
    }

    @Override
    public void onWRCategory(List<WRBillerCategoryResponse> responseList) {

    }

    @Override
    public void onSelectCategory(WRBillerCategoryResponse category) {
        delegate.onSelectCategory(category);
        cancelUpload();
    }

    @Override
    public void onResponseMessage(String message) {

    }
}
