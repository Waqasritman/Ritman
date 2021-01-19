package tootipay.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import tootipay.wallet.di.RequestHelper.QuickPayHistoryRequest;
import tootipay.wallet.di.ResponseHelper.TransactionHistoryResponse;
import tootipay.wallet.interfaces.OnGetTransactionHistory;
import tootipay.wallet.utils.ProgressBar;

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
