<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Modify Outing</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 600px;
            margin: 20px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        h1 {
            color: #333;
        }

        form {
            margin-top: 20px;
        }

        label {
            display: block;
            margin-bottom: 5px;
        }

        input[type="text"],
        input[type="url"],
        textarea,
        select {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border-radius: 3px;
            border: 1px solid #ccc;
        }

        button[type="submit"] {
            padding: 10px 20px;
            background-color: #007bff;
            color: #fff;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }

        button[type="submit"]:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Modify Outing</h1>
        <form action="/outings/${outingId}" method="POST">
            <input type="hidden" name="_method" value="PUT">

            <label for="name">Name:</label>
            <input type="text" id="name" name="name" value="${outing.name}" required><br>

            <label for="description">Description:</label>
            <textarea id="description" name="description" required>${outing.description}</textarea><br>

            <input type="hidden" id="jwtToken" name="jwtToken" value="${loggedInMember.jwtToken}">

            <label for="website">Website:</label>
            <input type="url" id="website" name="website" value="${outing.website}"><br>

            <label for="outingDate">Outing Date:</label>
            <input type="datetime-local" id="outingDate" name="outingDate" required><br>

            <label for="categoryId">Category:</label>
            <select id="categoryId" name="categoryId" required>
                <c:forEach var="category" items="${categories}">
                    <option value="${category.id}">${category.name}</option>
                </c:forEach>
            </select><br>

            <label for="memberName">Member Name:</label>
            <input type="text" id="memberName" name="memberName" required><br>

            <button type="submit">Save Changes</button>
        </form>
    </div>
</body>
</html>
