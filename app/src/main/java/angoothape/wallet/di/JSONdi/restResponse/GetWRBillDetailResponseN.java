package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.generic_response.ApiResponse;

public class GetWRBillDetailResponseN extends ApiResponse<GetWRBillDetailResponseN> {

    @SerializedName("billerID")
    public Integer billerID;
    @SerializedName("skuID")
    public Integer skuID ;
    @SerializedName("CustomerName")
    public String CustomerName ;
    @SerializedName("BalanceOrDue")
    public Double BalanceOrDue ;
    @SerializedName("BillDueDate")
    public String BillDueDate ;

    public Integer getBillerID() {
        return billerID;
    }

    public void setBillerID(Integer billerID) {
        this.billerID = billerID;
    }

    public Integer getSkuID() {
        return skuID;
    }

    public void setSkuID(Integer skuID) {
        this.skuID = skuID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public Double getBalanceOrDue() {
        return BalanceOrDue;
    }

    public void setBalanceOrDue(Double balanceOrDue) {
        BalanceOrDue = balanceOrDue;
    }

    public String getBillDueDate() {
        return BillDueDate;
    }

    public void setBillDueDate(String billDueDate) {
        BillDueDate = billDueDate;
    }
}
