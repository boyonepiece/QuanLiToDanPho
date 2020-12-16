package sample.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller_Home {
    @FXML
    private Label soHo;
    @FXML
    private Label soNhanKhau;
    @FXML
    private TextField so_Ho;
    @FXML
    private TextField so_NhanKhau;
    @FXML
    private Button update;
    @FXML
    private Button ds_CanBo;
    @FXML
    private Button save;

    //xu li Close App
    public void Close(ActionEvent e){
        Platform.exit();
        System.exit(0);
    }

    //sự kiện nhấn nút "Cập nhật thông tin chung"
    public void preButtonUpdate(ActionEvent e){
        update.setVisible(false);
        ds_CanBo.setVisible(false);
        save.setVisible(true);
        so_Ho.setText("");
        so_NhanKhau.setText("");
        so_Ho.setPromptText(soHo.getText());
        so_NhanKhau.setPromptText(soNhanKhau.getText());
        so_Ho.setVisible(true);
        so_NhanKhau.setVisible(true);
    }

    //nhấn "Lưu" thông tin cập nhật
    public void pressButtonSave(ActionEvent e){
        if(!so_Ho.getText().equals("")) {
            soHo.setText(so_Ho.getText());
        }
        if(!so_NhanKhau.getText().equals("")) {
            soNhanKhau.setText(so_NhanKhau.getText());
        }
        save.setVisible(false);
        so_Ho.setVisible(false);
        so_NhanKhau.setVisible(false);
        update.setVisible(true);
        ds_CanBo.setVisible(true);
    }

    //sự kiện nhấn chức năng 6
    public void pressButton6(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../Fxml/sample_Requested.fxml"));
        Stage window = (Stage)((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1520,790);
        //window.getIcons().add(new Image(getClass().getResourceAsStream("../book.png")));
        window.setTitle("Quản lý thông tin tổ dân phố");
        window.setX(0);
        window.setY(0);
        window.setScene(scene);
        window.show();
    }

    //sự kiện nhấn nút đăng xuất
    public void pressButtonOut(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../Fxml/sample_Login.fxml"));
        Stage window = (Stage)((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,600,400);
        //window.getIcons().add(new Image(getClass().getResourceAsStream("../book.png")));
        window.setX(450);
        window.setY(200);
        window.setTitle("Quản lý thông tin tổ dân phố");
        window.setScene(scene);
        window.show();
    }
}
