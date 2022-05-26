package angoothape.wallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import angoothape.wallet.databinding.FragmentDashBoardBinding
import angoothape.wallet.fragments.BaseFragment

class DashBoardFragment : BaseFragment<FragmentDashBoardBinding>() {

    override fun injectView() {

    }

    override fun setUp(savedInstanceState: Bundle?) {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_dash_board
    }


}