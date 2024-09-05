<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f8f9fa;
        }
        .error-container {
            max-width: 600px;
            margin: 50px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        }
        h1 {
            color: #d9534f; /* Bootstrap's danger color for error messages */
        }
        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            background-color: #007bff; /* Bootstrap's primary color for buttons */
            color: #fff;
            text-decoration: none;
        }
        .btn:hover {
            background-color: #0056b3; /* Darker shade on hover */
        }
    </style>
</head>
<body>
    <div class="error-container">
        <h1>Error ${pageContext.errorData.statusCode}</h1>
        <a href="/" class="btn">Back to Home</a>
        <a href="/your-custom-path" class="btn">Custom Redirect</a>
    </div>
</body>
</html>
