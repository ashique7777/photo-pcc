<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<% String logoutURL = request.getContextPath() + "/saml/logout"; %>   
    <header>

    <div class="row paddingTopBottom10">
      <div class="col-md-4"> 
      	<div class="row"><a class="navbar-brand">PictureCare Central</a> </div>
      	<div class="row">
	    	<div class="col-sm-4"><span class="wag-text-dark-mid-grey"><strong>Version : ${appVersion}</strong></span></div>
	    </div>
      </div>
      	 
      <div class="col-md-4"><img src="res/images/logo.png" /></div>
      <div class="col-md-4">
        
        <div class="btn-group pull-right" dropdown>
      
        <div type="button" class="btn btn-primary " >${loggedFullName}<span class="sr-only">Split button!</span></div>
      <button type="button" class="btn btn-danger dropdown-toggle" dropdown-toggle>
        <span class="glyphicon glyphicon-cog"></span>
        <span class="sr-only">Split button!</span>
      </button>
      <ul class="dropdown-menu" role="menu">
        <!-- <li><a href="#">Edit Profile</a></li>
        <li><a href="#">Change Role</a></li>
        <li class="divider"></li> -->
        <li><a href="<%=logoutURL%>">Logout</a></li>
      </ul>
    </div>

      </div>

    </div>

<div class="row nav-tabs">
	<ul class="col-sm-6 nav small-height top-nav wag-nav-tabs wag-nav-tabs2">
	  <li class="col-sm-4 nopadding" role="presentation" ui-sref-active="active"><a ui-sref="dashboard"  class="btn noBottomBorder" >Dashboard</a></li>
	  <li class="col-sm-4 nopadding" role="presentation" ng-class="{active: $state.includes('report')}"><a  class="btn noBottomBorder" ui-sref="report.default">Reports</a></li>
	  <li class="col-sm-4 nopadding" role="presentation" ng-class="{active: $state.includes('centralAdmin')}"><a  class="btn noBottomBorder" ui-sref="centralAdmin.default">Central Admin</a></li>
	  <!-- <li class="col-sm-3 nopadding" role="presentation" ui-sref-active="active"><a  class="btn" ui-sref="storeview">Store View</a></li>
	  <li class="col-sm-3 nopadding" role="presentation" ui-sref-active="active"><a  class="btn" ui-sref="oms">OMS</a></li> -->
	</ul>
</div>

    </header>
