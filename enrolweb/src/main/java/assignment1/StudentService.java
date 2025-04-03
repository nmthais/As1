package assignment1;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.w3c.dom.Document;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class StudentService {
    private CourseImplementation courseImp = new CourseImplementation();
    // private SemesterImplementation semImp = new SemesterImplementation();
    private StudentImplementation stuImp = new StudentImplementation();
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

    //Called only when tomcat server starts, hash all current password using argon2
    public void hashPassAll() {
        ArrayList<Student> students = stuImp.getAllStudents();
        for(Student s : students){
            // Generate salt and password hash
            PasswordSalt pSalt = new PasswordSalt();
            s.setPasswordSalt(pSalt.genSalt());
            s.setPasswordHash(pSalt.hashPass(s.getPasswordHash(), s.getPasswordSalt()));
            stuImp.hashPass(s);
        }
    }

    //check if the provided login credential matches with the one in database
    public Student checkLogin(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if(username == null || password == null || username.equals("") || password.equals("")){
            return null;
        }
        else{
            Student s = stuImp.getStudentByStdNo(username);
            if(s!=null){
                PasswordSalt pSalt = new PasswordSalt();
                if(pSalt.verifyPass(password, s)){
                    return s;
                }
            }
        }

        return null;
    }

    //Get offered courses from a provided semester
    public ArrayList<Course> checkOfferedCourse(int semesterID){
        ArrayList<String> courseIDList = new ArrayList<>();
        ArrayList<Course> courseList = new ArrayList<>();
        String sql =" Select * from courseOfferings Where semesterID = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, semesterID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                courseIDList.add(resultSet.getString("courseID"));
            }
            for(String courseID : courseIDList){
                courseList.add(courseImp.getCourse(courseID));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        return courseList;
    }

    //Enroll student into a course, reuturn true if enroll successfully
    public EnrollMessage StudentEnroll(Student student, ArrayList<String> courses, int semesterID){
        EnrollMessage enrollMessage = new EnrollMessage();
        String sql = "Insert into StudentCourseRegistration(stdNo, courseID, semesterID) Values (?, ?, ?) ";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            for(String courseID : courses){
                statement.setString(1, student.getStdNo());
                statement.setString(2, courseID);
                statement.setInt(3, semesterID);
                statement.executeUpdate();
            }
            enrollMessage.setisEnroll(true);
            enrollMessage.setEnrollMessage(getMessageSQL());
        } catch (SQLException eSQL) {
            enrollMessage.setisEnroll(false);
            enrollMessage.setEnrollMessage(eSQL.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return enrollMessage;
    }

    public String getMessageSQL(){
        String messageString= "";
        String sql = "Select * from Message";
            try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)){

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    messageString += resultSet.getString("message") + "\n";
                }
                System.out.println("messageString in method: " + messageString);
            }
            catch(Exception e){
                e.printStackTrace();
            }

        return messageString;
    }
}
