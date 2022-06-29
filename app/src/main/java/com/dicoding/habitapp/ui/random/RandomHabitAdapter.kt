package com.dicoding.habitapp.ui.random

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit

class RandomHabitAdapter(
    private val onClick: (Habit) -> Unit
) : RecyclerView.Adapter<RandomHabitAdapter.PagerViewHolder>() {

    private val habitMap = LinkedHashMap<PageType, Habit>()

    fun submitData(key: PageType, habit: Habit) {
        habitMap[key] = habit
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PagerViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.pager_item, parent, false))

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val key = getIndexKey(position) ?: return
        val pageData = habitMap[key] ?: return
        holder.bind(key, pageData)
    }

    override fun getItemCount() = habitMap.size

    private fun getIndexKey(position: Int) = habitMap.keys.toTypedArray().getOrNull(position)

    enum class PageType {
        HIGH, MEDIUM, LOW
    }

    inner class PagerViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        //TODO 14 : Create view and bind data to item view
        private val txt_title = itemView.findViewById<TextView>(R.id.pager_tv_title)
        private val txt_start = itemView.findViewById<TextView>(R.id.pager_tv_start_time)
        private val txt_minute = itemView.findViewById<TextView>(R.id.pager_tv_minutes)
        private val prior = itemView.findViewById<ImageView>(R.id.item_priority_level)
        private val btn_open_count = itemView.findViewById<Button>(R.id.btn_open_count_down)
        fun bind(pageType: PageType, pageData: Habit) {
            txt_title.text = pageData.title
            txt_start.text = pageData.startTime
            txt_minute.text = pageData.minutesFocus.toString()

            if (pageType.equals(PageType.HIGH)){
                prior.setImageResource(R.drawable.ic_priority_high)
            }else if (pageType.equals(PageType.MEDIUM)){
                prior.setImageResource(R.drawable.ic_priority_medium)
            }else if(pageType.equals(PageType.LOW)){
                prior.setImageResource(R.drawable.ic_priority_low)
            }

            btn_open_count.setOnClickListener {
                onClick(pageData)
            }
        }
    }
}
