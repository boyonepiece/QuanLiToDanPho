package sample.Entity;

import javafx.scene.control.CheckBox;

public class DonPhanAnh {
    private String name;
    private String address;
    private String phoneNumber;
    private String date;
    private String classify;
    private String chiTiet;
    private String idPetition;
    private boolean state;
    private int stt;
    private CheckBox remark ;

    public DonPhanAnh(String name, String address, String phoneNumber, String date,
                      String classify, String chiTiet,int stt,boolean state) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.classify = classify;
        this.chiTiet = chiTiet;
        this.stt = stt;
        this.state = state;
        this.remark = new CheckBox();
        this.idPetition = idPetition;
    }

    public String getIdPetition() {
        return idPetition;
    }

    public void setIdPetition(String idPetition) {
        this.idPetition = idPetition;
    }

    public boolean isState() {
        return state;
    }

    public CheckBox getRemark() {
        return remark;
    }

    public void setRemark(CheckBox remark) {
        this.remark = remark;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getChiTiet() {
        return chiTiet;
    }

    public void setChiTiet(String chiTiet) {
        this.chiTiet = chiTiet;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }


}
