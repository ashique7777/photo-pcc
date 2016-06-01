"use strict";
myApp.controller("centralAdminCtrl", ['$scope', '$stateParams', '$state', '$location', 'adminlist', 'utils','$http', function($scope, $stateParams, $state, $location, adminlist, utils,$http) {
	if(!$scope.$parent.centralcentralWorkspaces)
	        $scope.$parent.centralWorkspaces = [];
		
    $scope.centralWorkspaces = $scope.$parent.centralWorkspaces;
	$scope.parentmenu	= '';
    $scope.adminlist 	= adminlist;
	$scope.showFilter 	= true; // Open the filter box by default
	$scope.centralAdmin = {};
	
	if($scope.centralWorkspaces.length) {
		for(var i=0;i<$scope.centralWorkspaces.length;i++) {
			if($scope.centralWorkspaces[i].active == true) {
				$scope.activeIndex 	= utils.checkIfExist($scope.centralWorkspaces, $scope.centralWorkspaces[i].id);
				$state.go("centralAdmin.detail",{centralAdminId:$scope.centralWorkspaces[i].id});
			}
		}
	}
	
	$scope.currentCentralAdminId = null;
    $scope.$watch('currentCentralAdminId', function(newValue, oldValue) {
        if (!$scope.currentCentralAdminId) return;
        $scope.report 		= utils.findById($scope.adminlist, $scope.currentCentralAdminId);
        $scope.activeIndex 	= utils.checkIfExist($scope.centralWorkspaces, $scope.currentCentralAdminId);
		
        var setAllInactive = function() {
            angular.forEach($scope.centralWorkspaces, function(workspace) {
                workspace.active = false;
            });
        };
		$scope.parentmenu = $scope.report.cat.text;
		
        if ($scope.activeIndex != null) {
            setAllInactive();
            $scope.centralWorkspaces[$scope.activeIndex].active = true;
        } else {
            var workObj = {};
            workObj.id = $scope.currentCentralAdminId;
            workObj.parentmenu = $scope.report.cat.text;
            workObj.name = $scope.report.cat.text + " / " + $scope.report.subcat.text;
            workObj.active = true;
            workObj.urlfilterPage = $scope.report.subcat.filterTemplate;
            workObj.urlStoreTable = $scope.report.subcat.storeTemplate;
            workObj.dataJson = $scope.report.subcat.dataJson;
            try {
            	workObj.locationlistJson        = $scope.report.subcat.locationlistJson;
            	workObj.filterCriteriaJson      = $scope.report.subcat.filterCriteriaJson;	//For SIMS Retail Block
            	workObj.exportToCSVJson         = $scope.report.subcat.exportToCSVJson;
            } catch (e) {
                //console.log(e);
            }
            $scope.centralWorkspaces.push(workObj);           
        }
        
        /* hide loader image when content loaded */
    	$scope.hideLoading = function(id) {	
    		angular.element( document.querySelector( '#workspace'+id+' .loading' ) ).css({'display':'none'}) ;
    		angular.element( document.querySelector( '#workspace'+id+' .loading .loadingImg' ) ).css({'display':'none'}) ;
    		angular.element( document.querySelector( '#workspace'+id+' .loading .loadingImg img' ) ).css({'display':'none'}) ;
    		angular.element( document.querySelector( '#workspace'+id+' .loading .loadingOverlay' ) ).css({'display':'none'}) ;
    	}
    	/* show loader image before content loaded */
    	$scope.showLoading = function(id) {	
    		angular.element( document.querySelector( '#workspace'+id+' .loading' ) ).css({'display':'block'}) ;
    		angular.element( document.querySelector( '#workspace'+id+' .loading .loadingImg' ) ).css({'display':'block'}) ;
    		angular.element( document.querySelector( '#workspace'+id+' .loading .loadingImg img' ) ).css({'display':'block'}) ;
    		angular.element( document.querySelector( '#workspace'+id+' .loading .loadingOverlay' ) ).css({'display':'block'}) ;
    	}
        $scope.removeTab = function(index) {
            if ($scope.centralWorkspaces.length == 1) {
                alert("Can not delete all tabs.");
                return;
            }
            var changeIndex = true;
            if (!$scope.centralWorkspaces[index].active) changeIndex = false;
            $scope.centralWorkspaces.splice(index, 1);
            if (changeIndex) {
                $scope.setActive($scope.centralWorkspaces[0].id);
            };
        };
    });

    $scope.setActive = function(id) {
        $location.path("/centralAdmin/" + id);
    };
    $scope.callCentralAdmin = function(id) {
    	$scope.$state.go('centralAdmin.detail',{centralAdminId:id});
    };
}]);

