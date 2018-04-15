package cn.chinafst.dyquickcheckdevice.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SampleFoodBean {


    /**
     * foodCode : 00001
     * foodId : 6C073C67B79A78F5474B5D166F4D2BD4
     * foodName : 样品
     * foodPCode :
     * isParent : 0
     * udate : 2017-09-28 11:40:53
     */

    @Id(autoincrement = true)
    private Long ids;
    private String foodCode;
    private String foodId;
    private String foodName;
    private String foodPCode;
    private int isParent;
    private String udate;

    @Generated(hash = 793779472)
    public SampleFoodBean(Long ids, String foodCode, String foodId, String foodName,
            String foodPCode, int isParent, String udate) {
        this.ids = ids;
        this.foodCode = foodCode;
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPCode = foodPCode;
        this.isParent = isParent;
        this.udate = udate;
    }

    @Generated(hash = 1982187811)
    public SampleFoodBean() {
    }

    public Long getIds() {
        return ids;
    }

    public void setIds(Long ids) {
        this.ids = ids;
    }

    public String getFoodCode() {
        return foodCode;
    }

    public void setFoodCode(String foodCode) {
        this.foodCode = foodCode;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPCode() {
        return foodPCode;
    }

    public void setFoodPCode(String foodPCode) {
        this.foodPCode = foodPCode;
    }

    public int getIsParent() {
        return isParent;
    }

    public void setIsParent(int isParent) {
        this.isParent = isParent;
    }

    public String getUdate() {
        return udate;
    }

    public void setUdate(String udate) {
        this.udate = udate;
    }
}
