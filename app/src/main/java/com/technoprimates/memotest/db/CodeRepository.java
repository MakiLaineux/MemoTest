package com.technoprimates.memotest.db;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CodeRepository {

    // LiveData for the list of codes used by the RecyclerView, initialized in constructor
    private final LiveData<List<Code>> allCodes;

    // LiveData for search (why livedata?)
    private final MutableLiveData<List<Code>> searchResults = new MutableLiveData<>();

    // result of Code select queries
    private List<Code> results;

    // instance of Dao
    private final CodeDao codeDao;

    // a Handler receiving messages from the dao methods threads when a query has produced results
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            searchResults.setValue(results);
        }
    };

    // Constructor
    public CodeRepository(Application application) {
        CodeRoomDatabase db = CodeRoomDatabase.getDatabase(application);
        codeDao = db.codeDao();
        allCodes = codeDao.getAllCodes();
    }

    // calls of DAO methods in separate threads
    public void insertCode(@NonNull Code c) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            codeDao.insertCode(c);
        });
        executor.shutdown();
    }

    public void deleteCode(@NonNull String name) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            codeDao.deleteCode(name);
        });
        executor.shutdown();
    }

    public void findCode(@NonNull String name) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            results = codeDao.findCode(name);
            handler.sendEmptyMessage(0);
        });
        executor.shutdown();
    }

    public void updateCode(@NonNull Code code) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            codeDao.updateCode(code);
            handler.sendEmptyMessage(0);
        });
        executor.shutdown();
    }

    // get reference to LiveData object
    public LiveData<List<Code>> getAllCodes() {
        return allCodes;
    }

    public MutableLiveData<List<Code>> getSearchResults() {
        return searchResults;
    }

}
