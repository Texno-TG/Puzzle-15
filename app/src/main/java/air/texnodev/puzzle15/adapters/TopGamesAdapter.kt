package air.texnodev.puzzle15.adapters


import air.texnodev.puzzle15.databinding.ItemGameBinding
import air.texnodev.puzzle15.room.entity.StatisticsModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TopGamesAdapter(
    private var list: List<StatisticsModel>
) : RecyclerView.Adapter<TopGamesAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ItemGameBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.stepGame.text = step.toString()
                binding.timeGame.text = createTime(time)
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
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
