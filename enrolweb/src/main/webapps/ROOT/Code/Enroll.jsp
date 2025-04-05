<!DOCTYPE html>
<html>
    <head>
        <title>Enrollment</title>
        <link rel="stylesheet" href="/Code/Enroll_style.css">
        <script src="/Code/alertHandler.js"></script>
    </head>
    <body>
        <div id="topbar" class="header">
            <h1>Enrollment</h1>
            <div class="userinfo">
                <span class="student">Hi, ${student.givenNames} ${student.lastName}</span><!--get student name and student photo if available-->
                <img src="/images/student_photo.png" alt="Student Photo" class="studentphoto">
                <a href="/Logout" class="logout">Log Out</a>
            </div>
        </div>
        
        <div id="enroll">
            <div id="searchdisplay">
                <b style="font-size: 30px;">Current semester: <%= session.getAttribute("semName")%></b>
                <div class="searchdiv">
                    <input class="searchbar" id="searchBar">
                    <button class="searchbutton" id="searchButton" onclick="find(previousSibling.value)">Search</button>
                </div>
            </div>  
            
            <form id="form_container" action="/Enrollment" method="post">
                
                <%@ page import="assignment1.Course" %>
                <%@ page import="java.util.ArrayList" %>
                
                <% ArrayList<Course> courses = (ArrayList<Course>) session.getAttribute("unfinishedCourses");%>
                <% if(courses !=null && !courses.isEmpty()){ %>
                    <b style="font-size: 30px;">Unfinished courses</b>
                    <% for(Course course : courses){ %>
                        <div class="choices">
                            <input type="checkbox" id="<%=course.getCourseID()%>" name="course[]" value="<%=course.getCourseID()%>" onclick="updateSelection(this)">
                            <label for="<%=course.getCourseID()%>"><%=course.getCourseID()%> - <%=course.getCourseName()%> </label>
                        </div>
                    <% }%>
                <%}%>

                <b style="font-size: 30px;">Enrolled Courses</b>
                    <% ArrayList<String> finishedCourses = (ArrayList<String>) session.getAttribute("finishedCourses"); %>
                    <% if (finishedCourses != null && !finishedCourses.isEmpty()) { %>
                        <ul>
                            <% for (String finishedCourse : finishedCourses) { %>
                                <li><%= finishedCourse %></li>
                            <% } %>
                        </ul>
                    <% } else { %>
                        <p>No enrolled courses.</p>
                    <% } %>
                
                <div id="submitregion">
                    <div class="submitbox">
                        <p>Currently choosing <span id="selectCourses" style=" display: none"></span> </p>
                        <input type="submit" value="Confirm" class="submitbutton" id="submit" onclick="checkSelection()">
                    </div>
                </div>
            </form>
        </div><!-- add list of enroolled course, update message as to which course enrolled successfully, which one needs more course completed-->

        <div id="alert" class="alerthidden">
        <% if(session.getAttribute("eMessage") !=null && (boolean) session.getAttribute("displayAlert")) {%>
            <script>
                console.log("block was run")
                displayAlert();
            </script>
                <p style="color: rgb(0, 0, 164); font-size: 80%;"> <%=session.getAttribute("eMessage")%></p>
                <button onclick="hideAlert()">Confirm</button>
            <%session.setAttribute("displayAlert", false);%>
        <%}else {%>
            <script>
                console.log("hidden was run")
                hideAlert();
            </script>
        <%}%>
        </div>

    </body>


</html>
