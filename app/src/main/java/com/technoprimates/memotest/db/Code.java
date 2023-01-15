package com.technoprimates.memotest.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "codes")
public class Code {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="cId")
    private int codeId;

    @ColumnInfo(name="cName")
    private String codeName;

    private String codeValue;
    private String codeCategory;


    public Code(String codeName, String codeValue, String codeCategory) {
        this.codeName = codeName;
        this.codeValue=codeValue;
        this.codeCategory = codeCategory;
    }

    public int getCodeId() {return this.codeId;}
    public String getCodeName() {return this.codeName;}
    public String getCodeValue() {return this.codeValue;}
    public String getCodeCategory() {return this.codeCategory;}

    public void setCodeId(int codeId) {this.codeId = codeId;}
    public void setName(String codeName) {this.codeName = codeName;}
    public void setCodeValue(String codeValue) {this.codeName = codeValue;}
    public void setCodeCategory(String codeCategory) {this.codeName = codeCategory;}

}
