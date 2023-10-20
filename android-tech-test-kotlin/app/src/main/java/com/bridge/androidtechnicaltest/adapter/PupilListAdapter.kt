package com.bridge.androidtechnicaltest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bridge.androidtechnicaltest.R
import com.bridge.androidtechnicaltest.model.PupilUi
import kotlinx.android.synthetic.main.item_pupil_list.view.*

class PupilListAdapter(
    private val onClickListener: (PupilUi) -> Unit,
    private val onDeleteListener: () -> Unit
) : ListAdapter<PupilUi, PupilListAdapter.PupilViewHolder>(object : DiffUtil.ItemCallback<PupilUi>(){

    override fun areItemsTheSame(oldItem: PupilUi, newItem: PupilUi): Boolean =
        oldItem.pupilId == newItem.pupilId

    override fun areContentsTheSame(oldItem: PupilUi, newItem: PupilUi): Boolean =
        oldItem.pupilId == newItem.pupilId && oldItem.pupilName == newItem.pupilName

}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PupilViewHolder =
        PupilViewHolder(parent)

    override fun onBindViewHolder(holder: PupilViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PupilViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_pupil_list, parent, false)
    ) {
        fun bind(item: PupilUi) {
            itemView.tvpupilID.text = item.pupilId.toString()
            itemView.tvName.text = item.pupilName

            itemView.ivDelete.setOnClickListener { onDeleteListener.invoke() }
            itemView.setOnClickListener { onClickListener.invoke(item) }
        }
    }
}