package com.app.incroyable.lockscreen.adapter

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.app.incroyable.lockscreen.R
import com.app.incroyable.lockscreen.apihelper.baseUrl
import com.app.incroyable.lockscreen.apihelper.decodeUrl
import com.app.incroyable.lockscreen.apihelper.wallpaperUrl
import com.app.incroyable.lockscreen.databinding.ItemCustomWallpaperBinding
import com.app.incroyable.lockscreen.storage.getDefaultPreferences
import com.app.incroyable.lockscreen.storage.loadBitmapFromAssets
import com.app.incroyable.lockscreen.storage.prefWallpaper
import com.app.incroyable.lockscreen.utilities.*
import java.io.File

open class CustomWallpaperAdapter(
    private val mContext: Context,
    private val wallpaperData: ArrayList<String>,
    private val tabPosition: Int,
    private val callback: AdapterCallback
) :
    RecyclerView.Adapter<CustomWallpaperAdapter.MyViewHolder>() {

    interface AdapterCallback {
        fun onItemClicked(tabPosition: Int, value: String)
    }

    inner class MyViewHolder(val fBinding: ItemCustomWallpaperBinding) :
        RecyclerView.ViewHolder(fBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemCustomWallpaperBinding.inflate(
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
                        if (image.contains(tempFolder)) {
                            Glide.with(mContext)
                                .load(image)
                                .apply(RequestOptions().centerCrop())
                                .thumbnail(
                                    Glide.with(mContext)
                                        .load(R.drawable.placeholder)
                                )
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(wallpaperView)
                        } else {
                            val bitmap: Bitmap = mContext.loadBitmapFromAssets(
                                "$image$wallpaperExtension"
                            )!!
                            wallpaperView.setImageBitmap(bitmap)

//                            val path = decodeUrl(baseUrl) + decodeUrl(wallpaperUrl) + image
//                            Glide.with(mContext)
//                                .load(path)
//                                .apply(RequestOptions().centerCrop())
//                                .thumbnail(
//                                    Glide.with(mContext)
//                                        .load(Integer.valueOf(R.drawable.placeholder))
//                                )
//                                .into(wallpaperView)
                        }
                    } else {
                        val bitmap: Bitmap = mContext.loadBitmapFromAssets(
                            "$wallpaperFolder/$image$wallpaperExtension"
                        )!!
                        wallpaperView.setImageBitmap(bitmap)
                    }

                    btnDone.gone
                    btnDelete.gone
//                    if (tabPosition == 0) {
                        if (image.contains("/") && !image.contains(mainFolder)) {
                            if (image == mContext.getDefaultPreferences(prefWallpaper) as String) {
                                btnDone.visible
                                btnDelete.gone
                            } else
                                btnDelete.visible
                        } else {
                            btnDelete.gone
                            if (image == mContext.getDefaultPreferences(prefWallpaper) as String) {
                                btnDone.visible
                            }
                        }
//                    }

                    btnDelete.setOnClickListener {
                        mContext.commonDialog(layoutResId = R.layout.dialog_common,
                            cancelable = true,
                            title = mContext.getString(R.string.wallpaper_sure_to_delete),
                            message = "",
                            positiveClickListener = { dialog, _ ->
                                val file = File(wallpaperData[position])
                                if (file.exists()) {
                                    if (file.delete()) {
                                        mContext.toastMsg(mContext.getString(R.string.wallpaper_deleted_successfully))
                                    }
                                }
                                wallpaperData.removeAt(position)
                                notifyDataSetChanged()
                                dialog.dismiss()
                            },
                            negativeClickListener = {
                            })
                    }

                    itemView.setOnClickListener {
                        callback.onItemClicked(tabPosition, wallpaperData[position])
                    }
                }
            }
        }
    }
}