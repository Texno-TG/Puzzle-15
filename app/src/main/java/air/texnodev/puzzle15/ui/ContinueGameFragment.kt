package air.texnodev.puzzle15.ui

import air.texnodev.puzzle15.BaseFragment
import air.texnodev.puzzle15.R
import air.texnodev.puzzle15.databinding.DialogYouWinBinding
import air.texnodev.puzzle15.databinding.FragmentGameBinding
import air.texnodev.puzzle15.room.entity.ContinueModel
import air.texnodev.puzzle15.room.entity.StatisticsModel
import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs

class ContinueGameFragment : BaseFragment(), OnClickListener {
    //=================== VIEW BINDING ===================//
    private lateinit var binding: FragmentGameBinding


    //=================== LIST ===================//
    private val list: MutableList<Int> = ArrayList()

    //=================== CoordinateXY ===================//
    private var x = 3
    private var y = 3

    //=================== EMPTY BUTTON ===================//
    private lateinit var emptyBtn: AppCompatButton

    //=================== STEPS ===================//
    private var step = 0

    //=================== Time ===================//
    private var time = 0

    //=================== MediaPlayer ===================//
    private var mediaPlayer: MediaPlayer? = null
    private var playSound = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                showTime()
                mainHandler.postDelayed(this, 1000)
            }
        })

        appDatabase.continueDao().getContinue().observe(viewLifecycleOwner, Observer {
            Log.d("LOG_APP", " getContinue")
            if (it != null) {
                val type = object : TypeToken<List<Int>>() {}.type
                list.addAll(Gson().fromJson(it.list, type))
                time = it.time
                showTime()
                step = it.step
                binding.stepGame.text = step.toString()
                loadBtn()
                updateGrid()
                loadAnimation()
                clearContinue()
            } else {
                if (list.size == 0){
                findNavController().popBackStack()
                }
            }
        })


        showBestGameStep()
        play()

        binding.refresh.setOnClickListener {
            restartGrid()
        }
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.sound.setOnClickListener {
            if (playSound) {
                playSound = false
                binding.sound.setImageResource(R.drawable.baseline_volume_off_24)
                mediaPlayer?.pause()
            } else {
                playSound = true
                binding.sound.setImageResource(R.drawable.baseline_volume_up_24)
                mediaPlayer?.start()
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun showTime() {
        time++
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



        binding.timeGame.text = "$forShowMinute:$forShowSecond"
    }

    private fun createTime(): String {
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

    private fun restartGrid() {
        emptyBtn.visibility = View.VISIBLE
        generateNumbers()
        updateGrid()
        step = 0
        binding.stepGame.text = step.toString()
        loadAnimation()
        time = 0
        binding.timeGame.text = "00:00"

    }

    private fun generateNumbers() {
        do {
            shuffleGrid()
        } while (isSolvable(list))
    }

    private fun shuffleGrid() {
        list.shuffle()
    }

    private fun updateGrid() {
        for (i in 0 until binding.gridLayout.childCount) {
            val button = binding.gridLayout.getChildAt(i) as AppCompatButton
            if (list[i] != 0) {
                button.text = list[i].toString()
            } else {
                emptyBtn = button
                val tag = button.tag.toString()
                x = tag[0].digitToInt()
                y = tag[1].digitToInt()
                emptyBtn.visibility = View.INVISIBLE
            }
        }
    }

    override fun onClick(v: View?) {
        val clicked = v as AppCompatButton
        val tag = v.tag.toString()
        val tagX = tag[0].digitToInt()
        val tagY = tag[1].digitToInt()

        if (abs((tagX + tagY) - (x + y)) == 1 && abs(tagX - x) != 2 && abs(tagY - y) != 2) {
            emptyBtn.visibility = View.VISIBLE
            showAnimation(emptyBtn, 500L)
            clicked.visibility = View.INVISIBLE

            val text = clicked.text.toString()
            clicked.text = ""
            emptyBtn.text = text
            emptyBtn = clicked

            x = tagX
            y = tagY

            step++
            binding.stepGame.text = step.toString()

            checkWin()
        }
    }

    private fun isSolvable(numbers: List<Int>): Boolean {
        var inversions = 0

        for (i in numbers.indices) {

            if (numbers[i] == 0) {
                inversions += i / 4 + 1
                continue
            }

            for (j in i + 1 until numbers.size) {
                if (numbers[i] > numbers[j]) {
                    inversions++
                }
            }
        }

        Log.d("LOG_APP", " isSolvable")
        return inversions % 2 == 0
    }

    private fun checkWin() {
        var counter = 1
        for (i in 0 until 15) {
            val button = binding.gridLayout.getChildAt(i) as AppCompatButton
            if (button.text.isEmpty()) break
            if (button.text.toString().toInt() != counter) {
                break
            } else {
                counter++
            }

            if (counter == 16) {
                youWin()
            }
        }


    }

    private fun youWin() {
        saveStatistic(StatisticsModel(step = step, time = time))
        val dialogBinding = DialogYouWinBinding.inflate(layoutInflater)
        val dialog = Dialog(requireActivity())
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.show()
        dialogBinding.timeGame.text = createTime()
        dialogBinding.stepGame.text = step.toString()

        dialogBinding.btnNewGame.setOnClickListener {
            dialog.dismiss()
            restartGrid()
        }

        dialogBinding.btnBackGame.setOnClickListener {
            dialog.dismiss()
            findNavController().popBackStack()
        }

        step = -1
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun saveStatistic(statisticsModel: StatisticsModel) {
        GlobalScope.launch(Dispatchers.Main) {
            val id = withContext(Dispatchers.IO) {
                return@withContext appDatabase.statisticDao().insert(statisticsModel)
            }
        }

    }

    private fun loadBtn() {
        binding.buttonGrid1.setOnClickListener(this)
        binding.buttonGrid2.setOnClickListener(this)
        binding.buttonGrid3.setOnClickListener(this)
        binding.buttonGrid4.setOnClickListener(this)
        binding.buttonGrid5.setOnClickListener(this)
        binding.buttonGrid6.setOnClickListener(this)
        binding.buttonGrid7.setOnClickListener(this)
        binding.buttonGrid8.setOnClickListener(this)
        binding.buttonGrid9.setOnClickListener(this)
        binding.buttonGrid10.setOnClickListener(this)
        binding.buttonGrid11.setOnClickListener(this)
        binding.buttonGrid12.setOnClickListener(this)
        binding.buttonGrid13.setOnClickListener(this)
        binding.buttonGrid14.setOnClickListener(this)
        binding.buttonGrid15.setOnClickListener(this)
        binding.buttonGrid16.setOnClickListener(this)
    }

    private fun loadAnimation() {
        showAnimation(binding.buttonGrid1)
        showAnimation(binding.buttonGrid2)
        showAnimation(binding.buttonGrid3)
        showAnimation(binding.buttonGrid4)
        showAnimation(binding.buttonGrid5)
        showAnimation(binding.buttonGrid6)
        showAnimation(binding.buttonGrid7)
        showAnimation(binding.buttonGrid8)
        showAnimation(binding.buttonGrid9)
        showAnimation(binding.buttonGrid10)
        showAnimation(binding.buttonGrid11)
        showAnimation(binding.buttonGrid12)
        showAnimation(binding.buttonGrid13)
        showAnimation(binding.buttonGrid14)
        showAnimation(binding.buttonGrid15)
        showAnimation(binding.buttonGrid16)
    }

    private fun showAnimation(view: AppCompatButton) {
        YoYo.with(Techniques.BounceIn)
            .duration(1500)
            .repeat(0)
            .playOn(view);
    }

    private fun showAnimation(view: AppCompatButton, duration: Long) {
        YoYo.with(Techniques.BounceIn)
            .duration(duration)
            .repeat(0)
            .playOn(view);
    }

    private fun showBestGameStep() {
        appDatabase.statisticDao().getTopGame().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                binding.bestGame.text = it[0].step.toString()
            }
        })
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onDestroyView() {
        super.onDestroyView()

        if (step > 0) {
            val newList: MutableList<Int> = ArrayList()
            for (i in 0 until binding.gridLayout.childCount) {
                val button = binding.gridLayout.getChildAt(i) as AppCompatButton

                if (button.text.isNotEmpty()) {
                    newList.add(button.text.toString().toInt())
                } else {
                    newList.add(0)
                }
                if (i == 15) {
                    val model = ContinueModel(
                        list = Gson().toJson(newList),
                        step = step,
                        time = time
                    )
                    GlobalScope.launch(Dispatchers.Unconfined) {
                        val id = withContext(Dispatchers.IO) {
                            return@withContext appDatabase.continueDao().insertContinue(model)
                        }
                    }

                }
            }
        }
        mediaPlayer?.stop()
    }

    private fun play() {
        if (mediaPlayer == null) {
            playSound = true
            mediaPlayer = MediaPlayer.create(requireContext(), R.raw.audio)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        }
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