<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hello JSP!</title>
</head>
<body>
<table>
<c:forEach var="row" items="${myList}">
  <tr>
    <td>${row.testId}</td>
    <td>${row.testName}</td> 
  </tr>
</c:forEach>
</table>
</body>
</html>