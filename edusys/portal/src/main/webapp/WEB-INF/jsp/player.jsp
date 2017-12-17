<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <meta charset="utf-8">
    <title>player</title>
</head>
<body style="background-color:white;padding:0px;margin:0px;">
<iframe name="myframe" frameborder='0' width='100%' height='100%' marginheight='0' marginwidth='0' scrolling='yes' src='${url}'></iframe>
<jsp:include page="common/js.jsp" flush="true"/>
</body>
</html>
