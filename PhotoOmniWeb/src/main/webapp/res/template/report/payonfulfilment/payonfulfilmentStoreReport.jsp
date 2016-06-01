<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html ng-app="StoreReports">
<head>

<title>Reports : Centralization</title>
    <jsp:include page="/pages/includes/storeHeaderResource.jsp"/>
</head>
<body>
	<div ng-controller="payonfulfillmentStore">
        <div class="ticket-header btn-info">Pay On Fulfillment Report</div>
        <!-- <h3>Employee Level Promotional Money Report</h3> -->
        <input type="hidden" id="storeNumber"  value ="${storeNumber}"  />
        
        <div ng-show="showFilter">
        	<div ng-include="workspace.filterTemplate"></div>
        </div>
        <div class="loading"  style="display: none;">
			<div class="loadingImg" style="display: none;"><img src="res/images/ajax-loader.gif" alt="Loading..."  style="display: none;"/><!--<br />Loading Please Wait--></div>
			<div class="loadingOverlay" style="display: none;"></div>
		</div>
 		<div ng-show="showTable">
 			<div ng-include="workspace.urlStoreTable"></div>
 		</div>
	</div>
</body>
</html>