package sample.Controller;

import javafx.application.Platform;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller2 {
    @FXML
    private TextField hoTen, soNha, SDT, noiDung_Khac;

    @FXML
    private TextArea chiTiet;

    @FXML
    private AnchorPane ds_MoiNhan;
    @FXML
    private AnchorPane donNop;
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
    public void button_ds_MoiNhan(ActionEvent e){
        this.donNop.setVisible(false);
        this.ds_MoiNhan.setVisible(true);
        this.ds_XuLi.setVisible(false);
        this.timKiem.setVisible(false);
        this.thongKe_Quy.setVisible(false);
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
        Scene scene = new Scene(label,1700,950);
        //window.getIcons().add(new Image(getClass().getResourceAsStream("../book.png")));
        window.setTitle("Danh sách đơn mới nhận");
        window.setScene(scene);
        window.show();
    }

    //nút "Trang chủ" để quay lại trang ban đầu sau khi đăng nhập
    public void pressButtonHome(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../Fxml/sample1.fxml"));
        Stage window = (Stage)((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1700,950);
        //window.getIcons().add(new Image(getClass().getResourceAsStream("../book.png")));
        window.setTitle("Quản lý thông tin tổ dân phố");
        window.setScene(scene);
        window.show();
    }
}
