myApp.controller("workspaceCtrl", ['$scope', '$http', "workspaceList", '$interval', function($scope, $http, workspaceList, $interval) {
	if(!$scope.$parent.workspaces)
        $scope.$parent.workspaces = [];
	
	
	
    $scope.workspaceList 	= workspaceList;
	$scope.workspace		= {};
	//json file path and some name will depend on server response
    $scope.workspaces.workqueueJson = $scope.workspaceList.workqueue.workqueueJson;
    $scope.workspaces.tabbox 		= $scope.workspaceList.tabbox;
    $scope.workspaces.ticketsJson 	= $scope.workspaceList.tickets.ticketsJson;
    $scope.workspaces.earliestPromiseTimeJson 	= $scope.workspaceList.workqueue.earliestPromiseTimeJson;
	
	/* Get Earliest Promise Time (dropdown) data */
	$http.get($scope.workspaces.earliestPromiseTimeJson).then(function(response) {
        $scope.promiseTimeOptions = response.data;
    });
	
	/* Get Workqueue table data */
	$http.get($scope.workspaces.workqueueJson).then(function(response) {
        $scope.workqueue = response.data;
		$scope.hideWorkqueueLoader = true;
    });
	
	/* Get Tickets table data */
	$http.get($scope.workspaces.ticketsJson).then(function(response) {
        $scope.tickets = response.data;
		$scope.tickets.hideLoader = true;
    });
	
	$scope.workspaces.changeStorePagination = function(index) {
		//console.log($scope.workqueue.currrentPage);
        $http.get($scope.workspaces.workqueueJson,{params:{'currentPage':$scope.workqueue.currrentPage}}).then(function(response) {
            $scope.workqueue = response.data;
        });
    };
	
	
}]);

myApp.controller("workspaceTabCtrl", ['$scope', 'getWorkspaceTabData', '$http', function($scope, getWorkspaceTabData, $http) {
	/* Get tabbed (Ex: Alerts,Notes) data */
	$http.get($scope.tabData.dataJson).then(function(response) {
        $scope.tab	= response.data;
		$scope.tab.hideLoader	= true;	
		getWorkspaceTabData.setTabData($scope.tab); // Set the tab data in service to use in popup pages
    });
	
}]);

