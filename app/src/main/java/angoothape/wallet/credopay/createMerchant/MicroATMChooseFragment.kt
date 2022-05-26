package angoothape.wallet.credopay.createMerchant

import android.content.Intent
import android.os.Bundle
import androidx.navigation.Navigation
import angoothape.wallet.R
import angoothape.wallet.credopay.paymentsdk.CredoPayActivity
import angoothape.wallet.databinding.FragmentMicroATMChooseBinding
import angoothape.wallet.fragments.BaseFragment

class MicroATMChooseFragment : BaseFragment<FragmentMicroATMChooseBinding>() {

    override fun injectView() {

    }

    override fun setUp(savedInstanceState: Bundle?) {
        binding.createMechant.setOnClickListener {
            Navigation.findNavController(binding.root)
                    .navigate(R.id.merchantCompanyDetailsFragment)
        }


        binding.addTermainal.setOnClickListener {
            Navigation.findNavController(binding.root)
                    .navigate(R.id.merchantAddTerminalFragment)
        }

        binding.payment.setOnClickListener {
            val intent = Intent(context, CredoPayActivity::class.java)
            startActivity(intent)
        }


        binding.activateTerminal.setOnClickListener {
            Navigation.findNavController(binding.root)
                    .navigate(R.id.activateTerminal)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_micro_a_t_m_choose
    }
}