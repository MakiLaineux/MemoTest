package com.technoprimates.memotest;

import android.app.Application;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.technoprimates.memotest.db.Code;
import com.technoprimates.memotest.db.CodeRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private CodeRepository repository;
    private LiveData<List<Code>> allcodes;  // used by RV
    private MutableLiveData<List<Code>> searchresults;
    private Code currentCode; // Code currently selected or displayed
//    private List<Code> currentCodeList;

    public MainViewModel(Application application) {
        super(application);
        repository = new CodeRepository(application);
        allcodes = repository.getAllCodes();
        searchresults = repository.getSearchResults();
        currentCode = null;
    }

    // Methods called by UI controllers
    public MutableLiveData<List<Code>> getSearchresults() {return searchresults;}
    public LiveData<List<Code>> getAllcodes() {return allcodes;}
    public void findCode(String name) {repository.findCode(name);}
    public void deleteCode(String name) {repository.deleteCode(name);}
    public void setCurrentCode(Code code) {currentCode = code;}
    public Code getCurrentCode() {return currentCode;}
    public void insertCode(Code c) {
        // before inserting, set the UodateDay value in format dd-MM-yyyy
        c.setCodeUpdateDay(DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString());
        repository.insertCode(c);
    }

    // check that a Code matching the given name does not already exists in current code list
    @Nullable
    public boolean checkCodeName(@NonNull String name) {
        List<Code> currentCodeList;
        if (allcodes == null)
            return true;
        else
            currentCodeList = allcodes.getValue();
        if (currentCodeList == null) return true;
        for (Code c : currentCodeList) {
            if (c.getCodeName().equals(name)) {
                return false;
            }
        }
        return true; // no match found
    }


    // search first Code matching given "dbId" in current code list
    @Nullable
    public Code getCodeWithId(int dbId){
        List<Code> currentCodeList;
        if (allcodes == null)
            return null;
        else
            currentCodeList = allcodes.getValue();
        if (currentCodeList == null)
            return null;
        for (Code c : currentCodeList) {
            if (c.getCodeId() == dbId) {
                return c;
            }
        }
        return null;
    }
}