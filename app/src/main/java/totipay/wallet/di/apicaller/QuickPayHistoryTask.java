package totipay.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import totipay.wallet.di.RequestHelper.QuickPayHistoryRequest;
import totipay.wallet.di.ResponseHelper.TransactionHistoryResponse;
import totipay.wallet.interfaces.OnGetTransactionHistory;
import totipay.wallet.utils.ProgressBar;

public class QuickPayHistoryTask extends AsyncTask
        <QuickPayHistoryRequest, Void, List<TransactionHistoryResponse>> {


    ProgressBar progressBar;
    Context context;
    OnGetTransactionHistory onGetTransactionHistory;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected void onPostExecute(List<TransactionHistoryResponse> transactionHistoryResponses) {
        super.onPostExecute(transactionHistoryResponses);
    }

    @Override
    protected List<TransactionHistoryResponse> doInBackground(QuickPayHistoryRequest... quickPayHistoryRequests) {
        return null;
    }
}
