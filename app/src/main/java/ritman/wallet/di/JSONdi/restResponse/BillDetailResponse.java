package ritman.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ritman.wallet.di.generic_response.ApiResponse;

public class BillDetailResponse extends ApiResponse <BillDetailResponse>{
    @SerializedName("validationid")
    public String validationid;

    @SerializedName("objectid")
    public String objectid;

    @SerializedName("billerid")
    public String billerid;

    @SerializedName("biller_name")
    public String biller_name;
    @SerializedName("biller_category")
    public String biller_category;

    @SerializedName("payment_amount")
    public String payment_amount;

    @SerializedName("currency")
    public String currency;

    @SerializedName("message")
    public String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public List<BillDetailResponse.billlist> getBilllist() {
        return billlist;
    }

    public void setBilllist(List<BillDetailResponse.billlist> billlist) {
        this.billlist = billlist;
    }

    public List<BillDetailResponse.authenticators> getAuthenticators() {
        return authenticators;
    }

    public void setAuthenticators(List<BillDetailResponse.authenticators> authenticators) {
        this.authenticators = authenticators;
    }

    @SerializedName("validation_date")
    public String validation_date;

    @SerializedName("valid_until")
    public String valid_until;

    @SerializedName("billlist")
   public List<billlist> billlist = new ArrayList<billlist>();

    @SerializedName("authenticators")
    public List<authenticators> authenticators = new ArrayList<authenticators>();

    public static class billlist{
        public String getObjectid() {
            return objectid;
        }

        public void setObjectid(String objectid) {
            this.objectid = objectid;
        }

        public String getBillid() {
            return billid;
        }

        public void setBillid(String billid) {
            this.billid = billid;
        }

        public String getBillstatus() {
            return billstatus;
        }

        public void setBillstatus(String billstatus) {
            this.billstatus = billstatus;
        }

        public String getBillnumber() {
            return billnumber;
        }

        public void setBillnumber(String billnumber) {
            this.billnumber = billnumber;
        }

        public String getBillperiod() {
            return billperiod;
        }

        public void setBillperiod(String billperiod) {
            this.billperiod = billperiod;
        }

        public String getNet_billamount() {
            return net_billamount;
        }

        public void setNet_billamount(String net_billamount) {
            this.net_billamount = net_billamount;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public void setCustomer_name(String customer_name) {
            this.customer_name = customer_name;
        }

        public String getBillamount() {
            return billamount;
        }

        public void setBillamount(String billamount) {
            this.billamount = billamount;
        }

        public String getBilldate() {
            return billdate;
        }

        public void setBilldate(String billdate) {
            this.billdate = billdate;
        }

        public String getBillduedate() {
            return billduedate;
        }

        public void setBillduedate(String billduedate) {
            this.billduedate = billduedate;
        }

        @SerializedName("objectid")
        public String objectid;

        @SerializedName("billid")
        public String billid;

        @SerializedName("billstatus")
        public String billstatus;

        @SerializedName("billnumber")
        public String billnumber;

        @SerializedName("billperiod")
        public String billperiod;

        @SerializedName("net_billamount")
        public String net_billamount;

        @SerializedName("customer_name")
        public String customer_name;

        @SerializedName("billamount")
        public String billamount;

        @SerializedName("billdate")
        public String billdate;

        @SerializedName("billduedate")
        public String billduedate;
    }

    public static class authenticators{
        @SerializedName("parameter_name")
        public String parameter_name;

        @SerializedName("value")
        public String value;
    }




}

