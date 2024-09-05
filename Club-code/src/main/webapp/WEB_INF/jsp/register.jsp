<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Member Registration</title>
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
        input[type="password"],
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

        a {
            text-decoration: none;
            color: #007bff;
        }

        a:hover {
            color: #0056b3;
        }

        .links {
            margin-top: 10px;
        }

        .links a {
            margin-right: 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Member Registration</h1>
        <form action="/register" method="post">
            <label for="name">Name:</label>
            <input type="text" id="name" name="name" required><br>
            <label for="name">Surname:</label>
            <input type="text" id="surname" name="surname" required><br>
            <label for="emailId">Email:</label>
            <input type="text" id="emailId" name="emailId" required><br>
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required><br>
            <input type="submit" value="Register">
        </form>
        <div class="links">
            <a href="/login">Login</a> | <a href="/reset-password">Reset Password</a>
        </div>
    </div>
</body>
</html>
