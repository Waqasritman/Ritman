package ritman.wallet.dialogs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ritman.wallet.adapters.RelationListAdapter;
import ritman.wallet.R;
import ritman.wallet.base.BaseDialogFragment;
import ritman.wallet.beneficairyRegistration.BeneficiaryRegistrationActivity;
import ritman.wallet.databinding.TransferDialogPurposeBinding;
import ritman.wallet.di.JSONdi.restRequest.SimpleRequest;
import ritman.wallet.di.JSONdi.restResponse.RelationListResponse;
import ritman.wallet.di.JSONdi.retrofit.RestClient;
import ritman.wallet.di.XMLdi.ApiHelper;
import ritman.wallet.di.XMLdi.HTTPHelper;
import ritman.wallet.di.XMLdi.RequestHelper.GetRelationListRequest;
import ritman.wallet.di.XMLdi.ResponseHelper.GetRelationListResponse;
import ritman.wallet.di.XMLdi.SoapActionHelper;
import ritman.wallet.di.generic_response.SimpleResponse;
import ritman.wallet.interfaces.OnSelectRelation;
import ritman.wallet.utils.IsNetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

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
