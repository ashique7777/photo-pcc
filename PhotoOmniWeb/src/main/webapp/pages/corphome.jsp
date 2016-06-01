<%@ page import="java.util.Date,java.io.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="req" value="${pageContext.request}" />
<c:set var="url">${req.requestURL}</c:set>
<c:set var="uri" value="${req.requestURI}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%-- <base href="${pageContext.request.contextPath}/"> --%>
<base href="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}${req.contextPath}/">
<title>PictureCare - Central</title>
<script>
	var appContextPath = "${pageContext.request.contextPath}" + '/servlet';
	var photoOmniOmsPath = '/PhotoOmniOMSWeb';
</script>
    <link rel="stylesheet" type="text/css" href="res/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="res/css/flat-ui.min.css">
    <link rel="stylesheet" type="text/css" href="res/css/walgreens.css">
    <link rel="stylesheet" type="text/css" href="res/css/styles.css">
    
    <script src="res/js/vendor/angular.js"></script>
    <script src="res/js/vendor/angular-animate.js"></script>
    <script src="res/js/vendor/angular-ui-router.js"></script>
    <script src="res/js/vendor/ui-bootstrap-tpls-0.12.0.min.js"></script>
    <script src="res/js/main.js"></script>
    <script src="res/js/utils.js"></script>
    <script src="res/js/dashboard.js"></script>
    <script src="res/js/report.js"></script>
    <script src="res/js/centralAdmin.js"></script>    
    <script src="res/js/window.ui.directive.js"></script>  
</head>
<body ng-app="walgreenReports">
    <jsp:include page="./includes/header.jsp"/>
    <section>
       <div ui-view></div>
    </section>
</body>

</html>