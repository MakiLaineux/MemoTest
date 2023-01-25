package com.technoprimates.memotest;

import android.app.Application;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.technoprimates.memotest.db.Code;
import com.technoprimates.memotest.db.CodeRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final CodeRepository repository;
    private final LiveData<List<Code>> allcodes;  // used by RV
    private final MutableLiveData<List<Code>> searchresults;

    // Code currently selected or displayed
    private Code currentCode;

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
    public void deleteCode(String name) {repository.deleteCode(name);}
    public void setCurrentCode(Code code) {currentCode = code;}
    public Code getCurrentCode() {return currentCode;}


    public void updateCode(Code code) {
        // currentCode has the dbId to update, fill the other fields with user inputs
        currentCode.copyUIFields(code);
        // before updating, set the UpdateDay value in format dd-MM-yyyy
        currentCode.setCodeUpdateDay(DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString());
        repository.updateCode(currentCode);
    }

    public void insertCode(Code c) {
        currentCode = c;
        // before inserting, set the UpdateDay value in format dd-MM-yyyy
        currentCode.setCodeUpdateDay(DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString());
        repository.insertCode(c);
    }


    // check that a Code matching the name of the given Code does not already exists in current code list
    // returns true if the name is NOT in the list
    public boolean codenameAlreadyExists(@NonNull Code code) {

        if ((allcodes == null) || (allcodes.getValue() == null))
            return false;

        for (Code c : allcodes.getValue()) {
            if (c.getCodeName().equals(code.getCodeName())) {
                return true;
            }
        }
        return false; // no match found
    }
}