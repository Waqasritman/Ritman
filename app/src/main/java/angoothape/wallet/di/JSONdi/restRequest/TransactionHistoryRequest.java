package angoothape.wallet.di.JSONdi.restRequest;

public class TransactionHistoryRequest {
    public Credentials Credentials = new Credentials();
    public String fromDate = "";
    public String toDate = "";
    public String txnNo = "";
    public String mobileNo = "";
    public String beneName = "";
    public String beneAccountNo = "";


    @Override
    public String toString() {
        return fromDate + " " + toDate + " " + " " + txnNo + " " + mobileNo + " " + beneName + " " + beneAccountNo;
    }
}
