package totipay.wallet.interfaces;

public interface OnCardDetailsSubmit {
    void onCardDetailsSSubmit(String cardNumber, String cardExpire
            , String cardCVV);
}
