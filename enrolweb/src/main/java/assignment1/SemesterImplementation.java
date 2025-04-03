package assignment1;

import java.sql.Statement;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.w3c.dom.Document;

public class SemesterImplementation implements SemesterInterface{
    private static DataSource dataSource;

    static{
       try{
            String databaseConfig = "databaseConfig.xml";
            File file = new File(databaseConfig);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file.getAbsolutePath());
            String jdbc = document.getElementsByTagName("jdbcDriver").item(0).getTextContent();
            String databaseURL = document.getElementsByTagName("databaseURL").item(0).getTextContent();
            String usr = document.getElementsByTagName("user").item(0).getTextContent();
            String pwd = document.getElementsByTagName("password").item(0).getTextContent();
     
            // Setting the connection pool properties
            PoolProperties p = new PoolProperties();
            p.setUrl(databaseURL);
            p.setDriverClassName(jdbc);
            p.setUsername(usr);
            p.setPassword(pwd);
            // You can set additional pool properties
            p.setMaxActive(100); // Maximum number of connections in the pool
    
            // Setting the data source with the pool properties defined above
            dataSource = new DataSource();
            dataSource.setPoolProperties(p);
        }
        catch(Exception e){
            e.printStackTrace();
        } 
    }
    
    @Override
    public void addSemester(Semester semester) {
        String sql = "Insert INTO semester(semesterID, semester, year, openForEnrolment) Values (?, ?, ?, ?)";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, semester.getSemesterID());
            statement.setInt(2, semester.getSemester());
            statement.setInt(3, semester.getYear());
            statement.setInt(4, semester.getOpenForEnroll());
            statement.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Semester getSemester(int semesterID) {
        String sql = "Select * from semester where semesterID = ?";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            
            statement.setInt(1, semesterID);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                return new Semester(resultSet.getInt("semesterID"), resultSet.getInt("semester"),
                    resultSet.getInt("year"), resultSet.getInt("openForEnrolment"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Semester> getAllSemesters() {
        ArrayList<Semester> semesters = new ArrayList<>();
        String sql ="Select * from semester";
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);){
            
            while (resultSet.next()) {
                semesters.add(new Semester(resultSet.getInt("semesterID"), resultSet.getInt("semester"),
                resultSet.getInt("year"),resultSet.getInt("openForEnrolment")));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return semesters;
    }

    @Override
    public void updateSemester(Semester semester) {
        String sql = "Update semester Set semester = ?, year = ?, openForEnrolment = ? where semesterID = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, semester.getSemester());
            statement.setInt(2, semester.getYear());
            statement.setInt(3, semester.getOpenForEnroll());
            statement.setInt(4, semester.getSemesterID());
            statement.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteSemester(int semesterID) {
        String sql = "Delete From semester Where semesterID = ?";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            
            statement.setInt(1, semesterID);
            statement.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
