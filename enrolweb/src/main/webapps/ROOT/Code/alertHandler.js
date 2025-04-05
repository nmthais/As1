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

// document.getElementById("searchButton").addEventListener("click", function() {
// let searchValue = document.getElementById("searchBar").value;
// searchInput(searchValue);
// });

// function searchInput(searchValue) {
//     let courses = document.querySelectorAll(".choices"); // Select all course choices
//     courses.forEach(course => {
//         let label = course.querySelector("label").textContent.toLowerCase();
//         if (label.includes(searchValue.toLowerCase())) {
//             course.style.display = "block"; // Show matching courses
//             course.style.backgroundColor = "rgb(202, 202, 202)";
//         } else {
//             course.style.display = "none"; // Hide non-matching courses
//         }
//     });
// }