package sample;
import sample.Hash.HashID;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;


public class Database {
    private String driveName;
    private String url;
    private String username;
    private String password;
    private Connection connection;

    public Database(){
        this.driveName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
        this.url="jdbc:sqlserver://localhost:1433;databaseName=DANPHO";
        this.username="sa";
        this.password="23571113";
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

    public String getIDPetition(String peopleID,String classify,String date,String content,int table) throws SQLException{
        String nameTable="";
        if(table==-1){
            nameTable="DONMOINHAN";
        }
        else if(table==0){
            nameTable="DONDANGCHOXULY";
        }
        else if(table==1){
            nameTable="DONDAXULY";
        }

        var query="SELECT DPA.ID_DON FROM DONPHANANH DPA INNER JOIN "+nameTable+ " D ON DPA.ID_DON=D.ID_DON  WHERE CMT=? AND NGAY=? AND PHANLOAI=? AND NOIDUNGPHANANH=?";
        PreparedStatement pre=getConnection().prepareStatement(query);
        pre.setString(1,peopleID);
        pre.setString(2,date);
        pre.setNString(3,classify);
        pre.setNString(4,content);
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

    public String generatePetitionID(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString()+random.nextInt(10000);
        return generatedString.toUpperCase();
    }

    public void createNewPetition(String peopleID,String date,String classify,String content) throws SQLException{
        var query1 ="INSERT INTO DONPHANANH(ID_DON,CMT,NGAY,PHANLOAI) VALUES (?,?,?,?)";
        String petitionID=generatePetitionID();
        PreparedStatement pre1=getConnection().prepareStatement(query1);
        pre1.setString(1,petitionID);
        pre1.setString(2,peopleID);
        pre1.setString(3,date);
        pre1.setNString(4,classify);
        pre1.executeUpdate();

        var query2="INSERT INTO DONMOINHAN(ID_DON,NOIDUNGPHANANH,PAIR) VALUES(?,?,0)";
        PreparedStatement pre2=getConnection().prepareStatement(query2);
        pre2.setString(1,petitionID);
        pre2.setNString(2,content);
        pre2.executeUpdate();
    }

    public void insertPetitionIntoDatabase(String peopleID,String name, String birthday,String phoneNumber,String accommodation,
                                           String content,String day,String classify ) throws SQLException{
        HashID hashId=new HashID();
        String id=getPeopleID(phoneNumber);
        System.out.println(id);
        if(id!=null) {//if user exist in database
            if(hashId.checkPeopleIDExist(peopleID,id)==false){
                System.out.println("Typing incorrect,The phone number is used");
                return;
            }
            createNewPetition(id,day,classify,content);
            return;
        }
        // not exist
        String idUser=hashId.hash(peopleID);
        insertUser(idUser,name,birthday,phoneNumber,accommodation);
        createNewPetition(idUser,day,classify,content);
    }

    public void insertPendingPetition(String petitionID,String content,String dateSent,int pair) throws SQLException{
        var query="INSERT INTO DONDANGCHOXULY(ID_DON,NOIDUNGPHANANH,NGAYCHUYENLENCAPTREN,PAIR) VALUES (?,?,?,?)";
        PreparedStatement pre1=getConnection().prepareStatement(query);
        pre1.setString(1,petitionID);
        pre1.setNString(2,content);
        pre1.setString(3,dateSent);
        pre1.setInt(4,pair);
        pre1.executeUpdate();
    }

    public void insertSolvedPetition(String petitionID,String contentPetition,String phoneNumberResponder,
                                     String nameResponder,String organization,String contentResponse,String sentDate,String solvedDate,int pair) throws SQLException{
        deletePetitionForUpdate(petitionID,2);
        var query="INSERT INTO DONDAXULY(ID_DON,NOIDUNGPHANANH,SODIENTHOAINGUOIPHANANH,TENNGUOIPHANHOI,COQUAN,NOIDUNGPHANHOI,NGAYPHANHOI,NGAYCHUYENLENCAPTREN) VALUES(?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement=getConnection().prepareStatement(query);
        preparedStatement.setString(1,petitionID);
        preparedStatement.setNString(2,contentPetition);
        preparedStatement.setString(3,phoneNumberResponder);
        preparedStatement.setNString(4,nameResponder);
        preparedStatement.setNString(5,organization);
        preparedStatement.setNString(6,contentResponse);
        preparedStatement.setString(7,solvedDate);
        preparedStatement.setString(8,sentDate);
        preparedStatement.setInt(9,pair);
        preparedStatement.executeUpdate();
    }


    /*
     * DELETE  FROM TABLE
     * */

    public void deleteUser(String peopleID)throws SQLException{
        var query2="DELETE FROM NGUOIPHANANH WHERE CMT=?;";
        PreparedStatement pre2=getConnection().prepareStatement(query2);
        pre2.setString(1,peopleID);
        pre2.executeUpdate();
    }

    //delete when petition is spam
    public void deletePetition(String petitionID) throws SQLException{

        var query1="DELETE FROM DONMOINHAN WHERE ID_DON=?;";
        PreparedStatement pre1=getConnection().prepareStatement(query1);
        pre1.setString(1,petitionID);
        pre1.executeUpdate();

        var query="DELETE FROM DONPHANANH WHERE ID_DON=?;";
        PreparedStatement pre=getConnection().prepareStatement(query);
        pre.setString(1,petitionID);
        pre.executeUpdate();
    }
    public int countPetitionFromPeopleID(String peopleID)throws SQLException{
        var query="SELECT COUNT(ID_DON) FROM DONPHANANH WHERE CMT=?";
        PreparedStatement pre=getConnection().prepareStatement(query);
        pre.setString(1,peopleID);
        ResultSet result=pre.executeQuery();
        if(result.next()){
            return result.getInt(1);
        }
        return 0;
    }

    public void deleteSpamPetition(String name,String phoneNumber,String day,String classify,String content) throws SQLException{
        String peopleID=getPeopleID(phoneNumber);
        int count=countPetitionFromPeopleID(peopleID);
        String petitionID=getIDPetition(peopleID,classify,day,content,-1);
        if(count==1){
            deletePetition(petitionID);
            deleteUser(peopleID);
        }
        else if(count>1){
            deletePetition(petitionID);
        }
    }

    //delete when move the petition from table to another table
    public void deletePetitionForUpdate(String petitionID,int database) throws SQLException{
        //DELETE PETITION FROM DONMOINHAN
        Connection connection=getConnection();
        if(database==1){
            var query="DELETE FROM DONMOINHAN WHERE ID_DON=?";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,petitionID);
            preparedStatement.executeUpdate();
        }
        //DELETE PETITION FROM DONDANGCHOXULY
        else if(database==2){
            var query="DELETE FROM DONDANGCHOXULY WHERE ID_DON=?";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,petitionID);
            preparedStatement.executeUpdate();
        }
    }


