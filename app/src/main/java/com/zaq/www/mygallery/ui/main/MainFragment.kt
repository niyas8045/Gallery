package com.zaq.www.mygallery.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zaq.www.mygallery.Data.Pic
import com.zaq.www.mygallery.FirebaseHelperClass.newUUID
import com.zaq.www.mygallery.FirebaseHelperClass.picCollection
import com.zaq.www.mygallery.FirebaseHelperClass.picDirectory
import com.zaq.www.mygallery.ImageAdapter
import com.zaq.www.mygallery.ImageUtility
import com.zaq.www.mygallery.ImageUtility.getInputStreamFromUri
import com.zaq.www.mygallery.R
import kotlinx.android.synthetic.main.main_fragment.*


class MainFragment : Fragment() {

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModel: MainViewModel

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        when (requestCode) {
            0 -> if (resultCode == Activity.RESULT_OK) {
                val selectedImage: Uri = imageReturnedIntent!!.data
                //converting uri image to file
                val imageFile = getInputStreamFromUri(selectedImage, context)
                if (imageFile != null) {
                    val bitmap = BitmapFactory.decodeStream(imageFile)
                    val compressedFile = ImageUtility.compressImage(bitmap)
                    val key = newUUID
                    val imgName = "$key.jpg"
                    Toast.makeText(context, "UPLOADING....", Toast.LENGTH_SHORT).show()
                    val uploadTask = picDirectory.child(imgName).putBytes(compressedFile)
                    uploadTask.addOnFailureListener {
                        // Handle unsuccessful uploads
                    }.addOnSuccessListener { taskSnapshot ->
                        val post = Pic(
                                imgName
                        )
                        val postValues = post.toMap()
                        picCollection.document(key).set(postValues).addOnCompleteListener {
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.new_photo -> {
                val pickPhoto = Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto, 0)//one can be replaced with any action code
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        viewModel.init(activity!!.applicationContext)

        viewManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val adapter = ImageAdapter(activity!!.applicationContext)
        picListRV.setHasFixedSize(true)
        picListRV.layoutManager = viewManager
        picListRV.adapter = adapter
        viewModel.getTasks()?.observe(activity!!, Observer {
            adapter.tasks = it
        })
    }
}
