<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html ng-app="StoreReports">
<head>

<title>Reports : Centralization</title>
    <jsp:include page="/pages/includes/storeHeaderResource.jsp"/>
</head>
<body ng-controller="PmByEmpController">
        <div class="ticket-header btn-info">  Employee Level Promotional Money Report</div>
        
<div ng-show="showEmployee">
	<div class="error-container" ng-show="showErrorContainer">
		<section class="alert wag-alert alert-danger" role="alert">
			<span class="icon-Alert"></span>
			<span><strong>Error: </strong>{{errorMessage}}</span>
		</section>
	</div>
	      <div class="paddingTopBottom10">
		   <div class="row">
			  <div class="col-md-3 paddingTopBottom3">
				<div class="input-group">
				  <span class="input-group-addon" >
					Start Date 
				  </span>
	              <input type="hidden"  id="selectedEmpId"  value ="${selectedEmpId}" />
	              <input type="hidden"  id="storeNumber"  value ="${storeNumber}"  />
	              <input type="hidden"  id="storeLevelReport"  value="${storeLevelReport}" />
				  <input type="text" class="form-control start" placeholder="MM/DD/YYYY" datepicker-popup={{format}} ng-model="workspace.filterData.startDate" is-open="workspace.pmByEmp.openedStart"  ng-click="open($event,'start')" max-date="endMaxDate" min-date="startMinDate" close-text="Close"  />
				</div>
			 </div>
			 <div class="col-md-3 paddingTopBottom3">
				<div class="input-group">
				 <span class="input-group-addon" >
					End Date 
				  </span>
				  <input type="text" class="form-control end" placeholder="MM/DD/YYYY" datepicker-popup={{format}} ng-model="workspace.filterData.endDate" is-open="workspace.pmByEmp.openedEnd"  ng-click="open($event,'end')"  max-date="endMaxDate" min-date="startMinDate" close-text="Close"  />
				</div>
			  </div>
			  <div class="col-md-3 paddingTopBottom3">
					<button class="btn btn-info btn-xs wag-btn-primary-sm" ng-click="submitData($event)" >Generate Report</button>
				</div>
			</div>
	       <br/>
	       <div class="row">
	         <div class="col-md-3 paddingTopBottom3 "  ng-show = "showprintpagination">
			  <button  class="btn btn-default btn-xs marginTopMin20 paddingT7L11 wag-btn-default-sm ng-scope" ng-click="printPMByEmpRpt()">Print</button>
		    </div>
		<div class="col-md-3 paddingTopBottom3 pull-right"  ng-show = "showprintpagination">
			  	<wgpagination class="pull-right" start-index="1" total-page-count="{{totalEmpPageCount}}" id="{{$index}}" show-table-data="showTableData" ng-click="changeStorePagination()"></wgpagination>
		</div>   
		</div>       
	 <div class="row">
	 <div class="col-md-3 paddingTopBottom3 "  ng-show = "showtable">
					<h4>PM Dollars Earned - By Employee </h4>
			  </div>
	 </div>
	      <div class="row">
	      
	          <div class="col-md-3 paddingTopBottom3"  ng-show = "showtableEmp">
				<div class="input-group">
				  <span>
					<b>Employee Name: {{workspace.employeeName}} </b>
				  </span>
				</div>
			  </div>
	         <div class="col-md-3 paddingTopBottom3 "  ng-show = "showtable">
				<div class="input-group" ng-class="{'pull-right': showtableEmp}">
				  <span>
					<b>Store:  {{workspace.storeNumber}} </b>
				  </span>
				</div>
			  </div>
	         <div class="col-md-3 paddingTopBottom3 pull-right" ng-show = "showtable">
				<div class="input-group">
				  <span>
					<b>Start Date {{workspace.filterData.startDate | date : "MM/dd/yyyy"}} TO End Date {{workspace.filterData.endDate | date : "MM/dd/yyyy"}}</b>
				  </span>
				</div>
			  </div>
	     </div>
	         <div class="row filter-buttons">
	           <table class="table table-condensed table-hover table-bordered sortableTable" ng-show = "showtable" >
	             <thead>
		           <tr class="active">
		           <th ng-click="sortEmpName('Employee Name')">
	            	<span class="pull-left">Employee Name</span>
					<span class="indicator icon-ArrowDown wag-icon-arrow-gray pull-left downArrow" ng-class="colSortOrder.order && !colSortOrder.firstLoad ? 'selected' : ''"></span>
					<span class="indicator icon-ArrowUp wag-icon-arrow-gray pull-left upArrow" ng-class="!colSortOrder.order && !colSortOrder.firstLoad ?' selected' : ''"></span>
				   </th>
			         <!-- <th><a><span style="cursor:pointer;" ng-click="sortField='employeeName'; reverse =! reverse">Employee Name</span></a></th> -->
			         <th>PM Dollars Earned</th>
			         <th>PM Earned Qty</th>
		           </tr>
		         </thead>
		         <tbody style="overflow: auto;">
		           <tr ng-repeat="data in workspace.data.pmByEmployeeDetailBeans | orderBy:sortField:reverse"  tooltip-animation="false" ng-show = "showDataTD">
			        <td ng-click="openPopup(data.employeeId, data.employeeName)"><a href="">{{data.employeeName}}</a></td>
			        <td>$ {{data.pmDollarEarned}}</td>
			        <td>{{data.pmEarnedQty}}</td>
		           </tr>
		           <tr ng-show = "showNoDataTD">
		             <td colspan="3">No Records Found Corresponding to the Search Criterion Entered</td>
	               </tr>
	               <tr ng-show = "showNoAuthTD">
		             <td colspan="3">You are not authorized to view PM Report by Employee</td>
	               </tr>
		           </tbody>
	              </table>
	          </div>
			</div>
</div>

<div ng-hide="showEmployee">
	<div ng-include="employeePopupTemplate"></div>
</div>
</body>
</html>
