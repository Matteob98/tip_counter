package com.italianswapp.tipcounter.OfflineDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TipDao {

    @Query("Select * " +
            "from Tip " +
            "order by date desc")
    List<Tip> getAll();

    @Query("Select *" +
            " From Tip" +
            " where Tip.date between :start and :end" +
            " order by date desc")
    List<Tip> getBetweenDate(long start, long end);

    @Query("Select ROUND(AVG(value),2) " +
            "from Tip")
    float getAvgTip();

    @Insert
    void insertAll(Tip... tips);

    @Delete
    void delete(Tip tip);
}
