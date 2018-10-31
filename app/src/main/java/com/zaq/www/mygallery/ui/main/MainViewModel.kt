package com.zaq.www.mygallery.ui.main

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.zaq.www.mygallery.AppExecutors
import com.zaq.www.mygallery.Database.AppDatabase
import com.zaq.www.mygallery.Database.Pic
import com.zaq.www.mygallery.FirebaseHelperClass.picCollection
import com.zaq.www.mygallery.FirebaseHelperClass.picDirectory
import com.zaq.www.mygallery.JealousSky
import java.io.ByteArrayInputStream
import java.util.*

class MainViewModel : ViewModel() {

    private var pics: LiveData<List<String>>? = null
    var initFlag: String? = null
    lateinit var context: Context
    val baseQuery = picCollection.orderBy("timestamp", Query.Direction.ASCENDING)
    var nextTimestamp: Long = 0
    private val TAG: String? = javaClass.simpleName

    fun init(context: Context) {
        if (initFlag != null)
            return
        fetchDataFirebase(context)
        initFlag = ""
        pics = AppDatabase.getInstance(context).picDao().allPics
    }

    fun getTasks(): LiveData<List<String>>? {
        return pics
    }

    fun fetchDataFirebase(context: Context) {
        this.context = context
        FetchDatabaseAsyncTask(this).execute(context)
    }

    private fun fetchRef(query: Query) {
        query.addSnapshotListener { querySnapshot, exception ->
            for (dc in querySnapshot!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> downloadImage(dc)
                    DocumentChange.Type.MODIFIED -> Log.i(TAG, "MODIFIED")
                    DocumentChange.Type.REMOVED -> Log.i("PICREPO", "REMOVED")
                }
            }
        }
    }

    private fun downloadImage(dc: DocumentChange) {
        picDirectory.child(dc.document.data["imagePath"].toString()).getBytes(java.lang.Long.MAX_VALUE).addOnSuccessListener {
            val photoTimestamp: Timestamp? = dc.document.getTimestamp("timestamp")
            val jealousSky = JealousSky.getInstance()
            jealousSky.initialize(
                    "longestPasswordEverCreatedInAllTheUniverseOrMore",
                    "FFD7BADF2FBB1999")
            AppExecutors.getInstance().diskIO.execute {
                val bs = ByteArrayInputStream(it)
                jealousSky.encryptToFile(bs, dc.document.data["imagePath"].toString() + ".enc", context)
                AppDatabase.getInstance(context).picDao().insert(Pic(dc.document.data["imagePath"].toString() + ".enc", photoTimestamp?.toDate()?.time))
            }
        }
    }

    companion object {
        private class FetchDatabaseAsyncTask(private val picRepository: MainViewModel) : AsyncTask<Context, Void, Long>() {
            override fun doInBackground(vararg context: Context): Long {
                val mDb = AppDatabase.getInstance(context[0])
                val lastRawList = mDb!!.picDao().lastRow
                if (lastRawList.isEmpty())
                    picRepository.nextTimestamp = 0
                else {
                    picRepository.nextTimestamp = lastRawList[0]
                }
                return picRepository.nextTimestamp
            }

            override fun onPostExecute(result: Long) {
                if (result.toString().equals("0")) {
                    picRepository.fetchRef(picRepository.baseQuery)
                } else {
                    picRepository.fetchRef(picCollection.orderBy("timestamp", Query.Direction.ASCENDING).startAfter(Date(result)))
                }
            }
        }
    }

}
