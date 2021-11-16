package angoothape.wallet.dialogs;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Response;
import angoothape.wallet.adapters.RelationListAdapter;
import angoothape.wallet.R;
import angoothape.wallet.base.BaseDialogFragment;
import angoothape.wallet.beneficairyRegistration.BeneficiaryRegistrationActivity;
import angoothape.wallet.databinding.TransferDialogPurposeBinding;
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequest;
import angoothape.wallet.di.JSONdi.restResponse.RelationListResponse;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.interfaces.OnSelectRelation;
import angoothape.wallet.utils.IsNetworkConnection;

import java.util.ArrayList;
import java.util.List;

public class DialogRelationList extends BaseDialogFragment<TransferDialogPurposeBinding>
        implements OnSelectRelation {

    OnSelectRelation onSelectRelation;
    List<RelationListResponse> responseList;
    RelationListAdapter adapter;


    @Override
    public int getTheme() {
        return R.style.AppTheme_NoActionBar_FullScreenDialog;
    }

    @Override
    protected void injectView() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    public DialogRelationList(OnSelectRelation onSelectRelation) {
        this.onSelectRelation = onSelectRelation;
    }

    @Override
    public int getLayoutId() {
        return R.layout.transfer_dialog_purpose;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        setupRecyclerView();
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            getRelationList();
        } else {
            onMessage(getString(R.string.no_internet));
            cancelUpload();
        }

        binding.closeDialog.setOnClickListener(v -> {
            cancelUpload();
        });

        binding.titleOfPage.setText(getString(R.string.plz_select_relation));

        if (getBaseActivity() instanceof BeneficiaryRegistrationActivity) {
            binding.toolBarLayout.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.posBlue));
        } else {
            binding.toolBarLayout.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
        }
    }


    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {
        // purposeList = new ArrayList<>();

        responseList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                RelationListAdapter(responseList, this);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setHasFixedSize(true);
        binding.transferPurposeList.setAdapter(adapter);
    }

    @Override
    public void onSelectRelation(RelationListResponse relation) {
        onSelectRelation.onSelectRelation(relation);
        cancelUpload();
    }


    void getRelationList() {
        binding.progressBar.setVisibility(View.VISIBLE);
        SimpleRequest request = new SimpleRequest();
        Call<RelationListResponse> call = RestClient.get().getRelationList(RestClient.makeGSONRequestBody(request)
                , getSessionManager().getMerchantName());

        call.enqueue(new retrofit2.Callback<RelationListResponse>() {
            @Override
            public void onResponse(Call<RelationListResponse> call, Response<RelationListResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().responseCode.equals(101)) {
                        responseList.clear();
                        assert response.body() != null;
                        responseList.addAll(response.body().data);
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    onMessage(getString(R.string.some_thing_wrong));
                }
            }

            @Override
            public void onFailure(Call<RelationListResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                onMessage(t.getLocalizedMessage());
            }
        });


    }

}
