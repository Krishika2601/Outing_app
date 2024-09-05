<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
        }
        .navbar {
            background-color: #333;
            overflow: hidden;
        }
        .navbar a {
            float: left;
            display: block;
            color: #f2f2f2;
            text-align: center;
            padding: 14px 16px;
            text-decoration: none;
        }
        .navbar a:hover {
            background-color: #ddd;
            color: black;
        }
        .control-plane {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin: 20px 0;
            padding: 10px;
            background-color: #f2f2f2;
        }
        .add-button {
            margin-right: 10px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        
        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            color: #fff;
        }

        .btn-primary {
            background-color: #007bff;
        }
  .btn-primary2 {
            background-color: #D22B2B;
        }
        .btn-primary:hover {
            background-color: #0056b3;
        }
 .btn-primary2 :hover {
            background-color: #0056b3;
        }

        .search-input {
            padding: 10px; 
            margin-bottom: 10px;
            border-radius: 4px;
            border: 1px solid #ccc;
            width: 80%; 
            max-width: 400px;
            box-sizing: border-box;
        }
        
    </style>
</head>
<body>
    <div class="navbar">
        <a href="#">Home</a>
        <a href="categories">Categories</a>
        <a href="reset-password">PassWord Reset</a>
     <a href="login" onclick="logout('${loggedInMember.id}', '${loggedInMember.name}')">Logout</a>
    </div>

  

    <h1>Welcome to the Dashboard</h1>
    <h2>Hello, ${loggedInMember.name}</h2>

    <h3>Categories</h3>
    
    <input type="text" class="search-input" id="searchCategoriesInput" onkeyup="filterCategories()" placeholder="Search categories by name...">
    <table>
        <thead>
            <tr>
                <th>Name</th>
                <th>View Details</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody id="categoriesTableBody">
            <c:forEach var="category" items="${categories}">
                <tr>
                    <td>${category.name}</td>
                    <td>
                        <form action="/categories/${category.id}" method="GET">
                            <input type="hidden" name="outingId" value="${category.id}">
                            <input type="hidden" id="jwtToken" name="jwtToken" value="${loggedInMember.jwtToken}">
                            <button type="submit" class="btn btn-primary" >View Outing</button>
                        </form>
                    </td>
          <td>
          <br>
    <form action="/categories/${category.id}" method="POST">
        
          <input type="hidden" id="jwtToken" name="jwtToken" value="${loggedInMember.jwtToken}">
        <button type="submit"  onclick="confirmAndSubmit2(${category.id})" class="btn btn-primary2">Delete Category</button>
    </form>
    </td>
      </tr>
            </c:forEach>
        </tbody>
    </table>
    <form action="/create-category" method="GET" class="add-button">
        <input type="hidden" id="jwtToken" name="jwtToken" value="${loggedInMember.jwtToken}">
        <button type="submit" class="btn btn-primary">Add Category</button>
    </form> 
    
    <br>

    <h3>Outings</h3>

    <input type="text" class="search-input" id="searchOutingsInput" onkeyup="filterOutings()" placeholder="Search outings by name...">
    <table>
        <thead>
            <tr>
                <th>Name</th>
                <th>Description</th>
                <th>Date</th>
                <th>Website</th>
                <th>Creator</th>
                <th>Action</th>
                <th>Delete</th>
            </tr>
        </thead>
        <tbody id="outingsTableBody">
            <c:forEach var="outing" items="${outings}">
                <tr>
                    <td>${outing.name}</td>
                    <td>${outing.description}</td>
                    <td>${outing.outingDate}</td>
                    <td>${outing.website}</td>
                      <td>${outing.createdBy.name}</td> 
                    <td>
                        <form action="/outings/modify/${outing.id}" method="GET">
                            <input type="hidden" name="outingId" value="${outing.id}">
                            <input type="hidden" id="jwtToken" name="jwtToken" value="${loggedInMember.jwtToken}">
                            <button type="submit" class="btn btn-primary" >Modify Outing</button>
                        </form>
                    </td>
                    <td>
                    <form id="deleteForm${outing.id}" action="/outing/${outing.id}" method="post">
                        <input type="hidden" name="_method" value="delete">
                        <input type="hidden" name="jwtToken" value="${loggedInMember.jwtToken}">
                        <button type="button" onclick="confirmAndSubmit(${outing.id})" class="btn btn-primary2" >Delete</button>
                    </form>
                </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <br>
        <form action="/createOuting" method="GET" class="add-button">
        <input type="hidden" id="jwtToken" name="jwtToken" value="${loggedInMember.jwtToken}">
        <button type="submit" class="btn btn-primary" >Add Outing</button>
    </form>
    <br>
    

    <h3>Members</h3>
    <input type="text" class="search-input" id="searchMembersInput" onkeyup="filterMembers()" placeholder="Search members by name...">
    <table>
        <thead>
            <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Outings</th>
                <th>Action</th>
                <th>Delete Member</th>
                
            </tr>
        </thead>
        <tbody id="membersTableBody">
            <c:forEach var="member" items="${members}">
                <tr>
                    <td>${member.name} ${member.surname}</td>
                    <td>${member.emailId}</td>
                    <td>    <h4>Outings</h4>
                    <ul>
                        <c:forEach var="outing" items="${member.outings}">
                            <li>${outing.name} - ${outing.description}</li>
                        </c:forEach>
                    </ul></td>
                    <td>
       <form action="/members/${member.id}/modify" method="GET">
                            <input type="hidden" name="" value="${member.id}">
                            <input type="hidden" id="jwtToken" name="jwtToken" value="${loggedInMember.jwtToken}">
                            <button type="submit" class="btn btn-primary" >Modify Member</button>
                        </form>
    </td>
 <td>
                    <form id="deleteForm${member.id}" action="/members/${member.id}" method="post">
                        <input type="hidden" name="_method" value="delete">
                        <input type="hidden" name="jwtToken" value="${loggedInMember.jwtToken}">
                        <button type="button" onclick="confirmAndSubmit(${member.id})" class="btn btn-primary2" >Delete</button>
                    </form>
                </td>
 
                </tr>
               
            </c:forEach>
        </tbody>
    </table>



    <script>
    function logout(memberId, memberName) {
        fetch(`/logout?memberId=${memberId}&memberName=${memberName}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
             
            },
        })
        .then(response => {
            if (response.ok) {
              
                window.location.href = '/login'; 
            } else {
               
                console.error('Logout failed');
            }
        })
        .catch(error => {
            console.error('Error during logout:', error);
        });
    }
    
    function confirmAndSubmit(memberId) {
        if (confirm('Are you sure you want to delete this member?')) {
            var form = document.getElementById('deleteForm' + memberId);
            form.submit();
        }
    }
    function confirmAndSubmit2(categoryId) {
        if (confirm('Are you sure you want to delete this category?')) {
            var form = document.getElementById('deleteForm' + memberId);
            form.submit();
        }
    }
        function filterOutings() {
            var input, filter, table, tr, td, i, txtValue;
            input = document.getElementById("searchOutingsInput");
            filter = input.value.toUpperCase();
            table = document.getElementById("outingsTableBody");
            tr = table.getElementsByTagName("tr");

            for (i = 0; i < tr.length; i++) {
                td = tr[i].getElementsByTagName("td")[0];
                if (td) {
                    txtValue = td.textContent || td.innerText;
                    if (txtValue.toUpperCase().indexOf(filter) > -1) {
                        tr[i].style.display = "";
                    } else {
                        tr[i].style.display = "none";
                    }
                }
            }
        }

        function filterCategories() {
            var input, filter, table, tr, td, i, txtValue;
            input = document.getElementById("searchCategoriesInput");
            filter = input.value.toUpperCase();
            table = document.getElementById("categoriesTableBody");
            tr = table.getElementsByTagName("tr");

            for (i = 0; i < tr.length; i++) {
                td = tr[i].getElementsByTagName("td")[0]; 
                if (td) {
                    txtValue = td.textContent || td.innerText;
                    if (txtValue.toUpperCase().indexOf(filter) > -1) {
                        tr[i].style.display = "";
                    } else {
                        tr[i].style.display = "none";
                    }
                }
            }
        }

        
        function filterMembers() {
            var input, filter, table, tr, td, i, txtValue;
            input = document.getElementById("searchMembersInput");
            filter = input.value.toUpperCase();
            table = document.getElementById("membersTableBody");
            tr = table.getElementsByTagName("tr");

            for (i = 0; i < tr.length; i++) {
                td = tr[i].getElementsByTagName("td")[0]; 
                if (td) {
                    txtValue = td.textContent || td.innerText;
                    if (txtValue.toUpperCase().indexOf(filter) > -1) {
                        tr[i].style.display = "";
                    } else {
                        tr[i].style.display = "none";
                    }
                }
            }
        }
    </script>
    
    </body>
    </html>
    
