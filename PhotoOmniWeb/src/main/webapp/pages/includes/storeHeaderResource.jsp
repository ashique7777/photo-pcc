<meta charset="UTF-8">
<base href="${pageContext.request.contextPath}/">
<%	String logoutURL = request.getContextPath() + "/saml/logout";%>   
<script>var appContextPath = "${pageContext.request.contextPath}" + '/servlet'</script>
    <link rel="stylesheet" type="text/css" href="res/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="res/css/flat-ui.min.css">
    <link rel="stylesheet" type="text/css" href="res/css/walgreens.css">
    <link rel="stylesheet" type="text/css" href="res/css/styles.css">
    
    <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <script src="res/js/vendor/angular.js"></script>
    <script src="res/js/vendor/angular-animate.js"></script>
    <script src="res/js/vendor/angular-ui-router.js"></script>
    <script src="res/js/vendor/ui-bootstrap-tpls-0.12.0.min.js"></script>
    <script src="res/js/storeReport.js"></script> 
    <script src="res/js/utils.js"></script>
    <script src="res/js/window.ui.directive.js"></script>  
     
    
    <div class="row paddingTopBottom10">
      <div class="col-md-5"> <a class="navbar-brand" href="#">PictureCare Central</a> </div>
      <div class="col-md-4"><img src="res/images/logo.png" /></div>
    <div class="col-md-3">
       <div type="button" class="btn btn-danger pull-right" >
       		<li><a href="<%=logoutURL%>"><span class="label logout-style">Logout</span></a></li>
       </div>
    </div>
   </div>



