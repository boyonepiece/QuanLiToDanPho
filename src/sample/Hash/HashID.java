package sample.Hash;

import sample.Database;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class HashID {
    private int workload;
    public  HashID(){
        this.workload=12;
    }
    public String hash(String peopleID) {
        String salt = BCrypt.gensalt(workload);
        String hashed_password = BCrypt.hashpw(peopleID, salt);
        return(hashed_password);
    }
    public int getWorkload(){
        return this.getWorkload();
    }
    public void setWorkload(int newWordLoad){
        this.workload=newWordLoad;
    }

    public boolean checkPeopleIDExist(String peopleID,String peopleIDHash) throws SQLException {
        if(peopleIDHash==null){
            return false;
        }
        boolean peopleIDVerified = false;

        if(null == peopleIDHash || !peopleIDHash.startsWith("$2a$"))
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");

        peopleIDVerified = BCrypt.checkpw(peopleID, peopleIDHash);

        return peopleIDVerified;
    }
}
