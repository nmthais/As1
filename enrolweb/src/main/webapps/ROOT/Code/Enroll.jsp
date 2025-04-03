<!DOCTYPE html>
<html>
    <head>
        <title>Enrollment</title>
        <link rel="stylesheet" href="/Code/Enroll_style.css">
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
                <b style="font-size: 30px;">Unfinished courses</b>
                <div class="searchdiv">
                    <input class="searchbar" id="searchBar">
                    <button class="searchbutton" id="searchButton" onclick="find(previousSibling.value)">Search</button>
                </div>
            </div>  
            
            <form id="form_container" action="/Enrollment" method="post">
                <%@ page import="assignment1.Course" %>
                <%@ page import="java.util.ArrayList" %>
                
                <% ArrayList<Course> courses = (ArrayList<Course>) session.getAttribute("courses");%>
                <% if(courses !=null){ %>
                    <% for(Course course : courses){ %>
                        <div class="choices">
                            <input type="checkbox" id="<%=course.getCourseID()%>" name="course[]" value="<%=course.getCourseID()%>" onclick="updateSelection(this)">
                            <label for="<%=course.getCourseID()%>"><%=course.getCourseID()%> - <%=course.getCourseName()%> </label>
                        </div>
                    <% }%>
                <%}%>
                
                <div id="submitregion">
                    <div class="submitbox">
                        <p>Currently choosing <span id="selectCourses" style=" display: none"></span> </p>
                        <input type="submit" value="Confirm" class="submitbutton" id="submit" onclick="checkSelection()">
                    </div>
                </div>
            </form>
        </div>

        <%if(request.getAttribute("enroll") != null ){ %>
            <script>
                displayAlert();
            </script>
            <%boolean isEnrolled = (boolean) request.getAttribute("enroll");
            if(isEnrolled){ %>
                <div id="alert">
                    <p>Enrolled successfully!!</p>
                    <% if(!request.getAttribute("eMessage").equals("")) {%>
                    <p style="color: rgb(0, 0, 164); font-size: 80%;"> <%=request.getAttribute("eMessage")%></p>
                    <% } %>
                    <button onclick="location.href='/Logout'">Log Out</button>
                </div>
            <% }else { %>
                <div id="alert">
                    <p>Enrolled unsuccessfully</p>
                    <p style="color: crimson; font-size: 80%;">Reason: <%=request.getAttribute("eMessage")%></p>
                    <button onclick="hideAlert()">Confirm</button>
                </div>
            <% } %>
        <% } else { %>
            <script>
                hideAlert();
            </script>
        <% }%>
        
    </body>

    <script>
        function displayAlert(){
            document.getElementById("alert").classList.remove("alerthidden");
            document.getElementById("alert").classList.add("alertdisplay");
        }
        function hideAlert(){
            document.getElementById("alert").classList.remove("alertdisplay");
            document.getElementById("alert").classList.add("alerthidden");
        }
        const selections = new Array();
        function checkSelection(){
            if(selections.length ==0){
                alert("Please choose a semester before proceeding");
                event.preventDefault();
            }
        }
        
        function updateSelection(checkbox){
            let submitbutton = document.getElementsByClassName("submitbutton")[0];
            let index = selections.indexOf(checkbox.value);
            if(index !== -1){
                selections.splice(index, 1);
            }
            else{
                selections.push(checkbox.value);
            }
            console.log(selections);

            if(selections.length >0){
                document.getElementById("selectCourses").style.display = "inline";
                document.getElementById("selectCourses").innerHTML = selections;          
            }
            else{
                document.getElementById("selectCourses").style.display = "none";
            }
            
            
            if(selections.length !==0){
                submitbutton.style.border = "rgb(174, 44, 250) 1px solid";
                submitbutton.style.backgroundColor = "rgb(237, 218, 255)";
                submitbutton.style.cursor = "pointer";
            }
            else if(selections.length == 0){
                submitbutton.style.border = "rgb(160, 160, 160) 1px solid";
                submitbutton.style.backgroundColor = "rgb(202, 202, 202)";
                submitbutton.style.cursor = "not-allowed";
            }
        }

        document.getElementById("searchButton").addEventListener("click", function() {
        let searchValue = document.getElementById("searchBar").value;
        searchInput(searchValue);
        });

        function searchInput(searchValue) {
            let courses = document.querySelectorAll(".choices"); // Select all course choices
            courses.forEach(course => {
                let label = course.querySelector("label").textContent.toLowerCase();
                if (label.includes(searchValue.toLowerCase())) {
                    course.style.display = "block"; // Show matching courses
                } else {
                    course.style.display = "none"; // Hide non-matching courses
                }
            });
        }

    </script>
</html>
