<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<header>
    <div class="row paddingTopBottom10">
		<div class="col-md-2 col-sm-3 col-xs-3 pictureCare-store nopadding"> 
			<div class="row"><a class="navbar-brand store">PictureCare Store</a></div>
			<div class="row version-number"><a href="javascript:void(0);">Version</a>: 10.1.1</div>
		</div>
		<div class="col-md-7 col-sm-7 col-xs-7 store-logo nopadding">
			<div class="logo-img"><img src="res/images/logo.png" /></div>
			<div class="grayBg search-block advance-search pull-right">
				<form>
				  <div class="form-group">
					<label class="sr-only" for="search-box">Phone, Last Name, Envelope # or Kiosk Id</label>
					<div class="col-md-5 col-sm-5 col-xs-5  nopadding">
						<input type="text" class="form-control input-xs search-box" id="search-box" />
						<span class="glyphicon glyphicon-question-sign col-sm-hidden" popover-placement="bottom" popover="Phone, Last Name, Envelope # or Kiosk Id" popover-trigger="mouseenter" popover-append-to-body="true"></span>
					</div>
					

					<div class="col-md-2 col-sm-2 col-xs-2 nopadding">
						 <button type="submit" class="btn btn-default btn-xs">Search</button>
					</div>
					<div class="col-md-5 col-sm-5 col-xs-5 pull-right text-right nopadding ">
						 <a href="javascript:void(0);">Advanced Search</a>
						 <span class="glyphicon glyphicon-question-sign" popover-placement="bottom" popover="Order, Customer or Product" popover-trigger="mouseenter" popover-append-to-body="true"></span>
					</div>
				  </div>
				 
				</form>
			</div>
		</div>
		<div class="col-md-3 col-sm-12 col-xs-12 pm-potential-box">
			<div class="btn-group pull-right" dropdown>
			<div type="button" class="btn btn-primary " >John Doe <span class="label ">( Store User )</span> <span class="sr-only">Split button!</span></div>
			  <button type="button" class="btn btn-danger dropdown-toggle" dropdown-toggle>
				<span class="glyphicon glyphicon-cog"></span>
				<span class="sr-only">Split button!</span>
			  </button>
			  <ul class="dropdown-menu" role="menu">
				<li><a href="#">Edit Profile</a></li>
				<li><a href="#">Change Role</a></li>
				<li class="divider"></li>
				<li><a href="#">Logout</a></li>
			  </ul>
			</div>
			
			
				<div class="row greenBg col-md-11  col-sm-12 col-xs-12 pcp-block pull-right">
					<div class="col-md-6 col-sm-6 col-xs-6 nopadding pm-entered"><a href="javascript:void(0);">PM Entered per day:</a></div>
					<div class="col-md-2 col-sm-2 col-xs-2 nopadding">&nbsp;$5.00</div>
					<div class="col-md-4 col-sm-4 col-xs-4 nopadding pull-right"><a href="javascript:void(0);">PM Potential</a></div>
				</div>
			
	
		</div>
    </div>
	<ul class="nav nav-tabs  nav-justified small-height top-nav">
		<li role="presentation" ui-sref-active="active" ng-class="{active: ($state.includes('workspace')) }"><a ui-sref="workspace"  class="btn" >Workspace</a></li>
		<li role="presentation" ng-class="{active: $state.includes('orderdetails')}"><a  class="btn" ui-sref="orderManagement">Order Management</a></li>
		<li role="presentation" ng-class="{active: ( $state.includes('systemManagement') || $state.includes('reports') )}"><a  class="btn" ui-sref="systemManagement">System Management</a></li>
		<li role="presentation" ng-class="{active: $state.includes('dashboard')}"><a  class="btn" ui-sref="dashboard">Dashboard</a></li>
	</ul>
</header>