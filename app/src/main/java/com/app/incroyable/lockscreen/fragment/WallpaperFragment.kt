package com.app.incroyable.lockscreen.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.app.incroyable.lockscreen.R
import com.app.incroyable.lockscreen.adapter.CustomWallpaperAdapter
import com.app.incroyable.lockscreen.apihelper.baseUrl
import com.app.incroyable.lockscreen.apihelper.decodeUrl
import com.app.incroyable.lockscreen.apihelper.wallpaperUrl
import com.app.incroyable.lockscreen.base.BaseBindingFragment
import com.app.incroyable.lockscreen.databinding.FragmentWallpaperBinding
import com.app.incroyable.lockscreen.storage.fetchTemp
import com.app.incroyable.lockscreen.storage.folderPath
import com.app.incroyable.lockscreen.storage.folderTemp
import com.app.incroyable.lockscreen.storage.getWallpaperList
import com.app.incroyable.lockscreen.storage.prefWallpaper
import com.app.incroyable.lockscreen.storage.setPreferences
import com.app.incroyable.lockscreen.utilities.commonDialog
import com.app.incroyable.lockscreen.utilities.customWallpaperName
import com.app.incroyable.lockscreen.utilities.imgReplace
import com.app.incroyable.lockscreen.utilities.mainFolder
import com.app.incroyable.lockscreen.utilities.tabTitle
import com.app.incroyable.lockscreen.utilities.toastMsg
import com.app.incroyable.lockscreen.utilities.wallpaperExtension
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.Status
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.Locale

