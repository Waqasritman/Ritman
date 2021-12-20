package angoothape.wallet.aeps.viewmodels;

import androidx.lifecycle.ViewModel;

import angoothape.wallet.di.JSONdi.AppExecutors;
import angoothape.wallet.di.JSONdi.retrofit.RestApi;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;

public class BioDataViewModel extends ViewModel {

    RestApi restApi = RestClient.get();
    AppExecutors appExecutors = new AppExecutors();


    //GetBioData
   /* public LiveData<NetworkResource<SimpleResponse>> getBioData(BioRequest request, String userName) {
        MutableLiveData<NetworkResource<SimpleResponse>> data = new MutableLiveData<>(); // receiving
        Call<SimpleResponse> call = restApi.getBioData(RestClient.makeGSONRequestBody(request)
                , userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call1, Response<SimpleResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                         SimpleResponse model = new SimpleResponse();
                        model.responseCode = 500;
                        model.description = message;
                        data.postValue(NetworkResource.unSuccess(R.string.error_fatal, model));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.isSuccessful()) {
                    data.postValue(NetworkResource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call1, Throwable t) {
                 SimpleResponse temp = new SimpleResponse();
                if (t instanceof ConnectException) {
                    // COMPLETED handle no internet access case

                    data.postValue(NetworkResource.error(R.string.error_internet, temp));
                } else if (t instanceof IOException) {
                    // handle server error case
                    data.postValue(NetworkResource.error(R.string.error_server, temp));
                } else {
                    // serialization case, throw abort message (maybe save crash log)
                    data.postValue(NetworkResource.error(R.string.error_fatal, temp));
                }
            }
        }));
        return data;
    }
*/
}
