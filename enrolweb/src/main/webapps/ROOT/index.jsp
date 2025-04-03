<!DOCTYPE html>
<html>
    <head>
        <title>Log In</title>
        <img src="../images/uniX_AIgen.webp" alt="uniX logo">
        <link rel="stylesheet" href="/Code/UI_Style.css">
    </head>
    <body>
        <h1 id="h1" style="text-align: left;">Student Login</h1>
        <div id="LoginUI">
            <div id="form_container">
                <form action="Login" method="post" class="column">
                    <label for="username">Username: </label>
                    <input type="text" id="username" name="username" autocomplete="username">
                    <label for="password">Password: </label>
                    <input type="password" id="password" name="password">
                    <input type="submit" value="Log In" class="loginbutton">
                </form>
                <img src="../images/866-200x300.jpg" id="randomimage" alt="randomimage" class="column">
            </div>
            <% String errorMessage = (String) request.getAttribute("invalidMessage"); %>
                <% if (errorMessage != null) { %>
                    <p class="errormessage" style="color: red; font-size: 20px; text-indent: 5%;"><%= errorMessage %></p>
                <% } %>
        </div>
    </body>

</html>
