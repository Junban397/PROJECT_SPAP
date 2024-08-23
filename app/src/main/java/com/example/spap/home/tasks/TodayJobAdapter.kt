import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spap.R
import com.example.spap.data.CombinedPlantEvent
import com.example.spap.databinding.TodayWorkItemBinding
import com.example.spap.home.tasks.PlantEventViewModel

class TodayJobAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val plantEventList = mutableListOf<CombinedPlantEvent>()
    private var isEmptyState = false

    companion object {
        private const val VIEW_TYPE_EMPTY = 0
        private const val VIEW_TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (isEmptyState) VIEW_TYPE_EMPTY else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_EMPTY) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.empty_item, parent, false)
            EmptyViewHolder(view)
        } else {
            val binding =
                TodayWorkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            Holder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Holder) {
            holder.bind(plantEventList[position])
        }
    }

    override fun getItemCount(): Int {
        return if (isEmptyState) 1 else plantEventList.size
    }

    fun submitList(events: List<CombinedPlantEvent>) {
        if (events.isEmpty()) {
            isEmptyState = true
        } else {
            plantEventList.clear()
            plantEventList.addAll(events)
            isEmptyState = false
        }
        notifyDataSetChanged()
    }

    inner class Holder(private val binding: TodayWorkItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: CombinedPlantEvent) {
            binding.plantEvent = event
            binding.executePendingBindings()

            binding.clearBtn.setOnClickListener {
                val viewModel = ViewModelProvider(binding.root.context as FragmentActivity).get(
                    PlantEventViewModel::class.java
                )
                val scheduleId = event.scheduleId
                val intervalDays = event.intervalDays
                viewModel.completeTask(scheduleId, intervalDays)
            }
        }
    }

    inner class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}