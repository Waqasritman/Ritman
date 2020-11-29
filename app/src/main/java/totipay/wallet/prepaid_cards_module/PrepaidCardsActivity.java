package totipay.wallet.prepaid_cards_module;

import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;

import totipay.wallet.R;
import totipay.wallet.adapters.TabPagerAdapter;
import totipay.wallet.base.TootiBaseActivity;
import totipay.wallet.databinding.ActivityPrepaidCardsBinding;
import totipay.wallet.di.RequestHelper.GetCustomerCardNoRequest;
import totipay.wallet.di.apicaller.GetCustomerCardNoTask;
import totipay.wallet.interfaces.OnCustomerCardNo;
import totipay.wallet.prepaid_cards_module.fragments.LoadPrepaidCardFragment;
import totipay.wallet.prepaid_cards_module.fragments.ShowPrepaidCardFragment;
import totipay.wallet.utils.IsNetworkConnection;


public class PrepaidCardsActivity extends TootiBaseActivity<ActivityPrepaidCardsBinding>
        implements OnCustomerCardNo {

    TabPagerAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_prepaid_cards;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        binding.toolBar.titleTxt.setText(getString(R.string.prepaid_card_title));
        binding.toolBar.toolBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);

        binding.toolBar.backBtn.setOnClickListener(v -> {
            finish();
        });

        binding.toolBar.crossBtn.setVisibility(View.GONE);


        if (sessionManager.getVirtualCardNo().isEmpty()) {
            if (IsNetworkConnection.checkNetworkConnection(this)) {
                GetCustomerCardNoRequest request = new GetCustomerCardNoRequest();
                request.customerNo = sessionManager.getCustomerNo();
                request.languageID = sessionManager.getlanguageselection();

                GetCustomerCardNoTask task = new GetCustomerCardNoTask(this
                        , this);
                task.execute(request);
            } else {
                onMessage(getString(R.string.no_internet));
            }
        } else {
            initViews();
        }

    }


    void initViews() {
        adapter = new TabPagerAdapter(getSupportFragmentManager()
                , FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new ShowPrepaidCardFragment(), getString(R.string.my_card));
        if (!sessionManager.getVirtualCardNo().isEmpty()) {
            adapter.addFragment(new LoadPrepaidCardFragment(), getString(R.string.load_card));
        }

        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    @Override
    public void onCreateCustomerCardNo(String customerCardNo) {

    }

    @Override
    public void createCustomerCard() {

    }

    @Override
    public void onGetCustomerCardNo(String customerCardNo) {
        sessionManager.setVirtualCardNo(customerCardNo);
        initViews();
    }
}