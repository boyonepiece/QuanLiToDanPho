package sample;
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
        this.password="09042000";
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
        ResultSet result=preparedStatement.executeQuery();
        if(result.next()){
            return true;
        }
        return false;
    }

    /*
     * INSERT DATA INTO DATABASE
     * */

    public void insertUser(String peopleID,String name, String birthday,String phoneNumber,String accommodation) throws SQLException{
        var query="INSERT INTO NGUOIPHANANH(CMT,TEN,NGAYSINH,DIENTHOAI,NOISONG) VALUES(?,?,?,?,?)";
        Connection connection=getConnection();
        PreparedStatement preparedStatement=connection.prepareStatement(query);
        preparedStatement.setString(1,peopleID);
        preparedStatement.setString(2,name);
        preparedStatement.setString(3,birthday);
        preparedStatement.setString(4,phoneNumber);
        preparedStatement.setString(5,accommodation);
        preparedStatement.executeUpdate();
       /* connection.close();*/
    }

    public void createPetitionInDatabase(String peopleID,String content,String day,int quarterOfYear,String classify ,int state) throws SQLException{
        var query="INSERT INTO DONPHANANH (CMT,NOIDUNG,NGAY,QUY,PHANLOAI,TRANGTHAI) VALUES(?,?,?,?,?,?)";
        Connection connection=getConnection();
        PreparedStatement preparedStatement=connection.prepareStatement(query);
        preparedStatement.setString(1,peopleID);
        preparedStatement.setString(2,content);
        preparedStatement.setString(3,day);
        preparedStatement.setInt(4,quarterOfYear);
        preparedStatement.setString(5,classify);
        preparedStatement.setInt(6,state);
        preparedStatement.executeUpdate();
        connection.close();
    }

    public boolean checkUserExist(String peopleID) throws SQLException{
        var query="SELECT * FROM NGUOIPHANANH WHERE CMT=?";
        PreparedStatement preparedStatement=getConnection().prepareStatement(query);
        preparedStatement.setString(1,peopleID);
        ResultSet result=preparedStatement.executeQuery();
        if(result.next()){
            return true;
        }
        return false;
    }
    public void insertPetitionIntoDatabase(String peopleID,String name, String birthday,String phoneNumber,String accommodation,
                                           String content,String day,int quarterOfYear,String classify ,int state) throws SQLException{
        if(checkUserExist(peopleID)) {
            createPetitionInDatabase(peopleID,content,day,quarterOfYear,classify,state);
            return;
        }
        insertUser(peopleID,name,birthday,phoneNumber,accommodation);
        createPetitionInDatabase(peopleID,content,day,quarterOfYear,classify,state);
    }




    /*
     * CHANGE STATE FOR THE PETITION
     * -1: IS A NEW PETITION
     * 0: THE PETITION IS UNRESOLVED
     * 1: THE PETITION IS RESOLVED
     * */


    public void changeStatePetition(String name,String phoneNumber,int state) throws SQLException{
        var query="UPDATE DONPHANH SET TRANGTHAI=? WHERE CMT IN(SELECT CMT FROM NGUOIPHANANH WHERE" +
                "TEN=? AND DIENTHOAI=?)";
        PreparedStatement preparedStatement=getConnection().prepareStatement(query);
        preparedStatement.setInt(1,state);
        preparedStatement.setString(2,name);
        preparedStatement.setString(3,phoneNumber);
        preparedStatement.executeUpdate();
    }

    /**
     *SEARCH DATA FROM DATABASE
     */

    public ResultSet getListPetitionResolved() throws SQLException{
        var query="SELECT TEN,NOISONG,DIENTHOAI,DAY,PHANLOAI,NOIDUNG FROM DONPHANANH INNER JOIN NGUOIPHANANH" +
                "ON DONPHANANH.CMT=NGUOIPHANANH.CMT WHERE TRANGTHAI=1 ORDER BY NGAY DESC";
        PreparedStatement pre=getConnection().prepareStatement(query);
        ResultSet result=pre.executeQuery();
        return result;
    }


    public ResultSet getListPetitionUnsolved()throws SQLException{
        var query="SELECT TEN,NOISONG,DIENTHOAI,DAY,PHANLOAI,NOIDUNG FROM DONPHANANH INNER JOIN NGUOIPHANANH" +
                "ON DONPHANANH.CMT=NGUOIPHANANH.CMT WHERE TRANGTHAI=0 ORDER BY NGAY DESC";
        PreparedStatement pre=getConnection().prepareStatement(query);
        ResultSet result=pre.executeQuery();
        return result;
    }

    public ResultSet getListNewPetition()throws SQLException{
        var query="SELECT TEN,NOISONG,DIENTHOAI,DAY,PHANLOAI,NOIDUNG FROM DONPHANANH INNER JOIN NGUOIPHANANH" +
                "ON DONPHANANH.CMT=NGUOIPHANANH.CMT WHERE TRANGTHAI=-1 ORDER BY NGAY DESC";
        PreparedStatement pre=getConnection().prepareStatement(query);
        ResultSet result=pre.executeQuery();
        return result;
    }

    public ResultSet getListPetitionForQuarterOfYear(int quarterOfYear) throws SQLException{
        var query="SELECT TEN,NOISONG,DIENTHOAI,DAY,PHANLOAI,NOIDUNG FROM DONPHANANH INNER JOIN NGUOIPHANANH" +
                "ON DONPHANANH.CMT=NGUOIPHANANH.CMT WHERE QUY=? ORDER BY NGAY DESC";
        PreparedStatement pre=getConnection().prepareStatement(query);
        pre.setInt(1,quarterOfYear);
        ResultSet result=pre.executeQuery();
        return result;
    }

    public ResultSet getListPetitionFromTheCondition(String name,String phoneNumber,String day,String classify,int state) throws SQLException{
        var query="SELECT TEN,NOISONG,DIENTHOAI,DAY,PHANLOAI,NOIDUNG FROM DONPHANANH INNER JOIN NGUOIPHANANH" +
                "ON DONPHANANH.CMT=NGUOIPHANANH.CMT WHERE " +
                "TEN=? AND DIENTHOAI=? AND NGAY=? AND PHANLOAI=? AND TRANGTHAI=?" +
                " ORDER BY NGAY DESC";
        PreparedStatement pre=getConnection().prepareStatement(query);
        pre.setString(1,name);
        pre.setString(2,phoneNumber);
        pre.setString(2,day);
        pre.setString(3,classify);
        pre.setInt(4,state);
        return pre.executeQuery();
    }
    public ResultSet getListPetitionFromPeopleID(String peopleID) throws SQLException{
        var query="SELECT TEN,NOISONG,DIENTHOAI,DAY,PHANLOAI,NOIDUNG FROM DONPHANANH WHERE CMT=?";
        PreparedStatement pre=getConnection().prepareStatement(query);
        pre.setString(1,peopleID);
        return pre.executeQuery();
    }

}
