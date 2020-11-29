package totipay.wallet.login_signup_module.viewmodels;

import androidx.lifecycle.ViewModel;

import totipay.wallet.di.RequestHelper.ForgotPinSetNewRequest;
import totipay.wallet.login_signup_module.helper.RegisterUserRequest;

public class LoginRegisterViewModel extends ViewModel {
   public RegisterUserRequest registerUserRequest = new RegisterUserRequest();
   public ForgotPinSetNewRequest forgotPinSetNewRequest = new ForgotPinSetNewRequest();
}
