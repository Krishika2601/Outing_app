<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Category Details</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        h1, h2, h3 {
            color: #333;
        }

        ul {
            list-style-type: none;
            padding: 0;
        }

        li {
            margin-bottom: 10px;
            padding: 8px 12px;
            background-color: #f0f0f0;
            border-radius: 3px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Category Details</h1>
        <h2>Name: ${category.name}</h2>
        <h3>Outings:</h3>
        <ul>
            <c:forEach var="outing" items="${outings}">
                <li>${outing.name} - ${outing.description}</li>
            </c:forEach>
        </ul>
    </div>
</body>
</html>
