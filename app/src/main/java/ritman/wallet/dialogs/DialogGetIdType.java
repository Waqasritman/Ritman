package ritman.wallet.dialogs;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ritman.wallet.adapters.GetIdTypeAdapter;
import ritman.wallet.R;
import ritman.wallet.base.BaseDialogFragment;
import ritman.wallet.databinding.TransferDialogPurposeBinding;
import ritman.wallet.di.XMLdi.RequestHelper.GetIDTypeRequest;
import ritman.wallet.di.XMLdi.ResponseHelper.GetIdTypeResponse;
import ritman.wallet.di.XMLdi.apicaller.GetIdTypeTask;
import ritman.wallet.interfaces.OnSelectIdType;
import ritman.wallet.utils.IsNetworkConnection;

import java.util.ArrayList;
import java.util.List;

public class DialogGetIdType extends BaseDialogFragment<TransferDialogPurposeBinding>
        implements  OnSelectIdType {

    GetIdTypeAdapter adapter;
    OnSelectIdType onSelectItem;
    List<GetIdTypeResponse> idTypeResponseList;

    String idType = "";


    @Override
    public int getTheme() {
        return R.style.AppTheme_NoActionBar_FullScreenDialog;
    }

    @Override
    protected void injectView() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    public DialogGetIdType(String idType , OnSelectIdType onSelectItem) {
        this.idType = idType;
        this.onSelectItem = onSelectItem;
    }


    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.titleOfPage.setText(getString(R.string.selectid_type));
        if(IsNetworkConnection.checkNetworkConnection(getContext())) {
            GetIDTypeRequest request = new GetIDTypeRequest();
            request.countryShortName = idType;
            GetIdTypeTask task = new GetIdTypeTask(getContext(), this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }


        binding.closeDialog.setOnClickListener(v->{
            cancelUpload();
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.transfer_dialog_purpose;
    }

    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {
        // purposeList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                GetIdTypeAdapter(idTypeResponseList, this);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setHasFixedSize(true);
        binding.transferPurposeList.setAdapter(adapter);
    }


    @Override
    public void onResponseMessage(String message) {
        onSelectItem.onResponseMessage(message);
        cancelUpload();
    }

    @Override
    public void onSelectIdType(GetIdTypeResponse response) {
        onSelectItem.onSelectIdType(response);
        cancelUpload();
    }

    @Override
    public void onGetIdTypes(List<GetIdTypeResponse> responses) {
        binding.transferPurposeList.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
        idTypeResponseList = new ArrayList<>();
        idTypeResponseList.addAll(responses);
        setupRecyclerView();
    }
}
