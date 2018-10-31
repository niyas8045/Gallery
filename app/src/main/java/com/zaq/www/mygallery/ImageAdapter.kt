package com.zaq.www.mygallery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

class ImageAdapter(private val mContext: Context) : RecyclerView.Adapter<ImageAdapter.TaskViewHolder>() {

    var tasks: List<String>? = null
        set(taskEntries) {
            field = taskEntries
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(mContext)
                .inflate(R.layout.pic_list, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val jealousSky = JealousSky.getInstance()
        try {
            jealousSky.initialize(
                    "longestPasswordEverCreatedInAllTheUniverseOrMore",
                    "FFD7BADF2FBB1999")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        val taskEntry = tasks
        val picPath = taskEntry!![position]
        val newFile = File(mContext.filesDir, picPath)
        var isEncrypted: FileInputStream? = null
        try {
            isEncrypted = FileInputStream(newFile)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        try {
            holder.image.setImageBitmap(jealousSky.decryptToBitmap(isEncrypted))
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return if (tasks == null) {
            0
        } else tasks!!.size
    }

    // Inner class for creating ViewHolders
    inner class TaskViewHolder
    (itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView

        init {
            image = itemView.findViewById(R.id.image)
        }
    }
}
