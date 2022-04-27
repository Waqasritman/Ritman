package angoothape.wallet.di.JSONdi.restResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.di.generic_response.ApiResponse;

public class BillDetailResponse extends ApiResponse<BillDetailResponse> implements Parcelable {
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

    protected BillDetailResponse(Parcel in) {
        validationid = in.readString();
        objectid = in.readString();
        billerid = in.readString();
        biller_name = in.readString();
        biller_category = in.readString();
        payment_amount = in.readString();
        currency = in.readString();
        message = in.readString();
        validation_date = in.readString();
        valid_until = in.readString();
    }

    public static final Creator<BillDetailResponse> CREATOR = new Creator<BillDetailResponse>() {
        @Override
        public BillDetailResponse createFromParcel(Parcel in) {
            return new BillDetailResponse(in);
        }

        @Override
        public BillDetailResponse[] newArray(int size) {
            return new BillDetailResponse[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(validationid);
        dest.writeString(objectid);
        dest.writeString(billerid);
        dest.writeString(biller_name);
        dest.writeString(biller_category);
        dest.writeString(payment_amount);
        dest.writeString(currency);
        dest.writeString(message);
        dest.writeString(validation_date);
        dest.writeString(valid_until);
    }

    public static class billlist {
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
        public String billnumber = "";

        @SerializedName("billperiod")
        public String billperiod = "";

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

    public static class authenticators {
        @SerializedName("parameter_name")
        public String parameter_name;

        @SerializedName("value")
        public String value;
    }


}


