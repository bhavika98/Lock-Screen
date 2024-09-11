package com.app.incroyable.lockscreen.adapter

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.app.incroyable.lockscreen.R
import com.app.incroyable.lockscreen.databinding.ItemWallpaperBinding
import com.app.incroyable.lockscreen.storage.getDefaultPreferences
import com.app.incroyable.lockscreen.storage.loadBitmapFromAssets
import com.app.incroyable.lockscreen.storage.prefWallpaper
import com.app.incroyable.lockscreen.storage.setPreferences
import com.app.incroyable.lockscreen.utilities.gone
import com.app.incroyable.lockscreen.utilities.visible
import com.app.incroyable.lockscreen.utilities.wallpaperExtension
import com.app.incroyable.lockscreen.utilities.wallpaperFolder

open class WallpaperAdapter(
    private val mContext: Context,
    private var mSelection: String,
    private val wallpaperData: ArrayList<String>
) :
    RecyclerView.Adapter<WallpaperAdapter.MyViewHolder>() {

    inner class MyViewHolder(val fBinding: ItemWallpaperBinding) :
        RecyclerView.ViewHolder(fBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemWallpaperBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return wallpaperData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder) {
            with(fBinding) {
                with(wallpaperData[position]) {

                    val image = wallpaperData[position]
                    if (image.contains("/")) {
                        Glide.with(mContext)
                            .load(image)
                            .apply(RequestOptions().centerCrop())
                            .thumbnail(
                                Glide.with(mContext).load(Integer.valueOf(R.drawable.placeholder))
                            )
                            .into(wallpaperView)
                    } else {
                        val bitmap: Bitmap = mContext.loadBitmapFromAssets(
                            "$wallpaperFolder/$image$wallpaperExtension"
                        )!!
                        wallpaperView.setImageBitmap(bitmap)
                    }

                    if (mSelection == image)
                        wallpaperCheck.visible
                    else
                        wallpaperCheck.gone

                    itemView.setOnClickListener {
                        mSelection = wallpaperData[position]
                        mContext.setPreferences(prefWallpaper, mSelection)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

    fun dataChanged() {
        mSelection = mContext.getDefaultPreferences(prefWallpaper) as String
        notifyDataSetChanged()
    }
}