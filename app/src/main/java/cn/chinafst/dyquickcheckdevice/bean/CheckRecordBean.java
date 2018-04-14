package cn.chinafst.dyquickcheckdevice.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/9.
 */
@Entity
public class CheckRecordBean implements Serializable{


    /**
     * id : 402893195ecd0012132324210911
     * ids : 11
     * check_accord : 标准3.2
     * limit_value :
     * check_result : 12.5
     * check_unit : <
     * conclusion : 不合格
     * check_date : 2017-09-29 16:00:00
     * check_code : 5
     * check_username : 小米4
     * auditor_name : 审核人名称4
     * upload_name : 上报人名称5
     * upload_date : 2017-09-29 16:00:00
     * task_name : 士大夫多少
     * depart_id : 2
     * depart_name : 天河区食药监局
     * device_model : 检测方法3
     * device_method :
     * device_company : 厂家3
     * data_source : 1
     * point_id : 26
     * item_id : 2
     * item_name : 重金属
     * food_name : 辣椒
     * point_name : 天河1
     * task_id : 402893b65e1980fe015e1980fe900000
     * sampling_id : 5
     * sampling_detail_id : 5
     * food_type_id : f9e4d1a192bb4e59b6c116ed221e042a
     * food_type_name : 香辛料类
     * food_id : 50862012f92f4184ba2b4742818a867f
     * reg_id : 4028935f5e4b9322015e4b9322310000
     * reg_name :
     * reg_user_id :
     * reg_user_name : 火村农贸市场
     * check_userid : 4
     * auditor_id : 4
     * upload_id : 5
     * check_accord_id :
     * device_id : 2
     * device_name : LZ-7000农药残留快速测试仪
     * reload_flag : 0
     * status_falg : 0
     * create_by : 3
     * create_date : 2017-09-30 09:27:29
     * update_by : 3
     * update_date : 2017-09-30 09:27:29
     * remark :
     * param1 :
     * param2 :
     * param3 :
     * sampling_no : A20170619002
     * reg_licence : 43242K743A
     * reg_link_phone : 25000000001
     * ope_shop_code : A01
     * ope_phone : 15000000002
     * reg_link_person : 大曹操
     * ope_shop_name : 放心菜店
     * sampling_date : 2017-06-19 01:00:00
     * sample_number : 11.3
     * purchase_amount : 20.8
     * purchase_date : 2017-07-12 15:31:14
     * origin : 产地f
     * supplier : 供应商xxx
     * supplier_address : 供货地址：白宫
     * supplier_phone : 进货电话：1008612
     * supplier_person : 进货人：秦始皇22
     * batch_number : 批号：20170711A121
     * sample_code : 样品编号5
     */
    private static final long serialVersionUID = 1L;
    @Unique
    private String id;
    @Id(autoincrement = true)
    private Long ids;
    private String check_accord;
    private String limit_value;
    private String check_result;
    private String check_unit;
    private String conclusion;
    private String check_date;
    private String check_code;
    private String check_username;
    private String auditor_name;
    private String upload_name;
    private String upload_date;
    private String task_name;
    private String depart_id;
    private String depart_name;
    private String device_model;
    private String device_method;
    private String device_company;
    private String data_source;
    private String point_id;
    private String item_id;
    private String item_name;
    private String food_name;
    private String point_name;
    private String task_id;
    private String sampling_id;
    private String sampling_detail_id;
    private String food_type_id;
    private String food_type_name;
    private String food_id;
    private String reg_id;
    private String reg_name;
    private String reg_user_id;
    private String reg_user_name;
    private String check_userid;
    private String auditor_id;
    private String upload_id;
    private String check_accord_id;
    private String device_id;
    private String device_name;
    private String reload_flag;
    private String status_falg;
    private String create_by;
    private String create_date;
    private String update_by;
    private String update_date;
    private String remark;
    private String param1;
    private String param2;
    private String param3;
    private String sampling_no;
    private String reg_licence;
    private String reg_link_phone;
    private String ope_shop_code;
    private String ope_phone;
    private String reg_link_person;
    private String ope_shop_name;
    private String sampling_date;
    private String sample_number;
    private String purchase_amount;
    private String purchase_date;
    private String origin;
    private String supplier;
    private String supplier_address;
    private String supplier_phone;
    private String supplier_person;
    private String batch_number;
    private String sample_code;

