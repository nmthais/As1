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

public class CourseImplementation implements CourseInterface{
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
    public void addCourse(Course course) {
        String sql = "Insert Into Course(courseID, cName, credits) values (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            
            statement.setString(1, course.getCourseID());
            statement.setString(2, course.getCourseName());
            statement.setInt(3, course.getCredits());
            statement.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Course getCourse(String courseID) {
        String sql = "Select * from course where courseID = ?";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            
            statement.setString(1, courseID);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                return new Course(resultSet.getString("courseID"), 
                    resultSet.getString("cName"), resultSet.getInt("credits"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Course> getAllCourse() {
        ArrayList<Course> courseList = new ArrayList<>();
        String sql = "Select * from Course";
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);){
            
            while (resultSet.next()) {
                courseList.add(new Course(resultSet.getString("courseID"), 
                resultSet.getString("cName"), resultSet.getInt("credits")));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courseList;
    }

    @Override
    public void udpateCourse(Course course) {
        String sql = "Update course Set cName = ?, credits = ? where courseID = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, course.getCourseName());
            statement.setInt(2, course.getCredits());
            statement.setString(3, course.getCourseID());
            statement.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCourse(String courseID) {
        String sql = "Delete From course Where courseID = ?";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            
            statement.setString(1, courseID);
            statement.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