class WallpaperFragment : BaseBindingFragment<FragmentWallpaperBinding>(),
    CustomWallpaperAdapter.AdapterCallback {

    private var wallpaperList = ArrayList<String>()
    private lateinit var customWallpaperAdapter: CustomWallpaperAdapter

    companion object {
        private const val ARG_TAB_POSITION = "tabPosition"

        fun newInstance(tabPosition: Int): WallpaperFragment {
            val fragment = WallpaperFragment()
            val args = Bundle()
            args.putInt(ARG_TAB_POSITION, tabPosition)
            fragment.arguments = args
            return fragment
        }
    }

    override fun setBinding(
        layoutInflater: LayoutInflater, container: ViewGroup?
    ): FragmentWallpaperBinding {
        return FragmentWallpaperBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabPosition = arguments?.getInt(ARG_TAB_POSITION) ?: 0
        initRecyclerView(tabPosition)
        updateWallpapers(tabPosition)
    }

    private fun initRecyclerView(
        tabPosition: Int
    ) {
        customWallpaperAdapter = CustomWallpaperAdapter(
            requireContext(), wallpaperList, tabPosition, this
        )
        mBinding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mBinding.recyclerView.adapter = customWallpaperAdapter
    }

    private fun updateWallpapers(tabPosition: Int) {
        wallpaperList.clear()
        if (tabPosition == 0) {
            loadCustomWallpapers()
        } else if (tabPosition in 1..8) {
            loadBtsWallpapers(tabPosition)
//            loadServerWallpapers(tabPosition)
        }
        customWallpaperAdapter.notifyDataSetChanged()
    }

    private fun loadBtsWallpapers(tabPosition: Int) {
        for (i in 1..7) {
            val folderName = tabTitle[tabPosition].lowercase().replace("-", "")
            val wallpaperName = "$mainFolder/$folderName/${folderName}_$i"
            wallpaperList.add(wallpaperName)
        }
    }

    private fun loadCustomWallpapers() {
        for (i in 1..7) {
            val wallpaperName = "$customWallpaperName$i"
            wallpaperList.add(wallpaperName)
        }
        fetchMedia()
    }

    private fun loadServerWallpapers(tabPosition: Int) {
        val modifiedTabTitle = tabTitle[tabPosition].replace("-", "", ignoreCase = true).lowercase()
        val wallpaperCount = requireContext().getWallpaperList()[tabPosition - 1]
        for (i in 1..wallpaperCount) {
            val path = "$modifiedTabTitle/${modifiedTabTitle}_$i$wallpaperExtension"
            wallpaperList.add(path)
        }
    }

    private fun fetchMedia() {
        val folderPath = requireContext().fetchTemp()
        val folder = File(folderPath)
        if (folder.isDirectory) {
            val fileList = folder.listFiles()
            for (file in fileList!!) {
                wallpaperList.add(file.absolutePath)
            }
        }
    }

    fun newImage(path: String) {
        if (!imgReplace)
            wallpaperList.add(path)
        else
            imgReplace = false
        customWallpaperAdapter.notifyDataSetChanged()
        mBinding.recyclerView.scrollToPosition(wallpaperList.size - 1)
//        (requireActivity() as WallpaperActivity).setScroll(mBinding.recyclerView)
    }

    fun scrollingUp() {
        mBinding.recyclerView.smoothScrollToPosition(0)
    }

    override fun onItemClicked(tabPosition: Int, value: String) {
        mContext.setPreferences(prefWallpaper, value)
        customWallpaperAdapter.notifyDataSetChanged()
//        if (tabPosition == 0) {
//            mContext.setPreferences(prefWallpaper, value)
//            customWallpaperAdapter.notifyDataSetChanged()
//        } else {
//            PermissionUtils.checkStoragePermission(requireContext()) {
//                if (checkImageStatus(value))
//                    setWallpaper(value)
//                else
//                    downloadImage(value)
//            }
//        }
    }

    private fun setWallpaper(value: String) {
        val imageName = value.substringAfterLast("/")
        val tempPath = requireContext().folderTemp() + imageName
        val sharePath = requireContext().folderPath() + imageName
        val file = File(tempPath)
        if (file.exists()) {
            requireActivity().commonDialog(layoutResId = R.layout.dialog_common,
                cancelable = true,
                title = getString(R.string.replace_wallpaper),
                message = "",
                positiveClickListener = { dialog, _ ->
                    imgReplace = true
                    cropImage(value, sharePath)
                    dialog.dismiss()
                },
                negativeClickListener = {
                })
        } else {
            imgReplace = false
            cropImage(value, sharePath)
        }
    }

    private fun checkImageStatus(value: String): Boolean {
        val imageName = value.substringAfterLast("/")
        val sharePath = requireContext().folderPath() + imageName
        val file = File(sharePath)
        return file.exists()
    }

    var idDownload = 0
    var alertDialog: AlertDialog? = null
    private lateinit var txtSpeed: TextView
    private lateinit var txtProgress: TextView
    private lateinit var progressBar: ProgressBar

    @SuppressLint("RestrictedApi")
    private fun downloadImage(value: String) {
        val dialogView =
            LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_downloading, null)
        val builder =
            AlertDialog.Builder(requireActivity()).setView(dialogView).setCancelable(false)

        val dpi = resources.displayMetrics.density
        builder.setView(
            dialogView,
            (19 * dpi).toInt(),
            (5 * dpi).toInt(),
            (14 * dpi).toInt(),
            (5 * dpi).toInt()
        )

        dialogView.findViewById<TextView>(R.id.dialog_cancel).setOnClickListener {
            PRDownloader.cancelAll()
            alertDialog?.dismiss()
        }

        alertDialog = builder.create()
        alertDialog?.show()

        alertDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        txtSpeed = dialogView.findViewById(R.id.txt_speed)
        txtProgress = dialogView.findViewById(R.id.txt_progress)
        progressBar = dialogView.findViewById(R.id.progress_bar)

        if (PRDownloader.getStatus(idDownload) == Status.RUNNING) {
            PRDownloader.pause(idDownload)
            return
        }

        if (PRDownloader.getStatus(idDownload) == Status.PAUSED) {
            PRDownloader.resume(idDownload)
            return
        }

        val imageName = value.substringAfterLast("/")
        val sharePath = requireContext().folderPath() + imageName
        val path = decodeUrl(baseUrl) + decodeUrl(wallpaperUrl) + value

        idDownload = PRDownloader.download(path, requireContext().folderPath(), imageName)
            .build()
            .setOnStartOrResumeListener {
            }
            .setOnCancelListener {
                idDownload = 0
                progressBar.progress = 0
                txtProgress.text = "0%"
                txtSpeed.text = ""
            }
            .setOnProgressListener { progress ->
                val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                progressBar.progress = progressPercent.toInt()
                txtProgress.text = "$progressPercent%"
                txtSpeed.text = progressTxt(progress.currentBytes, progress.totalBytes)
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    alertDialog?.dismiss()
                    MediaScannerConnection.scanFile(
                        requireActivity(),
                        arrayOf(sharePath),
                        null
                    ) { _, _ -> }
                    imgReplace = false
                    cropImage(value, sharePath)
                }

                override fun onError(error: Error) {
                    alertDialog?.dismiss()
                    requireContext().toastMsg(getString(R.string.turn_on_connection))
                    txtSpeed.text = ""
                    progressBar.progress = 0
                    txtProgress.text = "0%"
                    idDownload = 0
                }
            })
    }

    private fun progressTxt(currentBytes: Long, totalBytes: Long): String {
        return bytesConverter(currentBytes) + "/" + bytesConverter(totalBytes)
    }

    private fun bytesConverter(bytes: Long): String {
        return String.format(Locale.ENGLISH, "%.2fMB", bytes / (1024.00 * 1024.00))
    }

    private fun cropImage(value: String, sharePath: String) {
        try {
            val imageName = value.substringAfterLast("/")
            val sourceUri = Uri.fromFile(File(sharePath))
            val options = UCrop.Options()
            options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
            val destinationPath = requireContext().folderTemp() + imageName
            UCrop.of(sourceUri, Uri.fromFile(File(destinationPath)))
                .withAspectRatio(9f, 16f)
                .withMaxResultSize(1440, 2560)
                .withOptions(options)
                .start(requireActivity())
        } catch (e: Exception) {
            requireContext().toastMsg(getString(R.string.pick_another_image))
        }
    }

}
