
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Database {

    private static Database db = null;

    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private final String CONN = "jdbc:mysql://localhost/project";
    private static Connection conn = null;
    private static Statement stmt = null;

    Database() {
        createConn();
        setupVoterTable();
        setupCandidTable();
//        setupResultTable();
    }

    public Database getConn() {
        db = new Database();
        return db;
    }

    void createConn() {
        try {
            conn = DriverManager.getConnection(CONN, USERNAME, PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setupVoterTable() {
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "VOTER", null);

            if (!tables.next()) {
                String createTable = "CREATE TABLE `project`.`voter` ( "
                        + "`voter_no` INT NOT NULL AUTO_INCREMENT , "
                        + "`voter_name` VARCHAR(50) NOT NULL , "
                        + "`voter_id` INT(11) NOT NULL , "
                        + "`password` VARCHAR(100) NOT NULL , "
                        + "`given` INT NOT NULL DEFAULT 0 , "
                        + "PRIMARY KEY (`voter_no`))";
                stmt.execute(createTable);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setupCandidTable() {
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "CANDID", null);

            if (!tables.next()) {
                String createTable = "CREATE TABLE `project`.`candid` ( "
                        + "`candid_no` INT NOT NULL AUTO_INCREMENT , "
                        + "`candid_name` VARCHAR(50) NOT NULL , "
                        + "`vote_count` INT NOT NULL DEFAULT '0', "
                        + "`icon` MEDIUMBLOB, "
                        + "PRIMARY KEY (`candid_no`))";
                stmt.execute(createTable);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void addCandit(String name, File file){
        System.out.println(file.length());
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        String qu = "INSERT INTO candid(`candid_name`, `icon`) VALUES(?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(qu);
            pstmt.setString(1, name);
            pstmt.setBinaryStream(2, (InputStream) fis, (int) file.length());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

//    void setupResultTable() {
//        try {
//            stmt = conn.createStatement();
//            DatabaseMetaData dbm = conn.getMetaData();
//            ResultSet tables = dbm.getTables(null, null, "RESULT", null);
//
//            if (!tables.next()) {
//                String createTable = "CREATE TABLE `project`.`result` ( "
//                        + "`candid_no` INT NOT NULL AUTO_INCREMENT , "
//                        + "`vote` INT NOT NULL , "
//                        + "PRIMARY KEY (`candid_no`))";
//                stmt.execute(createTable);
//            }
//
//        } catch (SQLException ex) {
//            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    public ResultSet execQuery(String qu) {
        ResultSet rs;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(qu);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return rs;
    }

    public boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean execUpdate(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(qu);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}