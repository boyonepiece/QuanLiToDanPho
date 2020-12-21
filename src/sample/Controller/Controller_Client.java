package sample.Controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sample.Database;

import java.io.IOException;
import java.sql.SQLException;

public class Controller_Client {
    @FXML
    private TextField hoTen, diaChi, SDT1,SDT2, noiDungKhac;
    @FXML
    private TextArea chiTiet;
    @FXML
    PasswordField cmt1,cmt2;
    @FXML
    private DatePicker ngaySinh,ngayNop,ngayNop2;
    @FXML
    private RadioButton radioAnNinh,radioCoSo,radioQuyDinh,radioKhac;
    @FXML
    private StackPane stackPane;


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

    public void Close(ActionEvent e){
        Platform.exit();
        System.exit(0);
    }
}
