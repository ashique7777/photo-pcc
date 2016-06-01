var myApp = angular.module('StoreReports', ['ui.router' ,'ui.bootstrap']);

myApp.filter('numberFixedLen', function () {
    return function (n, len) {
        var num = parseInt(n, 10);
        len = parseInt(len, 10);
        if (isNaN(num) || isNaN(len)) {
            return n;
        }
        num = ''+num;
        while (num.length < len) {
            num = '0'+num;
        }
        return num;
    };
});
/** For PM by Employee **/
myApp.controller('PmByEmpController', ['$scope','$http','$stateParams', '$state', '$location', '$modal', '$filter', '$timeout', function($scope, $http, $stateParams, $state, $location, $modal, $filter, $timeout){
	
	  $scope.format = 'MM/dd/yyyy';
	  $scope.workspace = {};
      $scope.showtable = false;
	  $scope.showDataTD = false;
	  $scope.showNoDataTD = false;
	  $scope.showNoAuthTD = false;
	  $scope.showTableData = false; //for pagination
	  var validationError = false;
	  $scope.showtableEmp = false;
	  $scope.workspace.pmByEmp = {};
	  $scope.workspace.filterData ={};
	  $scope.workspace.filter ={}
	  $scope.endMaxDate = new Date();	
	  $scope.showEmployee = true;
	  $scope.errorMessageText = [];
	  $scope.colSortOrder = {};
	  
	  /** set min date as SYSDATE-195 days **/
	  $scope.startMinDate = setStartMinDate();
	  
	  var defaultByEmpDate = new Date();
	  defaultByEmpDate.setDate(defaultByEmpDate.getDate());
	  $scope.workspace.filterData.startDate = defaultByEmpDate; //setting default Date as current date
	  $scope.workspace.filterData.endDate   = defaultByEmpDate; //setting default Date as current date
	  
	  $scope.open = function($event, ele) {
		    $scope.showErrorContainer = false;
	        $event.preventDefault();
	        $event.stopPropagation();
	        $scope.hideErrorMessage();
	        $scope.workspace.pmByEmp.openedEnd = false;
	        $scope.workspace.pmByEmp.openedStart = false;
	        if (ele == 'start') {
	        	$scope.workspace.pmByEmp.openedStart = true;
	        	//angular.element( document.querySelector('input[type="text"].form-control.start + ul.dropdown-menu table thead tr:nth-child(1) th:nth-child(2)') ).attr('colspan',5);
	        	//angular.element( document.querySelector('input[type="text"].form-control.startpop + ul.dropdown-menu table thead tr:nth-child(1) th:nth-child(2)') ).attr('colspan',5);
	        }
	        if (ele == 'end') {
	        	$scope.workspace.pmByEmp.openedEnd = true;
	        	//angular.element( document.querySelector('input[type="text"].form-control.end + ul.dropdown-menu table thead tr:nth-child(1) th:nth-child(2)') ).attr('colspan',5);
	        	//angular.element( document.querySelector('input[type="text"].form-control.endpop + ul.dropdown-menu table thead tr:nth-child(1) th:nth-child(2)') ).attr('colspan',5);
	        }
	    }
	  	
	  	$scope.hideErrorMessage = function() {
			$scope.showErrorContainer = false;
			$scope.showNoDataFound = false;
			$scope.showSuccessMessage = false;
			
			$scope.showProdErrorMessage = false;
			//$scope.showNoProdDataFound = false;
		}
	     $scope.submitData = function() {
	    	 validationError = false;
			 $scope.showErrorContainer =  false;
		  if($scope.workspace.validatePMByEmpReport()){
			  $scope.showTableData = false;//for pagination
			$scope.workspace.filter.employeeId = angular.element(document.querySelector('input[id="selectedEmpId"]')).attr("value");
			$scope.workspace.filter.storeNumber = angular.element(document.querySelector('input[id="storeNumber"]')).attr("value");
			$scope.workspace.filter.storeLevelReport = angular.element(document.querySelector('input[id="storeLevelReport"]')).attr("value");
			$scope.workspace.filter.startDate = dateConverter($scope.workspace.filterData.startDate);
			$scope.workspace.filter.endDate = dateConverter($scope.workspace.filterData.endDate);
			$scope.workspace.filter.sortOrder = 'ASC';
			$scope.workspace.filter.sortColumnName = "Employee Name";
			$http.post("/PhotoOmniOMSWeb/orders/pm_mbpm/employee",{filter: $scope.workspace.filter, messageHeader: PMByEmpMessageHeader(), currentPage : 1}).then(function(response) {
				$scope.showtable = true;
				$scope.showprintpagination = false;
				$scope.workspace.data = response.data;
				$scope.workspace.data.currentPage = $scope.workspace.data.currentPage;
				$scope.reverse = true;
				$scope.sortField = 'response.data.pmByEmployeeDetailBeans.employeeName';
				if($scope.workspace.data.pmByEmployeeDetailBeans.length > 0){
					var totalRow = $scope.workspace.data.totalPage;
					var pagesize = $scope.workspace.data.totalRows;
					//console.log("balaji total page size totalRow: "+ totalRow +">>> pagesize :"+pagesize);
					$scope.totalEmpPageCount = Math.ceil(totalRow / pagesize);
					//console.log("balaji no of page : "+ $scope.totalEmpPageCount);
				$scope.showprintpagination = true;
				if($scope.workspace.filter.employeeId != "")$scope.showtableEmp = true;
					$scope.showNoDataTD = false;
					$scope.showDataTD = true;
					$scope.showTableData = true;//for pagination
					$scope.colSortOrder.order = false;
					$scope.colSortOrder.firstLoad = true;
				}else if(!$scope.workspace.data.authorised){
					$scope.showNoAuthTD = true;
					$scope.showprintpagination = false;
					$scope.showNoDataTD = false;
					$scope.showDataTD = false;
				}
				else{
					$scope.showprintpagination = false;
					$scope.showNoDataTD = true;
					$scope.showDataTD = false;
				}
			}, function(response) {
					if(response.status == 401 || response.status == 302) {
						window.location.reload();
					}
			});
		}
	  }
	
	 $scope.changeStorePagination = function() {
				$http.post("/PhotoOmniOMSWeb/orders/pm_mbpm/employee",{filter: $scope.workspace.filter, messageHeader: PMByEmpMessageHeader(), currentPage : $scope.$parent.currentIndex}).then(function(response) {
					$scope.workspace.data = response.data;
					$scope.workspace.data.currentPage = $scope.workspace.data.currentPage;
				}, function(response) {
  					if(response.status == 401 || response.status == 302) {
  						window.location.reload();
  					}
  				});
		}
	
	$scope.printPMByEmpRpt = function() {
			 var obj = {
					 currentPage : $scope.workspace.data.currentPage,
					 filter : $scope.workspace.filter
			 }
			 sessionStorage.setItem('PMByEmpPrint', JSON.stringify(obj));
			 window.open('/PhotoOmniWeb/res/template/report/pmbyemp/pmbyemp-report-print.html'); 
		 }
	
	 $scope.workspace.validatePMByEmpReport = function() {
			// check if any filter is empty
			var errorMessage  = "";
			var inputStatus = false;
			var validationErrorMessage = "";
			var delimiter = "";
			var filterData = $scope.workspace.filterData;
			if (angular.isObject(filterData)) {
				if (angular.isUndefined(filterData.startDate) || filterData.startDate == null) {
						errorMessage = errorMessage + "Start Date is a required field";
    			   	inputStatus = true;
    			   delimiter = ",";
				}
				if (angular.isUndefined(filterData.endDate) || filterData.endDate == null) {
					errorMessage = errorMessage + 	delimiter + " End Date is a required field";
    				inputStatus = true;
    				delimiter = " ,";
				} 
				
				var stDateDiff = dateDifference(filterData.startDate, new Date());
				var endDateDiff = dateDifference(filterData.endDate, new Date());
				var stEndDateDiff = dateDifference(filterData.startDate, filterData.endDate);
				
				var sDays = dateDifference($scope.startMinDate, filterData.startDate);
				var eDays = dateDifference($scope.startMinDate, filterData.endDate);
				
				if( !inputStatus && stDateDiff < 0 ){
    				validationErrorMessage = "Start date should be today or less" + delimiter + validationErrorMessage ;
    				validationError = true;
    			}else if (!inputStatus && endDateDiff < 0) {
					validationErrorMessage = "End Date cannot be less than Start Date" + delimiter + validationErrorMessage ;
    				validationError = true;
				} else if (((filterData.startDate != null && filterData.endDate != null) && (stEndDateDiff < 0))) {
					validationErrorMessage = "End Date cannot be less than Start Date" + delimiter + validationErrorMessage ;
    				validationError = true;
				} else if (((filterData.startDate != null && filterData.endDate != null) && (sDays < 0) && (eDays < 0))) {
					validationErrorMessage = "Start date and End date must be within 195 days from current date" + delimiter + validationErrorMessage ;
    				validationError = true;
				} else if (((filterData.startDate != null && filterData.endDate != null) && (sDays < 0))) {
					validationErrorMessage = "Start date must be within 195 days from current date" + delimiter + validationErrorMessage ;
    				validationError = true;
				} else if (((filterData.startDate != null && filterData.endDate != null) && (eDays < 0))) {
					validationErrorMessage = "End date must be within 195 days from current date" + delimiter + validationErrorMessage ;
    				validationError = true;
				}
			} else {
				validationErrorMessage = errorMessage + "start date, end date , email id"
    			validationError = true;
			}
			if(validationError){
    		$scope.showErrorContainer = validationError;
    		$scope.errorMessage = validationErrorMessage;
    		return false;
    	}else if(inputStatus){
    		$scope.showErrorContainer = inputStatus;
    		$scope.errorMessage = errorMessage;
    		return false;
    	}else{
    		return true;
    	}
			$scope.workspace.filterData.push;
		};
		
		$scope.openPopup = function (index, empName) {
			$scope.employeePopupTemplate = 'res/template/report/employee.detail.html';
			$scope.showEmployee = false;
			$scope.showProdDataTD = false;
			$scope.showPMTableData = false;
			$scope.productFilterData = {};
			var selectedEmp = index;
			$scope.selectedEmployee = empName;
			$scope.productFilterData.employeeId  = selectedEmp;
			$scope.workspace.popStartDate = $scope.workspace.filterData.startDate;
			$scope.workspace.popEndDate = $scope.workspace.filterData.endDate;
			$scope.storeAddress = $scope.workspace.storeNumber;
			
			$scope.colSortOrder = {};
			$scope.printable = false;
			$scope.workspace.printTemplate   = "res/template/report/store-report-print.html";
			$scope.showPMByProdPrint = true;
			
			$timeout(function() {
				$scope.submitProductData();
		    }, 10);
		  };   
		  $scope.cancel = function () {
			  $scope.showEmployee = true;
			  $scope.employeePopupTemplate = '';
			  $scope.startIndex = $scope.workspace.data.currentPage;
		  };
		  
		  /*$scope.changeProductPagination = function() {
			  $scope.productFilterData.currentPageNo = $scope.$$childTail.currentIndex;
			  var filterData = angular.copy($scope.productFilterData);
			  
			  filterData.startDate = $filter('date')($scope.productFilterData.startDate, "yyyy-MM-dd");
			  filterData.endDate   = $filter('date')($scope.productFilterData.endDate, "yyyy-MM-dd");
	    	  $http.post("/PhotoOmniOMSWeb/orders/pm_mbpm/product", {messageHeader : PMByEmpMessageHeader(), filter: filterData}).then(function(response) {
				  $scope.workspace.product = response.data.pmReportResponseBeanList;
	          });
	      }*/
		  
		  $scope.printDiv = function (divName) {
			    var header = "PM Dollars Earned - by product";
			    var printContents = document.getElementsByClassName('showPMByProdPrint')[0].innerHTML;
			    var originalContents = document.body.innerHTML; 
				printableDiv(header,printContents);
			    return true;
		  }
		  
		  $scope.submitProductData = function() {
			  $scope.errorMessageText = [];
			  $scope.productFilterData.storeNumber = $scope.workspace.filter.storeNumber;
			  $scope.productFilterData.startDate   = $scope.workspace.popStartDate;
			  $scope.productFilterData.endDate     = $scope.workspace.popEndDate;
			  //$scope.productFilterData.currentPageNo = '1';
			  $scope.productFilterData.sortOrder = 'ASC';
			  var filterData = angular.copy($scope.productFilterData);
			  
			  filterData.startDate = $filter('date')($scope.productFilterData.startDate, "yyyy-MM-dd");
			  filterData.endDate   = $filter('date')($scope.productFilterData.endDate, "yyyy-MM-dd");
			  var endMaxDateValue = $filter('date')($scope.endMaxDate, "MM-dd-yyyy");
			  
			  var requiredFieldArray = ["startDate", "endDate"];
			  var errorCat = validateRequiredField(requiredFieldArray,$scope.productFilterData);
			  if(errorCat != '') { $scope.errorMessageText.push(errorCat); }
			  
			  validateDate($scope.productFilterData, $scope.endMaxDate, $scope.errorMessageText, $filter, undefined, 'pmByReport');
			  if($scope.errorMessageText.length == 0) {
				  var sDays = dateDifference($scope.startMinDate, $scope.productFilterData.startDate);
				  var eDays = dateDifference($scope.startMinDate, $scope.productFilterData.endDate);
				  if(sDays < 0 && eDays < 0) {
					  $scope.errorMessageText.push("Start date and End date must be within 195 days from current date");
				  } else if(sDays < 0) {
					  $scope.errorMessageText.push("Start date must be within 195 days from current date");
				  } else if(eDays < 0) {
					  $scope.errorMessageText.push("End date must be within 195 days from current date");
				  }
			  }

			  if($scope.errorMessageText.length == 0) {
					 $http.post("/PhotoOmniOMSWeb/orders/pm_mbpm/product", {messageHeader : PMByEmpMessageHeader(), filter: filterData}).then(function(response) {
						  $scope.workspace.product = response.data.pmReportResponseBeanList;
						  $scope.showPMByProdPrint = false;
						  if($scope.workspace.product == null || $scope.workspace.product.length == 0) {
							  $scope.showNoProdDataFound = true;
							  $scope.showProdDataTD = false;
						  } else {
							  $scope.showNoProdDataFound = false;
							  $scope.showProdDataTD = true;
							  $scope.showPMTableData = true;
				        	  //$scope.prodTotalPageCount = Math.ceil(response.data.totalRows/5); 
				        	  /*$scope.workspace.filterData.popStartDate = $scope.workspace.filterData.startDate;
				        	  $scope.workspace.filterData.popEndDate = $scope.workspace.filterData.endDate;*/
				        	  var dataList = $scope.workspace.product;
				        	  for(var k = 0; k < dataList.length; k++) {
									//For sorting
									var rowData = dataList[k];
									for(var eachCol in rowData) {
										$scope.colSortOrder[eachCol] = {};
										$scope.colSortOrder[eachCol].order = false;
										$scope.colSortOrder[eachCol].firstLoad = true;
									}
								}
						  }
					 }, function(response) {
		  					if(response.status == 401 || response.status == 302) {
		  						window.location.reload();
		  					}
		  			});
				} else {
					$scope.showProdErrorMessage = true;
					$scope.showProdDataTD = false;
					$scope.showNoProdDataFound = false;
				}
		  }
		  $scope.submitData();
		  
		  if($scope.workspace.filter.employeeId != ""){
			  var strEmpORLocID = $scope.workspace.filter.employeeId ;
			  var isEmployeeName = true;
			  
			  $http.post("/PhotoOmniOMSWeb/orders/pm_mbpm/getEmpNameOrStrAdd/"+$scope.workspace.filter.employeeId+"/"+true+"")
			  .then(function(response) {
				  $scope.showtableEmp = true;
				  $scope.workspace.employeeName = response.data.strEmpNameORStrAdd;
			  }, function(response) {
					if(response.status == 401 || response.status == 302) {
						window.location.reload();
					}
			  });
		  }
		$http.post("/PhotoOmniOMSWeb/orders/pm_mbpm/getEmpNameOrStrAdd/"+$scope.workspace.filter.storeNumber+"/"+false+"")
		  .then(function(response) {
			  $scope.workspace.storeNumber = response.data.strEmpNameORStrAdd;
		  	}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
		
		 $scope.sortEmpName = function(columnName) {
			 console.log(columnName);
			  	$scope.orderProperty = !$scope.orderProperty;
		    	($scope.orderProperty == true) ? $scope.workspace.filter.sortOrder  = 'DESC' : $scope.workspace.filter.sortOrder  = 'ASC';
		    	$scope.workspace.filter.sortColumnName = columnName;
		    	$scope.colSortOrder.order = $scope.orderProperty;
		    	$scope.colSortOrder.firstLoad = false;
		    	//$scope.showTableData = false;
		    	
		    	var EmpfilterData = angular.copy($scope.workspace.filter);
		    	EmpfilterData.startDate = $filter('date')($scope.workspace.filterData.startDate, "yyyy-MM-dd");
		    	EmpfilterData.endDate   = $filter('date')($scope.workspace.filterData.endDate, "yyyy-MM-dd");
				$http.post("/PhotoOmniOMSWeb/orders/pm_mbpm/employee",{filter: EmpfilterData, messageHeader: PMByEmpMessageHeader(), currentPage : 1}).then(function(response) {
					$scope.workspace.data = response.data;
					console.log($scope.workspace.data.currentPage);
					$scope.workspace.data.currentPage = $scope.workspace.data.currentPage;
					//$scope.$parent.currentIndex = '1';
					//$scope.showTableData = true;
				}, function(response) {
  					if(response.status == 401 || response.status == 302) {
  						window.location.reload();
  					}
  				});
		    };
		
		  $scope.sort = function(columnName) {
			  	$scope.orderProperty = !$scope.orderProperty;
		    	($scope.orderProperty == true) ? $scope.productFilterData.sortOrder = 'DESC' : $scope.productFilterData.sortOrder = 'ASC';
		    	$scope.productFilterData.sortColumnName = columnName;
		    	$scope.colSortOrder[columnName].order = $scope.orderProperty;
		    	$scope.colSortOrder[columnName].firstLoad = false;
		    	//$scope.productFilterData.currentPageNo = '1';
		    	
		    	var filterData = angular.copy($scope.productFilterData);
		    	filterData.startDate = $filter('date')($scope.productFilterData.startDate, "yyyy-MM-dd");
				filterData.endDate   = $filter('date')($scope.productFilterData.endDate, "yyyy-MM-dd");
		    	$http.post('/PhotoOmniOMSWeb/orders/pm_mbpm/product',{filter: filterData, messageHeader:PMByEmpMessageHeader()}).then(function(response) {
		    		$scope.workspace.product = response.data.pmReportResponseBeanList;
		    		resetSortIcon(columnName,$scope.colSortOrder);
		    	}, function(response) {
  					if(response.status == 401 || response.status == 302) {
  						window.location.reload();
  					}
  				});
		    };
}]);


/*** For machine downtime report ***/
myApp.controller('machineDowntime', ['$scope','$http','$stateParams', '$state', '$location', 'utils', '$q', '$filter', '$timeout', 'reportCache', function($scope, $http, $stateParams, $state, $location, utils, $q, $filter, $timeout, reportCache){
	
	  $scope.workspace = {};
	  $scope.showTableData = false; //for pagination
	  $scope.workspace.machineFilter = {};
	  $scope.workspace.filterData ={};
	  $scope.printable = false;
	  
	  $scope.currentReportId = 4;
	  $scope.showtable = false;
	  $scope.showFilter = false;
	  $scope.showForCorpScreen = true; //for machine filter report location 
	  $scope.report = {};
	  $scope.machineFilterDefault =  {};
	  $scope.machineFilter = [];
	  $scope.machineFilterDefault.machine = 'Select';
	  $scope.orderProperty = false; // for column sorting
	  $scope.columnName    = ''; // for column sorting
	  $scope.colSortOrder = {};
	  $scope.showNoDataFound = false;
	  $scope.printStatus = false;
	  
	  /* Message header for Machine Down Time report */
	  var machineMessageHeader = getMessageHeader("MachineDowntimeReportDetails", "Machine");
	  
	  var reportListJson = getReportConfig($http);
	  
	  $scope.startMinDate = setStartMinDate();
	  
	  $q.all([ reportListJson ]).then(function(result) {
		  	  $scope.reportlist = result[0].data.catagory;
		  	  $scope.report = utils.findById($scope.reportlist, $scope.currentReportId);
		  	  
		  	  $scope.machineFilterDefault.machine    = 'All';
		  	  $scope.machineFilterDefault.machineId = '';
		  	 /* $scope.machineFilterDefault.startDate = new Date();
		  	  $scope.machineFilterDefault.endDate   = new Date();*/
		  	  getDefaultDate($scope.machineFilterDefault); //set default date for machine downtime
		  	  $scope.endMacMaxDate = $scope.machineFilterDefault.endDate; // set end max date as yesterday's date
		  	  
			  $scope.workspace.machinelistJson   = $scope.report.subcat.machinelistJson;
			  $scope.workspace.filterTemplate    = $scope.report.subcat.filterTemplate;
			  $scope.workspace.storeTemplate     = $scope.report.subcat.storeTemplate;
			  $scope.workspace.dataJson          = $scope.report.subcat.dataJson;
			  $scope.workspace.machineStorePrint = $scope.report.subcat.machineStorePrint
			  $scope.workspace.printTemplate     = $scope.report.subcat.printTemplate;
			  
			  $scope.machineFilter = angular.copy($scope.machineFilterDefault);
			  
			  $scope.machineFilter.storeId = angular.element(document.querySelector('input[id="storeNumber"]')).attr("value");
			  
			  $scope.showFilter = true;
			  
			  if ($scope.workspace.machinelistJson) {
					$http.post($scope.workspace.machinelistJson,{storeNbr: $scope.machineFilter.storeId, messageHeader:machineMessageHeader}).then(function(response) {
						$scope.workspace.machineList = response.data.machine;
			            for(var machine in response.data.machine) {
			    			var machineNameObj = response.data.machine[machine].machineName.toLowerCase();
			    			$scope.workspace.machineList[machine].machineName = machineNameObj.charAt(0).toUpperCase() +  machineNameObj.substring(1);
			    		}
					}, function(response) {
	  					if(response.status == 401 || response.status == 302) {
	  						window.location.reload();
	  					}
	  				});
			  }		
			  $scope.updateData('','');
	  });	  
	  //update data
	  $scope.updateData = function(e,filterType) {
		  	$scope.errorMessageText = [];
		  	var endMaxDateValue  = $filter('date')($scope.endMaxDate, "MM-dd-yyyy"); 
			var requiredFieldArray = ["startDate", "endDate"];
			var errorCat = validateRequiredField(requiredFieldArray,$scope.machineFilter);
			if(errorCat != '') { $scope.errorMessageText.push(errorCat); }
			  
			validateDate($scope.machineFilter, $scope.endMaxDate, $scope.errorMessageText, $filter, '195 days');
			
			if($scope.errorMessageText.length == 0) {
				showLoading();
				//$scope.showLoading($scope.workspace.id);
				$scope.machineFilterDefault =  angular.copy($scope.machineFilter);
		        $scope.machineFilter.currrentPage=1;
		        $scope.machineFilter.isPagination = false;
		        $scope.machineFilter.sortColumnName = 'ID';
		        $scope.machineFilter.sortOrder = 'DESC';
		        $scope.machineFilter.locationId = '3'; //store has location is `3`
		       
		        if(!$scope.machineFilter.storeId) {
		        	$scope.machineFilter.storeId = "";
		        }
				$http.post($scope.workspace.dataJson,{filter: $scope.machineFilter, messageHeader:machineMessageHeader}).then(function(response) {
					$scope.workspace.data = response.data;
					$scope.workspace.urlStoreTable = $scope.workspace.storeTemplate;
					//$scope.hideLoading($scope.workspace.id);
					$scope.showTableData = true;//for pagination
					$scope.showTable = true;
					var store = $scope.workspace.data.store;
					if(store.length > 0) {
						var storeLength = store.length;
						for(var k = 0; k < storeLength; k++) {
							$scope.colSortOrder[k] = {};
							var totalPageCount = Math.ceil(store[k].totalRecord/5);
							$scope.workspace.data.store[k].totalPageCount = totalPageCount;
							$scope.workspace.data.store[k].regionNumber = false;
							$scope.workspace.data.store[k].districtNumber = false;
							//For sorting
							var rowData = store[k].data[0];
							for(var eachCol in rowData) {
								$scope.colSortOrder[k][eachCol] = {};
								$scope.colSortOrder[k][eachCol].order = false;
								$scope.colSortOrder[k][eachCol].firstLoad = true;
							}
						}
						$scope.showErrorContainer = false;
						$scope.showNoDataFound = false;
						$scope.showMacDowntimePrint = false;
						$scope.storeNumberAddress = $scope.workspace.data.store[0].storeNbrAddress;
					} else {
						/*$scope.showErrorContainer = true;
						$scope.errorMessage = " No data found ";*/
						$scope.showNoDataFound = true;
					}
					hideLoading();
				}, function(response) {
  					if(response.status == 401 || response.status == 302) {
  						window.location.reload();
  					}
  				});
			} else {
				$scope.showErrorContainer = true;
				$scope.showTableData = false;//for pagination
				$scope.showTable     = false;
			}
			return;
		};
		
		$scope.changeStorePagination = function(type,index, storeNumber,operation) {
			if($scope.$$childTail.currentIndex == '') { return; }
			showLoading();
			var pagination = angular.copy($scope.machineFilter);
			var currrentPage = $scope.workspace.data.store[0].currrentPage;
			pagination.storeIndex = $scope.index;
			pagination.storeId = storeNumber;
			pagination.isPagination = true;
			//pagination.sortColumnName = $scope.columnName;
			pagination.currrentPage = $scope.$$childTail.currentIndex;
			
			//($scope.orderProperty == true) ? pagination.sortOrder = 'DESC' : pagination.sortOrder = 'ASC';
			storeIndex = 0;
			$http.post($scope.workspace.dataJson,{filter: pagination, messageHeader:machineMessageHeader}).then(function(response) {
				var removedData = $scope.workspace.data.store.splice(0,1);
				var modifiedObject = extend(removedData[0], response.data.store[0]);
				
				$scope.workspace.data.store.unshift(modifiedObject);
				var store = $scope.workspace.data.store;
				var storeLength = store.length;
				for(var k = 0; k < storeLength; k++) {
					$scope.workspace.data.store[k].regionNumber = false;
					$scope.workspace.data.store[k].districtNumber = false;
				}
				hideLoading();
			}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
		};
	    
	    $scope.$watch('workspace.machineObj', function() { 
	    	var macObj = $scope.workspace.machineObj;
	    	if(macObj) {
	    		$scope.machineFilter.machine   = macObj.machineName;
	    		$scope.machineFilter.machineId = macObj.id;
	    	} 
	    	if(macObj === null) {
	    		$scope.machineFilter.machine   = 'All';
	    		$scope.machineFilter.machineId = '';
	    	} 
	    });
	  
	    $scope.hideErrorMessage = function() {
			$scope.showErrorContainer = false;
			$scope.showNoDataFound = false;
			$scope.showSuccessMessage = false;
		}
	  
	  /*--------------- Date format and min / max date --------------*/
		$scope.open = function($event, ele) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        $scope.hideErrorMessage();
	        $scope.workspace.openedEnd = false;
	        $scope.workspace.openedStart = false;
	        if (ele == 'start') {
	        	$scope.workspace.openedStart = true;
	        	//angular.element( document.querySelector('input[type="text"].form-control.start + ul.dropdown-menu table thead tr:nth-child(1) th:nth-child(2)') ).attr('colspan',5);
	        }
	        if (ele == 'end') {
	        	$scope.workspace.openedEnd = true;
	        	//angular.element( document.querySelector('input[type="text"].form-control.end + ul.dropdown-menu table thead tr:nth-child(1) th:nth-child(2)') ).attr('colspan',5);
	        }
	        
	        $scope.showErrorMessage = false;
	    };
	    
	    $scope.format = 'MM/dd/yyyy';
	    $scope.endMaxDate = new Date();
	    
		/*--------------- Date format and min / max date --------------*/
	  
	//sort facility
	    $scope.sort = function(filterType, columnName, storeNumber, index) {
	    	var filterData = '';
	    	filterData = prepareSortData(filterType, columnName, storeNumber, index);
	    	showLoading();
	    	var storeIndex = 0;
	    	$http.post($scope.workspace.dataJson,{filter: filterData, messageHeader:machineMessageHeader}).then(function(response) {
				//$scope.workspace.data = response.data;
	    		if(filterType == 'machineFilter'){
	    			var removedData = $scope.workspace.data.store.splice(storeIndex,1);
					var modifiedObject = extend(removedData[0], response.data.store[0]);
					
					if(storeIndex > 0){
						$scope.workspace.data.store.splice(storeIndex,0,modifiedObject);
					} else {
						$scope.workspace.data.store.unshift(modifiedObject);
					}
					var store = $scope.workspace.data.store;
					var storeLength = store.length;
					for(var k = 0; k < storeLength; k++) {
						$scope.workspace.data.store[k].regionNumber = false;
						$scope.workspace.data.store[k].districtNumber = false;
					}
					resetSortIcon(columnName,$scope.colSortOrder[index]);
					hideLoading();
	    		} 					
	    	}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
	    };
	    
	    var  prepareSortData = function(filterType, columnName, storeNumber, index) {
	    	var filterData = {};
	    	if(filterType == 'machineFilter'){
	    		filterData = angular.copy($scope.machineFilter);
	    		filterData.isPagination = true;
	        	filterData.storeId = storeNumber;
	        	filterData.currrentPage = $scope.workspace.data.store[index].currrentPage;
	    	}
	    	if($scope.columnName == columnName) {
	    		$scope.orderProperty = !$scope.orderProperty;
	    	} else {
	    		$scope.columnName    = columnName
	    		$scope.orderProperty = false;
	    	}
	    	($scope.orderProperty == true) ? filterData.sortOrder = 'DESC' : filterData.sortOrder = 'ASC';
	    	filterData.sortColumnName = columnName;
	    	$scope.machineFilter.sortColumnName = columnName;
	    	$scope.machineFilter.sortOrder = filterData.sortOrder;
	    	
	    	$scope.colSortOrder[index][columnName].order = $scope.orderProperty;
	    	$scope.colSortOrder[index][columnName].firstLoad = false;
	    	
	    	return filterData;
	    };
	    
	    $scope.exportMachineRptToExcel = function(a){
	    	var reportMachineObjParam = { filter : $scope.machineFilter }; 
	    	reportMachineObjParam.filter.storeNumber=a;
			var url = "/PhotoOmniAdminWeb/machines/machine-export?machineFilter="+JSON.stringify(reportMachineObjParam);
			 console.log(url)
			 var wind = window.open(url);
			 wind.focus();
	    }
	    
	    $scope.printPopulate = function() {
	    	//$scope.machineFilter.currrentPage = '';
	    	var printFilter = angular.copy($scope.machineFilter);
	    	printFilter.currrentPage = '';
	    	
	    	var cacheFilter   = reportCache.get('filterData');
			
			if(cacheFilter && !angular.equals(cacheFilter, printFilter)) {
				reportCache.removeAll('responseData');
			}
			var cacheResponse = reportCache.get('responseData');
			if (cacheResponse) {
				$scope.store = cacheResponse;
				$timeout(function() {
				    var header = $scope.report.subcat.text;
				    var printContents = document.getElementsByClassName('showMacDowntimePrint')[0].innerHTML;
				    var originalContents = document.body.innerHTML; 
					printableDiv(header,printContents);
					$scope.printable = false;
					$scope.printStatus = false;
			    }, 10);
			} else {
				$http.post($scope.workspace.machineStorePrint,{filter: printFilter, messageHeader:machineMessageHeader}).then(function(response) {
					$scope.store = response.data.store[0];
					reportCache.put('responseData', response.data.store[0]);
					reportCache.put('filterData', printFilter);
					$timeout(function() {
					    var header = $scope.report.subcat.text;
					    var printContents = document.getElementsByClassName('showMacDowntimePrint')[0].innerHTML;
					    var originalContents = document.body.innerHTML; 
						printableDiv(header,printContents);
						$scope.printable = false;
						$scope.printStatus = false;
				    }, 10);
		    	});
			}
	    	
	    }
	    $scope.printDiv = function (divName) {
	    	$scope.printPopulate();
	    	$scope.printStatus = true;
		}
}]);
/*** For exception report ***/
myApp.controller('fetchExceptionData', ['$scope','$http','$stateParams', '$state', '$location', 'utils', '$filter', '$modal', '$q', '$timeout', 'reportCache', function($scope, $http, $stateParams, $state, $location, utils, $filter, $modal, $q, $timeout, reportCache){
	
	  $scope.workspace = {};
	  $scope.showTableData = false; //for pagination
	  
	  $scope.currentReportId = 14;
	  $scope.showTable = false;
	  $scope.showFilter = true;
	  $scope.orderProperty = false; // for column sorting
	  $scope.columnName    = ''; // for column sorting
	  $scope.colSortOrder = {};
	  $scope.exceptionReportDefault = {};
	  $scope.exceptionReport  = [];
	  $scope.showErrorMessage = false;
	  $scope.exceptionReportByEnvelope = true;
	  $scope.exceptionReportByEmployee = false;
	  $scope.showNoDataFound = false;
	  $scope.printStatus = false;
	  $scope.showTableEnv = true;
	  
	  
	  /** set min date as SYSDATE-195 days **/
	  $scope.startMinDate = setStartMinDate();
	  
	  /* Message header for exception report */
      var exceptionMessageHeader = getMessageHeader("exceptionReport", "exception");
	  
	  var reportListJson = getReportConfig($http);
	  
	  $q.all([ reportListJson ]).then(function(result) {
	  	  $scope.reportlist = result[0].data.catagory;
	  	  $scope.report = utils.findById($scope.reportlist, $scope.currentReportId);
	  	  
	      $scope.exceptionReportDefault.viewSection = 'Exception Report - by Envelope';//for exception report
	      $scope.exceptionReportDefault.exceptionStatus   = 'All';//for exception report
	      $scope.exceptionReportDefault.reasonType      = 'All';//for exception report
			
	      $scope.exceptionReportDefault.envelopeEnteredDate;//for exception report
	      $scope.exceptionReportDefault.startDate = new Date();//for exception report
	      $scope.exceptionReportDefault.endDate   = new Date();//for exception report
			
	      $scope.exceptionReportDefault.sortColumnName = 'env';
	      $scope.exceptionReportDefault.sortOrder = 'ASC';
	      $scope.exceptionReportDefault.isPrint = false;
	  	  
	      $scope.exceptionReport = angular.copy($scope.exceptionReportDefault);
		  
	      $scope.workspace.filterTemplate          = $scope.report.subcat.filterTemplate;
		  $scope.workspace.viewSectionListJson     = $scope.report.subcat.viewSectionDetail; 
		  $scope.workspace.viewReasonListJson      = $scope.report.subcat.getReasonList; 
		  $scope.workspace.dataJson                = $scope.report.subcat.dataJson;
		  $scope.workspace.dataJsonByEmp           = $scope.report.subcat.dataJsonByEmp; 
		  $scope.workspace.storeTemplateEnvlp      = $scope.report.subcat.storeTemplate; 
		  $scope.workspace.storeTemplateEmpl       = $scope.report.subcat.storeEmplTemplate;
		  $scope.workspace.dataJsonItemDetail      = $scope.report.subcat.dataJsonItemDetail; 
		  $scope.workspace.printTemplate           = $scope.report.subcat.printTemplate;
		
				
		  //For section part
		    if ($scope.workspace.viewSectionListJson) {
		    	$http.get($scope.workspace.viewSectionListJson).then(function(response) {
		            $scope.workspace.sectionType = response.data.section;
		    	}, function(response) {
  					if(response.status == 401 || response.status == 302) {
  						window.location.reload();
  					}
  				});
		    }
		    
		    //For reason part
		    if($scope.workspace.viewReasonListJson) {
		    	$http.get($scope.workspace.viewReasonListJson).then(function(response) {
		    		$scope.workspace.reasonList = response.data.reasonList;
		    		for(var reason in response.data.reasonList) {
		    			var reasonObj = response.data.reasonList[reason].reason.toLowerCase();
		    			$scope.workspace.reasonList[reason].reason = reasonObj.charAt(0).toUpperCase() + reasonObj.substring(1);
		    			//console.log($scope.workspace.reasonList[reason].reason);
		    		}
		    	}, function(response) {
  					if(response.status == 401 || response.status == 302) {
  						window.location.reload();
  					}
  				});
		    }
		    $scope.updateData();
	  	});	 
	  
	  	$scope.toggled = function(status) {
			if(status == true) {
				angular.element( document.querySelector('.exception-report-section') ).css('z-index', '10');
				angular.element( document.querySelector('.exceptionReport .row:nth-child(1) .col-md-3:nth-child(1) .form-control') ).css('z-index','0');
			} else {
				angular.element( document.querySelector('.exceptionReport .row:nth-child(1) .col-md-3:nth-child(1) .form-control') ).css('z-index','');
			}
		};
		
		
  		
  		$scope.$watch('exceptionReport.viewSection', function() { 
  			$scope.showErrorMessage = false;
  			$scope.hideErrorMessage();
  			var section = $scope.exceptionReport.viewSection;
	    	if(section) {
	    		$scope.exceptionReportByEnvelope = false;
		    	$scope.exceptionReportByEmployee = false;
	    		if(section == 'Exception Report - by Envelope') {
					$scope.exceptionReportByEnvelope = true;
				} else {
					$scope.exceptionReportByEmployee = true;
					var defaultLateEnvDate = new Date();
					defaultLateEnvDate.setDate(defaultLateEnvDate.getDate() - 1);
					$scope.exceptionReport.startDate = defaultLateEnvDate;
					$scope.exceptionReport.endDate   = defaultLateEnvDate;
				}
		    	$scope.showTableData = false;
		    	$scope.showTable     = false;
		    	
		    	$timeout(function() {
		    		$scope.updateData();
		        }, 50); // delay 250 ms
	    	}    	
	    });
  		
	    $scope.$watch('workspace.reasonTypeObj', function() { 
	    	var reasonTypeObj = $scope.workspace.reasonTypeObj;
	    	if(reasonTypeObj) {
	    		$scope.exceptionReport.reasonType = reasonTypeObj.reason;
	    	}   
	    	if(reasonTypeObj === null) {
	    		$scope.exceptionReport.reasonType = 'All';
	    	}
	    });
	    
	    $scope.$watch('exceptionReport.envNumber', function() { 
			var storeNo = $scope.exceptionReport.envNumber;
			if(!storeNo) return
			if (storeNo.match(/^[0-9+]*$/) == null) { //checks for non-numeric characters
				$scope.exceptionReport.envNumber = '';
				if(storeNo.match( /\d+/g ) !== null) { //checks for alpha-numeric character
					$scope.exceptionReport.envNumber = storeNo.match( /\d+/g )[0];
				}
			}
			/*if(storeNo.length > 7) {
				$scope.exceptionReport.envNumber = storeNo.substring(0, 7);
			}*/
			return '';
	    });
	    
	    /*--------------- Date format and min / max date --------------*/
		$scope.open = function($event, ele) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        $scope.workspace.openedEnd = false;
	        $scope.workspace.openedStart = false;
	        $scope.workspace.openedEntered = false;
	        $scope.hideErrorMessage();
	        
	        if (ele == 'start') {
	        	$scope.workspace.openedStart = true;
	        	//angular.element( document.querySelector('input[type="text"].form-control.start + ul.dropdown-menu table thead tr:nth-child(1) th:nth-child(2)') ).attr('colspan',5);
	        	//angular.element( document.querySelector('input[type="text"].form-control.startEmp + ul.dropdown-menu table thead tr:nth-child(1) th:nth-child(2)') ).attr('colspan',5);
	        }
	        if (ele == 'end') {
	        	$scope.workspace.openedEnd = true;
	        	//angular.element( document.querySelector('input[type="text"].form-control.end + ul.dropdown-menu table thead tr:nth-child(1) th:nth-child(2)') ).attr('colspan',5);
	        	//angular.element( document.querySelector('input[type="text"].form-control.endEmp + ul.dropdown-menu table thead tr:nth-child(1) th:nth-child(2)') ).attr('colspan',5);
	        }
	        if(ele == 'entered') {
	        	$scope.exceptionReportDefault.envelopeEnteredDate = new Date();//for exception report
	        	$scope.workspace.openedEntered = true;
	        	//angular.element( document.querySelector('input[type="text"].form-control.entered + ul.dropdown-menu table thead tr:nth-child(1) th:nth-child(2)') ).attr('colspan',5);
	        }
	        $scope.showErrorMessage = false;
	    };
	    
	    $scope.format = 'MM/dd/yyyy';
	    $scope.endMaxDate  = $scope.endMaxDate ? null : new Date();
	    $scope.endMinDate  = null;
	    
	    $scope.updateData = function() {
			if($scope.exceptionReport.viewSection !== 'Select') {
				$scope.errorMessageText = [];
				$scope.showErrorMessage = false;
				$scope.showTableData = false;
				//$scope.exceptionReportDefault =  angular.copy($scope.exceptionReport);
				var dataURL = '';
				var tempURL = '';
				var filterData = {};
				if($scope.exceptionReport.viewSection == 'Exception Report - by Envelope') {
					dataURL = $scope.workspace.dataJson;
					tempURL = $scope.workspace.storeTemplateEnvlp;
					filterData = angular.copy($scope.exceptionReport);
					filterData.startDate = $filter('date')($scope.exceptionReport.startDate, "yyyy-MM-dd");
					filterData.endDate = $filter('date')($scope.exceptionReport.endDate, "yyyy-MM-dd");
					filterData.envelopeEnteredDate = $filter('date')($scope.exceptionReport.envelopeEnteredDate, "yyyy-MM-dd"); //yyyy-MM-dd
				} else {
					dataURL = $scope.workspace.dataJsonByEmp;
					tempURL = $scope.workspace.storeTemplateEmpl;					
					filterData.startDate = $filter('date')($scope.exceptionReport.startDate, "yyyy-MM-dd");
					filterData.endDate = $filter('date')($scope.exceptionReport.endDate, "yyyy-MM-dd");
					filterData.sortColumnName = 'empName';
					filterData.sortOrder = 'ASC';
					//filterData.isPrint = false;
				}
				filterData.currentPageNo = '1';
				filterData.storeNumber = angular.element(document.querySelector('input[id="storeNumber"]')).attr("value");
				
				var endMaxDateValue  = $filter('date')($scope.endMaxDate, "MM-dd-yyyy"); 
				var requiredFieldArray = ["startDate", "endDate"];
				var errorCat = validateRequiredField(requiredFieldArray,$scope.exceptionReport);
				if(errorCat != '') { $scope.errorMessageText.push(errorCat); }
				  
				validateDate($scope.exceptionReport, $scope.endMaxDate, $scope.errorMessageText, $filter);
				if($scope.errorMessageText.length == 0) {
					var sDays = dateDifference($scope.startMinDate, $scope.exceptionReport.startDate);
					var eDays = dateDifference($scope.startMinDate, $scope.exceptionReport.endDate);
					if(sDays < 0 && eDays < 0) {
						$scope.errorMessageText.push("Start date and End date must be within 195 days from current date");
					} else if(sDays < 0) {
						$scope.errorMessageText.push("Start date must be within 195 days from current date");
					} else if(eDays < 0) {
						$scope.errorMessageText.push("End date must be within 195 days from current date");
					}
				}
				if($scope.errorMessageText.length == 0) {
					showLoading();
					$scope.exceptionReport.storeNumber = angular.element(document.querySelector('input[id="storeNumber"]')).attr("value"); //store number hardcoded
					$http.post(dataURL,{messageHeader : exceptionMessageHeader, filter : filterData}).then(function(response) {
						$scope.workspace.data = response.data;
						$scope.workspace.urlStoreTable = tempURL;
						var dataList = [];
						
						if($scope.exceptionReport.viewSection.match(/by Envelope/)) {
							dataList = $scope.workspace.data.repByEnvList;
						} else if($scope.exceptionReport.viewSection.match(/by Employee/)) {
							dataList = $scope.workspace.data.empResponseList;
						}
						/*$scope.exceptionReport.startDate = $scope.exceptionReportDefault.startDate;
						$scope.exceptionReport.endDate = $scope.exceptionReportDefault.endDate;*/
						if(dataList.length == 0) {
							$scope.showTableData = false;
							$scope.showTable     = false;
							$scope.showNoDataFound = true;
						} else {
							$scope.showNoDataFound = false;
							$scope.showTableData = true;
							$scope.showTable     = true;
							var totalRow = response.data.totalRows;
							$scope.totalPageCount = Math.ceil(totalRow / 5);
							
							$scope.workspace.urlStoreTable = tempURL;
							for(var k = 0; k < dataList.length; k++) {
								//For sorting
								var rowData = dataList[k];
								for(var eachCol in rowData) {
									$scope.colSortOrder[eachCol] = {};
									$scope.colSortOrder[eachCol].order = false;
									$scope.colSortOrder[eachCol].firstLoad = true;
								}
							}
							$scope.showExceptionPrint = false;
						}
						hideLoading();
					}, function(response) {
	  					if(response.status == 401 || response.status == 302) {
	  						window.location.reload();
	  					}
	  				});
				} else {
					$scope.showErrorMessage = true;
					//$scope.hideLoading($scope.workspace.id);
					$scope.showTableData = false;
				}
				
			} else {
				$scope.showErrorMessage = true;
				$scope.errorMessageText = "Please select any exception report type";
				$scope.showTableData = false;
			}
	    }
	    
	    $scope.printDiv = function (divName) {
	    	$scope.printPopulate();
	    	$scope.printStatus = true;
	    }
	    
	    $scope.printPopulate = function() {
	    	$scope.exceptionReport.isPrint = true;
	    	var filterData = {};
	    	if($scope.exceptionReport.viewSection == 'Exception Report - by Envelope') {
				dataURL = $scope.workspace.dataJson;
				tempURL = $scope.workspace.storeTemplateEnvlp;
				filterData = angular.copy($scope.exceptionReport);
			} else {
				dataURL = $scope.workspace.dataJsonByEmp;
				tempURL = $scope.workspace.storeTemplateEmpl;					
				filterData.startDate = $scope.exceptionReport.startDate;
				filterData.endDate = $scope.exceptionReport.endDate;
				filterData.sortColumnName = 'empName';
				filterData.sortOrder = 'ASC';
				filterData.currentPageNo = '1';
				filterData.storeNumber   = $scope.exceptionReport.storeNumber;
				filterData.isPrint = true;
			}
	    	
	    	var cacheFilter   = reportCache.get('filterData');
			
			if(cacheFilter && !angular.equals(cacheFilter, filterData)) {
				reportCache.removeAll('responseData');
			}
			var cacheResponse = reportCache.get('responseData');
			if (cacheResponse) {
				$scope.workspace.printableData = cacheResponse;
				$scope.exceptionReport.isPrint = false;
				$scope.showExceptionPrint = true;
				
				$timeout(function() {
					$scope.showExceptionPrint = true;
				    var header = $scope.report.subcat.text;
				    var printContents = document.getElementsByClassName('showExceptionPrint')[0].innerHTML;
				    var originalContents = document.body.innerHTML; 
					printableDiv(header,printContents);
					$scope.printable = false;
					$scope.printStatus = false;
			    }, 10);
			} else {
				$http.post(dataURL,{filter: filterData, messageHeader:exceptionMessageHeader}).then(function(response) {
					$scope.workspace.printableData = response.data;
					$scope.exceptionReport.isPrint = false;
					$scope.showExceptionPrint = true;
					reportCache.put('responseData', response.data);
					reportCache.put('filterData', filterData);
					
					$timeout(function() {
						$scope.showExceptionPrint = true;
					    var header = $scope.report.subcat.text;
					    var printContents = document.getElementsByClassName('showExceptionPrint')[0].innerHTML;
					    var originalContents = document.body.innerHTML; 
						printableDiv(header,printContents);
						$scope.printable = false;
						$scope.printStatus = false;
				    }, 10);
		    	});
			}
	    }
		
		$scope.changeStorePagination = function(type,index, storeNumber,operation) {
			if($scope.currentIndex == '') { return; }
			showLoading();
			var filterData = {};
			var dataURL = '';
			
	    	if($scope.exceptionReport.viewSection == 'Exception Report - by Envelope') {
				dataURL = $scope.workspace.dataJson;
				filterData = angular.copy($scope.exceptionReport);
			} else {
				dataURL = $scope.workspace.dataJsonByEmp;
				filterData.startDate = $scope.exceptionReport.startDate;
				filterData.endDate = $scope.exceptionReport.endDate;
				filterData.sortColumnName = 'empName';
				filterData.sortOrder = 'ASC';
				filterData.storeNumber = $scope.exceptionReport.storeNumber;
				//filterData.isPrint = false;
			}
	    	filterData.currentPageNo = $scope.currentIndex;
			
			$http.post(dataURL,{filter: filterData,messageHeader:exceptionMessageHeader}).then(function(response) {
				$scope.workspace.data = response.data;
				hideLoading();
			}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
		};
	  
	    $scope.hideErrorMessage = function() {
			$scope.showErrorMessage = false;
			$scope.showNoDataFound = false;
			$scope.showSuccessMessage = false;
		}
	  
	    //sort facility
	    $scope.sort = function(filterType, columnName, storeNumber, index) {
	    	showLoading();
	    	if($scope.columnName == columnName) {
	    		$scope.orderProperty = !$scope.orderProperty;
	    	} else {
	    		$scope.columnName    = columnName
	    		$scope.orderProperty = false;
	    	}
	    	($scope.orderProperty == true) ? $scope.exceptionReport.sortOrder = 'DESC' : $scope.exceptionReport.sortOrder = 'ASC';
	    	$scope.exceptionReport.sortColumnName = columnName;
	    	$scope.colSortOrder[columnName].order = $scope.orderProperty;
	    	$scope.colSortOrder[columnName].firstLoad = false;
	    	$scope.exceptionReport.currentPageNo = $scope.workspace.data.currentPageNo;
	    	
	    	var dataURL = '';
			var tempURL = '';
			
	    	if($scope.exceptionReport.viewSection == 'Exception Report - by Envelope') {
				dataURL = $scope.workspace.dataJson;
			} else {
				dataURL = $scope.workspace.dataJsonByEmp;
			}
	    	
	    	$http.post(dataURL,{filter: $scope.exceptionReport, messageHeader:exceptionMessageHeader}).then(function(response) {
	    		$scope.workspace.data = response.data;	
	    		resetSortIcon(columnName,$scope.colSortOrder);
	    		hideLoading();
	    	}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
	    };
	    /* openPopup modifed by shikha for getting envelopeEntered */
	    $scope.openPopup = function (index, enteredDttm, orderType, orderId) {
	    	var exceptionMessageHeader = getMessageHeader("exceptionReport", "exception");
		    var modalInstance = $modal.open({
		     templateUrl: 'res/template/report/envelope.popup.template.html',
		     controller: 'envelopePopup',
		      size: 'lg',
		      resolve: {
		    	  workspaceData: function () {
		    		  $scope.workspace.itemId = index;
		    		  /* openPopup modifed by shikha for getting envelopeEntered */
		    		  $scope.workspace.startDate = enteredDttm;
		    		  $scope.workspace.orderType = orderType;
		    		  $scope.workspace.processingTypeCD = '';
		    		  $scope.workspace.orderId = orderId;
		    		  $scope.workspace.messageHeader = exceptionMessageHeader;
		    		  return $scope.workspace;
		        }
		      }
		    });
		  };   
		  $scope.closePopup = function() {
			  $scope.showErrorContainer = false;
		  }
}]);

myApp.controller('fetchLateEnvelopeData', ['$scope','$http','$stateParams', '$state', '$location', 'utils', '$filter', '$modal', '$timeout', 'reportCache', function($scope, $http, $stateParams, $state, $location, utils, $filter, $modal, $timeout, reportCache){
	
	  $scope.workspace = {};
	  //$scope.format = 'MM/dd/yyyy';
	  $scope.showTableData = false; //for pagination
	  $scope.workspace.filterData ={};
	  $scope.lateEnveDefault = {};//for late Envelope report
	  $scope.lateEnveReport  = [];//for late Envelope report
	  
	  var defaultLateEnvDate = new Date();
	  defaultLateEnvDate.setDate(defaultLateEnvDate.getDate() - 1);
	  $scope.lateEnveDefault.startDate = defaultLateEnvDate; //for late envelope report
	  $scope.lateEnveDefault.endDate   = defaultLateEnvDate; //for late envelope report
	  
	  /** set min date as SYSDATE-195 days **/
	  $scope.startMinDate = setStartMinDate();
			
	  $scope.lateEnveReport = angular.copy($scope.lateEnveDefault); //for late envelope report
	  $scope.currentReportId = 4;
	  $scope.showtable = false;
	  $scope.showFilter = true;
	  $scope.showForCorpScreen = true; //for machine filter report location 
	  $scope.report = {};
	  $scope.lateEnveReport.storeId = angular.element(document.querySelector('input[id="storenumber"]')).attr("value");
	  
	  $scope.orderProperty = false; // for column sorting
	  $scope.columnName    = 'envNumber'; // for column sorting
	  $scope.showNoDataFound = false;
	  $scope.printable = false;
	  $scope.colSortOrder = {};
	  $scope.printStatus = false;
	  
	  
	  /* Message header for Machine Down Time report */
	  var lateEnvMsgHeader = getMessageHeader("FetchLateEnvelopeData", "LateEnvp");
	  $scope.workspace.filterTemplate  = "res/template/report/lateEnvelopeRprt/filter.template.html";
	  $scope.workspace.storeTemplate   = "res/template/report/lateEnvelopeRprt/store.template.html";
	  $scope.workspace.dataJson        = "/PhotoOmniOMSWeb/orders/lateEnvlpRprtReq";
	  $scope.workspace.printJson       = "/PhotoOmniOMSWeb/orders/lateEnvlpRprtReq";
	  $scope.workspace.printTemplate   = "res/template/report/store-report-print.html";
	  
	  //update data
	  $scope.updateData = function() {
		  $scope.errorMessageText = [];
		  var endMaxDateValue  = $filter('date')($scope.endMaxDate, "MM-dd-yyyy"); ; 
		  var requiredFieldArray = ["startDate", "endDate"];
		  var errorCat = validateRequiredField(requiredFieldArray,$scope.lateEnveReport);
		  if(errorCat != '') { $scope.errorMessageText.push(errorCat); }
		  
		  validateDate($scope.lateEnveReport, $scope.endMaxDate, $scope.errorMessageText, $filter);
		  if($scope.errorMessageText.length == 0) {
			  var sDays = dateDifference($scope.startMinDate, $scope.lateEnveReport.startDate);
			  var eDays = dateDifference($scope.startMinDate, $scope.lateEnveReport.endDate);
			  if(sDays < 0 && eDays < 0) {
				  $scope.errorMessageText.push("Start date and End date must be within 195 days from current date");
			  } else if(sDays < 0) {
				  $scope.errorMessageText.push("Start date must be within 195 days from current date");
			  } else if(eDays < 0) {
				  $scope.errorMessageText.push("End date must be within 195 days from current date");
			  }
		  }
		  
			if($scope.errorMessageText.length == 0) {
				showLoading();
		        $scope.lateEnveReport.currrentPage = '1' ;
		        $scope.lateEnveReport.sortColumnName = 'envelopeNumber';
		        $scope.lateEnveReport.sortOrder = 'ASC';
		        //$scope.machineFilter.locationId = '3'; //store has location is `3`
		        var filterData =  angular.copy($scope.lateEnveReport);
		        filterData.startDate = $filter('date')($scope.lateEnveReport.startDate, "dd-MMM-yyyy");
		        filterData.endDate   = $filter('date')($scope.lateEnveReport.endDate, "dd-MMM-yyyy");
				$scope.lateEnveReport.flag = false;
				$scope.showTableData = false;
				$http.post($scope.workspace.dataJson,{filter: filterData, messageHeader:lateEnvMsgHeader}).then(function(response) {
					$scope.workspace.data = response.data.data;
					$scope.workspace.responseData = response.data;
					/*$scope.lateEnveReport.startDate = $scope.lateEnveDefault.startDate;
					$scope.lateEnveReport.endDate = $scope.lateEnveDefault.endDate;*/
					$scope.workspace.urlStoreTable = $scope.workspace.storeTemplate;
					$scope.showTable = true;
					$scope.totalPageCount = Math.ceil(response.data.totalRows/5);
					$scope.showTableData = true;//for pagination
					if(response.data.data.length == 0) {
						$scope.showTable = false;
						$scope.showNoDataFound = true;
					} else {
						$scope.showNoDataFound = false;
						var dataList = $scope.workspace.data;
						for(var k = 0; k < dataList.length; k++) {
							//For sorting
							var rowData = dataList[k];
							for(var eachCol in rowData) {
								$scope.colSortOrder[eachCol] = {};
								$scope.colSortOrder[eachCol].order = false;
								$scope.colSortOrder[eachCol].firstLoad = true;
							}
						}
					}
					hideLoading();
				}, function(response) {
  					if(response.status == 401 || response.status == 302) {
  						window.location.reload();
  					}
  				});
				$scope.showLateEnvPrint = false;
			} else {
				$scope.showErrorMessage   = true;
				$scope.showTableData      = false;
				$scope.showTable          = false;
			}
			
			return;
			
		};
		
		$scope.changeStorePagination = function(index) {
			if($scope.$$childHead.currentIndex == '') { return; }
			showLoading();
			var pagination = angular.copy($scope.lateEnveReport);
			pagination.currrentPage = $scope.$$childHead.currentIndex;
			pagination.sortColumnName = $scope.columnName;
			pagination.startDate = $filter('date')($scope.lateEnveReport.startDate, "dd-MMM-yyyy");
			pagination.endDate   = $filter('date')($scope.lateEnveReport.endDate, "dd-MMM-yyyy");
			
			
			($scope.orderProperty == true) ? pagination.sortOrder = 'DESC' : pagination.sortOrder = 'ASC';
			storeIndex = 0;
			$http.post($scope.workspace.dataJson,{filter: pagination, messageHeader:lateEnvMsgHeader}).then(function(response) {
				$scope.workspace.data = response.data.data;
				$scope.showTableData = true;//for pagination
				hideLoading();
			}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
		};
		
	    $scope.hideErrorMessage = function() {
			$scope.showErrorContainer = false;
			$scope.showNoDataFound = false;
			$scope.showSuccessMessage = false;
		}
	    $scope.printDiv = function (divName) {
		    $scope.printPopulate();
		    $scope.printStatus = true;
	    }
	    
	    $scope.printPopulate = function() {
	    	var printFilter = angular.copy($scope.lateEnveReport);
	    	printFilter.startDate = $filter('date')($scope.lateEnveReport.startDate, "dd-MMM-yyyy");
	    	printFilter.endDate   = $filter('date')($scope.lateEnveReport.endDate, "dd-MMM-yyyy");
	    	printFilter.flag = true;
	    	
	    	var cacheFilter   = reportCache.get('filterData');
			
			if(cacheFilter && !angular.equals(cacheFilter, printFilter)) {
				reportCache.removeAll('responseData');
			}
			var cacheResponse = reportCache.get('responseData');
			if (cacheResponse) {
				$scope.workspace.printData = cacheResponse;
				$timeout(function() {
				    var header = "Late Envelope Report";
				    var printContents = document.getElementsByClassName('showLateEnvPrint')[0].innerHTML;
				    var originalContents = document.body.innerHTML; 
					printableDiv(header,printContents);
					$scope.printable = false;
					$scope.printStatus = false;
			    }, 10);
			} else {
				$http.post($scope.workspace.printJson,{filter: printFilter,messageHeader:lateEnvMsgHeader}).then(function(response) {
					$scope.workspace.printData = response.data.data;
					reportCache.put('responseData', response.data.data);
					reportCache.put('filterData', printFilter);
					$scope.printable = false;
					$scope.printStatus = false;
					$timeout(function() {
					    var header = "Late Envelope Report";
					    var printContents = document.getElementsByClassName('showLateEnvPrint')[0].innerHTML;
					    var originalContents = document.body.innerHTML; 
						printableDiv(header,printContents);
				    }, 10);
		    	});
			}
	    }
	    
	    $scope.updateData();
	  
	  /*--------------- Date format and min / max date --------------*/
		$scope.open = function($event, ele) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        $scope.hideErrorMessage();
	        $scope.workspace.openedEnd = false;
	        $scope.workspace.openedStart = false;
	        if (ele == 'start') {
	        	$scope.workspace.openedStart = true;
	        	//angular.element( document.querySelector('input[type="text"].form-control.start + ul.dropdown-menu table thead tr:nth-child(1) th:nth-child(2)') ).attr('colspan',5);
	        }
	        if (ele == 'end') {
	        	$scope.workspace.openedEnd = true;
	        	//angular.element( document.querySelector('input[type="text"].form-control.end + ul.dropdown-menu table thead tr:nth-child(1) th:nth-child(2)') ).attr('colspan',5);
	        }
	        
	        $scope.showErrorMessage = false;
	        //angular.element( document.querySelector('ul.dropdown-menu table thead tr:nth-child(1) th:nth-child(2)') ).attr('colspan',5);
	    };
	    
	    $scope.format = 'MM/dd/yyyy';
	    $scope.endMaxDate = defaultLateEnvDate; // Today's date cannot be selected
	   
		/*--------------- Date format and min / max date --------------*/
	  
	//sort facility
	    $scope.sort = function(columnName, storeNumber, index) {
	    	showLoading();
	    	var filterData = angular.copy($scope.lateEnveReport);
	    	filterData = prepareSortData(columnName, storeNumber, index, filterData);
	    	filterData.startDate = $filter('date')(filterData.startDate, "dd-MMM-yyyy");
	    	filterData.endDate   = $filter('date')(filterData.endDate, "dd-MMM-yyyy");
	    	
	    	var storeIndex = 0;
	    	$http.post($scope.workspace.dataJson,{filter: filterData, messageHeader:lateEnvMsgHeader}).then(function(response) {
	    		$scope.workspace.data = response.data.data;
				$scope.showTableData = true;//for pagination
				resetSortIcon(columnName,$scope.colSortOrder);
				hideLoading();
	    	}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
	    };
	    
	    var  prepareSortData = function(columnName, storeNumber, index, filterData) {
	    	if($scope.columnName == columnName) {
	    		$scope.orderProperty = !$scope.orderProperty;
	    	} else {
	    		$scope.columnName    = columnName
	    		$scope.orderProperty = false;
	    	}
	    	($scope.orderProperty == true) ? filterData.sortOrder = 'DESC' : filterData.sortOrder = 'ASC';
	    	filterData.sortColumnName = columnName;
	    	$scope.colSortOrder[columnName].order = $scope.orderProperty;
	    	
	    	$scope.colSortOrder[columnName].firstLoad = false;
	    	
	    	return filterData;
	    };
	     
	    $scope.openPopup = function (index,orderOriginType,processingTypeCD,orderid,timeSubmitted) {
	    	var lateEnvMsgHeader = getMessageHeader("FetchLateEnvelopeData", "LateEnvp");
	    	var date = new Date(timeSubmitted);
	    	var modStartDate = $filter('date')(date, "MM-dd-yyyy"); ; 
		    var modalInstance = $modal.open({
		     templateUrl: 'res/template/report/envelope.popup.template.html',
		     controller: 'envelopePopup',
		      size: 'lg',
		      resolve: {
		    	  workspaceData: function () {
		    		  $scope.workspace.itemId = index;
		    		  $scope.workspace.orderType  = orderOriginType;
		    		  $scope.workspace.processingTypeCD = processingTypeCD;
		    		  $scope.workspace.orderId = orderid;
		    		  $scope.workspace.startDate = modStartDate;
		    		  $scope.workspace.messageHeader = lateEnvMsgHeader;
		    		  return $scope.workspace;
		        }
		      }
		    });
		  };   
		  $scope.closePopup = function() {
			  $scope.showErrorContainer = false;
		  }
}]);

myApp.controller("envelopePopup", ['$scope', '$modalInstance', 'workspaceData', '$timeout', '$http', '$filter', function($scope, $modalInstance, workspaceData, $timeout, $http, $filter) {
	$scope.showtable = false;
	  $scope.showDataTD = false;
	  $scope.showNoDataTD = false;
	  $scope.workspace = workspaceData;
	  var filterData = {};
	  $scope.envHistNoData = false;
	  $scope.prodDtlNoData = false;
	  
	  var messageHeader = $scope.workspace.messageHeader;
	  
	  $scope.$watch('workspaces[0].data', function() {
		  var envNum = $scope.workspace.itemId;
		  filterData.envelopeNbr  = $scope.workspace.itemId;
		  filterData.orderType  = $scope.workspace.orderType;
		  filterData.processingTypeCD  = $scope.workspace.processingTypeCD;
		  
		  var date = $scope.workspace.startDate.split(' ')[0];
		 // var date = $filter('date')($scope.workspace.startDate.split(' ')[0];
		 /* var today = $filter('date')(new Date(),'yyyy-MM-dd HH:mm:ss Z');
		  filterData.startDate = $filter('date')($scope.workspace.startDate, "dd-MMM-yyyy");*/
		  
		  filterData.startDate  = date;
		  filterData.orderId = $scope.workspace.orderId;
		  
		  $scope.showSendOut = false;
		  $scope.showMobInt  = false;
		  $scope.showKioImem = false;
		  $scope.showInStore = false;
		 
		  $http.post('/PhotoOmniOMSWeb/orders/fetchEnvelopeDtlsRequest', {messageHeader : messageHeader, filter: filterData}).then(function(response) {
			  $scope.workspace.popupdata = response.data;
			 // console.log($scope.workspace.popupdata);
	      	  $scope.showDataTD = true;
		      if(response.data.envHistoryList && response.data.envHistoryList.length == 0) {
		  		  $scope.envHistNoData = true;
		  	  }
		      
		  	  if(response.data.orderLineInfo && response.data.orderLineInfo.length == 0) {
		  		  $scope.prodDtlNoData = true;
		  	  }
		  	  
		  	  if(response.data != "") {
		  		$scope.envNumber = $scope.workspace.popupdata.envelopeNbr;
		  		var processingTypeCD = $scope.workspace.processingTypeCD.toUpperCase();
			  	var orderType = $scope.workspace.orderType.toUpperCase();
			  	
	          	if(orderType == 'SENDOUT' || processingTypeCD == 'S') {
	          		$scope.showSendOut = true;
	          	}
	          	if(orderType == 'MOBILE' || orderType == 'INTERNET') {
	          		$scope.showMobInt = true;
	          	}
	          	if(orderType == 'KIOSK' || orderType == 'IMEMORIES') {
	          		$scope.showKioImem = true;
	          	}
	          	if (orderType == 'IN-STORE') {
	          		$scope.showInStore = true;
	          	}
		  	  }
		  }, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
		  });
	  });
	  
	  $scope.cancel = function () {
	    $modalInstance.dismiss('cancel');
	  };
}]);
	
	/***** silver canister report *****/

myApp.controller('SilverCanisterController', ['$scope','$http','$stateParams', '$state', '$location', 'utils', '$filter', '$timeout', 'reportCache', function($scope, $http, $stateParams, $state, $location, utils, $filter, $timeout, reportCache){
	
	  $scope.workspace = {};
	  $scope.showTableData = false; //for pagination
	  $scope.workspace.filterData ={};
	  $scope.silverCanisterRprtDefault = {};
	  $scope.silverCanisterRprt  = [];
	  $scope.showErrorContainer = false;
	  $scope.errorMessage = '';
	  $scope.showNoDataFound = false;
	  
			
	  $scope.silverCanisterRprt = angular.copy($scope.silverCanisterRprtDefault); 
	  $scope.report = {};
	  $scope.silverCanisterRprt.location = angular.element(document.querySelector('input[id="storeNumber"]')).attr("value");
	  
	  $scope.silverCanisterRprt.sortColumnName = 'lastCanisterChangeDate';
	  $scope.silverCanisterRprt.sortOrder      = 'ASC';
	  var defaultDate = new Date();
	  defaultDate.setDate(defaultDate.getDate() - 1);
	  $scope.silverCanisterRprt.endDate = defaultDate;
	  
	  var defaultStartDate = new Date();
	  defaultStartDate.setDate(defaultStartDate.getDate() - 1);
	  defaultStartDate.setFullYear(defaultStartDate.getFullYear() - 1);
	  $scope.silverCanisterRprt.startDate = defaultStartDate;
	  
	  $scope.orderProperty = true; // for column sorting
	  $scope.columnName    = 'lastCanisterChangeDate'; // for column sorting
	  
	  $scope.colSortOrder = {};
	  
	  /* Message header for Machine Down Time report */
	  var silverCanisterRprtMsgHeader = getMessageHeader("silverCanReport", "silverCan");
	  $scope.workspace.dataJson       = "/PhotoOmniWeb/servlet/exceptionReport/submitSilverCanisterStoreReportRequest";		  
	 // $scope.updateData('','');
	  //update data
	  
	  $scope.updateData = function() {
		  $scope.errorMessageText = [];
		  var endMaxDateValue  = $filter('date')($scope.endMaxDate, "MM-dd-yyyy"); ; 
		  var requiredFieldArray = ["startDate", "endDate"];
		  var errorCat = validateRequiredField(requiredFieldArray,$scope.silverCanisterRprt);
		  if(errorCat != '') { $scope.errorMessageText.push(errorCat); }
		  
		  validateDate($scope.silverCanisterRprt, $scope.endMaxDate, $scope.errorMessageText, $filter);
		  
			if($scope.errorMessageText.length == 0) {
				showLoading();
				var filterData = angular.copy($scope.silverCanisterRprt);
				filterData.currentPage = 1;
		       
				filterData.startDate = $filter('date')($scope.silverCanisterRprt.startDate, "dd-MM-yy");
				filterData.endDate   = $filter('date')($scope.silverCanisterRprt.endDate, "dd-MM-yy");
				$http.post($scope.workspace.dataJson,{filter: filterData, messageHeader:silverCanisterRprtMsgHeader}).then(function(response) {
					$scope.workspace.data = response.data.SilverCanisterStroeReportRepList;
					var dataLength = $scope.workspace.data.length;
					if(dataLength > 0) {
						$scope.showTableData = true;//for pagination
						$scope.workspace.totalPageCount = Math.ceil(response.data.totalRecords/5);
						$scope.showNoDataFound = false;
						for(var k = 0; k < dataLength; k++) {
							//For sorting
							var rowData = $scope.workspace.data[k];
							for(var eachCol in rowData.SilverCanisterStoreDetails) {
								$scope.colSortOrder[eachCol] = {};
								$scope.colSortOrder[eachCol].order = false;
								$scope.colSortOrder[eachCol].firstLoad = true;
							}
						}
					} else {
						$scope.showNoDataFound = true;
						$scope.showTableData = false;
					}
					hideLoading();
				}, function(response) {
  					if(response.status == 401 || response.status == 302) {
  						window.location.reload();
  					}
  				});
			} else {
				$scope.showErrorContainer = true;
				$scope.showTableData = false;
			}
		};
		
		$scope.changeStorePagination = function(type,index, storeNumber,operation) {
			if($scope.$$childTail.currentIndex == '') { return; }
			showLoading();
			var pagination = angular.copy($scope.silverCanisterRprt);
			pagination.currentPage = $scope.$$childTail.currentIndex;
			pagination.startDate = $filter('date')($scope.silverCanisterRprt.startDate, "dd-MM-yy");
			pagination.endDate   = $filter('date')($scope.silverCanisterRprt.endDate, "dd-MM-yy");
			$http.post($scope.workspace.dataJson,{filter: pagination, messageHeader:silverCanisterRprtMsgHeader}).then(function(response) {
				$scope.workspace.data = response.data.SilverCanisterStroeReportRepList;
				hideLoading();
			}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
		};
		
	    $scope.hideErrorMessage = function() {
			$scope.showErrorContainer = false;
			$scope.showNoDataFound = false;
			$scope.showSuccessMessage = false;
		}
	
	  
	  /*--------------- Date format and min / max date --------------*/
		$scope.open = function($event, ele) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        $scope.hideErrorMessage();
	        $scope.workspace.openedEnd = false;
	        $scope.workspace.openedStart = false;
	        if (ele == 'start') {
	        	$scope.workspace.openedStart = true;
	        	//angular.element( document.querySelector('input[type="text"].form-control.start + ul.dropdown-menu table thead tr:nth-child(1) th:nth-child(2)') ).attr('colspan',5);
	        }
	        if (ele == 'end') {
	        	$scope.workspace.openedEnd = true;
	        	//angular.element( document.querySelector('input[type="text"].form-control.end + ul.dropdown-menu table thead tr:nth-child(1) th:nth-child(2)') ).attr('colspan',5);
	        }
	        
	        $scope.showErrorMessage = false;
	    };
	    
	    $scope.format = 'MM/dd/yyyy';
	    $scope.endMaxDate = new Date();
	   
		/*--------------- Date format and min / max date --------------*/
	  
	//sort facility
	   $scope.sort = function(columnName) {
		    showLoading();
	    	$scope.orderProperty = !$scope.orderProperty;	    	
	    	($scope.orderProperty == true) ? $scope.silverCanisterRprt.sortOrder = 'DESC' : $scope.silverCanisterRprt.sortOrder = 'ASC';
	    	
	    	$scope.colSortOrder[columnName].order = $scope.orderProperty;
	    	$scope.colSortOrder[columnName].firstLoad = false;
	    	
	    	var filterData = angular.copy($scope.silverCanisterRprt);
			filterData.currentPage = $scope.$$childTail.currentIndex;
	       
			filterData.startDate = $filter('date')($scope.silverCanisterRprt.startDate, "dd-MM-yy");
			filterData.endDate   = $filter('date')($scope.silverCanisterRprt.endDate, "dd-MM-yy");
	    	$http.post($scope.workspace.dataJson,{filter: filterData, messageHeader:silverCanisterRprtMsgHeader}).then(function(response) {
	    		$scope.workspace.data = response.data.SilverCanisterStroeReportRepList;
	    		resetSortIcon(columnName,$scope.colSortOrder);
	    		hideLoading();
	    	}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
	    };
	    
	    $scope.updateData();
	    
}]);

/*** For Unclaimed Orders report ***/
myApp.controller('unclaimedOrders', ['$scope','$http','$stateParams', '$state', '$location', 'utils', '$q', '$modal', '$timeout', 'reportCache', function($scope, $http, $stateParams, $state, $location, utils, $q, $modal, $timeout, reportCache){
	
	  $scope.workspace = {};
	  $scope.showTableData = false; //for pagination
	  
	  $scope.currentReportId = 16;
	  $scope.showTable = true;
	  $scope.machineFilter = [];
	  $scope.orderProperty = false; // for column sorting
	  $scope.columnName    = ''; // for column sorting
	  $scope.colSortOrder = {};
	  $scope.unclaimedOrderReport = {};
	  $scope.showErrorMessage = true;
	  $scope.printable = false;
	  $scope.printStatus = false;
	  
	  /* Unclaimed Order report message header */
	  var unclaimedOrderMessageHeader = getMessageHeader("unclaimedOrderReportDetails", "unclaimedOrder");
	  
	  var reportListJson = getReportConfig($http);
	  
	  $q.all([ reportListJson ]).then(function(result) {
	  	  $scope.reportlist = result[0].data.catagory;
	  	  $scope.report = utils.findById($scope.reportlist, $scope.currentReportId);

		  $scope.workspace.storeTemplate   = $scope.report.subcat.storeTemplate;
		  $scope.workspace.dataJson        = $scope.report.subcat.dataJson;
		  $scope.workspace.printJson       = $scope.report.subcat.printJson;
		  $scope.workspace.customerDetailJson = $scope.report.subcat.customerDetailJson;
		  $scope.workspace.printTemplate      = $scope.report.subcat.printTemplate;
		  
		  $scope.unclaimedOrderReport.storeNumber      = angular.element(document.querySelector('input[id="storeNumber"]')).attr("value");
		  
	      $scope.unclaimedOrderReport.sortColumnName = 'ID';
	      $scope.unclaimedOrderReport.sortOrder      = 'ASC';
	      $scope.unclaimedOrderReport.currentPageNo  = '1';
	      $http.post($scope.workspace.dataJson,{filter: $scope.unclaimedOrderReport,messageHeader:unclaimedOrderMessageHeader}).then(function(response) {
	    	  $scope.workspace.urlStoreTable = $scope.workspace.storeTemplate;
	    	  if(response.data.unclaimedBeanList.length > 0) {
            	$scope.workspace.data = response.data;
            	var totalRow = response.data.totalRecord;
				$scope.totalPageCount = Math.ceil(totalRow / 5);
	            $scope.showTableData = true;
	            $scope.showNoDataFound = false;
	            var dataList = $scope.workspace.data.unclaimedBeanList;
	            for(var k = 0; k < dataList.length; k++) {
					//For sorting
					var rowData = dataList[k];
					for(var eachCol in rowData) {
						$scope.colSortOrder[eachCol] = {};
						$scope.colSortOrder[eachCol].order = false;
						$scope.colSortOrder[eachCol].firstLoad = true;
					}
				}
	            //$scope.showUnclaimedPrint = true;
            } else {
            	$scope.showNoDataFound = true;
            }
	    	hideLoading();
	      }, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
	      });
	  });
	  
		$scope.changeStorePagination = function(type,index, storeNumber,operation) {
			if($scope.currentIndex == '') { return; }
			showLoading();
			var pagination = angular.copy($scope.unclaimedOrderReport);
			pagination.currentPageNo = $scope.currentIndex;
			
			$http.post($scope.workspace.dataJson,{filter: pagination,messageHeader:unclaimedOrderMessageHeader}).then(function(response) {
				$scope.workspace.data = response.data;
				hideLoading();
			}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
		};
	  
	    $scope.hideErrorMessage = function() {
			$scope.showErrorContainer = false;
			$scope.showNoDataFound = false;
			$scope.showSuccessMessage = false;
		}
	  
	    //sort facility
	    $scope.sort = function(filterType, columnName, storeNumber, index) {
	    	showLoading();
	    	if($scope.columnName == columnName) {
	    		$scope.orderProperty = !$scope.orderProperty;
	    	} else {
	    		$scope.columnName    = columnName
	    		$scope.orderProperty = false;
	    	}
	    	($scope.orderProperty == true) ? $scope.unclaimedOrderReport.sortOrder = 'DESC' : $scope.unclaimedOrderReport.sortOrder = 'ASC';
	    	$scope.unclaimedOrderReport.sortColumnName = columnName;
	    	$scope.colSortOrder[columnName].order = $scope.orderProperty;
	    	$scope.colSortOrder[columnName].firstLoad = false;
	    	
	    	
	    	$scope.unclaimedOrderReport.currentPageNo = $scope.$$childTail.currentIndex;
	    	
	    	$http.post($scope.workspace.dataJson,{filter: $scope.unclaimedOrderReport, messageHeader:unclaimedOrderMessageHeader}).then(function(response) {
	    		$scope.workspace.data = response.data;
	    		resetSortIcon(columnName,$scope.colSortOrder);
	    		hideLoading();
	    	}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
	    };
	    
	    $scope.openPopup = function (index) {
		    var modalInstance = $modal.open({
		     templateUrl: 'res/template/report/unclaimed.order.html',
		     controller: 'unclaimedOrderCtrl',
		      size: 'lg',
		      resolve: {
		    	  Empworkspace: function () {
		    		  $scope.workspace.custId = index;
		    		  return $scope.workspace;
		        }
		      }
		    });
		  }; 
		  
		  $scope.closePopup = function() {
			  $modalInstance.dismiss('cancel');
		  }
		  $scope.printPopulate = function() {
			 var printableFilter = angular.copy($scope.unclaimedOrderReport);
			 printableFilter.currentPageNo = '';
			 
			 var cacheFilter   = reportCache.get('filterData');
			 if(cacheFilter && !angular.equals(cacheFilter, printableFilter)) {
				 reportCache.removeAll('responseData');
			 }
			 var cacheResponse = reportCache.get('responseData');
			 if (cacheResponse) {
				 $scope.workspace.printableData = cacheResponse;
				 $timeout(function() {
					 $scope.showUnclaimedPrint = false;
					 var header = $scope.report.subcat.text;
					 var printContents = document.getElementsByClassName('showUnclaimedPrint')[0].innerHTML;
					 var originalContents = document.body.innerHTML; 
					 printableDiv(header,printContents);
					 $scope.printStatus = false;
				 }, 10);
			 } else {
				 $http.post($scope.workspace.printJson,{filter: printableFilter, messageHeader:unclaimedOrderMessageHeader}).then(function(response) {
					 $scope.workspace.printableData = response.data;
					 reportCache.put('responseData', response.data);
					 reportCache.put('filterData', printableFilter);
					 $timeout(function() {
						 $scope.showUnclaimedPrint = false;
						 var header = $scope.report.subcat.text;
						 var printContents = document.getElementsByClassName('showUnclaimedPrint')[0].innerHTML;
						 var originalContents = document.body.innerHTML; 
						 printableDiv(header,printContents);
						 $scope.printStatus = false;
					 }, 10);
				 }); 
			 }
		  }
		  $scope.printDiv = function (divName) {
			  $scope.printPopulate();
			  $scope.printStatus = true;
		  }
		  
}]);
//for exception report
myApp.controller("unclaimedOrderCtrl", ['$scope', '$modalInstance', 'Empworkspace', '$timeout', '$http', 
    function($scope, $modalInstance, Empworkspace, $timeout, $http) {
		$scope.showtable = false;
		$scope.workspace = Empworkspace;
		var filterData = {};
        
        /* Message header for unclaimed order report */
         var unclaimedMessageHeader = getMessageHeader("unclaimedReport", "unclaimed");

        $scope.$watch('workspace[0].data', function(newValue, oldValue) {
            $http.post($scope.workspace.customerDetailJson, {messageHeader : unclaimedMessageHeader, customerId: $scope.workspace.custId }).then(function(response) {
            	$scope.customerDetailList = response.data.unclaimedDataList;
            	if($scope.customerDetailList.length > 0){
            		$scope.showNoDataFound = false;
            	} else {
            		$scope.showNoDataFound = true;
            	}
            }, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
                        
        });
        
        $scope.cancel = function () {
    	    $modalInstance.dismiss('cancel');
    	};
    }
]);


/*** For Pay On Fulfillment report ***/
myApp.controller('payonfulfillmentStore', ['$scope','$http','$stateParams', '$state', '$location', 'utils', '$filter', '$q', '$timeout', 'reportCache', function($scope, $http, $stateParams, $state, $location, utils, $filter, $q, $timeout, reportCache){
	
	  $scope.workspace = {};
	  $scope.showTableData = false; //for pagination
	  $scope.workspace.filterData ={};
	  $scope.payOnFilterDefault = {};
	  $scope.payOnFilter  = [];
			
	  $scope.currentReportId = 1;
	  $scope.showtable = false;
	  $scope.showFilter = true;
	  $scope.orderProperty = false; // for column sorting
	  $scope.columnName    = 'envNumber'; // for column sorting
	  $scope.storeNumberDisable = true;
	  $scope.pofHelpText = "EDIUPC is a required field. Please enter a valid EDIUPC number";
	  $scope.dateErrorMessage = '';
	  $scope.printStatus = false;
	  $scope.showStoreHeader = true;
	  
	  hideLoading();
	  
	  /* Message header for Machine Down Time report */
	  var pofMessageHeader = getMessageHeader("PayOnFulFillment", "pof");
	  
	  var reportListJson = getReportConfig($http);
	  
	  $q.all([ reportListJson ]).then(function(result) {
	  	  $scope.reportlist = result[0].data.catagory;
	  	  $scope.report = utils.findById($scope.reportlist, $scope.currentReportId);

	  	  $scope.workspace.filterTemplate         = $scope.report.subcat.filterTemplate;
		  $scope.workspace.storeTemplateDefault   = $scope.report.subcat.storeTemplate;
		  $scope.workspace.storeTemplateEDIUPC    = $scope.report.subcat.storeTemplateEdiupc;
		  $scope.workspace.dataJson        = $scope.report.subcat.dataJson;
		  $scope.workspace.vendorlistJson  = $scope.report.subcat.vendorlistJson;
		  $scope.workspace.exportToCSVJson = $scope.report.subcat.exportToCSVJson;
		  $scope.workspace.printTemplate   = $scope.report.subcat.printTemplate;
	      
		  $scope.payOnFilterDefault.startDate = ''; 
		  $scope.payOnFilterDefault.endDate   = ''; 
		  $scope.payOnFilterDefault.filtertypePay = 'ediupc';
	      $scope.payOnFilterDefault.vendorDesc = 'Select';
		  
		  $scope.payOnFilter = angular.copy($scope.payOnFilterDefault); 
		  
		  $scope.payOnFilter.storeNumber = angular.element(document.querySelector('input[id="storeNumber"]')).attr("value");
		  
		  
		  if ($scope.workspace.vendorlistJson) { 
			  $scope.payOnFilter.vendor = 'Select';
		      $http.post($scope.workspace.vendorlistJson).then(function(response) {
		    	  $scope.workspace.vendorList = response.data.vendor;
		          $scope.workspace.uniqueVendorList = utils.getUniqueVendorName($scope.workspace.vendorList);
		         // $scope.updateData('','');
		      }, function(response) {
					if(response.status == 401 || response.status == 302) {
						window.location.reload();
					}
		      });
		  }
	  });	
	  
	  	$scope.hideErrorMessage = function() {
			$scope.showErrorMessage = false;
			$scope.showNoDataFound = false;
			$scope.showSuccessMessage = false;
	  	}
	  	
	  	/*$scope.exportPOFRptToExcel = function(){
			var filterData = {};
			filterData.filter = angular.copy($scope.payOnFilter);
			var url = $scope.workspace.exportToCSVJson + "?pofDownloadCSVFilter="+JSON.stringify(filterData);
			console.log(url)
			var wind = window.open(url);
			wind.focus();
		}*/
	  	$scope.$watch('workspace.vendorObjPof', function() { 
	    	var vendorObjPof = $scope.workspace.vendorObjPof;
	    	if(vendorObjPof) {
	    		$scope.payOnFilter.vendorDesc = vendorObjPof.description;
	    		var vendorIdArray = utils.getVendorIdArray($scope.workspace.vendorList, vendorObjPof.description);
	    		$scope.payOnFilter.vendor = vendorIdArray.toString();
	    	}
	    	if(vendorObjPof == null) {
	    		$scope.payOnFilter.vendorDesc = 'Select';
	    	}
	    });
	  	
	  	$scope.$watch('payOnFilter.filtertypePay', function() {
	    	$scope.showTableData = false;
	    	$scope.payOnFilter.ediupcValue = '';
	    	$scope.payOnFilter.vendorDesc = 'Select';
	    	$scope.payOnFilter.startDate = '';
	    	$scope.payOnFilter.endDate = '';
	    	$scope.workspace.vendorObjPof = null;
	    	
	    	if($scope.payOnFilter.filtertypePay != 'default') {
	    		$scope.ediUPCStatus = true;
	    	} else {
	    		$scope.ediUPCStatus = false;
	    	}
	    	$scope.showTableData = false;
			$scope.showTable     = false;
	    });
	  //update data
	  $scope.updateData = function(e,filterType) {
		  	$scope.errorMessageText = [];
		  	$scope.payOnFilterDefault =  angular.copy($scope.payOnFilter);
			//modified to show notifications on error
		  	if($scope.payOnFilter.filtertypePay == 'ediupc'){
	      		var requiredDataPresent = $scope.payOnFilter.ediupcValue;
	      		if(!$scope.payOnFilter.ediupcValue.match(/^[0-9]+$/)) {
	      			requiredDataPresent = false;
	      		}
	      	} else {
	      		var endMaxDateValue  = $filter('date')($scope.endMaxDate, "MM-dd-yyyy"); 

	      		var requiredFieldArray = ['startDate', 'endDate'];
	      		var errorCat = validateRequiredField(requiredFieldArray,$scope.payOnFilter);
	      		var requiredDataPresent = true;
	      		
	      		if(errorCat == '') {
	      			var fieldStDate  = angular.element( document.querySelectorAll('input[type="text"].form-control.start') ).val();
		      		var fieldEndDate = angular.element( document.querySelectorAll('input[type="text"].form-control.end') ).val();
		      		
		      		var validationStDate1  = $filter('date')($scope.payOnFilter.startDate, "MM/dd/yyyy"); 
	      			var validationEndDate1 = $filter('date')($scope.payOnFilter.endDate, "MM/dd/yyyy"); 
	      			
	      			if((validationStDate1 && validationEndDate1) && (fieldStDate !== validationStDate1 || fieldEndDate !== validationEndDate1)) {
	      				requiredDataPresent = false;
	      			}
	      			var dateDifferenc = dateDifference($scope.payOnFilter.startDate, $scope.payOnFilter.endDate);
					if(dateDifferenc < 0) {
						requiredDataPresent = false;
					}
					var dateDifference1 = dateDifference($scope.payOnFilter.startDate,$scope.endMaxDate);
					var dateDifference2 = dateDifference($scope.payOnFilter.endDate,$scope.endMaxDate);
					if((dateDifference1 < 0) || (dateDifference2 < 0)) {
		      			requiredDataPresent = false;
		      		}
					
				} else {
					requiredDataPresent = false;
				}
	      	}
          	if(requiredDataPresent) { //true if start date, end date and store number are not blank
          		showLoading();
          		$scope.showErrorMessage = false;
          		$scope.payOnFilter.currrentPage = '1';
          		$scope.showTableData = false;
          		
          		if($scope.payOnFilter.vendorDesc.toLowerCase() == 'select') {
					$scope.payOnFilter.vendor = '';
				}
          		$scope.payOnFilter.storePrint = false;
          		
          		var filterData = angular.copy($scope.payOnFilter);
          		filterData.startDate = $filter('date')($scope.payOnFilter.startDate, "yyyy-MM-dd");
          		filterData.endDate = $filter('date')($scope.payOnFilter.endDate, "yyyy-MM-dd");
          		$http.post($scope.workspace.dataJson,{filter: filterData, messageHeader:pofMessageHeader}).then(function(response) {
  					$scope.workspace.data = response.data;
  					
  					if($scope.payOnFilter.filtertypePay == 'ediupc'){
  						if($scope.workspace.data.productList.length == 0) {
  							$scope.showNoDataFound = true;
  						} else {
  							$scope.showNoDataFound = false;
  							$scope.workspace.urlStoreTable = $scope.workspace.storeTemplateEDIUPC;
  							$scope.showErrorMessage = false;
  		  					$scope.startIndex = '1';
  		  					$scope.showTableData = true;
  		  					$scope.showTable     = true;
  		  					$scope.totalPageCount = Math.ceil(response.data.totalRecord/5);
  						}
  					} else {
  						if($scope.workspace.data.repByPayOnFulfillmentList.length == 0) {
  							$scope.showNoDataFound = true;
  							$scope.showTable       = false;
  						} else {
  							$scope.showNoDataFound = false;
  							$scope.workspace.urlStoreTable = $scope.workspace.storeTemplateDefault;
  							$scope.showErrorMessage = false;
  		  					$scope.startIndex = '1';
  		  					$scope.showTableData = true;
  		  					$scope.showTable     = true;
  		  					$scope.totalPageCount = Math.ceil(response.data.totalRecord/5);
  						}
  					}
  					hideLoading();
          		}, function(response) {
  					if(response.status == 401 || response.status == 302) {
  						window.location.reload();
  					}
  				});
          	} else {
          		$scope.showErrorMessage = true;
          		var errorMessage = "";
          		if($scope.payOnFilter.filtertypePay == 'ediupc'){
          			if(!$scope.payOnFilter.ediupcValue) {
          				errorMessage = "EDIUPC value is a required field";
          			} else {
          				if(!$scope.payOnFilter.ediupcValue.match(/^[0-9]+$/)) {
          					errorMessage = "EDIUPC value should only contain numeric characters";
          				}
          			}
          			$scope.errorMessageText.push(errorMessage);
          		} else {
          			if(errorCat != '') {
      					$scope.errorMessageText.push(errorCat);
          			}
          			errorMessage = validateDate($scope.payOnFilter, $scope.endMaxDate, $scope.errorMessageText, $filter, '270 days');
      				
      				if(!$scope.payOnFilter.storeNumber.match(/^[0-9]+$/)) {
      					errorMessage = "Store number should only contain numeric characters";
      					$scope.errorMessageText.push(errorMessage);
  					}
          		}          		
          	}
          return;			
		};
		
		$scope.changeStorePagination = function(type,index, storeNumber,operation) {
			if($scope.$$childTail.currentIndex == '') { return; }
			showLoading();
			$scope.payOnFilter.currrentPage = $scope.$$childTail.currentIndex;
			$scope.payOnFilter.storePrint = false;
    		$http.post($scope.workspace.dataJson,{filter: $scope.payOnFilter, messageHeader:pofMessageHeader}).then(function(response) {
				$scope.workspace.data = response.data;
				$scope.showErrorMessage = false;
				$scope.showTableData = true;
				hideLoading();
    		}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
		};
		
		$scope.setMachine = function(data, id, e) {
	        e.preventDefault();
	        $scope.hideErrorMessage();
			$scope.machineFilter.machine = data;
			$scope.machineFilter.machineId = id;
	    };
	  
	    $scope.hideErrorMessage = function() {
			$scope.showErrorContainer = false;
			$scope.showNoDataFound = false;
		}
	    
	    $scope.exportPOFRptToExcel = function(){
			var filterData = {};
			filterData.filter = angular.copy($scope.payOnFilter);
			filterData.filter.startDate = $filter('date')($scope.payOnFilter.startDate, "yyyy-MM-dd");
			filterData.filter.endDate   = $filter('date')($scope.payOnFilter.endDate, "yyyy-MM-dd");
			var url = $scope.workspace.exportToCSVJson + "?pofDownloadCSVFilter="+JSON.stringify(filterData);
			
			$http.get(url).then(function(response) {
				var csvFile = response.data;
				var timeStamp = $filter('date')(new Date(), "MM_dd_yyyy") + '_' + $filter('date')(new Date(), "HHmmss_a");
	            var fileName = "PayonFullFilmentReport_"+timeStamp+".csv";
	            var blob = new Blob([csvFile], { type: 'text/csv;charset=utf-8;' });
	            if (navigator.msSaveBlob) { // IE 10+
	                navigator.msSaveBlob(blob, fileName);
	            } else {
	                var link = document.createElement("a");
	                if (link.download !== undefined) { // feature detection
	                    // Browsers that support HTML5 download attribute
	                    var url = URL.createObjectURL(blob);
	                    link.setAttribute("href", url);
	                    link.setAttribute("download", fileName);
	                    link.style.visibility = 'hidden';
	                    document.body.appendChild(link);
	                    link.click();
	                    document.body.removeChild(link);
	                }
	            }
			}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
		}
	
	  
	  /*--------------- Date format and min / max date --------------*/
		$scope.open = function($event, ele) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        $scope.hideErrorMessage();
	        $scope.workspace.openedEnd = false;
	        $scope.workspace.openedStart = false;
	        if (ele == 'start') {
	        	$scope.workspace.openedStart = true;
	        	//angular.element( document.querySelector('input[type="text"].form-control.start + ul.dropdown-menu table thead tr:nth-child(1) th:nth-child(2)') ).attr('colspan',5);
	        }
	        if (ele == 'end') {
	        	$scope.workspace.openedEnd = true;
	        	//angular.element( document.querySelector('input[type="text"].form-control.end + ul.dropdown-menu table thead tr:nth-child(1) th:nth-child(2)') ).attr('colspan',5);
	        }
	        
	        $scope.showErrorMessage = false;
	    };
	    
	    $scope.format = 'MM/dd/yyyy';
	    $scope.endMaxDate = new Date();
	   
		/*--------------- Date format and min / max date --------------*/
	  
	//sort facility
	    $scope.sort = function(columnName, storeNumber, index) {
	    	var filterData = '';
	    	filterData = prepareSortData(columnName, storeNumber, index);
	    	showLoading();
	    	var storeIndex = 0;
	    	$http.post($scope.workspace.dataJson,{filter: filterData, messageHeader:lateEnvMsgHeader}).then(function(response) {
				//$scope.workspace.data = response.data;
	    		hideLoading();				
	    	}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
	    };
	    
	    var  prepareSortData = function(columnName, storeNumber, index) {
	    	var filterData = {};
	    	
	    	if($scope.columnName == columnName) {
	    		$scope.orderProperty = !$scope.orderProperty;
	    	} else {
	    		$scope.columnName    = columnName
	    		$scope.orderProperty = false;
	    	}
	    	($scope.orderProperty == true) ? filterData.sortOrder = 'DESC' : filterData.sortOrder = 'ASC';
	    	filterData.sortColumnName = columnName;
	    	
	    	return filterData;
	    };
	    $scope.printPopulate = function() {
	    	$scope.payOnFilter.storePrint = true;
	    	var filterData = angular.copy($scope.payOnFilter);
	    	var cacheFilter   = reportCache.get('filterData');
			
			if(cacheFilter && !angular.equals(cacheFilter, filterData)) {
				reportCache.removeAll('responseData');
			}
			var cacheResponse = reportCache.get('responseData');
			if (cacheResponse) {
				$scope.workspace.printableData = cacheResponse;
				$scope.showPOFPrint = false;
				$timeout(function() {
					var header = $scope.report.subcat.text;
				    var printContents = document.getElementsByClassName('showPOFPrint')[0].innerHTML;
				    var originalContents = document.body.innerHTML; 
					printableDiv(header,printContents);
					$scope.printStatus = false;
			    }, 10);
			} else {
				$http.post($scope.workspace.dataJson,{filter: $scope.payOnFilter, messageHeader:pofMessageHeader}).then(function(response) {
					$scope.workspace.printableData = response.data;
					$scope.printStatus = false;
					$scope.showPOFPrint = false;
					reportCache.put('responseData', response.data);
					reportCache.put('filterData', filterData);
					$timeout(function() {
						var header = $scope.report.subcat.text;
					    var printContents = document.getElementsByClassName('showPOFPrint')[0].innerHTML;
					    var originalContents = document.body.innerHTML; 
						printableDiv(header,printContents);
						$scope.printStatus = false;
				    }, 10);
				});
			}
		}
	    	    
	    $scope.printDiv = function (divName) {
		    $scope.printPopulate();
		    $scope.printStatus = true;
		}
}]);


/*** common methods ***/
var transactionID = function guid() {
    function _p8(s) {
        var p = (Math.random().toString()+"000000000").substr(2,8);
        return s ? "-" + p.substr(0,4) + "-" + p.substr(4,4) : p ;
    }
    return _p8() + _p8(true) + _p8(true) + _p8();
};

var messageHeaderCurrentTimestamp = function(){
    var str = "";
    var currentTime = new Date()
    var year = currentTime.getFullYear();
     var month = currentTime.getMonth() + 1;
     month = ((month+"").length)<2 ? ("0"+month): month;
    var day = ((currentTime.getDate()+"").length)<2 ? ("0"+currentTime.getDate()): currentTime.getDate();
    var hours = ((currentTime.getHours()+"").length)<2 ? ("0"+currentTime.getHours()): currentTime.getHours();
    var minutes = ((currentTime.getMinutes()+"").length)<2 ? ("0"+currentTime.getMinutes()): currentTime.getMinutes();
    var seconds = ((currentTime.getSeconds()+"").length)<2 ? ("0"+currentTime.getSeconds()): currentTime.getSeconds();
    var millSeconds = ((currentTime.getMilliseconds()+"").length)<2 ? ("0"+currentTime.getMilliseconds()): currentTime.getMilliseconds();
    millSeconds = ((millSeconds+"").length) <3 ? ("0"+millSeconds): millSeconds;
    str += year + "-" + month + "-" + day + "T" + hours + ":" + minutes + ":" + seconds+":"+ millSeconds;
    return str;
};

var PMByEmpMessageHeader = function(cmdName){
	var pMByEmpMessageHeader = {
		"appID": "PHOTOOMNI",
		"transactionID": transactionID(),
		"msgSentTimestamp": messageHeaderCurrentTimestamp(),
		"commandName":"PMReportDetails",
		"versionOfCommand":"1",
		"origin":"PMReport"
	}
	return pMByEmpMessageHeader;
}

/* To get proper date formate*/
/* To get proper date formate*/
var dateConverter = function(date){
    var str = "";
    var currentTime = date;
    var year = currentTime.getFullYear();
    var month = currentTime.getMonth() + 1;
    month = ((month+"").length)<2 ? ("0"+month): month;
    var day = ((currentTime.getDate()+"").length)<2 ? ("0"+currentTime.getDate()): currentTime.getDate();
    str += year + "-" + month + "-" + day;
    return str;
};

var getMessageHeader = function(commandName, origin){
	var header = {
		 "appID": "PHOTOOMNI",
		 "transactionID": transactionID(),
		 "msgSentTimestamp": messageHeaderCurrentTimestamp(),
		 "commandName":commandName,
		 "commandVersion":"1",
		 "origin":origin
	};
	return header;
};

var extend = function ( defaults, options ) {
    var extended = {};
    var prop;
    for (prop in defaults) {
        if (Object.prototype.hasOwnProperty.call(defaults, prop)) {
            extended[prop] = defaults[prop];
        }
    }
    for (prop in options) {
        if (Object.prototype.hasOwnProperty.call(options, prop)) {
            extended[prop] = options[prop];
        }
    }
    return extended;
};
var getReportConfig = function($http) {
	var path = 'res/sampledata/config.report.json';
	var reportListJson = $http.get(path);
	return reportListJson;
};

var printableDiv = function(header,printContents) {
	if (navigator.userAgent.toLowerCase().indexOf('chrome') > -1) {
        var popupWin = window.open('', '_blank', 'width=600,height=600,scrollbars=no,menubar=no,toolbar=no,location=no,status=no,titlebar=no');
        popupWin.window.focus();
        popupWin.document.write('<!DOCTYPE html><html><head>' +
            '<link rel="stylesheet" type="text/css" href="res/css/bootstrap.min.css" />' +
            '<link rel="stylesheet" type="text/css" href="res/css/flat-ui.min.css">'+
            '<link rel="stylesheet" type="text/css" href="https://www.walgreens.com/livestyleguidenew/themes/css/walgreens.css" />'+
            '<link rel="stylesheet" type="text/css" href="res/css/styles.css" />'+
            '</head><body onload="window.print()">'+
            '<h1>' + header + '</h1>'+
            '<div class="reward-body">' + printContents + '</div></html>');
        popupWin.onbeforeunload = function (event) {
            popupWin.close();
            return '.\n';
        };
        popupWin.onabort = function (event) {
            popupWin.document.close();
            popupWin.close();
        }
    } else {
    	var popupWin = window.open('', "mywindow","menubar=1,resizable=1,scrollbars=1");
        popupWin.document.open();
        popupWin.document.write('<html><head>'+
        		'<link rel="stylesheet" type="text/css" href="res/css/bootstrap.min.css" />'+
        		'<link rel="stylesheet" type="text/css" href="res/css/flat-ui.min.css">'+
	            '<link rel="stylesheet" type="text/css" href="https://www.walgreens.com/livestyleguidenew/themes/css/walgreens.css" />'+
	            '<link rel="stylesheet" type="text/css" href="res/css/styles.css" />'+
        		'</head><body onload="window.print()">'+
	            '<h1>' + header + '</h1>'+
	            '<div>'+ printContents + '</div></html>');
        popupWin.document.close();
    }
};
var getDefaultDate = function(cloneDate) {
	var defaultLateEnvDate = new Date();
	var defaultLateEnvStDate = new Date();
	defaultLateEnvDate.setDate(defaultLateEnvDate.getDate() - 1);
	defaultLateEnvStDate.setDate(defaultLateEnvStDate.getDate() - 1);
	cloneDate.startDate = defaultLateEnvStDate;
	cloneDate.endDate   = defaultLateEnvDate;
}
var resetSortIcon = function(columnName,dataList) {
	for(var col in dataList) {
		if(col !== columnName) {
			dataList[col].firstLoad = true;
		}
	}
}

//required field validation
var validateRequiredField = function(requiredFldArray, filterData) {
	   var singularReqIssue = ' is a required field';
	   var pluralReqIssue   = ' are required fields';
	   var errorCategory = [];

	   angular.forEach(requiredFldArray, function (input) {
			if (!filterData[input] || filterData[input] == 'Select') {  
				errorCategory.push(getErrorParam(input));
			}
		});
	   
	   if(errorCategory.length == 0) { return ''; } 
	   else if(errorCategory.length == 1) { return (errorCategory.toString() + singularReqIssue); } 
	   else { return (errorCategory.toString() + pluralReqIssue); }
}

var getErrorParam = function(errorMsg) {
	   var errorObjMapper = errorTextMap();
	   return errorObjMapper[errorMsg];
}

//error params mapping
var errorTextMap = function() {
	   var errorTextMap = {
			'vendorDesc'   : 'Vendor Name',
			'storeNumber'  : 'Store Number',
			'startDate'    : 'Start Date (MM/DD/YYYY)',
			'endDate'      : 'End Date (MM/DD/YYYY)',
			'location'     : 'Location',
			'storeId'      : 'Number',
			'machine'      : 'Machine',
			'name'         : 'Vendor name',
			'fileName'     : '*.csv file',
			'emailIds'     : 'Email Id',
			'searchResult' : 'Event Id',
			'number'       : 'Store Number'
	   }
	   return errorTextMap;
}

var differenceInMonths = function (startDate,endDate, $filter) { 
    var months = dateDifference(startDate,endDate, 'monthDifference');
    return months;      
};

var dateDifference = function (startDate,endDate, month) { 
	if(month) {
		var divider = 1000 * 60 * 60 * 24 * 30;
	} else {
		var divider = 1000 * 60 * 60 * 24;
	}
	
	var dt1 = new Date(startDate);
	var dt2 = new Date(endDate);

	var date = dateDiffInDays(dt1, dt2, divider);
	return date;

};
//a and b are javascript Date objects (start date and end date)
function dateDiffInDays(a, b, divider) {
	
	// Discard the time and time-zone information.
	var utc1 = Date.UTC(a.getFullYear(), a.getMonth(), a.getDate());
	var utc2 = Date.UTC(b.getFullYear(), b.getMonth(), b.getDate());

	return Math.floor((utc2 - utc1) / divider);
}

//date range validation
var validateDate = function(filterData, endMaxDateValue,errorMessageTextArray, $filter, difference, isPMReport) {
	   	var errorMessageText = ''
	   	
  		if(isPMReport && isPMReport == 'pmByReport') { // done for PM by Product
  			var inputStBox = angular.element( document.querySelectorAll('input[type="text"].form-control.start') );
  			var fieldStDate = angular.element(inputStBox[inputStBox.length-1]).val();
  	  		
  			var inputEndBox = angular.element( document.querySelectorAll('input[type="text"].form-control.end') )
  			var fieldEndDate = angular.element(inputEndBox[inputEndBox.length-1]).val();
  		} else {
  			var fieldStDate = angular.element( document.querySelector('input[type="text"].form-control.start') ).val();
  	  		var fieldEndDate = angular.element( document.querySelector('input[type="text"].form-control.end') ).val();
  		}
  		
		var validationStDate1 = $filter('date')(filterData.startDate, "MM/dd/yyyy"); 
		var validationEndDate1 = $filter('date')(filterData.endDate, "MM/dd/yyyy"); 
		if((validationStDate1 && validationEndDate1) && ((fieldStDate && fieldStDate !== validationStDate1) || (fieldEndDate && fieldEndDate !== validationEndDate1))) {
			if(validationStDate1 && fieldStDate !== validationStDate1) {
				errorMessageText = "Start Date is not a valid date in MM/DD/YYYY format";
				errorMessageTextArray.push(errorMessageText);
			}
			if(validationEndDate1 && fieldEndDate !== validationEndDate1) {
				errorMessageText = "End Date is not a valid date in MM/DD/YYYY format";
				errorMessageTextArray.push(errorMessageText);
			}
			return;
		}
	   		
	   	var dateDifferences = dateDifference(filterData.startDate, filterData.endDate);
		
		if((filterData.startDate && filterData.endDate) && dateDifferences < 0) {
			errorMessageText = "Start date cannot be greater than end date";
			errorMessageTextArray.push(errorMessageText);
			return;
		}
		
		var endDateDifference   = dateDifference(filterData.endDate, endMaxDateValue);
		var startDateDifference = dateDifference(filterData.startDate, endMaxDateValue);
		
		if(endDateDifference < 0 && startDateDifference < 0) {
			errorMessageText = "Start Date and End Date cannot be greater than  current date";
			errorMessageTextArray.push(errorMessageText);
			return;
		}
		
		if(startDateDifference < 0) {
			errorMessageText = "Start Date cannot be greater than current date";
			errorMessageTextArray.push(errorMessageText);
			return;
		}
		if(endDateDifference < 0) {
			errorMessageText = "End Date cannot be greater than current date";
			errorMessageTextArray.push(errorMessageText);
			return;
		}
		
		if(difference) {
			var dayDifference = parseInt(difference.split(' ')[0]);
			var daysOrMonths  = difference.split(' ')[1];
			if(daysOrMonths == 'months') {
				var days = differenceInMonths(filterData.startDate, filterData.endDate);
			} else if(daysOrMonths == 'days') {
				var days = dateDifference(filterData.startDate, filterData.endDate);
			}
			
			if(days > dayDifference) { 
				errorMessageText = 'Date range cannot be more than '+difference;
				errorMessageTextArray.push(errorMessageText); 
				return;
			}
		}
		
}

var setStartMinDate = function() {
	var defaultMinDate = new Date();
	defaultMinDate.setDate(defaultMinDate.getDate() - 195);
	return defaultMinDate;
}

/* for making parent menu active ends */

/* hide loader image when content loaded */
var hideLoading = function() {	
	angular.element( document.querySelector( '.loading' ) ).css({'display':'none'}) ;
	angular.element( document.querySelector( '.loading .loadingImg' ) ).css({'display':'none'}) ;
	angular.element( document.querySelector( '.loading .loadingImg img' ) ).css({'display':'none'}) ;
	angular.element( document.querySelector( '.loading .loadingOverlay' ) ).css({'display':'none'}) ;
}
/* show loader image before content loaded */
var showLoading = function() {	
	angular.element( document.querySelector( '.loading' ) ).css({'display':'block'}) ;
	angular.element( document.querySelector( '.loading .loadingImg' ) ).css({'display':'block'}) ;
	angular.element( document.querySelector( '.loading .loadingImg img' ) ).css({'display':'block'}) ;
	angular.element( document.querySelector( '.loading .loadingOverlay' ) ).css({'display':'block'}) ;
}
