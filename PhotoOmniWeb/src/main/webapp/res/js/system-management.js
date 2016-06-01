"use strict";
myApp.controller("systemManagementCtrl", ['$scope', '$state', '$stateParams', '$http',  'utils', function($scope, $state, $stateParams, $http,utils) {
	// On click on System Management menu redirect to Reports tab(as for now there is no page to display except reports)
	$state.go("reports");
}]);
myApp.controller("systemReportsCtrl", ['$scope', '$stateParams', '$state', '$http', 'reportlist','utils', '$location', function($scope, $stateParams, $state, $http, reportlist, utils, $location) {
	if(!$scope.$parent.reportworkspaces)
        $scope.$parent.reportworkspaces = [];
	
	$scope.reportlist = reportlist;	//Assign reportlist into $scope (get reportlist from resolve in state)
	$scope.reportlist.hideLoader = true;
	$scope.reportworkspaces = $scope.$parent.reportworkspaces; 

	$scope.currentReportId = null;
	$scope.mainReportActive = true;
	//Watch the change in currentReportId(chaging from systemReportsDetailsCtrl
    $scope.$watch('currentReportId', function(newValue, oldValue) {
		//if !currentReportId no need to execute further
		if (!$scope.currentReportId) return; 
		
		//Get the specific report details & index(as activeIndex) that was clicked in the view.
		$scope.report 		= utils.findById($scope.reportlist, $scope.currentReportId);
		$scope.activeIndex 	= utils.checkIfExist($scope.reportworkspaces, $scope.currentReportId);
		
		//Make all tabs inactive
		var setAllInactive = function() {
			angular.forEach($scope.reportworkspaces, function(reportworkspaces) {
				reportworkspaces.active = false;
			});
			$scope.mainReportActive = false;
		};
		if ($scope.activeIndex != null) {
			setAllInactive();
			//Active the current tab only
			$scope.reportworkspaces[$scope.activeIndex].active = true;
		} else {
			//Add the object in the scope and based on this scope will generate the tabs in view
			$scope.reportworkspaces.push($scope.report.cat);
			
			var activeIndex 	= utils.checkIfExist($scope.reportworkspaces, $scope.currentReportId);
			$scope.reportworkspaces[activeIndex].active = true;
		}
	});
	// On click of cross remove that tab & set active tab properly
	$scope.removeTab = function(index) {
		var changeIndex = true;
		if (!$scope.reportworkspaces[index].active) changeIndex = false;
		$scope.reportworkspaces.splice(index, 1);
		if (changeIndex) {
			if($scope.reportworkspaces.length) {
				$scope.setActive($scope.reportworkspaces[0].id);
			}else {
				$scope.mainReportActive = true;//Inactive the Reports tab
				$scope.currentReportId = null;
				$state.go("reports");
			}
		}else {
			$scope.mainReportActive = true;//Inactive the Reports tab
			$scope.currentReportId = null;
			$state.go("reports");
		}
	}
	$scope.setActive = function(id) {
		var activeIndex 	= utils.checkIfExist($scope.reportworkspaces, id);
		if($scope.reportworkspaces[activeIndex])
			$scope.reportworkspaces[activeIndex].active = true;
        $location.path("/reports/" + id);
    }	
}]);
myApp.controller("systemReportsDetailsCtrl", ['$scope', '$stateParams', '$http', 'utils', function($scope, $stateParams, $http, utils){
	//Set the currentReportId as clicked on the report link, this change will execute the code in above controller(inside watch)
	$scope.$parent.currentReportId = $stateParams.reportId;
}]);
/*For each reports get details and based on this populate the data in view tab*/
myApp.controller("singleReportWorkspaceCtrl", ['$scope', '$stateParams', '$http', 'utils', function($scope, $stateParams, $http, utils){
	if(!$scope.reportworkspace.dataJson)
		return;
		
	$http.get($scope.reportworkspace.dataJson).then(function(response) {
		$scope.reportworkspace.data = response.data;
	});
	
}]);

