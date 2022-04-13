package angoothape.wallet.di.generic_response;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T>{

    @SerializedName("ResponseCode")
    public Integer responseCode = 500;
    @SerializedName("Description")
    public String description = "";
    @SerializedName("data")
    public T data;

    public T getData() {
        return data;
    }

}
