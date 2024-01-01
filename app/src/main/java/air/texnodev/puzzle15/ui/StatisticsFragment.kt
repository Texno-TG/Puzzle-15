package air.texnodev.puzzle15.ui

import air.texnodev.puzzle15.BaseFragment
import air.texnodev.puzzle15.adapters.TopGamesAdapter
import air.texnodev.puzzle15.databinding.FragmentStatisticsBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

class StatisticsFragment : BaseFragment() {

    //=================== VIEW BIDING ===================//
    private lateinit var binding: FragmentStatisticsBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticsBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appDatabase.statisticDao().getTopGame().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()){
                binding.recycler.adapter = TopGamesAdapter(it)
                binding.topGames.text = "Top ${it.size} Games"
                var best = it[0]
                binding.stepGame.text = best.step.toString()
                binding.timeGame.text = createTime(best.time)
            }else{
                binding.topGames.visibility = View.GONE
            }
        })

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun createTime(time:Int): String {
        val minute = time / 60
        val second = time % 60

        val forShowMinute = if (minute < 10) {
            "0$minute"
        } else {
            "$minute"
        }
        val forShowSecond = if (second < 10) {
            "0$second"
        } else {
            "$second"
        }
        return "$forShowMinute:$forShowSecond"
    }


}