    @Generated(hash = 1098826185)
    public CheckRecordBean(String id, Long ids, String check_accord,
                           String limit_value, String check_result, String check_unit,
                           String conclusion, String check_date, String check_code,
                           String check_username, String auditor_name, String upload_name,
                           String upload_date, String task_name, String depart_id,
                           String depart_name, String device_model, String device_method,
                           String device_company, String data_source, String point_id,
                           String item_id, String item_name, String food_name, String point_name,
                           String task_id, String sampling_id, String sampling_detail_id,
                           String food_type_id, String food_type_name, String food_id,
                           String reg_id, String reg_name, String reg_user_id,
                           String reg_user_name, String check_userid, String auditor_id,
                           String upload_id, String check_accord_id, String device_id,
                           String device_name, String reload_flag, String status_falg,
                           String create_by, String create_date, String update_by,
                           String update_date, String remark, String param1, String param2,
                           String param3, String sampling_no, String reg_licence,
                           String reg_link_phone, String ope_shop_code, String ope_phone,
                           String reg_link_person, String ope_shop_name, String sampling_date,
                           String sample_number, String purchase_amount, String purchase_date,
                           String origin, String supplier, String supplier_address,
                           String supplier_phone, String supplier_person, String batch_number,
                           String sample_code) {
        this.id = id;
        this.ids = ids;
        this.check_accord = check_accord;
        this.limit_value = limit_value;
        this.check_result = check_result;
        this.check_unit = check_unit;
        this.conclusion = conclusion;
        this.check_date = check_date;
        this.check_code = check_code;
        this.check_username = check_username;
        this.auditor_name = auditor_name;
        this.upload_name = upload_name;
        this.upload_date = upload_date;
        this.task_name = task_name;
        this.depart_id = depart_id;
        this.depart_name = depart_name;
        this.device_model = device_model;
        this.device_method = device_method;
        this.device_company = device_company;
        this.data_source = data_source;
        this.point_id = point_id;
        this.item_id = item_id;
        this.item_name = item_name;
        this.food_name = food_name;
        this.point_name = point_name;
        this.task_id = task_id;
        this.sampling_id = sampling_id;
        this.sampling_detail_id = sampling_detail_id;
        this.food_type_id = food_type_id;
        this.food_type_name = food_type_name;
        this.food_id = food_id;
        this.reg_id = reg_id;
        this.reg_name = reg_name;
        this.reg_user_id = reg_user_id;
        this.reg_user_name = reg_user_name;
        this.check_userid = check_userid;
        this.auditor_id = auditor_id;
        this.upload_id = upload_id;
        this.check_accord_id = check_accord_id;
        this.device_id = device_id;
        this.device_name = device_name;
        this.reload_flag = reload_flag;
        this.status_falg = status_falg;
        this.create_by = create_by;
        this.create_date = create_date;
        this.update_by = update_by;
        this.update_date = update_date;
        this.remark = remark;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.sampling_no = sampling_no;
        this.reg_licence = reg_licence;
        this.reg_link_phone = reg_link_phone;
        this.ope_shop_code = ope_shop_code;
        this.ope_phone = ope_phone;
        this.reg_link_person = reg_link_person;
        this.ope_shop_name = ope_shop_name;
        this.sampling_date = sampling_date;
        this.sample_number = sample_number;
        this.purchase_amount = purchase_amount;
        this.purchase_date = purchase_date;
        this.origin = origin;
        this.supplier = supplier;
        this.supplier_address = supplier_address;
        this.supplier_phone = supplier_phone;
        this.supplier_person = supplier_person;
        this.batch_number = batch_number;
        this.sample_code = sample_code;
    }

    @Generated(hash = 724057123)
    public CheckRecordBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getIds() {
        return ids;
    }

    public void setIds(Long ids) {
        this.ids = ids;
    }

    public String getCheck_accord() {
        return check_accord;
    }

    public void setCheck_accord(String check_accord) {
        this.check_accord = check_accord;
    }

    public String getLimit_value() {
        return limit_value;
    }

    public void setLimit_value(String limit_value) {
        this.limit_value = limit_value;
    }

    public String getCheck_result() {
        return check_result;
    }

    public void setCheck_result(String check_result) {
        this.check_result = check_result;
    }

    public String getCheck_unit() {
        return check_unit;
    }

