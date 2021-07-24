package ritman.wallet.beneficairyRegistration.fragments;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;


import com.basgeekball.awesomevalidation.AwesomeValidation;

import ritman.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import ritman.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import ritman.wallet.R;
import ritman.wallet.beneficairyRegistration.BeneficiaryRegistrationActivity;
import ritman.wallet.beneficairyRegistration.viewmodel.RegisterBeneficiaryViewModel;
import ritman.wallet.databinding.ActivitySendMoneyViaBankFirstBinding;
import ritman.wallet.di.JSONdi.restRequest.RegisterBeneficiaryRequest;
import ritman.wallet.di.JSONdi.restResponse.RelationListResponse;
import ritman.wallet.di.XMLdi.RequestHelper.GetSendRecCurrencyRequest;
import ritman.wallet.di.XMLdi.ResponseHelper.GetCountryListResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetRelationListResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetSendRecCurrencyResponse;
import ritman.wallet.di.XMLdi.apicaller.GetSendRecCurrencyTask;
import ritman.wallet.dialogs.DialogCountry;
import ritman.wallet.dialogs.DialogCurrency;
import ritman.wallet.dialogs.DialogRelationList;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.interfaces.OnSelectCountry;
import ritman.wallet.interfaces.OnSelectCurrency;
import ritman.wallet.interfaces.OnSelectRelation;
import ritman.wallet.utils.CountrySelector;
import ritman.wallet.utils.IsNetworkConnection;

import java.util.List;

import ritman.wallet.utils.CountryParser;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class AddBeneficiaryBankTransferPersonalDetailFragment
        extends BaseFragment<ActivitySendMoneyViaBankFirstBinding>
        implements OnSelectRelation {
    private AwesomeValidation mAwesomeValidation;
    RegisterBeneficiaryViewModel viewModel;

    @Override
    protected void injectView() {

    }


    @Override
    public void onResume() {
        super.onResume();

        if (getBaseActivity() instanceof BeneficiaryRegistrationActivity) {
            ((BeneficiaryRegistrationActivity) getBaseActivity()).binding.toolBar.titleTxt
                    .setText(getString(R.string.bank_beneficairy));
            viewModel = ((BeneficiaryRegistrationActivity) getBaseActivity()).viewModel;
        } else {
            ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                    .setText(getString(R.string.bank_beneficairy));
            viewModel = ((MoneyTransferMainLayout) getBaseActivity()).viewModel;

            binding.merchaentNumber.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_beneficiary_bank_pos_red));
            binding.firstName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_beneficiary_bank_pos_red));
            binding.lastName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_beneficiary_bank_pos_red));
            binding.address.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_beneficiary_bank_pos_red));
            binding.nextLayout.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
            binding.selectRelation.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_beneficiary_bank_pos_red));
            binding.merchaentNumber.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_beneficiary_bank_pos_red));
            binding.merchaentNumber.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_beneficiary_bank_pos_red));
            binding.mobileno.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_beneficiary_bank_pos_red));
           // binding.selectRelation.getCompoundDrawables()[0].setTint(R.color.colorPrimary);

            setTextViewDrawableColor(binding.selectRelation,R.color.colorPrimary);
        }


        viewModel.beneRegister.observe(this
                , registerBeneficiaryRequest -> {
                    binding.firstName.setText(registerBeneficiaryRequest.FirstName);
                    binding.merchaentNumber.setText(registerBeneficiaryRequest.CustomerNo);
                    binding.lastName.setText(registerBeneficiaryRequest.LastName);
                    binding.address.setText(registerBeneficiaryRequest.Address);
                    binding.selectRelation.setText(registerBeneficiaryRequest.CustomerRelation);
                    binding.mobileno.setText(registerBeneficiaryRequest.Telephone);
                });
    }

    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.merchaentNumber.getText().toString())) {
            onMessage(getString(R.string.enter_merchant_number));
            return false;
        } else if (TextUtils.isEmpty(binding.firstName.getText().toString())) {
            onMessage(getString(R.string.enter_name_bene__first_name_error));
            return false;
        } else if (TextUtils.isEmpty(binding.lastName.getText().toString())) {
            onMessage(getString(R.string.enter_name_bene__last_name_error));
            return false;
        } else if (TextUtils.isEmpty(binding.address.getText().toString())) {
            onMessage(getString(R.string.enter_adress));
        } else if (TextUtils.isEmpty(binding.selectRelation.getText().toString())) {
            onMessage(getString(R.string.plz_select_relation));
            return false;
        }

//        else if ((CheckValidation.isValidName(
//                binding.beneficairyName.getText().toString()))) {
//            onMessage(getString(R.string.bene_name_special_character_not_allowed));
//            return false;
//        }
        return true;
    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        assert getArguments() != null;
        binding.merchaentNumber.setText(getArguments().getString("customer_no"));
        binding.merchaentNumber.setEnabled(false);

        mAwesomeValidation = new AwesomeValidation(BASIC);

        mAwesomeValidation.addValidation(binding.firstName, "^[A-Za-z\\s]+", getResources().getString(R.string.firstname_personal));
        mAwesomeValidation.addValidation(binding.lastName, "^[A-Za-z\\s]+", getResources().getString(R.string.lastname_personal));
      //  mAwesomeValidation.addValidation(binding.address, "[\\w\\s-,.]+$", getResources().getString(R.string.addre_line_one));
        mAwesomeValidation.addValidation(binding.mobileno, "[6-9][0-9]{9}", getResources().getString(R.string.invalid_mob_no));

        binding.nextLayout.setOnClickListener(v -> {
            if (mAwesomeValidation.validate()) {
                 if (binding.address.getText().toString().equals("")){
                    onMessage("Please enter beneficiary address");
                }

                 else if (binding.selectRelation.getText().toString().equals("")) {
                    onMessage("Please Select Your Relation with beneficiary ");
                }

                else {
                    RegisterBeneficiaryRequest request = new RegisterBeneficiaryRequest();
                    request.FirstName = binding.firstName.getText().toString();
                    request.LastName = binding.lastName.getText().toString();
                    request.CustomerRelation = binding.selectRelation.getText().toString();
                    request.Address = binding.address.getText().toString();
                    request.CustomerNo = binding.merchaentNumber.getText().toString();
                    request.Telephone = binding.mobileno.getText().toString();
                    viewModel.beneRegister.setValue(request);

                    Navigation.findNavController(binding.getRoot())
                            .navigate(R.id.action_sendMoneyViaBankFirstActivity_to_indiaBankBeneficiary);
                }


            }
        });


        binding.selectRelation.setOnClickListener(v -> {
            DialogRelationList dialog = new DialogRelationList(this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialog.show(transaction, "");
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_money_via_bank_first;
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onSelectRelation(RelationListResponse relation) {
        binding.selectRelation.setText(relation.relationName);
    }
}
