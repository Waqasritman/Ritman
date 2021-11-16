package angoothape.wallet.dialogs;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.adapters.AccomodationAdapter;
import angoothape.wallet.base.BaseDialogFragment;
import angoothape.wallet.databinding.TransferDialogPurposeBinding;
import angoothape.wallet.di.JSONdi.restResponse.GetCasheAccomodationListResponse;
import angoothape.wallet.interfaces.CasheAccomodationListInterface;

public class DialogAccomodation extends BaseDialogFragment<TransferDialogPurposeBinding>
        implements CasheAccomodationListInterface {

    AccomodationAdapter adapter;
    List<GetCasheAccomodationListResponse> accomodationList;
    CasheAccomodationListInterface casheAccomodationListInterface;

    @Override
    public int getTheme() {
        return R.style.AppTheme_NoActionBar_FullScreenDialog;
    }

    @Override
    protected void injectView() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    public DialogAccomodation(List<GetCasheAccomodationListResponse> accomodationList, CasheAccomodationListInterface casheAccomodationListInterface) {
        this.accomodationList = accomodationList;
        this.casheAccomodationListInterface = casheAccomodationListInterface;
    }

    @Override
    public int getLayoutId() {
        return R.layout.transfer_dialog_purpose;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        setupRecyclerView();

        binding.closeDialog.setOnClickListener(v -> {
            cancelUpload();
        });

        binding.titleOfPage.setText(getString(R.string.select_the_purpose_txt));
    }

    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                AccomodationAdapter(getContext(),accomodationList, this);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setHasFixedSize(true);
        binding.transferPurposeList.setAdapter(adapter);
    }

    @Override
    public void onAccomodation(List<GetCasheAccomodationListResponse> responseAccomodationList) {

    }

    @Override
    public void onSelectAccomodation(GetCasheAccomodationListResponse AccomodationSelect) {

        casheAccomodationListInterface.onSelectAccomodation(AccomodationSelect);
        cancelUpload();
    }

    @Override
    public void onResponseMessage(String message) {

    }
}
