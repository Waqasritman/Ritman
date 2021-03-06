package angoothape.wallet.my_bank_cards;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.adapters.CardDetailsAdapter;
import angoothape.wallet.databinding.ActivityMyCardAndBankBinding;
import angoothape.wallet.di.XMLdi.RequestHelper.GetCardDetailsRequest;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetCardDetailsResponse;
import angoothape.wallet.di.XMLdi.apicaller.GetCardDetailsTask;
import angoothape.wallet.dialogs.SaveCardDialog;
import angoothape.wallet.dialogs.ShowCardDetailsDialog;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.home_module.ClassChangerHelper;
import angoothape.wallet.home_module.NewDashboardActivity;
import angoothape.wallet.home_module.fragments.HomeFragment;
import angoothape.wallet.interfaces.OnCardDetailsSubmit;
import angoothape.wallet.interfaces.OnGetCardDetails;
import angoothape.wallet.utils.IsNetworkConnection;

public class MyCardAndBankActivity extends BaseFragment<ActivityMyCardAndBankBinding>
        implements OnCardDetailsSubmit, OnGetCardDetails {


    CardDetailsAdapter detailsAdapter;
    List<GetCardDetailsResponse> responseList;


    @Override
    public void onResume() {
        super.onResume();
        ((NewDashboardActivity) getBaseActivity()).hideHumBurger(ClassChangerHelper.MY_BANK);

    }

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        setupRecyclerView();
        binding.addNewCardsTv.setOnClickListener(view -> {
            SaveCardDialog dialog = new SaveCardDialog(this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialog.show(transaction, "");
        });

        binding.inculdeLayout.backBtn.setOnClickListener(v -> {
            ((NewDashboardActivity) getBaseActivity())
                    .moveToFragment(new HomeFragment());
        });


        binding.loadCards.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                GetCardDetailsRequest request = new GetCardDetailsRequest();
                request.customerNo = getSessionManager().getCustomerNo();
                request.languageID = getSessionManager().getlanguageselection();

                GetCardDetailsTask task = new GetCardDetailsTask(getContext(),
                        this);
                task.execute(request);
            } else {
                onMessage(getString(R.string.no_internet));
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_card_and_bank;
    }

    @Override
    public void onCardDetailsSSubmit(String cardNumber, String cardExpire, String cardCVV) {

    }


    @Override
    public void onCardDetailsGet(List<GetCardDetailsResponse> cardDetailsResponses) {
        this.responseList.clear();
        this.responseList.addAll(cardDetailsResponses);
        detailsAdapter.notifyDataSetChanged();

        if (cardDetailsResponses.size() > 0) {
            binding.loadCards.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSelectCard(GetCardDetailsResponse cardDetail) {
        ShowCardDetailsDialog dialog = new ShowCardDetailsDialog(cardDetail);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {
        responseList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        detailsAdapter = new
                CardDetailsAdapter(responseList, this);
        binding.cardDetailsRecyclerView.setLayoutManager(mLayoutManager);
        binding.cardDetailsRecyclerView.setHasFixedSize(true);
        binding.cardDetailsRecyclerView.setAdapter(detailsAdapter);
    }
}