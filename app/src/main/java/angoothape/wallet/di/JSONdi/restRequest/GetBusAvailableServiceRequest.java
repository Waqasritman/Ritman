package angoothape.wallet.di.JSONdi.restRequest;

import android.os.Parcel;
import android.os.Parcelable;

public class GetBusAvailableServiceRequest implements Parcelable {
    Credentials Credentials = new Credentials();
    public String source_id = "";
    public String designation_id = "";
    public String journey_date = "";
    public String startStation = "";
    public String endStation = "";

    public GetBusAvailableServiceRequest() {
    }


    protected GetBusAvailableServiceRequest(Parcel in) {
        Credentials = in.readParcelable(angoothape.wallet.di.JSONdi.restRequest.Credentials.class.getClassLoader());
        source_id = in.readString();
        designation_id = in.readString();
        journey_date = in.readString();
        startStation = in.readString();
        endStation = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(Credentials, flags);
        dest.writeString(source_id);
        dest.writeString(designation_id);
        dest.writeString(journey_date);
        dest.writeString(startStation);
        dest.writeString(endStation);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetBusAvailableServiceRequest> CREATOR = new Creator<GetBusAvailableServiceRequest>() {
        @Override
        public GetBusAvailableServiceRequest createFromParcel(Parcel in) {
            return new GetBusAvailableServiceRequest(in);
        }

        @Override
        public GetBusAvailableServiceRequest[] newArray(int size) {
            return new GetBusAvailableServiceRequest[size];
        }
    };
}
