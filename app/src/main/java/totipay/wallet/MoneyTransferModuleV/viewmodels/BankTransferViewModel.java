package totipay.wallet.MoneyTransferModuleV.viewmodels;

import androidx.lifecycle.ViewModel;

import totipay.wallet.di.RequestHelper.BeneficiaryAddRequest;
import totipay.wallet.di.RequestHelper.TootiPayRequest;
import totipay.wallet.di.ResponseHelper.GetBankNetworkListResponse;
import totipay.wallet.di.ResponseHelper.GetBeneficiaryListResponse;

public class BankTransferViewModel extends ViewModel {
    //to use to store selcted beneficairy
    public GetBeneficiaryListResponse beneficiaryDetails = new GetBeneficiaryListResponse();
    public BeneficiaryAddRequest beneficiaryAddRequest = new BeneficiaryAddRequest();

    public TootiPayRequest request = new TootiPayRequest();

    public void fillBeneficiaryDetails(BeneficiaryAddRequest request) {
        beneficiaryDetails.firstName = request.FirstName;
        beneficiaryDetails.lastName = request.LastName;
        beneficiaryDetails.middleName = request.MiddleName;
        beneficiaryDetails.customerNo = request.customerNo;
        beneficiaryDetails.beneficiaryRelationName  = request.CustomerRelation;
        beneficiaryDetails.address = request.Address;
        beneficiaryDetails.payOutCurrency = request.PayOutCurrency;
        beneficiaryDetails.beneficiaryNo =  request.beneficiaryNo;
        beneficiaryDetails.purposeCode = String.valueOf(request.PurposeCode);
    }


    public void fillBankOf(GetBankNetworkListResponse response) {
        beneficiaryAddRequest.BankCode = response.bankCode;
        beneficiaryAddRequest.BankName = response.bankName;
        beneficiaryAddRequest.BankBranch = response.branchName;
        beneficiaryAddRequest.BranchNameAndAddress = response.bankAddress;
        beneficiaryAddRequest.Address = response.bankAddress;
        beneficiaryAddRequest.BankCountry = beneficiaryAddRequest.PayoutCountryCode;
        beneficiaryAddRequest.PayOutBranchCode = response.bankCode;
    }
}
