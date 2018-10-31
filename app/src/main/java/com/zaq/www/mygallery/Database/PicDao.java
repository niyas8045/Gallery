package com.zaq.www.mygallery.Database;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PicDao {

    @Query("SELECT DISTINCT pic_path FROM pic ORDER BY timestamp")
    LiveData<List<String>> getAllPics();

    @Query("SELECT timestamp FROM pic ORDER BY timestamp DESC LIMIT 1")
    List<Long> getLastRow();

    @Insert
    void insert(Pic... pics);

    @Update
    void update(Pic... pics);

    @Delete
    void delete(Pic... pics);
}
