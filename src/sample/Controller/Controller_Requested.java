package sample.Controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.Database;
import sample.Entity.DonPhanAnh;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Controller_Requested {

    @FXML
    private TextArea chiTiet;

    @FXML
    private AnchorPane ds_MoiNhan;
    //donNop
    @FXML
    private TextField hoTen, diaChi, SDT1,SDT2, noiDungKhac;
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
    private TableColumn<DonPhanAnh,Boolean>colState;
    @FXML
    private TableColumn<DonPhanAnh,Integer> colSTT;
    @FXML
    private TableView tableNewList;
    @FXML
    private Label allDon;
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
    public void button_donNop(){
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
    public void button_ds_XuLi(ActionEvent e){
        this.donNop.setVisible(false);
        this.ds_MoiNhan.setVisible(false);
        this.ds_XuLi.setVisible(true);
        this.timKiem.setVisible(false);
        this.thongKe_Quy.setVisible(false);
    }

    //button tìm kiếm
    public void button_timKiem(){
        //this.tableSearch = new TableView();
        ObservableList<DonPhanAnh> list1 = FXCollections.observableArrayList();
        this.tableSearch.setItems(list1);
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
        Parent root = FXMLLoader.load(getClass().getResource("../Fxml/sample_Home.fxml"));
        Stage window = (Stage)((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1529,900);
        //window.getIcons().add(new Image(getClass().getResourceAsStream("../book.png")));
        window.setTitle("Quản lý thông tin tổ dân phố");
        window.setScene(scene);
        window.show();
    }

    //chức năng gộp đơn
    public void group(ActionEvent e) throws IOException {
        AnchorPane group = new AnchorPane();

        Label label1 = new Label("Chi tiết của nhóm đơn:");
        label1.setFont(new Font("System",18));
        label1.setLayoutX(45);
        label1.setLayoutY(40);

        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setLayoutX(45);
        textArea.setLayoutY(85);
        textArea.setPrefSize(500,230);

        Button button = new Button("Cập nhật đơn chờ xử lí");
        button.setLayoutX(210);
        button.setLayoutY(340);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Database database = new Database();
                for(DonPhanAnh donPhanAnh : list){
                    if(donPhanAnh.getRemark().isSelected()){
                        LocalDate date1 = LocalDate.now();
                        String currDate =  date1.toString();
                        try {
                            database.confirmFromNewPetitionToPendingPetition(donPhanAnh.getPhoneNumber(),donPhanAnh.getDate(),
                                    donPhanAnh.getClassify(),donPhanAnh.getChiTiet(),currDate);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                }
                showAlter();
                try {
                    button_ds_MoiNhan();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        group.getChildren().add(label1);
        group.getChildren().add(textArea);
        group.getChildren().add(button);
        Scene scene = new Scene(group,600,400);
        Stage group1 = new Stage();
        group1.setTitle("Gộp nhóm đơn");
        group1.setScene(scene);

        group1.show();
    }
    //chức năng tìm kiếm
    public void search() throws SQLException{
        String ho_ten = hoten.getText();
        String so_dien_thoai = sdt.getText();
        String _ngay = ngay.getText();
        String noidung = "";
        if(tim_Noidung.getValue() != null) noidung = tim_Noidung.getValue().toString();
        String trangthai = tim_Trangthai.getValue().toString();
        int tt = -2;
        if(trangthai == "Mới ghi nhận") tt = -1;
        else if(trangthai == "Chưa giải quyết") tt = 0;
        else if(trangthai == "Đã giải quyết") tt = 1;
        Database database = new Database();
        ResultSet newPetition = database.searchPetition(ho_ten, so_dien_thoai, _ngay, noidung, tt);
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

        stt_search.setCellValueFactory(new PropertyValueFactory<>("stt"));
        hoten_search.setCellValueFactory(new PropertyValueFactory<>("name"));
        diachi_search.setCellValueFactory(new PropertyValueFactory<>("address"));
        sdt_search.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        ngay_search.setCellValueFactory(new PropertyValueFactory<>("date"));
        noidung_search.setCellValueFactory(new PropertyValueFactory<>("classify"));
        chitiet_search.setCellValueFactory(new PropertyValueFactory<>("chiTiet"));
        //trangthai_search.setCellValueFactory(new PropertyValueFactory<>("remark"));
        tableSearch.setItems(list1);
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
        button_ds_MoiNhan();
    }

    public void delete(ActionEvent actionEvent) throws SQLException {
        Database database = new Database();
        for(DonPhanAnh donPhanAnh : list){
            if(donPhanAnh.getRemark().isSelected()){
                database.deleteSpamPetition(donPhanAnh.getName(),donPhanAnh.getPhoneNumber(),donPhanAnh.getDate(),donPhanAnh.getClassify(),donPhanAnh.getChiTiet());
            }
        }
        button_ds_MoiNhan();
    }
}
