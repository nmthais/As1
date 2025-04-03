<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Choose your semester</title>
        <link rel="stylesheet" href="/Code/ChoosingSem_Style.css">
    </head>
    <body>
        <div id="topbar" class="header">
            <h1>Choose your semester</h1>
            <div class="userinfo">
                <span class="student">Hi, ${student.givenNames} ${student.lastName}</span><!--get student name and student photo if available-->
                <img src="/images/student_photo.png" alt="Student Photo" class="studentphoto">
                <a href="/Logout" class="logout">Log Out</a>
            </div>
        </div>
        
        <div class="studentinfo">
            <img src="student_photo.png" alt="Student Photo" class="studentphoto">
            <div class="student_info_text">
                <b>${student.givenNames} ${student.lastName}</b>
                <p>ID: ${student.stdNo}</p>
                
            </div>

            
        </div>
        <div id="form_container">
            <b>Choosing Semester: </b>
            <form action="ChoosingSem" method="post">
                
                <%@ page import="assignment1.Semester" %>
                <%@ page import="java.util.ArrayList" %>
                
                <% ArrayList<Semester> semestersL =  (ArrayList<Semester>) session.getAttribute("semesters");%>
                <% if(semestersL !=null){ %>
                    <%for(Semester sem : semestersL){ %>
                    <%int index = sem.getSemester(); %> 
                    <%int year = sem.getYear(); %>  
                    <div class="choices">
                        <input type="radio" id="Semester <%=index%> year <%=year%>" name="semester" value="<%= sem.getSemesterID()%> - Semester <%=index%> year <%=year%>" onclick="updateSelection(this)">
                        <label for="Semester <%=index%> year <%=year%>">Semester <%= sem.getSemester() %> Year <%= sem.getYear()%> </label> 
                    </div>
                    <%}%>
                <%}%>
                    
                <div id="submitregion">
                    <div class="submitbox">
                        <p>Currently choosing <span id="selectedSemester" style=" display: none">none</span> </p>
                        <input type="submit" value="Confirm" class="submitbutton" id="submit" onclick="checkSelection()">
                    </div>
                </div>
            </form>
        </div>
    </body>
    <script>
        function checkSelection(){
            if(document.getElementById("selectedSemester").textContent =="none"){
                alert("Please choose a semester before proceeding");
                event.preventDefault();
            }
        }
        function updateSelection(radio){
            const submitbutton = document.getElementsByClassName("submitbutton")[0];

            submitbutton.style.border = "rgb(174, 44, 250) 1px solid";
            submitbutton.style.backgroundColor = "rgb(237, 218, 255)";
            submitbutton.style.cursor = "pointer";
            document.getElementById("selectedSemester").style.display = "";
            document.getElementById("selectedSemester").textContent = radio.value;
        }
    </script>
</html>
