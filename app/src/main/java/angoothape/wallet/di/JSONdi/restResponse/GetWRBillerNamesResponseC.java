package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import angoothape.wallet.di.generic_response.ApiResponse;

public class GetWRBillerNamesResponseC extends ApiResponse<List<GetWRBillerNamesResponseC>> {

    @SerializedName("Biller_ID")
    public String Biller_ID;

    @SerializedName("Biller_Name")
    public String Biller_Name;

    @SerializedName("Biller_Type")
    public String Biller_Type;

    @SerializedName("countryCode")
    public String countryCode;

    @SerializedName("billerTypeId")
    public Integer billerTypeId;

    @SerializedName("billerCategoryId")
    public Integer billerCategoryId;

    @SerializedName("biller_location")
    public String biller_location;

    @SerializedName("biller_mode")
    public String biller_mode;

    @SerializedName("biller_status")
    public String biller_status;

    @SerializedName("biller_logo")
    public String biller_logo;

    @SerializedName("bill_pay_biller_type")
    public String bill_pay_biller_type;

    public String getBiller_ID() {
        return Biller_ID;
    }

    public void setBiller_ID(String biller_ID) {
        Biller_ID = biller_ID;
    }

    public String getBiller_Name() {
        return Biller_Name;
    }

    public void setBiller_Name(String biller_Name) {
        Biller_Name = biller_Name;
    }

    public String getBiller_Type() {
        return Biller_Type;
    }

    public void setBiller_Type(String biller_Type) {
        Biller_Type = biller_Type;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getBillerTypeId() {
        return billerTypeId;
    }

    public void setBillerTypeId(Integer billerTypeId) {
        this.billerTypeId = billerTypeId;
    }

    public Integer getBillerCategoryId() {
        return billerCategoryId;
    }

    public void setBillerCategoryId(Integer billerCategoryId) {
        this.billerCategoryId = billerCategoryId;
    }

    public String getBiller_location() {
        return biller_location;
    }

    public void setBiller_location(String biller_location) {
        this.biller_location = biller_location;
    }

    public String getBiller_mode() {
        return biller_mode;
    }

    public void setBiller_mode(String biller_mode) {
        this.biller_mode = biller_mode;
    }

    public String getBiller_status() {
        return biller_status;
    }

    public void setBiller_status(String biller_status) {
        this.biller_status = biller_status;
    }

    public String getBiller_logo() {
        return biller_logo;
    }

    public void setBiller_logo(String biller_logo) {
        this.biller_logo = biller_logo;
    }

    public String getBill_pay_biller_type() {
        return bill_pay_biller_type;
    }

    public void setBill_pay_biller_type(String bill_pay_biller_type) {
        this.bill_pay_biller_type = bill_pay_biller_type;
    }

    public String getPartial_pay() {
        return partial_pay;
    }

    public void setPartial_pay(String partial_pay) {
        this.partial_pay = partial_pay;
    }

    public String getPay_after_duedate() {
        return pay_after_duedate;
    }

    public void setPay_after_duedate(String pay_after_duedate) {
        this.pay_after_duedate = pay_after_duedate;
    }

    public String getOnline_validation() {
        return online_validation;
    }

    public void setOnline_validation(String online_validation) {
        this.online_validation = online_validation;
    }

    public String getIsbillerbbps() {
        return isbillerbbps;
    }

    public void setIsbillerbbps(String isbillerbbps) {
        this.isbillerbbps = isbillerbbps;
    }

    public String getPaymentamount_validation() {
        return paymentamount_validation;
    }

    public void setPaymentamount_validation(String paymentamount_validation) {
        this.paymentamount_validation = paymentamount_validation;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @SerializedName("partial_pay")
    public String partial_pay;

    @SerializedName("pay_after_duedate")
    public String pay_after_duedate;

    @SerializedName("online_validation")
    public String online_validation;

    @SerializedName("isbillerbbps")
    public String isbillerbbps;

    @SerializedName("paymentamount_validation")
    public String paymentamount_validation;

    @SerializedName("currency")
    public String currency;


}
