package cn.chinafst.dyquickcheckdevice.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class SampleItemAndStand {

    /**
     * checkId : 000cdc68d2644a7ebbd61f15f308d2c0
     * checkSign : ≤
     * checkValueUnit : mg/kg
     * foodPCode : 00001001100010001
     * itemName : 苋菜红
     * sampleName : 流质糖果
     * sampleNum : 000010011000100010011
     * standardName : GB 2760-2014
     * standardValue : 50.0
     * uDate : 2017-09-30 15:16:33.0
     */
    @Id(autoincrement = true)
    private Long ids;
    private String checkId;
    private String checkSign;
    private String checkValueUnit;
    private String foodPCode;
    private String itemName;
    private String sampleName;
    private String sampleNum;
    private String standardName;
    private String standardValue;
    private String uDate;


    @Generated(hash = 2057984883)
    public SampleItemAndStand(Long ids, String checkId, String checkSign,
            String checkValueUnit, String foodPCode, String itemName,
            String sampleName, String sampleNum, String standardName,
            String standardValue, String uDate) {
        this.ids = ids;
        this.checkId = checkId;
        this.checkSign = checkSign;
        this.checkValueUnit = checkValueUnit;
        this.foodPCode = foodPCode;
        this.itemName = itemName;
        this.sampleName = sampleName;
        this.sampleNum = sampleNum;
        this.standardName = standardName;
        this.standardValue = standardValue;
        this.uDate = uDate;
    }

    @Generated(hash = 60936105)
    public SampleItemAndStand() {
    }


    public Long getIds() {
        return ids;
    }

    public void setIds(Long ids) {
        this.ids = ids;
    }

    public String getuDate() {
        return uDate;
    }

    public void setuDate(String uDate) {
        this.uDate = uDate;
    }
    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getCheckSign() {
        return checkSign;
    }

    public void setCheckSign(String checkSign) {
        this.checkSign = checkSign;
    }

    public String getCheckValueUnit() {
        return checkValueUnit;
    }

    public void setCheckValueUnit(String checkValueUnit) {
        this.checkValueUnit = checkValueUnit;
    }

    public String getFoodPCode() {
        return foodPCode;
    }

    public void setFoodPCode(String foodPCode) {
        this.foodPCode = foodPCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public String getSampleNum() {
        return sampleNum;
    }

    public void setSampleNum(String sampleNum) {
        this.sampleNum = sampleNum;
    }

    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    public String getStandardValue() {
        return standardValue;
    }

    public void setStandardValue(String standardValue) {
        this.standardValue = standardValue;
    }

    public String getUDate() {
        return uDate;
    }

    public void setUDate(String uDate) {
        this.uDate = uDate;
    }
}
