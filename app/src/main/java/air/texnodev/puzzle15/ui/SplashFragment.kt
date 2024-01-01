package air.texnodev.puzzle15.ui

import air.texnodev.puzzle15.R
import air.texnodev.puzzle15.databinding.FragmentSplashBinding
import air.texnodev.puzzle15.tools.Nav
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

class SplashFragment : Fragment() {
    //=================== VIEW BINDING ===================//
    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        YoYo.with(Techniques.SlideInLeft)
            .duration(3000)
            .repeat(0)
            .playOn(binding.text);

        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(
                R.id.mainFragment,
                bundleOf(),
                Nav().optionB(R.id.splashFragment)
            )

        }, 4000)
    }

}