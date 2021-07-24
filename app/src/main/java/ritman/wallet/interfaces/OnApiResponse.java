package ritman.wallet.interfaces;

public interface OnApiResponse<T> {
    void onSuccessResponse(T response);
    void onError(String message);
}
