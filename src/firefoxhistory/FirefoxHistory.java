/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firefoxhistory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author falcu
 */
public class FirefoxHistory {

    /**
     * @param args the command line arguments
     */
    static String path = System.getenv("APPDATA") + "\\Mozilla\\Firefox\\Profiles";

    public ArrayList<Integer> idList = new ArrayList<>();
    public ArrayList<String> urlList = new ArrayList<>();

    public static void main(String[] args) throws Exception {

    }

    public void getHistory() throws Exception{

        File dirrectory = new File(path);
        File file = new File(dirrectory.getPath() + "\\" + dirrectory.list()[0]);
        String historySring = file.getPath() + "\\places.sqlite";

        File logFile = new File(System.getProperty("user.dir"), "log.txt");
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
        FileWriter fw = new FileWriter(logFile, true);
        BufferedWriter bw = new BufferedWriter(fw);

        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:" + historySring);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("SELECT * FROM 'moz_places'");
            while (rs.next()) {
                idList.add(rs.getInt("id"));
                urlList.add(rs.getString("url"));
                //System.out.println("id = " + rs.getInt("id") + " url = " + rs.getString("url"));
                bw.append("id = " + rs.getInt("id") + " url = " + rs.getString("url"));
                bw.newLine();

            }
            bw.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

}