myApp.controller("centralAdminDetailCtrl", ['$scope', '$stateParams', '$state', function($scope, $stateParams, $state) {
    $scope.$parent.currentCentralAdminId = $stateParams.centralAdminId;
}]);

myApp.controller("singleAdminCtrl", ['$scope', '$stateParams', '$state', '$location', 'utils','$http', '$filter', '$timeout', '$modal', function($scope, $stateParams, $state, $location, utils, $http, $filter, $timeout, $modal) {
	
	$scope.simsBlockFilterDefault = {};//for SIMS Retail Block
	$scope.simsBlockFilter 		  = [];//for SIMS Retail Block
	$scope.selectedStores = true;
	
	$scope.simsBlockFilterDefault.locationType   = 'Chain';
	$scope.simsBlockFilterDefault.locationId = '4';
	$scope.simsBlockFilter = angular.copy($scope.simsBlockFilterDefault); //For SIMS Retail Block 
	$scope.selectedStoreNumberArray = []; //For SIMS Retail Block 
	$scope.simsBlockFilter.xlsFileName = "";
	$scope.simsBlockFilter.number = "";
	
	$scope.colSortOrder = {};
	$scope.showTableData   = false;
	
	//Hide the loading as there is no data table initially
    $timeout(function() {
		$scope.hideLoading($scope.workspace.id);
    }, 100);
    
  //For SIMS Retail Block
	if($scope.workspace.filterCriteriaJson) {
		$http.post($scope.workspace.filterCriteriaJson).then(function(response) {
            $scope.workspace.simBlockList = response.data.simRetailBlocOnloadList;
            $scope.workspace.simBlockListArr = [];
            var simsBlockArray = $scope.workspace.simBlockList;
            $scope.workspace.simBlockDefault = simsBlockArray[0];
            for(var k=1; k<simsBlockArray.length; k++) {
            	$scope.workspace.simBlockListArr.push(simsBlockArray[k]);
            }
            $scope.workspace.retailBlockObj = {"retailBlock":"US","priceLevel":"30"};
        });
	}
	
	if ($scope.workspace.locationlistJson) {
        $http.get($scope.workspace.locationlistJson).then(function(response) {
            $scope.workspace.locationType = response.data.location;
        }, function(response) {
			if(response.status == 401 || response.status == 302) {
				window.location.reload();
			}
		});
    }
	
	$scope.$watch('simsBlockFilter.number', function() { 
		var storeNo = $scope.simsBlockFilter.number;
		if(!storeNo) return
		if (storeNo.match(/^[0-9]*$/) == null) { //checks for non-numeric characters
			$scope.simsBlockFilter.number = '';
			if(storeNo.match( /\d+/g ) !== null) { //checks for alpha-numeric character
				$scope.simsBlockFilter.number = storeNo.match( /\d+/g )[0];
			}
		}
		if(storeNo.length > 6) {
			$scope.simsBlockFilter.number = storeNo.substring(0, 6);
		}
		return '';
    });
	
	 $scope.$watch('workspace.simsLocationObj', function() { 
	    	var locationObj = $scope.workspace.simsLocationObj;
	    	if(locationObj) {
	    		$scope.simsBlockFilter.locationId = locationObj.id;
	    		$scope.simsBlockFilter.locationType   = locationObj.text;
	    		$scope.enableFileUpload = false;
	    		$scope.disableNumber = false;
	    		$scope.simsBlockFilter.xlsFileName  = "";
	    		$scope.file = undefined;
	    		if(locationObj.text == 'Upload file for multiple stores' && locationObj.id == '4') {
	    			$scope.enableFileUpload = true;
	    			$scope.disableNumber = true;
	    			$scope.simsBlockFilter.locationType = "store";
	    			$scope.simsBlockFilter.number = "";
	    		}
	    	}
	    	if(locationObj == null) {
	    		$scope.simsBlockFilter.locationType = 'Chain';
	    		$scope.simsBlockFilter.locationId = '5';
	    		$scope.enableFileUpload = false;
	    		$scope.disableNumber = true;
	    		$scope.simsBlockFilter.number = "";
	    		$scope.simsBlockFilter.xlsFileName  = "";
	    	}
	    });
	    
	    $scope.$watch('workspace.retailBlockObj', function() { 
	    	var retailBlockObj = $scope.workspace.retailBlockObj;
	    	if(retailBlockObj) {
	    		$scope.simsBlockFilter.retailBlock = retailBlockObj.retailBlock;
	    	}
	    	
	    	if(retailBlockObj == null) {
	    		$scope.simsBlockFilter.retailBlock = "US";
	    	}
	    });
	
	// for SIMS retail block
    $scope.setSimsFiles = function(element) {
        $scope.$apply(function($scope) {
        	$scope.hideErrorMessage();
        	$scope.showTableData = false;
            console.log('files:', element.files);
	          $scope.file = element.files[0];
              $scope.simsBlockFilter.xlsFileName = $scope.file.name;
              /*for (var i = 0; i < element.files.length; i++) {
                $scope.files.push(element.files[i])
              }*/
        });
    };
    
    $scope.singleRetailUpdate = function(storeNumber) {
    	$scope.hideErrorMessage();
    	var simsBlockValue = [];
    	simsBlockValue.push(storeNumber);
    	var retailBlockArray = $scope.workspace.simBlockList;
    	var prevRetailBlock = $scope.simsBlockFilter.retailBlock;
    	$scope.$state.go('centralAdmin.retailBlock.item', {centralAdminId: $scope.currentCentralAdminId,simsBlockValue: simsBlockValue,retailBlockArray: retailBlockArray, prevRetailBlock: prevRetailBlock });
    };
    
    $scope.updateRetailBlock = function(e) {
    	e.preventDefault();
    	$scope.hideErrorMessage();
    	var simsBlockValue = $scope.selectedStoreNumberArray;
    	var retailBlockArray = $scope.workspace.simBlockList;
    	var prevRetailBlock = $scope.simsBlockFilter.retailBlock;
    	$scope.$state.go('centralAdmin.retailBlock.item', {centralAdminId: $scope.currentCentralAdminId,simsBlockValue: simsBlockValue,retailBlockArray: retailBlockArray, prevRetailBlock: prevRetailBlock });
    }
    
    // for SIMS Retail Block
    $scope.updateSimsData = function() {
    	$scope.clearAllSelection();
    	$scope.errorMessageText = [];
    	$scope.selectedStoreNumberArray = [];
    	var file = "";

    	if($scope.file) {
    		file = $scope.file;
    	}
    	
    	if($scope.simsBlockFilter.locationType == 'Chain') {
    		var requiredFieldArray = ["retailBlock"];
    	} else {
    		if($scope.simsBlockFilter.locationType == 'store') {
        		var requiredFieldArray = ["retailBlock", "xlsFileName"];
        	} else {
        		var requiredFieldArray = ["retailBlock"];
        	}
    	}
		
  		var errorCat = $scope.validateRequiredField(requiredFieldArray,$scope.simsBlockFilter);
		
  		if($scope.simsBlockFilter.locationType == 'Region' && !$scope.simsBlockFilter.number) {
  			errorCat = 'Region Number is a required field';
  		}
  		if($scope.simsBlockFilter.locationType == 'District' && !$scope.simsBlockFilter.number) {
  			errorCat = 'District Number is a required field';
  		}
  		if($scope.simsBlockFilter.locationType == 'Store' && !$scope.simsBlockFilter.number) {
  			errorCat = 'Store Number is a required field';
  		}
  		if(errorCat != '') {
			$scope.errorMessageText.push(errorCat);
		}
		
		if($scope.errorMessageText.length == 0) {
			$scope.showTableData = false;
			$scope.showLoading($scope.workspace.id);
			if(!$scope.simsBlockFilter.number) {
				$scope.simsBlockFilter.number = "";
			}
			var fd = new FormData();
			if($scope.file) { fd.append("file", file); }
			fd.append("retailBlock", $scope.simsBlockFilter.retailBlock);
			fd.append("locationType", $scope.simsBlockFilter.locationType);
			fd.append("number", $scope.simsBlockFilter.number);
			fd.append("pageNo", '1');
			fd.append("sortColumnName", 'storeNumber');
			fd.append("sortOrder", 'ASC');
	    	
	    	var oReq = new XMLHttpRequest();
	    	oReq.open("POST", $scope.workspace.dataJson, true);
	    	oReq.onload = function(oEvent) {
				if (oReq.status == 200) {
					var responseData = JSON.parse(oReq.response);
					$scope.simsBlockFilter.dataList = responseData;
					$scope.workspace.errorString = '';
					if(responseData.errorDetails) {
						$scope.workspace.errorString = responseData.errorDetails.errorString;
					}
					
					var dataList = responseData.simRetailBlockReportResp;
					for(var k = 0; k < dataList.length; k++) {
						//For sorting
						var rowData = dataList[k];
						for(var eachCol in rowData) {
							$scope.colSortOrder[eachCol] = {};
							$scope.colSortOrder[eachCol].order = false;
							$scope.colSortOrder[eachCol].firstLoad = true;
						}
					}
					$timeout(function() {
						var dataLength = $scope.simsBlockFilter.dataList.simRetailBlockReportResp.length;
						if(dataLength > 0) {
							$scope.showTableData = true;
							$scope.totalPageCount  = Math.ceil($scope.simsBlockFilter.dataList.simRetailBlockReportResp[0].totalrecords/5);
							$scope.showNoDataFound = false;
						} else {
							$scope.showTableData = false;
							$scope.showNoDataFound = true;
						}
						$scope.hideLoading($scope.workspace.id);
						
			        }, 50); // delay 250 ms
					
				} else {
					//alert('failure');
				}
	    	};
	    	oReq.send(fd);
		} else {
			$scope.showErrorMessage = true;
			$scope.showTableData = false;
		}
    }
    
    //for export to CSV
    
    $scope.exportSIMSBlock = function() {
    	$scope.selectedStoreNumberArray = [];
    	var file = "";

    	if($scope.file) {
    		file = $scope.file;
    	}
    	
    	if(!$scope.simsBlockFilter.number) {
			$scope.simsBlockFilter.number = "";
		}
		var fd = new FormData();
		if($scope.file) { fd.append("file", file); }
    	fd.append("retailBlock", $scope.simsBlockFilter.retailBlock);
    	fd.append("locationType", $scope.simsBlockFilter.locationType);
    	fd.append("number", $scope.simsBlockFilter.number);
    	fd.append("pageNo", '1');
    	fd.append("sortColumnName", 'storeNumber');
    	fd.append("sortOrder", 'ASC');
    	
    	var oReq = new XMLHttpRequest();
    	oReq.open("POST", $scope.workspace.exportToCSVJson, true);
    	oReq.onload = function(oEvent) {
			if (oReq.status == 200) {
				var csvFile = oReq.response;
			    
			    var blob = new Blob([csvFile], { type: 'text/csv;charset=utf-8;' });
			    if (navigator.msSaveBlob) { // IE 10+
			        navigator.msSaveBlob(blob, "sims_retail_block.csv");
			    } else {
			        var link = document.createElement("a");
			        if (link.download !== undefined) { // feature detection
			            // Browsers that support HTML5 download attribute
			            var url = URL.createObjectURL(blob);
			            link.setAttribute("href", url);
			            link.setAttribute("download", "sims_retail_block.csv");
			            link.style.visibility = 'hidden';
			            document.body.appendChild(link);
			            link.click();
			            document.body.removeChild(link);
			        }
			    }
			} else {
				//alert('failure');
			}
    	};
    	oReq.send(fd);
    	
    }
    
    // for SIMS Retail Block
    $scope.selectedRow = function(storeNumber) {
    	$scope.selectedStores = true;
    	var checkedStatus = $scope.simsBlockFilter.checkedOrNot[storeNumber];
    	var selectedArray = $scope.selectedStoreNumberArray;
    	var index = selectedArray.indexOf(storeNumber);
    	if(checkedStatus && index < 0) {
    		$scope.selectedStoreNumberArray.push(storeNumber);
    	} else {
    		if(!checkedStatus &&  index>= 0) {
        		$scope.selectedStoreNumberArray.splice(index, 1);
        	}
    	}
    	//console.log($scope.selectedStoreNumberArray);
    	if($scope.selectedStoreNumberArray.length > 0) {
    		$scope.selectedStores = false;
    	}
    }
    // for SIMS Retail Block
    $scope.clearAllSelection = function() {
    	var storeArrayList = $scope.simsBlockFilter.checkedOrNot;
    	for(var storeNo in storeArrayList) {
    		storeArrayList[storeNo] = false;
    	}
    	$scope.selectedStoreNumberArray = [];
    	$scope.selectedStores = true;
    }
    
    $scope.simsSortPagination = function(columnName, pagination) {
    	
    	if(pagination !== 'pagination' && columnName !== '') {
    		if($scope.columnName == columnName) {
        		$scope.orderProperty = !$scope.orderProperty;
        	} else {
        		$scope.columnName    = columnName
        		$scope.orderProperty = false;
        	}
    		$scope.colSortOrder[columnName].order = $scope.orderProperty;
	    	$scope.colSortOrder[columnName].firstLoad = false;
    	} else {
    		if($scope.$$childTail.currentIndex == '') { return; }
    	}
    	$scope.showLoading($scope.workspace.id);
    	var file = "";
    	
    	if($scope.file) {
    		file = $scope.file;
    	}
    	
    	var sortOrder = '';
    	var fd = new FormData();
		if($scope.file) { fd.append("file", file); }
		fd.append("retailBlock", $scope.simsBlockFilter.retailBlock);
		fd.append("locationType", $scope.simsBlockFilter.locationType);
		fd.append("number", $scope.simsBlockFilter.number);
		fd.append("pageNo", $scope.currentIndex);
		fd.append("sortColumnName", $scope.columnName);
		
    	($scope.orderProperty == true) ? sortOrder = 'DESC' : sortOrder = 'ASC';
    	fd.append("sortOrder", sortOrder);
    	var oReq = new XMLHttpRequest();
    	oReq.open("POST", $scope.workspace.dataJson, true);
    	oReq.onload = function(oEvent) {
			if (oReq.status == 200) {
				var responseData = JSON.parse(oReq.response);
				$scope.simsBlockFilter.dataList = responseData;
				//$scope.workspace.errorString = responseData.errorDetails.errorString;
				
				if(pagination !== 'pagination' && columnName !== '') {
					$scope.resetSortIcon(columnName,$scope.colSortOrder);
				}
				
				$timeout(function() {
					$scope.totalPageCount = Math.ceil(responseData.simRetailBlockReportResp.length/5);
					$scope.showTableData = true;
					$scope.hideLoading($scope.workspace.id);
		        }, 50); // delay 250 ms
				
			} else {
				//alert('failure');
			}
    	};
    	oReq.send(fd);
    };
    
    $scope.printDiv = function (divName) {
	    var header = $scope.report.subcat.text;
	    var printContents = document.getElementById(divName).innerHTML;	
	    var originalContents = document.body.innerHTML;   
	    var filterData = "";
	    
	    if(document.getElementById('filterData')) {
	    	var filterData = document.getElementById('filterData').innerHTML;
	    }
	    
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
	           ' <div>' + filterData + '</div>'+
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
		            ' <div>' + filterData + '</div>'+
		            '<div>'+ printContents + '</div></html>');
	        popupWin.document.close();
	    }
	    popupWin.document.close();
	    return true;
	}
	
	$scope.open = function($event, ele) {
		$event.preventDefault();
        $event.stopPropagation();
        $scope.hideErrorMessage();
        $scope.workspace.openedEnd   = false;
        $scope.workspace.openedStart = false;
        /*$scope.showErrorMessage = false;*/
        
        if (ele == 'start') {
        	$scope.workspace.openedStart = true;
        }
        if (ele == 'end') {
        	$scope.workspace.openedEnd = true;
        }
    };
    
    $scope.endMaxDate = new Date();
    
	$scope.toggleMin = function() {
		$scope.minDate = $scope.minDate ? null : new Date();
	};
	
	$scope.toggleMin();
	
	 
	$scope.format = 'MM/dd/yyyy';
	
	$scope.resetSortIcon = function(columnName,dataList) {
    	for(var col in dataList) {
    		if(col !== columnName) {
    			dataList[col].firstLoad = true;
    		}
    	}
    }
	
	//required field validation
    $scope.validateRequiredField = function(requiredFldArray, filterData) {
 	   var singularReqIssue = ' is a required field';
 	   var pluralReqIssue   = ' are required fields';
 	   var errorCategory = [];

 	   angular.forEach(requiredFldArray, function (input) {
 			if (!filterData[input] || filterData[input] == 'Select') {  
 				errorCategory.push($scope.getErrorParam(input));
 			}
 		});
 	   
 	   if(errorCategory.length == 0) {
 		   return '';
 	   } else if(errorCategory.length == 1) {
 		   return (errorCategory.toString() + singularReqIssue);
 	   } else {
 		   return (errorCategory.toString() + pluralReqIssue);
 	   }
    };
    
    $scope.getErrorParam = function(errorMsg) {
 	   var errorObjMapper = $scope.errorTextMap();
 	   return errorObjMapper[errorMsg];
    };
    
    //error params mapping
    $scope.errorTextMap = function() {
 	   var errorTextMap = {
 			'vendorDesc'    : 'Vendor Name',
 			'storeNumber'   : 'Store Number',
 			'startDate'     : 'Start Date (MM/DD/YYYY)',
 			'endDate'       : 'End Date (MM/DD/YYYY)',
 			'location'      : 'Location',
 			'storeId'       : 'Number',
 			'machine'       : 'Machine',
 			'name'          : 'Vendor name',
 			'fileName'      : '*.csv file',
 			'emailIds'      : 'Email Id',
 			'searchResult'  : 'Event Id',
 			'number'        : 'Store Number',
 			'retailBlock'   : 'Retail Block',
 			'xlsFileName'   : '*.xlsx file',
 			'equipmentType' : 'Equipment type',
 			'alertMessage'  : 'Alert Message'
 		};
 	   return errorTextMap;
    };
    
    $scope.hideErrorMessage = function() {
		$scope.showErrorMessage    = false;
		$scope.showErrorContainer  = false;
		$scope.showNoDataFound     = false;
		$scope.showSuccessMessage  = false;
        $scope.showSucessContainer = false;
	};
	
	$scope.hideSuccessMessage = function() {
		$scope.showSuccessMessage = false;
	};
	
	$scope.filterToggle = function() {
        if ($scope.showFilter) $scope.showFilter = false;
        else $scope.showFilter = true;
    };
    $scope.dateDiffInDays = function(a, b) {
    	var _MS_PER_DAY = 1000 * 60 * 60 * 24;
    	// Discard the time and time-zone information.
    	var utc1 = Date.UTC(a.getFullYear(), a.getMonth(), a.getDate());
    	var utc2 = Date.UTC(b.getFullYear(), b.getMonth(), b.getDate());

    	return Math.floor((utc2 - utc1) / _MS_PER_DAY);
    };
}]);

