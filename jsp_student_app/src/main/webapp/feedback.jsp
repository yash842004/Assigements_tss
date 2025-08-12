<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tss.model.Feedback" %>
<html>
<head>
    <title>Feedback Form</title>
</head>
<body>
    <h2>Submit Feedback</h2>
    <form action="submitFeedback" method="post">
        Name: <input type="text" name="name"><br>
        Date: <input type="date" name="date"><br>
        Session Content (1-10): <input type="number" name="sessionContent" min="1" max="10"><br>
        Query Resolution (1-10): <input type="number" name="queryResolution" min="1" max="10"><br>
        Interactivity (1-10): <input type="number" name="interactivity" min="1" max="10"><br>
        Impactful Learning (1-10): <input type="number" name="impactfulLearning" min="1" max="10"><br>
        Content Delivery (1-10): <input type="number" name="contentDelivery" min="1" max="10"><br>
        <input type="submit" value="Submit">
    </form>

    <%
        Feedback feedback = (Feedback) request.getAttribute("feedback");
        String error = (String) request.getAttribute("error");

        if (feedback != null) {
    %>
        <h3>Feedback Submitted:</h3>
        Name: <%= feedback.getName() %><br>
        Date: <%= feedback.getDate() %><br>
        Session Content: <%= feedback.getSessionContent() %><br>
        Query Resolution: <%= feedback.getQueryResolution() %><br>
        Interactivity: <%= feedback.getInteractivity() %><br>
        Impactful Learning: <%= feedback.getImpactfulLearning() %><br>
        Content Delivery: <%= feedback.getContentDelivery() %><br>
    <%
        }

        if (error != null) {
    %>
        <p style="color:red;"><%= error %></p>
    <%
        }
    %>
</body>
</html>
