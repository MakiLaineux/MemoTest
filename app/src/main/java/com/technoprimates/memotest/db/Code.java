package com.technoprimates.memotest.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "codes")
public class Code {

    public static final int FINGERPRINT_PROTECTED = 1;
    public static final int NOT_FINGERPRINT_PROTECTED = 2;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="cId")
    private int codeId;

    @ColumnInfo(name="cName")
    private String codeName;

    @ColumnInfo(name="cValue")
    private String codeValue;

    @ColumnInfo(name="cCategory")
    private String codeCategory;

    @ColumnInfo(name="cUpdateDay")
    private String codeUpdateDay;

    @ColumnInfo(name="cProtectMode")
    private int codeProtectMode;


    // Code constructor
    // codeUpdateDay member is set to an empty String, it will be modified when inserting in database
    // ProtectMode is set by default to NOT_FINGERPRINT_PROTECTED
    public Code(String codeName, String codeValue, String codeCategory, int codeProtectMode) {
        this.codeName = codeName;
        this.codeValue=codeValue;
        this.codeCategory = codeCategory;
        this.codeUpdateDay = "";
        this.codeProtectMode = codeProtectMode;
    }

    public int getCodeId() {return this.codeId;}
    public String getCodeName() {return this.codeName;}
    public String getCodeValue() {return this.codeValue;}
    public String getCodeCategory() {return this.codeCategory;}
    public String getCodeUpdateDay() {return this.codeUpdateDay;}
    public int getCodeProtectMode() {return this.codeProtectMode;}

    public void setCodeId(int codeId) {this.codeId = codeId;}
    public void setName(String codeName) {this.codeName = codeName;}
    public void setCodeValue(String codeValue) {this.codeValue = codeValue;}
    public void setCodeCategory(String codeCategory) {this.codeCategory = codeCategory;}
    public void setCodeUpdateDay(String codeUpdateDay) {this.codeUpdateDay = codeUpdateDay;}
    public void setCodeProtectMode(int protectMode) {this.codeProtectMode = protectMode;}
}