    public void setCheck_unit(String check_unit) {
        this.check_unit = check_unit;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getCheck_date() {
        return check_date;
    }

    public void setCheck_date(String check_date) {
        this.check_date = check_date;
    }

    public String getCheck_code() {
        return check_code;
    }

    public void setCheck_code(String check_code) {
        this.check_code = check_code;
    }

    public String getCheck_username() {
        return check_username;
    }

    public void setCheck_username(String check_username) {
        this.check_username = check_username;
    }

    public String getAuditor_name() {
        return auditor_name;
    }

    public void setAuditor_name(String auditor_name) {
        this.auditor_name = auditor_name;
    }

    public String getUpload_name() {
        return upload_name;
    }

    public void setUpload_name(String upload_name) {
        this.upload_name = upload_name;
    }

    public String getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getDepart_id() {
        return depart_id;
    }

    public void setDepart_id(String depart_id) {
        this.depart_id = depart_id;
    }

    public String getDepart_name() {
        return depart_name;
    }

    public void setDepart_name(String depart_name) {
        this.depart_name = depart_name;
    }

    public String getDevice_model() {
        return device_model;
    }

    public void setDevice_model(String device_model) {
        this.device_model = device_model;
    }

    public String getDevice_method() {
        return device_method;
    }

    public void setDevice_method(String device_method) {
        this.device_method = device_method;
    }

    public String getDevice_company() {
        return device_company;
    }

    public void setDevice_company(String device_company) {
        this.device_company = device_company;
    }

    public String getData_source() {
        return data_source;
    }

    public void setData_source(String data_source) {
        this.data_source = data_source;
    }

    public String getPoint_id() {
        return point_id;
    }

    public void setPoint_id(String point_id) {
        this.point_id = point_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getPoint_name() {
        return point_name;
    }

    public void setPoint_name(String point_name) {
        this.point_name = point_name;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getSampling_id() {
        return sampling_id;
    }

    public void setSampling_id(String sampling_id) {
        this.sampling_id = sampling_id;
    }

    public String getSampling_detail_id() {
        return sampling_detail_id;
    }

    public void setSampling_detail_id(String sampling_detail_id) {
        this.sampling_detail_id = sampling_detail_id;
    }

    public String getFood_type_id() {
        return food_type_id;
    }

    public void setFood_type_id(String food_type_id) {
        this.food_type_id = food_type_id;
    }

    public String getFood_type_name() {
        return food_type_name;
    }

    public void setFood_type_name(String food_type_name) {
        this.food_type_name = food_type_name;
    }

    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public String getReg_id() {
        return reg_id;
    }

    public void setReg_id(String reg_id) {
        this.reg_id = reg_id;
    }

    public String getReg_name() {
        return reg_name;
    }

    public void setReg_name(String reg_name) {
        this.reg_name = reg_name;
    }

    public String getReg_user_id() {
        return reg_user_id;
    }

    public void setReg_user_id(String reg_user_id) {
        this.reg_user_id = reg_user_id;
    }

    public String getReg_user_name() {
        return reg_user_name;
    }

    public void setReg_user_name(String reg_user_name) {
        this.reg_user_name = reg_user_name;
    }

    public String getCheck_userid() {
        return check_userid;
    }

    public void setCheck_userid(String check_userid) {
        this.check_userid = check_userid;
    }

    public String getAuditor_id() {
        return auditor_id;
    }

    public void setAuditor_id(String auditor_id) {
        this.auditor_id = auditor_id;
    }

    public String getUpload_id() {
        return upload_id;
    }

    public void setUpload_id(String upload_id) {
        this.upload_id = upload_id;
    }

    public String getCheck_accord_id() {
        return check_accord_id;
    }

    public void setCheck_accord_id(String check_accord_id) {
        this.check_accord_id = check_accord_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getReload_flag() {
        return reload_flag;
    }

    public void setReload_flag(String reload_flag) {
        this.reload_flag = reload_flag;
    }

    public String getStatus_falg() {
        return status_falg;
    }

    public void setStatus_falg(String status_falg) {
        this.status_falg = status_falg;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(String update_by) {
        this.update_by = update_by;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getSampling_no() {
        return sampling_no;
    }

    public void setSampling_no(String sampling_no) {
        this.sampling_no = sampling_no;
    }

    public String getReg_licence() {
        return reg_licence;
    }

    public void setReg_licence(String reg_licence) {
        this.reg_licence = reg_licence;
    }

    public String getReg_link_phone() {
        return reg_link_phone;
    }

    public void setReg_link_phone(String reg_link_phone) {
        this.reg_link_phone = reg_link_phone;
    }

    public String getOpe_shop_code() {
        return ope_shop_code;
    }

    public void setOpe_shop_code(String ope_shop_code) {
        this.ope_shop_code = ope_shop_code;
    }

    public String getOpe_phone() {
        return ope_phone;
    }

    public void setOpe_phone(String ope_phone) {
        this.ope_phone = ope_phone;
    }

    public String getReg_link_person() {
        return reg_link_person;
    }

    public void setReg_link_person(String reg_link_person) {
        this.reg_link_person = reg_link_person;
    }

    public String getOpe_shop_name() {
        return ope_shop_name;
    }

    public void setOpe_shop_name(String ope_shop_name) {
        this.ope_shop_name = ope_shop_name;
    }

    public String getSampling_date() {
        return sampling_date;
    }

    public void setSampling_date(String sampling_date) {
        this.sampling_date = sampling_date;
    }

    public String getSample_number() {
        return sample_number;
    }

    public void setSample_number(String sample_number) {
        this.sample_number = sample_number;
    }

    public String getPurchase_amount() {
        return purchase_amount;
    }

    public void setPurchase_amount(String purchase_amount) {
        this.purchase_amount = purchase_amount;
    }

    public String getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(String purchase_date) {
        this.purchase_date = purchase_date;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getSupplier_address() {
        return supplier_address;
    }

    public void setSupplier_address(String supplier_address) {
        this.supplier_address = supplier_address;
    }

    public String getSupplier_phone() {
        return supplier_phone;
    }

    public void setSupplier_phone(String supplier_phone) {
        this.supplier_phone = supplier_phone;
    }

    public String getSupplier_person() {
        return supplier_person;
    }

    public void setSupplier_person(String supplier_person) {
        this.supplier_person = supplier_person;
    }

    public String getBatch_number() {
        return batch_number;
    }

    public void setBatch_number(String batch_number) {
        this.batch_number = batch_number;
    }

    public String getSample_code() {
        return sample_code;
    }

    public void setSample_code(String sample_code) {
        this.sample_code = sample_code;
    }
}
