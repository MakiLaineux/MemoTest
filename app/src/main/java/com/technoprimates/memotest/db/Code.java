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

    @ColumnInfo(name="cComments")
    private String codeComments;

    @ColumnInfo(name="cUpdateDay")
    private String codeUpdateDay;

    @ColumnInfo(name="cProtectMode")
    private int codeProtectMode;


    // Code constructor
    // codeUpdateDay member is set to an empty String, it will be modified when inserting in or updating database
    // this constructor does not set the database Id (codeId)
    public Code(String codeName, String codeValue, String codeCategory, String codeComments, int codeProtectMode) {
        this.codeName = codeName;
        this.codeValue=codeValue;
        this.codeCategory = codeCategory;
        this.codeComments = codeComments;
        this.codeUpdateDay = "";
        this.codeProtectMode = codeProtectMode;
    }

    // prepare code for updating
    public void copyUIFields(Code c) {
        this.codeName = c.codeName;
        this.codeValue = c.codeValue;
        this.codeCategory = c.codeCategory;
        this.codeComments = c.codeComments;
        this.codeProtectMode = c.codeProtectMode;
    }

    public int getCodeId() {return this.codeId;}
    public String getCodeName() {return this.codeName;}
    public String getCodeValue() {return this.codeValue;}
    public String getCodeCategory() {return this.codeCategory;}
    public String getCodeComments() {return this.codeComments;}
    public String getCodeUpdateDay() {return this.codeUpdateDay;}
    public int getCodeProtectMode() {return this.codeProtectMode;}

    public void setCodeId(int codeId) {this.codeId = codeId;}
    public void setCodename(String codeName) {this.codeName = codeName;}
    public void setCodeValue(String codeValue) {this.codeValue = codeValue;}
    public void setCodeCategory(String codeCategory) {this.codeCategory = codeCategory;}
    public void setCodeComments(String codeComments) {this.codeComments = codeComments;}
    public void setCodeUpdateDay(String codeUpdateDay) {this.codeUpdateDay = codeUpdateDay;}
    public void setCodeProtectMode(int protectMode) {this.codeProtectMode = protectMode;}
}