    /*
     * CONFIRM PETITION
     * */

    public void confirmFromNewPetitionToPendingPetition(String phoneNumber,String date,String classify,String content,String sentDate) throws SQLException{
        String peopleID=getPeopleID(phoneNumber);
        String petitionID=getIDPetition(peopleID,classify,date,content,-1);
        deletePetitionForUpdate(petitionID,1);
        insertPendingPetition(petitionID,content,sentDate,0);
    }


    /*
    * COMBINE THE SAME PETITION
    * */

    /*
     * Chú thích:
     * Khi tổng hợp các đơn có nội dung trùng nhau (len(listPetitionID)>1) thì sẽ truyền danh sách có id_dơn và nội dung mới do tổ trưởng tóm tắt lại các
     * nội dùng trùng nhau, khi đó sẽ chèn đơn cũ đã được thay thế nội dung mới vào bảng ĐONANGCHOXULY
     *
     * */

    public int getMaxPair() throws SQLException{
        var query="SELECT TOP 1 PAIR FROM DONDANGCHOXULY ORDER BY PAIR DESC";
        PreparedStatement pre=getConnection().prepareStatement(query);
        ResultSet result=pre.executeQuery();
        if(result.next()){
            return result.getInt(1);
        }
        return -1;
    }
//    public void updatePair(String petitionID,int pair) throws SQLException{
//        var query="UPDATE DONMOINHAN SET PAIR="+pair+ "WHERE ID_DON=?";
//        PreparedStatement pre=getConnection().prepareStatement(query);
//        pre.setString(1,petitionID);
//        pre.executeUpdate();
//    }


    public void combinePetition(ArrayList<String> listPetitionID,String newContent,String dateSent) throws SQLException{
        int maxPair=getMaxPair();
        for(String petitionID: listPetitionID){
            //updatePair(petitionID,maxPair+1);
            deletePetitionForUpdate(petitionID,1);
            insertPendingPetition(petitionID,newContent,dateSent,maxPair+1);
        }
    }

    /*
     ADD RESPONSE FROM ORGANIZATION
     */
    public int getPair(String petitionID) throws SQLException{
        var query="SELECT PAIR FROM DONDANGCHOXULY WHERE ID_DON='"+petitionID+"'";
        PreparedStatement pre=getConnection().prepareStatement(query);
        ResultSet resultSet=pre.executeQuery();
        if(resultSet.next()) return resultSet.getInt(1);
        return -1;
    }
    public void addResponses(String nameUser,String phoneNumber,String classify,String date,
                                             String contentPetition,String phoneNumberResponder,
                                             String nameResponder,String organization,String contentResponse,
                                             String sentDate,String solvedDate ) throws SQLException {
        String peopleID=getPeopleID(phoneNumber);
        String petitionID=getIDPetition(peopleID,classify,date,contentPetition,0);
        int pair=getPair(petitionID);
        deletePetitionForUpdate(petitionID,2);
        insertSolvedPetition(petitionID,contentPetition,phoneNumberResponder,nameResponder,organization,contentResponse,sentDate,solvedDate,pair);
    }



