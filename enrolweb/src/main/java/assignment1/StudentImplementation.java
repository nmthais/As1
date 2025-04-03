package assignment1;

import java.sql.PreparedStatement;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;


public class StudentImplementation implements StudentInterface{

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
    public void addStudent(Student student){
        String sql = "INSERT INTO student (stdNO, givenNames, lastName, passwordHash, passwordSalt) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getStdNo());
            statement.setString(2, student.getGivenNames());
            statement.setString(3, student.getLastName());
            statement.setString(4, student.getPasswordHash());
            statement.setDouble(5, student.getPasswordSalt());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Student getStudentByStdNo(String stdNo) {
        String sql = "SELECT * FROM student WHERE stdNo = ?";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, stdNo);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new Student(rs.getString("stdNo"), rs.getString("givenNames"), 
                    rs.getString("lastName"), rs.getString("passwordHash"), rs.getDouble("passwordSalt"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM student";
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql)) {
             
            while (rs.next()) {
                students.add(new Student(rs.getString("stdNo"), rs.getString("givenNames"), 
                        rs.getString("lastName"), rs.getString("passwordHash"), rs.getDouble("passwordSalt")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    @Override
    public void updateStudent(Student student) {
        String sql = "UPDATE student SET givenNames = ?, lastName = ? WHERE stdNo = ?";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getGivenNames());
            statement.setString(2, student.getLastName());
            statement.setString(3, student.getStdNo());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteStudent(String stdNo) {
        String sql = "DELETE FROM student WHERE stdNo = ?";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, stdNo);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hashPass(Student student) {
        String sql = "Update student Set passwordHash = ?, passwordSalt = ? where stdNo = ?";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
                
            statement.setString(1, student.getPasswordHash());
            statement.setDouble(2, student.getPasswordSalt());
            statement.setString(3, student.getStdNo());
            statement.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
