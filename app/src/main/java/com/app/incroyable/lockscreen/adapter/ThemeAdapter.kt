package com.app.incroyable.lockscreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.incroyable.lockscreen.databinding.ItemThemeBinding
import com.app.incroyable.lockscreen.model.ThemeData
import com.app.incroyable.lockscreen.storage.getDefaultPreferences
import com.app.incroyable.lockscreen.storage.prefTheme
import com.app.incroyable.lockscreen.storage.setPreferences
import com.app.incroyable.lockscreen.utilities.gone
import com.app.incroyable.lockscreen.utilities.visible

open class ThemeAdapter(
    private val mContext: Context,
    private var mSelection: Int,
    private val themeData: ArrayList<ThemeData>
) :
    RecyclerView.Adapter<ThemeAdapter.MyViewHolder>() {

    inner class MyViewHolder(val fBinding: ItemThemeBinding) :
        RecyclerView.ViewHolder(fBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemThemeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return themeData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder) {
            with(fBinding) {
                with(themeData[position]) {
                    themeView.setImageResource(themeData[position].thumb)

                    if (mSelection == themeData[position].position)
                        themeCheck.visible
                    else
                        themeCheck.gone

                    itemView.setOnClickListener {
                        mSelection = themeData[position].position
                        mContext.setPreferences(prefTheme, mSelection)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

    fun dataChanged() {
        mSelection = mContext.getDefaultPreferences(prefTheme) as Int
        notifyDataSetChanged()
    }
}