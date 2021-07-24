package ritman.wallet.di.XMLdi.apicaller;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import ritman.wallet.di.XMLdi.RequestHelper.QuickPayHistoryRequest;
import ritman.wallet.di.XMLdi.ResponseHelper.TransactionHistoryResponse;
import ritman.wallet.interfaces.OnGetTransactionHistory;
import ritman.wallet.utils.ProgressBar;

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
