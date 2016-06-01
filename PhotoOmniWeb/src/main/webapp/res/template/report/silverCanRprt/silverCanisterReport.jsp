<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="StoreReports">
<head>
<meta charset="UTF-8">
<title>Reports : Centralization</title>
<jsp:include page="/pages/includes/storeHeaderResource.jsp"/>
    
</head>
<body>
 <div ng-controller="SilverCanisterController">
 <div class="ticket-header btn-info">Silver Canister Report</div>
	<div ng-repeat="error in errorMessageText">
	    <div ng-show="showErrorContainer" class="error-container">
			<section class="alert wag-alert alert-danger" role="alert">
				<span class="icon-Alert"></span>
				<span><strong>Error:</strong> {{error}}</span>
			</section>
	  	</div>
	</div>
	<div ng-show="showNoDataFound" class="error-container">
 		<section class="alert wag-alert alert-info" role="alert">
			<span class="icon-Info text-primary"></span>
			<span><strong></strong> No data found for the search criteria </span>
		</section>
 	</div>
	<div class="paddingTopBottom10">
	   <div class="row">
		  <div class="col-md-3 paddingTopBottom3">
			<div class="input-group">
			  <span class="input-group-addon" >
				Start Date 
			  </span>
              <input type="hidden"  id="storeNumber"  value ="${storeNumber}"  />
			  <input type="text" class="form-control start cursor" placeholder="MM/DD/YYYY" datepicker-popup={{format}} ng-model="silverCanisterRprt.startDate" is-open="workspace.openedStart"  ng-click="open($event,'start')"  max-date="endMaxDate"/>
			</div>
		 </div>
		 <div class="col-md-3 paddingTopBottom3">
			<div class="input-group">
				<span class="input-group-addon" >
				End Date 
			   </span>
			  <input type="text" class="form-control end cursor" placeholder="MM/DD/YYYY" datepicker-popup={{format}} ng-model="silverCanisterRprt.endDate" is-open="workspace.openedEnd"  ng-click="open($event,'end')"  max-date="endMaxDate"/>
			</div>
		  </div>
		  <div class="col-md-3 paddingTopBottom3">
				<button class="btn btn-info btn-xs wag-btn-primary-sm" ng-click="updateData()" >Generate Report</button>
			</div>
		</div>
       <br/>
       <br/>
    </div> 
    <div class="loading"  style="display: none;">
		<div class="loadingImg" style="display: none;"><img src="res/images/ajax-loader.gif" alt="Loading..."  style="display: none;"/><!--<br />Loading Please Wait--></div>
		<div class="loadingOverlay" style="display: none;"></div>
	</div>
    <div class="storeView"  ng-show="showTableData">
	     <div class="row paddingTopBottom3 ">
		    <div class="col-md-6">
			  <span>
				<b>Store: {{silverCanisterRprt.location}} , {{workspace.data[0].SilverCanisterStoreDetails.storeAddress}}</b>
			  </span>
			</div>
			<div class="col-md-6">
			  <span class="pull-right">
				<b>Start Date {{silverCanisterRprt.startDate | date : "MM-dd-yyyy"}} TO End Date {{silverCanisterRprt.endDate | date : "MM-dd-yyyy"}}</b>
			  </span>
			</div>
		</div>
		
		<div class="row">
    		<!-- <button class="btn btn-default btn-xs marginTopMin20 paddingT7L11 wag-btn-default-sm ng-scope pull-left" ng-click="printPMByEmpRpt()">Print</button> -->
			<div class="pull-right">
		   		<wgpagination start-index="1" total-page-count="{{workspace.totalPageCount}}" id="{{$index}}" show-table-data="showTableData" ng-click="changeStorePagination()"></wgpagination>
			</div> 
    	</div>
         <div class="row filter-buttons">
           <table class="table table-condensed table-hover table-bordered sortableTable">
             <thead>
	           <tr>
				    <th>Company</th>
					<th ng-click="sort('lastCanisterChangeDate')">
						<span class="pull-left">Last Canister Change Date</span>
						<span class="indicator icon-ArrowDown wag-icon-arrow-gray pull-left downArrow" ng-class="colSortOrder['lastCanisterChangeDate'].order ? 'selected' : ''"></span>
						<span class="indicator icon-ArrowUp wag-icon-arrow-gray pull-left upArrow" ng-class="!colSortOrder['lastCanisterChangeDate'].order && !colSortOrder['lastCanisterChangeDate'].firstLoad ?' selected' : ''"></span>
					</th>
					<th>Description</th>
	           </tr>
	         </thead>
	         <tbody>
	           <tr ng-repeat="data in workspace.data">
		        <td>{{data.SilverCanisterStoreDetails.silverCompany}}</td>
		        <td>{{data.SilverCanisterStoreDetails.lastCanisterChangeDate}}</td>
		        <td>{{data.SilverCanisterStoreDetails.serviceDescription}}</td>
	           </tr>
	         </tbody>
           </table>
          </div>
	</div>
       
 </div>
</body>
</html>