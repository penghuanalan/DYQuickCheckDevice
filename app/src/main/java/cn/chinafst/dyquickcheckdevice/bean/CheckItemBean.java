package cn.chinafst.dyquickcheckdevice.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CheckItemBean {


    /**
     * checkSign : ≤
     * checkValueUnit : ppb
     * detectCode : 000011
     * detectName : 巴比妥
     * standardName : 《食品安全法》
     * standardValue : 0.0
     * uDate : 2017-09-30 15:03:57
     */

    @Id(autoincrement = true)
    private Long ids;

    public Long getIds() {
        return ids;
    }


    private String checkSign;
    private String checkValueUnit;
    private String detectCode;
    private String detectName;
    private String standardName;
    private String standardValue;
    private String uDate;

    @Generated(hash = 1442984683)
    public CheckItemBean(Long ids, String checkSign, String checkValueUnit,
            String detectCode, String detectName, String standardName,
            String standardValue, String uDate) {
        this.ids = ids;
        this.checkSign = checkSign;
        this.checkValueUnit = checkValueUnit;
        this.detectCode = detectCode;
        this.detectName = detectName;
        this.standardName = standardName;
        this.standardValue = standardValue;
        this.uDate = uDate;
    }

    @Generated(hash = 1394073385)
    public CheckItemBean() {
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

    public String getDetectCode() {
        return detectCode;
    }

    public void setDetectCode(String detectCode) {
        this.detectCode = detectCode;
    }

    public String getDetectName() {
        return detectName;
    }

    public void setDetectName(String detectName) {
        this.detectName = detectName;
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

    public void setIds(Long ids) {
        this.ids = ids;
    }

    public String getuDate() {
        return uDate;
    }

    public void setuDate(String uDate) {
        this.uDate = uDate;
    }
}
