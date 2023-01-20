package com.technoprimates.memotest.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Code.class}, version = 1)
public abstract class CodeRoomDatabase extends RoomDatabase {
    public abstract CodeDao codeDao();
    private static CodeRoomDatabase INSTANCE;

    static CodeRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CodeRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CodeRoomDatabase.class, "code-database").build();
                }
            }
        }
        return INSTANCE;
    }
}
