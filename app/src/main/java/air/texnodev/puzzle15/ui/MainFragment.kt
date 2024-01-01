package air.texnodev.puzzle15.ui

import air.texnodev.puzzle15.BaseFragment
import air.texnodev.puzzle15.R
import air.texnodev.puzzle15.databinding.FragmentMainBinding
import air.texnodev.puzzle15.tools.Nav
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : BaseFragment() {
    //=================== VIEW BINDING ===================//
    private lateinit var binding: FragmentMainBinding

    //=================== SharedPreferences ===================//
    private var sharedPreferences: SharedPreferences? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        sharedPreferences =
            requireActivity().getSharedPreferences("SavedState", Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAnimation()

        appDatabase.continueDao().getContinue().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.continueGameCard.visibility = View.VISIBLE
            }
        })

        binding.startGame.setOnClickListener {
            clearContinue()
            findNavController().navigate(
                R.id.gameFragment,
                bundleOf(),
                Nav().optionB()
            )
        }
        binding.continueGame.setOnClickListener {
            findNavController().navigate(
                R.id.continueGameFragment,
                bundleOf(),
                Nav().optionB()
            )
        }

        binding.statisticGame.setOnClickListener {
            findNavController().navigate(R.id.statisticsFragment, bundleOf(), Nav().optionB())
        }
        binding.infoGame.setOnClickListener {
            findNavController().navigate(R.id.infoFragment, bundleOf(), Nav().optionB())
        }

        binding.exitGame.setOnClickListener {
            requireActivity().finish()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("LOG_APP", " " + sharedPreferences?.getInt("step", 0))
        if (sharedPreferences?.contains("step") == true) {
            binding.continueGameCard.visibility = View.VISIBLE
        }
    }

    private fun loadAnimation() {
        showAnimation(binding.startGameCard, 1000)
        showAnimation(binding.continueGameCard, 1200)
        showAnimation(binding.statisticGameCard, 1400)
        showAnimation(binding.infoGameCard, 1600)
        showAnimation(binding.exitGameCard, 1800)

        showAnimation(binding.icon, 1500)
        showAnimation(binding.nameGame, 1800)

    }

    private fun showAnimation(view: View, duration: Long) {
        YoYo.with(Techniques.Bounce)
            .duration(duration)
            .repeat(0)
            .playOn(view);
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun clearContinue() {
        GlobalScope.launch(Dispatchers.Main) {
            val id = withContext(Dispatchers.IO) {
                return@withContext appDatabase.continueDao().clear()
            }
        }

    }


}