<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Modify Member</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 400px;
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
        input[type="email"],
        input[type="submit"] {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border-radius: 3px;
            border: 1px solid #ccc;
        }

        input[type="submit"] {
            background-color: #007bff;
            color: #fff;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }

        input[type="submit"]:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Modify Member</h1>
        <c:if test="${loggedInMember != null}">
            <form:form method="POST" modelAttribute="modifiedMember" action="/members/${member.id}/modify">
                <label for="name">Name:</label>
                <form:input type="text" id="name" path="name" value="${member.name}" /><br/>
                <label for="surname">Surname:</label>
                <form:input type="text" id="surname" path="surname" value="${member.surname}" /><br/>
                <label for="emailId">Email:</label>
                <form:input type="email" id="emailId" path="emailId" value="${member.emailId}" /><br/>
                <input type="hidden" id="jwtToken" name="jwtToken" value="${loggedInMember.jwtToken}">
                <input type="submit" value="Update Member" />
            </form:form>
        </c:if>
    </div>
</body>
</html>
