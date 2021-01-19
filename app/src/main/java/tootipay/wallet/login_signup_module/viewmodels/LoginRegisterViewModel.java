package tootipay.wallet.login_signup_module.viewmodels;

import androidx.lifecycle.ViewModel;

import tootipay.wallet.di.RequestHelper.ForgotPinSetNewRequest;
import tootipay.wallet.login_signup_module.helper.RegisterUserRequest;

public class LoginRegisterViewModel extends ViewModel {
   public RegisterUserRequest registerUserRequest = new RegisterUserRequest();
   public ForgotPinSetNewRequest forgotPinSetNewRequest = new ForgotPinSetNewRequest();
}
