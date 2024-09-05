<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Visitor Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
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

        .btn-primary:hover {
            background-color: #0056b3;
        }

        .search-input {
            padding: 10px; /* Increased padding for bigger search bar */
            margin-bottom: 10px;
            border-radius: 4px;
            border: 1px solid #ccc;
            width: 80%; /* Adjusted width for better visibility */
            max-width: 400px; /* Limiting max-width to prevent overly large search bars */
            box-sizing: border-box; /* Include padding in width calculation */
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }

        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
    <div class="navbar">
        <a href="#">Home</a>
        <a href="categories">Categories</a>
        <a href="reset-password">Password Reset</a>
        <a href="login">Login</a>
        <a href="register">Register</a>
    </div>

    <div class="control-plane">
        <!-- No buttons needed for visitors -->
    </div>

    <h1 style="margin-left: 20px;">Welcome to the Visitor Page</h1>

    <h3 style="margin-left: 20px;">Categories</h3>

    <input type="text" id="searchCategoriesInput" class="search-input" onkeyup="filterCategories()" placeholder="Search categories by name...">
    <table>
        <thead>
            <tr>
                <th>Name</th>
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
                            <button type="submit" class="btn btn-primary">View Outing</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <h3 style="margin-left: 20px;">Outings</h3>

    <input type="text" id="searchOutingsInput" class="search-input" onkeyup="filterOutings()" placeholder="Search outings by name...">
    <table>
        <thead>
            <tr>
                <th>Name</th>
                <th>Description</th>
                <th>Date</th>
            </tr>
        </thead>
        <tbody id="outingsTableBody">
            <c:forEach var="outing" items="${outings}">
                <tr>
                    <td>${outing.name}</td>
                    <td>${outing.description}</td>
                    <td>${outing.outingDate}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>




    <script>
    function logout(memberId, memberName) {
        // Make an AJAX call to the logout API endpoint with member ID or name
        fetch(`/logout?memberId=${memberId}&memberName=${memberName}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                // Add any other headers if needed
            },
        })
        .then(response => {
            if (response.ok) {
                // Redirect to the login page or perform any other action after successful logout
                window.location.href = '/login'; // Redirect to the login page
            } else {
                // Handle errors or display appropriate message
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

        function filterOutings() {
            var input, filter, table, tr, td, i, txtValue;
            input = document.getElementById("searchOutingsInput");
            filter = input.value.toUpperCase();
            table = document.getElementById("outingsTableBody");
            tr = table.getElementsByTagName("tr");

            for (i = 0; i < tr.length; i++) {
                td = tr[i].getElementsByTagName("td")[0]; // Assuming name is in the first column
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
                td = tr[i].getElementsByTagName("td")[0]; // Assuming name is in the first column
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
                td = tr[i].getElementsByTagName("td")[0]; // Assuming name is in the first column
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