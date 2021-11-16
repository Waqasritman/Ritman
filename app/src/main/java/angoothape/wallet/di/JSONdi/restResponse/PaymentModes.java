package angoothape.wallet.di.JSONdi.restResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PaymentModes implements Parcelable {
    @SerializedName("Payment_ID")
    public String paymentID;
    @SerializedName("Payment_Name")
    public String paymentName;

    public PaymentModes() {
    }

    protected PaymentModes(Parcel in) {
        paymentID = in.readString();
        paymentName = in.readString();
    }

    public static final Creator<PaymentModes> CREATOR = new Creator<PaymentModes>() {
        @Override
        public PaymentModes createFromParcel(Parcel in) {
            return new PaymentModes(in);
        }

        @Override
        public PaymentModes[] newArray(int size) {
            return new PaymentModes[size];
        }
    };

    @Override
    public String toString() {
        return this.paymentName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(paymentID);
        dest.writeString(paymentName);
    }
}

