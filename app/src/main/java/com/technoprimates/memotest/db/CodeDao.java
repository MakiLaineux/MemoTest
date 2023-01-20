package com.technoprimates.memotest.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CodeDao {

    @Insert
    void insertCode(Code code);

    @Query("SELECT * FROM codes WHERE cName = :name")
    List<Code> findCode(String name);

    @Query("SELECT * FROM codes")
    LiveData<List<Code>> getAllCodes();

    @Query("DELETE FROM codes WHERE cName = :name")
    void deleteCode(String name);

    @Update
    void updateCode(Code code);
}
