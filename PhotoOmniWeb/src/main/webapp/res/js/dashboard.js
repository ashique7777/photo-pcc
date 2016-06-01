"use strict";
myApp.controller("DashboardCtrl", ['$scope', '$http', '$interval', "windowList", function($scope, $http, $interval, windowList) {
	
    $scope.windowList = windowList;
    
    var loadValidReports = function(){
    	angular.forEach($scope.windowList,function(rptObj){
			if (rptObj.reportName != undefined) {
				$http.get(
						appContextPath + '/dashboard/load/'
								+ rptObj.reportName + '/'
								+ rptObj.defaultItem + '/templates').then(
						function(res) {
							rptObj.storeTemplate = res.data.storeTemplate;
							rptObj.filterTemplate = res.data.filterTemplate;
							rptObj.defaultFilterData = {};
							rptObj.savedFilterData = {};
							rptObj.storeData = {};
							loadStoreData(rptObj);
						})
			}
    	})
    }
    loadValidReports();
    
    var loadReportFilterData = function(){
    	
    }
	
	var loadStoreData = function(rptObj){
		$http.post(appContextPath + '/dashboard/load/'
									+ rptObj.reportName + '/'
									+ rptObj.defaultItem + '/storeData',{}).then(function(res){
										rptObj.asOfDate = res.data.data.asOfDate;
										rptObj.storeData = res.data.data;
										rptObj.title = res.data.data.reportName;
									})
	}
	
	$scope.hideLoading = function(id) {
		angular.element( document.querySelector( '#window'+id+' .loading' ) ).css({'display':'none'}) ;
	}
	
	$scope.showLoading = function(id) {
		angular.element( document.querySelector( '#window'+id+' .loading' ) ).css({'display':'block'}) ;
	}
	
	$scope.filterToggle = function(rpt){
		rpt.showFilter = !rpt.showFilter;
	}
	
	$scope.printDashboard = function(rpt){
		sessionStorage.setItem("rpt", JSON.stringify(rpt));
		$http.get('/PhotoOmniWeb/servlet/dashboard/print').then(function(res){
			window.open('/PhotoOmniWeb/'+res.data.printTemplate,"Print Window");
		})
	}
	
	$scope.exportDashboard = function(rpt){
		 var url = "/PhotoOmniWeb/servlet/dashboard/"+rpt.reportName+"/"+rpt.defaultItem+"/export";
		 var wind = window.open(url);
		 wind.focus();
	}
	
}]);

myApp.directive('autoRefresh', ['$parse', '$timeout', '$http', function ($parse, $timeout, $http) {
    return {
        restrict: 'E',
        template: '<div></div>',
        replace: true,
        scope:{
        	report : '='
        },
        link: function (scope, element, attrs) {
            var isRunning = true;
            var url = '';
            var interval;
            function successFunction(data, obj) {
              if (isRunning) {
                $timeout(function () { refreshFromUrl(url, interval,obj); }, interval);
	                obj.storeData.asOfDate = data.data.asOfDate;
	                obj.storeData.data = data.data.data;
	                obj.storeData.reportName = data.data.reportName;
	                scope.report = obj;
              }
            }

            function refreshFromUrl(url, interval, obj) {
              if(url != ''){
            	  $http.post(url,{}).success(function (data) {
	                successFunction(data, obj);
	              })
	              .error(function (data) {
	                
	              });
              }
            }

            window.setTimeout(function() {
                if (attrs.report !== undefined) 
                {
                	var  obj = scope.report;
                	interval = obj.refreshInterval;
                	url =  appContextPath+'/dashboard/load/'+obj.reportName+'/'+obj.defaultItem+'/storeData';
                    refreshFromUrl(url, interval, obj);
                }
            }, 5000);
 
            scope.$on('$destroy', function () {
                isRunning = false;
            });
        }
    }
}]);