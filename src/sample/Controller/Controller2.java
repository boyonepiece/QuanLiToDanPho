package sample.Controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sample.Database;
import sample.Entity.DonPhanAnh;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Controller2 {

    @FXML
    private TextArea chiTiet;

    @FXML
    private AnchorPane ds_MoiNhan;
    //donNop
    @FXML
    private TextField hoTen, diaChi, SDT1,SDT2, noiDungKhac, Year;
    @FXML
    PasswordField cmt1,cmt2;
    @FXML
    private Group groupNopDon;
    @FXML
    private StackPane stackPane;
    @FXML
    private ScrollPane donNop;
    @FXML
    private RadioButton radioAnNinh,radioCoSo,radioQuyDinh,radioKhac;
    @FXML
    private DatePicker ngaySinh,ngayNop,ngayNop2;
    @FXML
    private TableColumn<DonPhanAnh,String> colName,
            colAddress,colPhone,colDate,colClassify,colNoiDung;

    @FXML
    private TableColumn<DonPhanAnh,String> colName1,
            colAddress1,colPhone1,colDate1,colClassify1,colNoiDung1;
    @FXML
    private TableColumn<DonPhanAnh,Boolean>colState1;
    @FXML
    private TableColumn<DonPhanAnh,Integer> colSTT1;
    @FXML
    private TableColumn<DonPhanAnh,String> colName2,
            colAddress2,colPhone2,colDate2,colClassify2,colNoiDung2;
    @FXML
    private TableColumn<DonPhanAnh,Boolean>colState2;
    @FXML
    private TableColumn<DonPhanAnh,Integer> colSTT2;
    @FXML
    private TableColumn<DonPhanAnh,Boolean>colState;
    @FXML
    private TableColumn<DonPhanAnh,Integer> colSTT;
    @FXML
    private TableView tableNewList;
    @FXML
    private TableView tableWaitingList;
    @FXML
    private TableView tableViewQuarterOfYear;
    @FXML
    private Label allDon;
    @FXML
    private Label allDon1;
    @FXML
    private Label allDon2;
    @FXML
    private CheckBox selectAllDonNop;


    //
    @FXML
    private AnchorPane ds_XuLi;
    @FXML
    private AnchorPane timKiem;
    @FXML
    private AnchorPane thongKe_Quy;

    @FXML
    public ComboBox quy;
    @FXML
    public ComboBox tim_Noidung;
    @FXML
    public ComboBox tim_Trangthai;
    @FXML
    public ComboBox<String> Tim_TrangthaiQuy;
    @FXML
    private TextField hoten;
    @FXML
    private TextField sdt;
    @FXML
    private TextField ngay;
    @FXML
    private TableView tableSearch;
    @FXML
    private TableColumn<DonPhanAnh,String> hoten_search,
            diachi_search,sdt_search,ngay_search,noidung_search,chitiet_search;
    @FXML
    private TableColumn<DonPhanAnh, Boolean> trangthai_search;
    @FXML
    private TableColumn<DonPhanAnh, Integer> stt_search;

    ObservableList<Integer> list_quy = FXCollections.observableArrayList(1, 2, 3, 4);
    ObservableList<String> list_Noidung = FXCollections.observableArrayList("An ninh, trật tự", "Cơ sở hạ tầng", "Quy định, quy chế", "Khác...");
    ObservableList<String> list_Trangthai = FXCollections.observableArrayList("Mới ghi nhận", "Chưa giải quyết", "Đã giải quyết");

    //button Nộp đơn
    public void button_donNop(ActionEvent e){
        this.donNop.setVisible(true);
        this.ds_MoiNhan.setVisible(false);
        this.ds_XuLi.setVisible(false);
        this.timKiem.setVisible(false);
        this.thongKe_Quy.setVisible(false);
    }

    //button danh sách các đơn mới ghi nhận
    public void button_ds_MoiNhan() throws SQLException {
        this.donNop.setVisible(false);
        this.ds_MoiNhan.setVisible(true);
        this.ds_XuLi.setVisible(false);
        this.timKiem.setVisible(false);
        this.thongKe_Quy.setVisible(false);

        donMoiGhiNhan();
    }

    //button danh sách đơn đang xử lí
    public void button_ds_XuLi(ActionEvent e) throws SQLException {
        this.donNop.setVisible(false);
        this.ds_MoiNhan.setVisible(false);
        this.ds_XuLi.setVisible(true);
        this.timKiem.setVisible(false);
        this.thongKe_Quy.setVisible(false);

        donDangXuli();
    }

    //button tìm kiếm
    public void button_timKiem(ActionEvent e){
        this.donNop.setVisible(false);
        this.ds_MoiNhan.setVisible(false);
        this.ds_XuLi.setVisible(false);
        this.timKiem.setVisible(true);
        this.thongKe_Quy.setVisible(false);
        tim_Noidung.setItems(list_Noidung);
        tim_Trangthai.setItems(list_Trangthai);
    }

    //button danh sách quý
    public void button_thongKe_Quy(ActionEvent e){
        this.donNop.setVisible(false);
        this.ds_MoiNhan.setVisible(false);
        this.ds_XuLi.setVisible(false);
        this.timKiem.setVisible(false);
        this.thongKe_Quy.setVisible(true);
        quy.setItems(list_quy);
        Tim_TrangthaiQuy.setItems(list_Trangthai);

    }
    public void buttonShow(ActionEvent e) throws SQLException {
        int QuarterOfYear = (int)quy.getValue();
        String value = Tim_TrangthaiQuy.getValue();
        int table;
        if (value.equals("Mới ghi nhận")) table = -1;
        else if (value.equals("Chưa giải quyết")) table = 0;
        else table = 1;
        listQuarterOfYear(QuarterOfYear, table);
    }

    //Xử lí nút Close ứng dụng
    public void Close(ActionEvent e){
        Platform.exit();
        System.exit(0);
    }

    //hiện không dùng đến
    public void pressButtonRecent(ActionEvent e) throws IOException {
        AnchorPane label = FXMLLoader.load(getClass().getResource("../Fxml/Recent_List.fxml"));
        Stage window = new Stage();
        Scene scene = new Scene(label,1700,900);
        //window.getIcons().add(new Image(getClass().getResourceAsStream("../book.png")));
        window.setTitle("Danh sách đơn mới nhận");
        window.setScene(scene);
        window.show();
    }

    //nút "Trang chủ" để quay lại trang ban đầu sau khi đăng nhập
    public void pressButtonHome(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../Fxml/sample1.fxml"));
        Stage window = (Stage)((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1529,900);
        //window.getIcons().add(new Image(getClass().getResourceAsStream("../book.png")));
        window.setTitle("Quản lý thông tin tổ dân phố");
        window.setScene(scene);
        window.show();
    }

    //chức năng tìm kiếm
    public void search() throws SQLException{
        /*String ho_ten = hoten.getText();
        String so_dien_thoai = sdt.getText();
        String _ngay = ngay.getText();
        String noidung = tim_Noidung.getValue().toString();
        String trangthai = tim_Trangthai.getValue().toString();
        int tt = -2;
        if(trangthai == "Mới ghi nhận") tt = -1;
        else if(trangthai == "Chưa giải quyết") tt = 0;
        else if(trangthai == "Đã giải quyết") tt = 1;
        Database database = new Database();
        ResultSet newPetition = database.getListPetitionFromTheCondition(ho_ten, so_dien_thoai, _ngay, noidung, tt);
        ArrayList<DonPhanAnh> list = new ArrayList<DonPhanAnh>();
        int i=0;
        while(newPetition.next()){
            DonPhanAnh donPhanAnh;
            String name = newPetition.getString("TEN");
            String address = newPetition.getString("NOISONG");
            String phone = newPetition.getString("DIENTHOAI");
            String day = newPetition.getString("NGAY");
            String classify = newPetition.getString("PHANLOAI");
            String noiDung = newPetition.getString("NOIDUNG");
            donPhanAnh = new DonPhanAnh(name,address,phone,day,
                    classify,noiDung,i+1,false);
            list.add(donPhanAnh);
            i++;
        }
        ObservableList<DonPhanAnh> list1 = FXCollections.observableArrayList();
        int size = list.size();
        for(int k=0;k<size;k++){
            list1.add(list.get(k));
        }
        allDon.setText(String.valueOf(size));

        stt_search.setCellValueFactory(new PropertyValueFactory<>("stt"));
        hoten_search.setCellValueFactory(new PropertyValueFactory<>("name"));
        diachi_search.setCellValueFactory(new PropertyValueFactory<>("address"));
        sdt_search.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        ngay_search.setCellValueFactory(new PropertyValueFactory<>("date"));
        noidung_search.setCellValueFactory(new PropertyValueFactory<>("classify"));
        chitiet_search.setCellValueFactory(new PropertyValueFactory<>("chiTiet"));
        trangthai_search.setCellValueFactory(new PropertyValueFactory<>("remark"));
        tableSearch.setItems(list1);*/
    }

    public String getClassify(){
        String classify=null;
        if(radioAnNinh.isSelected()){
            classify = "An ninh, trật tự";
        }
        else if(radioCoSo.isSelected()){
            classify = "Cơ sở hạ tầng";
        }
        else if(radioQuyDinh.isSelected()){
            classify = "Quy định quy chế";
        }
        else{
            classify = noiDungKhac.getText();
        }
        return classify;
    }
    public void showAlter(){
        Alert hi = new Alert(Alert.AlertType.INFORMATION);
        hi.setTitle("Update data");
        hi.setHeaderText(null);
        hi.setContentText("Update to the database successfully !");
        hi.showAndWait();
    }

    public void luuLai(ActionEvent actionEvent) throws SQLException {
        ObservableList<Node> list = this.stackPane.getChildren();
        Node hasInfo = list.get(list.size()-1);
        Node noInfo = list.get(list.size()-2);
        Database database = new Database();
        if(hasInfo.isVisible()){
            String cmt = cmt2.getText();
            String date = String.valueOf(ngayNop2.getValue());
            String ndChiTiet = chiTiet.getText();
            String []arrayQuy= date.split("-");
            int quy = Integer.parseInt(arrayQuy[1])/3+1;
            int state = -1;
            String classify = null;
            classify = getClassify();
            database.createNewPetition(cmt,date,classify,ndChiTiet);
            showAlter();
        }
        else{
            String name = hoTen.getText();
            String cmt = cmt1.getText();
            String accommodation = diaChi.getText();
            String birthday = String.valueOf(ngaySinh.getValue());
            String sdt = SDT1.getText();

            //insert nguoi lam don
            //database.insertUser(cmt,name,birthday,sdt,accommodation);
            String classify = null;
            classify = getClassify();
            //insert noi dung
            String date = String.valueOf(ngayNop.getValue());
            String ndChiTiet = chiTiet.getText();
            String []arrayQuy= date.split("-");
            int quy = Integer.parseInt(arrayQuy[1])/3+1;
            int state = -1;

            database.insertPetitionIntoDatabase(cmt,name,birthday,sdt,accommodation,ndChiTiet,date,classify);

            showAlter();
        }

        database = null;


    }

    public void changeHasInfo(ActionEvent actionEvent) {
        ObservableList<Node> list = this.stackPane.getChildren();
        Node hasInfo = list.get(list.size()-1);
        Node noInfo = list.get(list.size()-2);
        hasInfo.setVisible(true);
        noInfo.setVisible(false);
    }

    public void changeNoInfo(ActionEvent actionEvent) {
        ObservableList<Node> list = this.stackPane.getChildren();
        Node hasInfo = list.get(list.size()-1);
        Node noInfo = list.get(list.size()-2);
        hasInfo.setVisible(false);
        noInfo.setVisible(true);
    }
    ObservableList<DonPhanAnh> list;

    public void donMoiGhiNhan(/*ObservableList<DonPhanAnh> list*/) throws SQLException {


        colSTT.setCellValueFactory(new PropertyValueFactory<>("stt"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colClassify.setCellValueFactory(new PropertyValueFactory<>("classify"));
        colNoiDung.setCellValueFactory(new PropertyValueFactory<>("chiTiet"));


        colState.setCellValueFactory(new PropertyValueFactory<>("remark"));
        list = getList();

        tableNewList.setItems(list);
    }
    public ObservableList<DonPhanAnh> getList() throws SQLException {
        Database database = new Database();
        ResultSet newPetition = database.getListNewPetition();
        ArrayList<DonPhanAnh> list = new ArrayList<DonPhanAnh>();
        int i=0;
        while(newPetition.next()){
            DonPhanAnh donPhanAnh;
            String name = newPetition.getString("TEN");
            String address = newPetition.getString("NOISONG");
            String phone = newPetition.getString("DIENTHOAI");
            String day = newPetition.getString("NGAY");
            String classify = newPetition.getString("PHANLOAI");
            String noiDung = newPetition.getString("NOIDUNGPHANANH");
            donPhanAnh = new DonPhanAnh(name,address,phone,day,
                    classify,noiDung,i+1,false);
            list.add(donPhanAnh);
            i++;
        }
        ObservableList<DonPhanAnh> list1 = FXCollections.observableArrayList();
        int size = list.size();
        for(int k=0;k<size;k++){
            list1.add(list.get(k));
        }
        allDon.setText(String.valueOf(size));
        return list1;
    }

    public void initialize(){
        selectAllDonNop.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                ObservableList<DonPhanAnh> items = tableNewList.getItems();
                for(DonPhanAnh a :items){
                    if(selectAllDonNop.isSelected()){
                        a.getRemark().setSelected(true);
                    }
                    else{
                        a.getRemark().setSelected(false);
                    }
                }
            }
        });
    }

    public void luuChange(ActionEvent actionEvent) throws SQLException {
        Database database = new Database();
        for(DonPhanAnh donPhanAnh : list){
            if(donPhanAnh.getRemark().isSelected()){
                LocalDate date1 = LocalDate.now();
                String currDate =  date1.toString();
                database.confirmFromNewPetitionToPendingPetition(donPhanAnh.getPhoneNumber(),donPhanAnh.getDate(),
                        donPhanAnh.getClassify(),donPhanAnh.getChiTiet(),currDate);
            }
        }
        showAlter();
    }

    public void delete(ActionEvent actionEvent) throws SQLException {
        Database database = new Database();
        for(DonPhanAnh donPhanAnh : list){
            if(donPhanAnh.getRemark().isSelected()){
                System.out.println(donPhanAnh.getName()+" "+donPhanAnh.getPhoneNumber()+" "+donPhanAnh.getDate()+" "+donPhanAnh.getClassify());
                database.deleteSpamPetition(donPhanAnh.getName(),donPhanAnh.getPhoneNumber(),donPhanAnh.getDate(),donPhanAnh.getClassify());

            }
        }
        button_ds_MoiNhan();
    }
    // Hien thi danh sach dang xu li.
    ObservableList<DonPhanAnh> list1;
    public void donDangXuli() throws SQLException {


        colSTT1.setCellValueFactory(new PropertyValueFactory<>("stt"));
        colName1.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress1.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhone1.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colDate1.setCellValueFactory(new PropertyValueFactory<>("date"));
        colClassify1.setCellValueFactory(new PropertyValueFactory<>("classify"));
        colNoiDung1.setCellValueFactory(new PropertyValueFactory<>("chiTiet"));
        colState1.setCellValueFactory(new PropertyValueFactory<>("remark"));
        list1 = getListWaiting();

        tableWaitingList.setItems(list1);
    }

    private ObservableList<DonPhanAnh> getListWaiting() throws SQLException {
        Database database = new Database();
        ResultSet PetitionWaiting = database.getListPetitionUnsolved();
        ArrayList<DonPhanAnh> list = new ArrayList<DonPhanAnh>();
        int i=0;
        while(PetitionWaiting.next()){
            DonPhanAnh donPhanAnh;
            String name = PetitionWaiting.getString("TEN");
            String address = PetitionWaiting.getString("NOISONG");
            String phone = PetitionWaiting.getString("DIENTHOAI");
            String day = PetitionWaiting.getString("NGAY");
            String classify = PetitionWaiting.getString("PHANLOAI");
            String noiDung = PetitionWaiting.getString("NOIDUNGPHANANH");
            donPhanAnh = new DonPhanAnh(name,address,phone,day,
                    classify,noiDung,i+1,false);
            list.add(donPhanAnh);
            i++;
        }
        ObservableList<DonPhanAnh> list2 = FXCollections.observableArrayList();
        int size = list.size();
        for(int k=0;k<size;k++){
            list2.add(list.get(k));
        }
        allDon1.setText(String.valueOf(size));
        return list2;
    }

    // Hien thi thong ke theo quy
    public void listQuarterOfYear(int Quarter, int table) throws SQLException {
        ObservableList<DonPhanAnh> list1;

        colSTT2.setCellValueFactory(new PropertyValueFactory<>("stt"));
        colName2.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress2.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhone2.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colDate2.setCellValueFactory(new PropertyValueFactory<>("date"));
        colClassify2.setCellValueFactory(new PropertyValueFactory<>("classify"));
        colNoiDung2.setCellValueFactory(new PropertyValueFactory<>("chiTiet"));


        colState2.setCellValueFactory(new PropertyValueFactory<>("remark"));
        list1 = getListQuarterOfYear(Quarter, table);

        tableViewQuarterOfYear.setItems(list1);
    }

    private ObservableList<DonPhanAnh> getListQuarterOfYear(int quarter, int table) throws SQLException {
        Database database = new Database();
        ResultSet PetitionWaiting = database.getListPetitionForQuarterOfYear(quarter, table);
        ArrayList<DonPhanAnh> list = new ArrayList<DonPhanAnh>();
        int i=0;
        while(PetitionWaiting.next()){
            DonPhanAnh donPhanAnh;
            String name = PetitionWaiting.getString("TEN");
            String address = PetitionWaiting.getString("NOISONG");
            String phone = PetitionWaiting.getString("DIENTHOAI");
            String day = PetitionWaiting.getString("NGAY");
            String classify = PetitionWaiting.getString("PHANLOAI");
            String noiDung = PetitionWaiting.getString("NOIDUNGPHANANH");
           // Boolean state = PetitionWaiting.getBoolean("TRANGTHAI");
            donPhanAnh = new DonPhanAnh(name,address,phone,day,
                    classify,noiDung,i+1,false);
            list.add(donPhanAnh);
            i++;
        }
        ObservableList<DonPhanAnh> list2 = FXCollections.observableArrayList();
        int size = list.size();
        for(int k=0;k<size;k++){
            list2.add(list.get(k));
        }
        allDon2.setText(String.valueOf(size));
        return list2;
    }



}