myApp.controller("retailBlockCntrl", ['$scope', '$stateParams', '$state', 'utils', '$window','$http',
                                      function($scope, $stateParams, $state, utils, $window, $http) {
      $scope.centralAdminId = $stateParams.centralAdminId;
      $scope.storeNumbersArray  = $stateParams.simsBlockValue; 
      $scope.storeNumbers = $scope.storeNumbersArray.toString();
      $scope.retailBlockArray1 = $stateParams.retailBlockArray;
      $scope.retailBlockArray = [];
      $scope.showUpdateMessage = false;
      $scope.prevRetailBlock = $stateParams.prevRetailBlock;
      
      $scope.prepareDefaultRetailBlock = function() { 
    	  var retailBlocks = $scope.retailBlockArray1;
    	  var prevRetailBlock = $scope.prevRetailBlock;
    	  for(var retail in retailBlocks) {
    		  if(retailBlocks[retail].retailBlock === prevRetailBlock) {
    			  $scope.defaultRetailBlock = retailBlocks[retail].retailBlock;
    			  $scope.retailBlockValue   = $scope.defaultRetailBlock;
    		  } else {
    			  $scope.retailBlockArray.push(retailBlocks[retail]);
    		  }
    	  }
    	  //console.info($scope.retailBlockArray);
      }
      
      $scope.$watch('retailBlockObject', function() { 
      	var retailBlockObj = $scope.retailBlockObject;
      	if(retailBlockObj) {
      		var retailValue = retailBlockObj.retailBlock;
      		$scope.retailBlockValue = retailValue;//retailBlockObj.priceLevel; --- commented on 02-15-2016 ---
      	}
      	if(retailBlockObj == null) {
      		$scope.retailBlockValue   = $scope.defaultRetailBlock;
      	}
      });
      
      $scope.windowWidth = "innerWidth" in window ? window.innerWidth : document.documentElement.offsetWidth;
      // Change left position based on window width
      angular.element( document.querySelector('.modal-custom-backdrop')).css('left','-12px');
      if($scope.windowWidth > 900) {
      	angular.element( document.querySelector('.modal-custom-backdrop + .spopupOuterContainer')).css('left','20%');
      	angular.element( document.querySelector('.modal-custom-backdrop')).css('height','100vh');
      } else {
      	angular.element( document.querySelector('.modal-custom-backdrop + .spopupOuterContainer')).css('left','25%'); 
      	angular.element( document.querySelector('.modal-custom-backdrop')).css('height','120vh');
      	angular.element( document.querySelector('.modal-custom-backdrop')).css('top','-205px');
      }

      
      /* Message header for Print Sign report */
      var simsRetailMessageHeader = getMessageHeader("simsRetailMessageHeader", "simsRetailBlock");
     
      $scope.updateRetailValue = function() {
    	if(!$scope.retailBlockValue) {
    		$scope.showErrorMessage = true;
    		return;
    	}
    	if($scope.defaultRetailBlock != $scope.retailBlockValue) {
    		var simRetailBlockUpdateReq = {};
          	simRetailBlockUpdateReq.storeNumber = $scope.storeNumbersArray;
          	simRetailBlockUpdateReq.retailBlock = $scope.retailBlockValue;
          	//console.info(filterData);
          	var updateRetailBlock = "/PhotoOmniWeb/servlet/submitReportRequest/updateRetailBlockRequest";
          	$http.post(updateRetailBlock, {messageHeader : simsRetailMessageHeader, simRetailBlockUpdateReq: simRetailBlockUpdateReq }).then(function(response) {
          		$scope.statusMessage = response.data.updateStatusMessage;
          		$scope.showUpdateMessage = true;
            });
    	}
      }
      
      $scope.hideStatusMessage = function() {
      	$scope.showUpdateMessage = false;
      	$scope.showErrorMessage = false;
      }
      angular.element($window).on('keydown', function(e){
          if (e.keyCode == 27) {
              $scope.goToReport(e);
          }
      });
      
      $scope.goToReport = function(e) {
          e.preventDefault();
          $state.go("centralAdmin.retailBlock");
      };
          
      $scope.cancel = function(e) {
      	$scope.goToReport(e);
      }
}]);
