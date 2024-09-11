package com.app.incroyable.lockscreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.incroyable.lockscreen.R
import com.app.incroyable.lockscreen.activity.QuestionActivity
import com.app.incroyable.lockscreen.databinding.ItemQuestionBinding
import com.app.incroyable.lockscreen.databinding.ItemQuestionHeaderBinding
import com.app.incroyable.lockscreen.model.QuestionData
import com.app.incroyable.lockscreen.utilities.firstCharColor
import com.app.incroyable.lockscreen.utilities.gone
import com.app.incroyable.lockscreen.utilities.visible

class QuestionAdapter(
    private val mContext: Context,
    private var mSelection: Int,
    private val questionData: ArrayList<QuestionData>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1

    inner class MyViewHolder(val fBinding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(fBinding.root)

    inner class HeaderViewHolder(val fBinding: ItemQuestionHeaderBinding) :
        RecyclerView.ViewHolder(fBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val headerBinding = ItemQuestionHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            HeaderViewHolder(headerBinding)
        } else {
            val itemBinding = ItemQuestionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            MyViewHolder(itemBinding)
        }
    }

    override fun getItemCount(): Int {
        return questionData.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (questionData[position].isSectionHeader) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val questionItem = questionData[position]
        if (holder is HeaderViewHolder) {
            val headerHolder = holder as HeaderViewHolder
            val header = getSectionHeaderText(questionItem.quality)
            headerHolder.fBinding.txtQuestionHeader.text = mContext.firstCharColor(header)
        } else if (holder is MyViewHolder) {

            holder.setIsRecyclable(false)
            val itemHolder = holder as MyViewHolder
            itemHolder.fBinding.txtQuestion.text = questionItem.question

            val value: Boolean = mSelection == questionItem.position

            if (questionItem.quality != 0) {
                if (value) itemHolder.fBinding.clickDelete.setImageResource(
                    R.drawable.icon_done
                ) else itemHolder.fBinding.clickDelete.gone
            } else {
                if (value) itemHolder.fBinding.clickDelete.setImageResource(
                    R.drawable.icon_done
                ) else itemHolder.fBinding.clickDelete.visible
            }

            itemHolder.fBinding.topView.setOnClickListener {
                (mContext as QuestionActivity).giveAnswer(questionItem)
            }

            itemHolder.fBinding.clickDelete.setOnClickListener {
                if (!value)
                    (mContext as QuestionActivity).deleteQuestion(position, questionItem)
            }
        }
    }

    private fun getSectionHeaderText(quality: Int): String {
        return when (quality) {
            0 -> mContext.resources.getString(R.string.que_personal)
            1 -> mContext.resources.getString(R.string.que_good)
            2 -> mContext.resources.getString(R.string.que_fair)
            3 -> mContext.resources.getString(R.string.que_poor)
            else -> mContext.resources.getString(R.string.que_unknown)
        }
    }

    fun updateData(value: Int) {
        mSelection = value
        notifyDataSetChanged()
    }
}