    /**
     *SEARCH DATA FROM DATABASE
     */

    public ResultSet getListPetitionSolved() throws SQLException{
        var query="SELECT TEN,NOISONG,DIENTHOAI,NGAY,PHANLOAI,NOIDUNGPHANANH " +
                "FROM NGUOIPHANANH NPA INNER JOIN DONPHANANH DPA ON NPA.CMT=DPA.CMT " +
                "INNER JOIN DONDAXULY D ON DPA.ID_DON=D.ID_DON ORDER BY NGAY DESC";
        PreparedStatement pre=getConnection().prepareStatement(query);
        return pre.executeQuery();
    }


    public ResultSet getListPetitionUnsolved()throws SQLException{
        var query="SELECT TEN,NOISONG,DIENTHOAI,NGAY,PHANLOAI,NOIDUNGPHANANH " +
                "FROM NGUOIPHANANH NPA INNER JOIN DONPHANANH DPA ON NPA.CMT=DPA.CMT " +
                "INNER JOIN DONDANGCHOXULY D ON DPA.ID_DON=D.ID_DON ORDER BY NGAY DESC";
        PreparedStatement pre=getConnection().prepareStatement(query);
        return pre.executeQuery();
    }

    public ResultSet getListNewPetition()throws SQLException{
        var query="SELECT TEN,NOISONG,DIENTHOAI,NGAY,PHANLOAI,NOIDUNGPHANANH " +
                "FROM NGUOIPHANANH NPA INNER JOIN DONPHANANH DPA ON NPA.CMT=DPA.CMT " +
                "INNER JOIN DONMOINHAN DMN ON DPA.ID_DON=DMN.ID_DON ORDER BY NGAY DESC";
        Connection connection=getConnection();
        PreparedStatement pre=connection.prepareStatement(query);
        return pre.executeQuery();
    }

    /*
     * -1: DON MOI NHAN
     * 0:  DON DANG CHO XU LY
     * 1:  DON DA XULY
     *
     * */
    public ResultSet getListPetitionForQuarterOfYear(int quarterOfYear,int year,int table) throws SQLException{
        String nameTable="";
        if(table==-1){
            nameTable="DONMOINHAN";
        }
        else if(table==0){
            nameTable="DONDANGCHOXULY";
        }
        else if(table==1){
            nameTable="DONDAXULY";
        }

        var query="SELECT TEN,NOISONG,DIENTHOAI,NGAY,PHANLOAI,NOIDUNGPHANANH \n" +
                "FROM NGUOIPHANANH NPA INNER JOIN DONPHANANH DPA ON NPA.CMT=DPA.CMT \n" +
                "INNER JOIN " +nameTable+" D ON DPA.ID_DON=D.ID_DON ";
        int count=0;
        if(year!=0){
            query+= " WHERE YEAR(NGAY)="+year;
            count++;
        }

        if(quarterOfYear!=0){
            int a=3*(quarterOfYear-1)+1;
            int b=3*quarterOfYear;
            if(count==0){
                query+= " WHERE ";
            }
            if(count!=0){
                query+=" AND ";
            }
            query+= " MONTH(NGAY) BETWEEN "+a+ " AND "+b;

        }
        query+= " ORDER BY NGAY DESC ";
        PreparedStatement pre=getConnection().prepareStatement(query);
        return pre.executeQuery();
    }

    public ResultSet searchPetition(String name, String phoneNumber, String day, String classify, int table) throws SQLException{
        String nameTable="";
        if(table==-1){
            nameTable="DONMOINHAN";
        }
        else if(table==0){
            nameTable="DONDANGCHOXULY";
        }
        else if(table==1){
            nameTable="DONDAXULY";
        }
        int count=0;
        var query="SELECT TEN,NOISONG,DIENTHOAI,NGAY,PHANLOAI,NOIDUNGPHANANH " +
                "FROM NGUOIPHANANH N INNER JOIN DONPHANANH DPA ON N.CMT=DPA.CMT " +
                "INNER JOIN "+nameTable+" D ON DPA.ID_DON=D.ID_DON";
        if(name.length()!=0){
            query+=" WHERE TEN= N'"+ name+"'";
            count++;
        }

        if(phoneNumber.length()!=0){
            if(count==0) query+=" WHERE ";
            if(count!=0){
                query+=" AND ";
            }
            query+=" DIENTHOAI='"+phoneNumber+"'";
            count++;
        }

        if(day.length()!=0){
            if(count==0) query+=" WHERE ";
            if(count!=0){
                query+=" AND ";
            }
            query+=" NGAY='"+day+"'";
            count++;
        }

        if(classify.length()!=0){
            if(count==0) query+=" WHERE ";
            if(count!=0){
                query+=" AND ";
            }
            query+=" PHANLOAI=N'"+classify+"'";
        }

        PreparedStatement pre=getConnection().prepareStatement(query);
        ResultSet resultSet=pre.executeQuery();
        return resultSet;
    }


}
