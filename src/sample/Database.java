package sample;
import sample.Hash.HashID;

import java.sql.*;


public class Database {
    private String driveName;
    private String url;
    private String username;
    private String password;
    private Connection connection;

    public Database(){
        this.driveName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
        this.url="jdbc:sqlserver://localhost:1433;databaseName=QUANLYTODANPHO";
        this.username="sa";
        this.password="20102000";
        this.connection=createConnection();
    }
    public Connection createConnection(){
        Connection connection = null;
        try{
            Class.forName(getDriveName());
            try{
                connection= DriverManager.getConnection(getUrl(),getUsername(),getPassword());
            }
            catch (SQLException x){
                x.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(connection ==null) {
            System.out.println("Connection error");
        }
        return connection;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriveName() {
        return driveName;
    }

    public void setDriveName(String driveName) {
        this.driveName = driveName;
    }

    public Connection getConnection(){
        return this.connection;
    }


    public boolean checkLogin(String username,String password)throws SQLException{
        var query="SELECT * FROM TOTRUONG WHERE USENAME=? AND PASSWORD=?";
        PreparedStatement preparedStatement=getConnection().prepareStatement(query);
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,password);
        ResultSet result=preparedStatement.executeQuery();
        if(result.next()){
            return true;
        }
        return false;
    }

    public String getPeopleID(String phoneNumber) throws  SQLException{
        var query="SELECT CMT FROM NGUOIPHANANH WHERE DIENTHOAI=?";
        PreparedStatement pre=getConnection().prepareStatement(query);
        pre.setString(1,phoneNumber);
        ResultSet result=pre.executeQuery();
        if(result.next()) {
            return result.getString("CMT");
        }
        return null;
    }

    public String getIDPetition(String peopleID,String classify,String date) throws SQLException{
        var query="SELECT ID_DON FROM DONPHANANH WHERE CMT=? AND NGAY=? AND PHANLOAI=?";
        PreparedStatement pre=getConnection().prepareStatement(query);
        ResultSet result=pre.executeQuery();
        if(result.next()){
            return result.getString(1);
        }
        return null;
    }

    /*
     * INSERT DATA INTO DATABASE
     * */

    public void insertUser(String peopleID,String name, String birthday,String phoneNumber,String accommodation) throws SQLException{
        var query="INSERT INTO NGUOIPHANANH(CMT,TEN,NGAYSINH,DIENTHOAI,NOISONG) VALUES(?,?,?,?,?)";
        Connection connection=getConnection();
        PreparedStatement preparedStatement=connection.prepareStatement(query);
        preparedStatement.setString(1,peopleID);
        preparedStatement.setNString(2,name);
        preparedStatement.setString(3,birthday);
        preparedStatement.setString(4,phoneNumber);
        preparedStatement.setNString(5,accommodation);
        preparedStatement.executeUpdate();
    }

    public void createPetitionInDatabase(String peopleID,String content,String day,int quarterOfYear,String classify ,int state) throws SQLException{
        var query="INSERT INTO DONPHANANH (CMT,NOIDUNG,NGAY,QUY,PHANLOAI,TRANGTHAI) VALUES(?,?,?,?,?,?)";
        Connection connection=getConnection();
        PreparedStatement preparedStatement=connection.prepareStatement(query);
        preparedStatement.setString(1,peopleID);
        preparedStatement.setNString(2,content);
        preparedStatement.setString(3,day);
        preparedStatement.setInt(4,quarterOfYear);
        preparedStatement.setNString(5,classify);
        preparedStatement.setInt(6,state);
        preparedStatement.executeUpdate();
    }


    public void insertPetitionIntoDatabase(String peopleID,String name, String birthday,String phoneNumber,String accommodation,
                                           String content,String day,int quarterOfYear,String classify ,int state) throws SQLException{
        HashID hashId=new HashID();
        String id=getPeopleID(phoneNumber);
        if(id!=null) {//if user exist in database
            if(hashId.checkPeopleIDExist(peopleID,id)==false){
                System.out.println("Typing incorrect,The phone number is used");
                return;
            }
            createPetitionInDatabase(id,content,day,quarterOfYear,classify,state);
            return;
        }
        // not exist
        String newID= hashId.hash(peopleID);
        insertUser(newID,name,birthday,phoneNumber,accommodation);
        createPetitionInDatabase(newID,content,day,quarterOfYear,classify,state);
    }




    /*
     * CHANGE STATE FOR THE PETITION
     * -1: IS A NEW PETITION
     * 0: THE PETITION IS UNRESOLVED
     * 1: THE PETITION IS RESOLVED
     * */


    public void changeStatePetition(String name,String phoneNumber,String day,int state) throws SQLException{
        var query="UPDATE DONPHANANH SET TRANGTHAI=? WHERE CMT IN(SELECT CMT FROM NGUOIPHANANH WHERE TEN=? AND DIENTHOAI=?) AND NGAY=?";
        PreparedStatement preparedStatement=getConnection().prepareStatement(query);
        preparedStatement.setInt(1,state);
        preparedStatement.setNString(2,name);
        preparedStatement.setString(3,phoneNumber);
        preparedStatement.setString(4,day);
        preparedStatement.executeUpdate();
    }

    /**
     *SEARCH DATA FROM DATABASE
     */

    public ResultSet getListPetitionResolved() throws SQLException{
        var query="SELECT TEN,NOISONG,DIENTHOAI,NGAY,PHANLOAI,NOIDUNG " +
                "FROM DONPHANANH INNER JOIN NGUOIPHANANH ON DONPHANANH.CMT=NGUOIPHANANH.CMT" +
                " WHERE TRANGTHAI=1 ORDER BY NGAY DESC";
        PreparedStatement pre=getConnection().prepareStatement(query);
        return pre.executeQuery();
    }


    public ResultSet getListPetitionUnsolved()throws SQLException{
        var query="SELECT TEN,NOISONG,DIENTHOAI,NGAY,PHANLOAI,NOIDUNG" +
                " FROM DONPHANANH INNER JOIN NGUOIPHANANH ON DONPHANANH.CMT=NGUOIPHANANH.CMT" +
                " WHERE TRANGTHAI=0 ORDER BY NGAY DESC";
        PreparedStatement pre=getConnection().prepareStatement(query);
        return pre.executeQuery();
    }

    public ResultSet getListNewPetition()throws SQLException{
        var query="SELECT TEN,NOISONG,DIENTHOAI,NGAY,PHANLOAI,NOIDUNG " +
                "FROM DONPHANANH INNER JOIN NGUOIPHANANH ON DONPHANANH.CMT=NGUOIPHANANH.CMT" +
                " WHERE TRANGTHAI=-1 ORDER BY NGAY DESC";
        Connection connection=getConnection();
        PreparedStatement pre=connection.prepareStatement(query);
        return pre.executeQuery();
    }

    public ResultSet getListPetitionForQuarterOfYear(int quarterOfYear) throws SQLException{
        var query="SELECT TEN,NOISONG,DIENTHOAI,NGAY,PHANLOAI,NOIDUNG " +
                "FROM DONPHANANH INNER JOIN NGUOIPHANANH ON DONPHANANH.CMT=NGUOIPHANANH.CMT" +
                " WHERE QUY=? ORDER BY NGAY DESC";
        PreparedStatement pre=getConnection().prepareStatement(query);
        pre.setInt(1,quarterOfYear);
        return pre.executeQuery();
    }

    public ResultSet getListPetitionFromTheCondition(String name,String phoneNumber,String day,String classify,int state) throws SQLException{
        var query="SELECT TEN,NOISONG,DIENTHOAI,NGAY,PHANLOAI,NOIDUNG" +
                " FROM DONPHANANH INNER JOIN NGUOIPHANANH ON DONPHANANH.CMT=NGUOIPHANANH.CMT" +
                " WHERE TEN=? AND DIENTHOAI=? AND NGAY=? AND PHANLOAI=? AND TRANGTHAI=?" +
                " ORDER BY NGAY DESC";
        PreparedStatement pre=getConnection().prepareStatement(query);
        pre.setNString(1,name);
        pre.setString(2,phoneNumber);
        pre.setString(3,day);
        pre.setNString(4,classify);
        pre.setInt(5,state);
        return pre.executeQuery();
    }
    public ResultSet getListPetitionFromPeopleIDAndPhoneNumber(String peopleID,String phoneNumber) throws SQLException{
        String id=getPeopleID(phoneNumber);
        if(id==null){
            return null;
        }
        if(!new HashID().checkPeopleIDExist(peopleID, id)){
            return null;
        }
        var query="SELECT TEN,NOISONG,DIENTHOAI,NGAY,PHANLOAI,NOIDUNG " +
                "FROM NGUOIPHANANH INNER JOIN DONPHANANH ON NGUOIPHANANH.CMT=DONPHANANH.CMT" +
                " WHERE NGUOIPHANANH.CMT=?";
        PreparedStatement pre=getConnection().prepareStatement(query);
        pre.setString(1,id);
        return pre.executeQuery();
    }

    /*
     * DELETE PETITION FROM DATABASE
     * */

    public int countPetitionForUser(String name,String phoneNumber)throws SQLException{
        var query="SELECT COUNT(ID_DON) FROM DONPHANANH " +
                "WHERE CMT IN " +
                "(SELECT CMT FROM NGUOIPHANANH WHERE TEN=? AND DIENTHOAI=?)";
        PreparedStatement preparedStatement=getConnection().prepareStatement(query);
        preparedStatement.setNString(1,name);
        preparedStatement.setString(2,phoneNumber);
        ResultSet resultSet=preparedStatement.executeQuery();
        if(resultSet.next())
            return resultSet.getInt(1);
        return 0;


    }

    public void deleteUser(String peopleID) throws SQLException{
        var query="DELETE FROM NGUOIPHANANH WHERE CMT=?";
        PreparedStatement pre1=getConnection().prepareStatement(query);
        pre1.setString(1,peopleID);
        pre1.executeUpdate();
    }

    public void removePetition(String peopleID,String day,String classify) throws SQLException{
        var query="DELETE FROM DONPHANANH WHERE CMT=? AND NGAY=? AND PHANLOAI=?";
        PreparedStatement pre2=getConnection().prepareStatement(query);
        pre2.setString(1,peopleID);
        pre2.setString(2,day);
        pre2.setString(3,classify);
        pre2.executeUpdate();
    }

    public void deletePetitionFromDatabase(String name,String phoneNumber,String day,String classify)throws SQLException{
        int countPetition=countPetitionForUser(name,phoneNumber);
        if(countPetition==0){
            System.out.println("No row is matching with parameter");
            return;
        }

        String peopleID=getPeopleID(phoneNumber);
        if(countPetition==1){
            removePetition(peopleID,day,classify);
            deleteUser(peopleID);
            return;
        }

        //else count>=2
        removePetition(peopleID,day,classify);
    }


    /*
     ADD RESPONSE FROM ORGANIZATION
     */
    public void addResponsesFromOrganization(String name,String phoneNumber,String classify,String date,
               String phoneNumberResponder,String nameResponder,String organization,String content,String dateResponses  ) throws SQLException {
        String peopleID=getPeopleID(phoneNumber);
        String idPetition=getIDPetition(peopleID,classify,date);
        changeStatePetition(name,phoneNumber,date,1);

        var query="INSERT INTO PHANHOI(ID_DON,SODIENTHOAI_NGUOIPHANHOI,TENNGUOIPHANHOI,COQUAN,NOIDUNGPHANHOI,NGAY) VALUES(?,?,?,?,?,?)";
        PreparedStatement pre=getConnection().prepareStatement(query);
        pre.setString(1,idPetition);
        pre.setString(2,phoneNumberResponder);
        pre.setNString(3,nameResponder);
        pre.setNString(4,organization);
        pre.setNString(5,content);
        pre.setString(6,dateResponses);
        pre.executeUpdate(query);
    }
}
