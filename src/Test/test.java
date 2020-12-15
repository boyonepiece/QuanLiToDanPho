package Test;

import sample.Database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class test {
    public static void main(String[] args) throws SQLException {
        Database test=new Database();
        ResultSet result=test.getListPetitionForQuarterOfYear(3,1);
        while(result.next()){
            String name=result.getNString(1);
            System.out.println(name);
        }
    }
}
