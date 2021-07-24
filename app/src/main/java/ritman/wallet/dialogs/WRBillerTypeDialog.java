package ritman.wallet.dialogs;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import ritman.wallet.R;
import ritman.wallet.adapters.WRBillerTypeAdapter;
import ritman.wallet.base.BaseDialogFragment;
import ritman.wallet.databinding.TransferDialogPurposeBinding;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillerTypeResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.WRBillerTypeResponse;
import ritman.wallet.interfaces.OnWRBillerType;

public class WRBillerTypeDialog extends BaseDialogFragment<TransferDialogPurposeBinding>
        implements OnWRBillerType {


    List<GetWRBillerTypeResponse> billerTypeResponseList;
    OnWRBillerType onWRBillerType;
    WRBillerTypeAdapter adapter;


    public WRBillerTypeDialog(List<GetWRBillerTypeResponse> billerTypeResponseList, OnWRBillerType onWRBillerType) {
        this.billerTypeResponseList = billerTypeResponseList;
        this.onWRBillerType = onWRBillerType;
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

        Collections.sort(billerTypeResponseList, (o1, o2) ->
                o1.getName().compareToIgnoreCase(o2.getName()));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                WRBillerTypeAdapter(getContext() ,billerTypeResponseList, this);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setHasFixedSize(true);
        binding.transferPurposeList.setAdapter(adapter);
    }

    @Override
    public void onBillerTypeList(List<GetWRBillerTypeResponse> billerTypeList) {

    }

    @Override
    public void onBillerTypeSelect(GetWRBillerTypeResponse billerType) {
        onWRBillerType.onBillerTypeSelect(billerType);
        cancelUpload();
    }

    @Override
    public void onResponseMessage(String message) {

    }
}