myApp.controller("workspaceDetailsCtrl", ['$scope', '$stateParams', 'utils', '$state', 'getWorkspaceTabData', '$window','$http', function($scope, $stateParams, utils, $state, getWorkspaceTabData, $window, $http) {
	$scope.itemType 	= $stateParams.itemType;
	var itemId 		= $stateParams.itemId;
	var tabData		= getWorkspaceTabData.getTabData(); // Get the tab data in service that was set by controller
	$scope.detailsData	= '';
	if(!tabData.length) {
		for(var i=0;i<($scope.workspaces.tabbox).length;i++) {
			if($scope.workspaces.tabbox[i].itemType == $scope.itemType) {
				$http.get($scope.workspaces.tabbox[i].dataJson).then(function(response) {
					for(var j=0;j<(response.data.data).length;j++) {
						if(response.data.data[j].id == itemId) {
							$scope.detailsData = response.data.data[j];
							getWorkspaceTabData.setTabData(response.data.data[j]);// Set the tab data in service to use in popup pages
						}
					}
				});
			}
		}
	}else {
		for(var i=0;i<tabData.length;i++) {
			if(tabData[i].itemtype == $scope.itemType) {
				for(var j=0;j<(tabData[i].data).length;j++) {
					if(tabData[i].data[j].id == itemId) {
						$scope.detailsData = tabData[i].data[j];
					}
				}
			}
		}
	}
	
	
	angular.element($window).on('keydown', function(e){
		if (e.keyCode == 27) {
		   $scope.goToWorkspace(e);
		}
	});
	$scope.goToWorkspace = function(e) {
		e.preventDefault();
		$state.go("workspace");
	};
	
	$scope.getNextItem = function(e,ele){
             var currentTabArr = '';
			
			 for(var i=0;i<tabData.length;i++) {
				if(tabData[i]['itemtype'] == $scope.itemType) {
					currentTabArr	= tabData[i]['data'];
				}
			 }
			 var cItem = utils.getItem(ele,currentTabArr,itemId);
             if(cItem) return cItem;
        }
        $scope.changeItem = function(e,ele){  
            e.preventDefault();
            var cItem = $scope.getNextItem(e,ele);
            if(cItem)
            $state.go("workspace.details",{itemType: $scope.itemType, itemId:cItem.id});
        };
		
		
}]);
myApp.controller("orderDetailsCtrl", ['$scope', '$stateParams', '$state', '$window','$http', function($scope, $stateParams, $state, $window, $http) {
	$scope.orderDetails = [];
	$scope.orderData	= {};
	$scope.envelopeID 	= $state.params.envId;
	var path = 'res/sampledata/workspace/order.details.json';
    $http.get(path).then(function(response) {
        $scope.orderData	 = response.data;
		$scope.orderData.envelopdata.hideLoader = true;
		if(typeof($scope.envelopeID) === 'undefined') {
			$scope.envelopeID	= $scope.orderData.envelopdata[0].id;
			$state.go("orderdetails.envelope",{envId:$scope.envelopeID});
		}
    });
	$scope.orderDetails.changeOrderPagination = function() {
		// $http.get($scope.workspaces.workqueueJson,{params:{'currentPage':$scope.workqueue.currrentPage}}).then(function(response) {
       console.log('current page',$scope.orderData.currrentPage);
	   $http.get(path,{params:{'currentPage':$scope.orderData.currrentPage}}).then(function(response) {
        $scope.orderData	 = response.data;
		$scope.orderData.envelopdata.hideLoader = true;
		if(typeof($scope.envelopeID) === 'undefined') {
			$scope.envelopeID	= $scope.orderData.envelopdata[0].id;
			$state.go("orderdetails.envelope",{envId:$scope.envelopeID});
		}
    });
    };
	
	$scope.showEnvDetails = function(id) {
		$scope.envelopeID	= id;
		$state.go("orderdetails.envelope",{envId:id});
	};
}]);
myApp.controller("envelopeDetailsCtrl", ['$scope', '$stateParams', '$state', '$window','$http', function($scope, $stateParams, $state, $window, $http) {
	$scope.envelopeNumber 	= $stateParams.envNo;
	$scope.envelopeID 		= $stateParams.envId;
	$scope.exceptionType	= "Cancel";
	
	$scope.envelopeDetails	= [];
	if(!$scope.orderData.envelopdata) {
		var path = 'res/sampledata/workspace/order.details.json';
		$http.get(path).then(function(response) {
			$scope.orderData	 = response.data;

			for(var i = 0; i < ($scope.orderData.envelopdata).length;i++){
				if($scope.envelopeID == $scope.orderData.envelopdata[i].id){
					$scope.envelopeDetails.push($scope.orderData.envelopdata[i]);
				}
			}
		});
	}
	else {
		for(var i = 0; i < ($scope.orderData.envelopdata).length;i++){
			if($scope.envelopeID == $scope.orderData.envelopdata[i].id){
				$scope.envelopeDetails.push($scope.orderData.envelopdata[i]);
			}
		}
	}
}]);

myApp.controller("envelopePopCtrl", ['$scope', '$stateParams', '$state', '$window', 'utils', '$http', function($scope, $stateParams, $state, $window, utils, $http) {
	$scope.envelopeNumber 	= $stateParams.envNo;
	var envelopeID 			= $stateParams.envId;
	$scope.envelopePopDetails	= [];
	
	
	$scope.$watch('orderData',function(orderDetailsData,newData) {
		
		if(Object.keys(orderDetailsData).length) {
			for(var i=0;i<(orderDetailsData.envelopdata).length;i++) {
				if(envelopeID == orderDetailsData.envelopdata[i].id) {
					$scope.envelopePopDetails = orderDetailsData.envelopdata[i];
				}
			}

		}
	});
	
	angular.element($window).on('keydown', function(e){
		if (e.keyCode == 27) {
		   $scope.goToEnvelope(e);
		}
	});
	$scope.goToEnvelope = function(e) {
		e.preventDefault();
		if(typeof($scope.envelopeID) !== 'undefined') {
			$state.go("orderdetails.envelope",{envId:$scope.envelopeID});
		} else {
			$state.go("orderdetails",{envNo: $scope.envelopeNumber});
		}
	};
	
	
	$scope.getNextItem = function(e,ele){
		var currentDataArr = '';
		if(Object.keys($scope.orderData).length) {
			 var cItem = utils.getItem(ele,$scope.orderData.envelopdata,envelopeID);
			 if(cItem) return cItem;
		 }
	}
		
		
	
    $scope.changeItem = function(e,ele){  
		e.preventDefault();
		var cItem = $scope.getNextItem(e,ele);
		if(cItem) {
			$state.go("orderdetails.details",{envId: cItem.id});
		}
	};
}]);
