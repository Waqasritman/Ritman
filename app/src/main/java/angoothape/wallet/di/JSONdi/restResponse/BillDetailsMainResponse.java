package angoothape.wallet.di.JSONdi.restResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.generic_response.ApiResponse;

public class BillDetailsMainResponse extends ApiResponse<BillDetailResponse> implements Parcelable {
    @SerializedName("Customer_ID_")
    public String customerID;
    @SerializedName("Bill_Details_Resp_")
    public BillDetailResponse billDetailResponse;

    protected BillDetailsMainResponse(Parcel in) {
        customerID = in.readString();
        billDetailResponse = in.readParcelable(BillDetailResponse.class.getClassLoader());
    }

    public static final Creator<BillDetailsMainResponse> CREATOR = new Creator<BillDetailsMainResponse>() {
        @Override
        public BillDetailsMainResponse createFromParcel(Parcel in) {
            return new BillDetailsMainResponse(in);
        }

        @Override
        public BillDetailsMainResponse[] newArray(int size) {
            return new BillDetailsMainResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customerID);
        dest.writeParcelable(billDetailResponse, flags);
    }
}
