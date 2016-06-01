<%-- <jsp:include page="${pageContext.request.contextPath}/pages/includes/storeReportHeaders.jsp"/> 
<jsp:include page="${pageContext.request.contextPath}/src/main/webapp/res/template/report/pmbyemp/storeReportHeaders.jsp"/> --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html ng-app="StoreReports">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Reports : Centralization</title>
<jsp:include page="/pages/includes/storeHeaderResource.jsp"/>
    
</head>
<body>
	<div ng-controller="fetchExceptionData">
        <div class="ticket-header btn-info">Exception Report</div>
        <input type="hidden" id="storeNumber"  value ="${storeNumber}"  />
        
        <div ng-show="showFilter">
        	<div ng-include="workspace.filterTemplate"></div>
        </div>
        <div class="loading"  style="display: none;">
			<div class="loadingImg" style="display: none;"><img src="res/images/ajax-loader.gif" alt="Loading..."  style="display: none;"/><!--<br />Loading Please Wait--></div>
			<div class="loadingOverlay" style="display: none;"></div>
		</div>
 		<div ng-include="workspace.urlStoreTable"></div>
	</div>
</body>
</html>