<!DOCTYPE html>
<html>
<head>
    <title>Common Page</title>
    <style>
        .pink-button {
            display: block;
            text-align: center;
            margin-top: 10px;
            color: #FFF;
            background-color: #FF69B4;
            padding: 8px 15px;
            border-radius: 5px;
            text-decoration: none;
            transition: background-color 0.3s;
        }

        .home-button {
            background-color: #ff6090;
            color: #fff;
            border: none;
            padding: 10px 20px;
            font-size: 16px;
            cursor: pointer;
            border-radius: 5px;
            transition: background-color 0.3s ease;
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 100;
        }

        .home-button:hover {
            background-color: #d15273;
        }
    </style>
</head>
<body>
<a class="home-button pink-button" href="${pageContext.request.contextPath}/HTML/Homepage.jsp">Home</a>

</body>
</html>
