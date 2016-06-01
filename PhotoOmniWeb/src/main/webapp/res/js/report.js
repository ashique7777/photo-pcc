"use strict";
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
myApp.controller("reportMainCtrl", ['$scope', '$stateParams', '$state', '$location', 'reportlist', 'utils','$http', 'reportIdMappinglist', function($scope, $stateParams, $state, $location, reportlist, utils,$http, reportIdMappinglist) {
    if(!$scope.$parent.workspaces)
        $scope.$parent.workspaces = [];
     
    /* message header for KIOKS report */
    var kioskMessageHeader = {
			 "appID": "PHOTOOMNI",
			 "transactionID": transactionID(),
			 "msgSentTimestamp": messageHeaderCurrentTimestamp(),
			 "commandName":"kioskReportDetails",
			 "commandVersion":"1",
			 "origin":"kiosk"
	 }
    
    $scope.showLocation = false;
    $scope.workspaces   = $scope.$parent.workspaces;
	$scope.parentmenu	= '';
    $scope.reportlist 	= reportlist;
    $scope.reportIdMappinglist = reportIdMappinglist;
	$scope.showFilter 	= true; // Open the filter box by default
	$scope.showFilterOption = true; // for unclaimed order report it will be false
	
	//$scope.showDataTable = false; // show/hide the data table
	$scope.showLoader 	= false;
	$scope.printSignLookupSearch = ''; //for print sign
	
	/* for making parent menu active(when page navigation comes to this page from another page)  starts */
    if($scope.workspaces.length) {
		for(var i=0;i<$scope.workspaces.length;i++) {
			if($scope.workspaces[i].active == true) {
				$scope.activeIndex 	= utils.checkIfExist($scope.workspaces, $scope.workspaces[i].id);
				$state.go("report.detail",{reportId:$scope.workspaces[i].id});
			}
		}
	}
	/* for making parent menu active ends */
	
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

    $scope.currentReportId = null;
    $scope.$watch('currentReportId', function(newValue, oldValue) {
        if (!$scope.currentReportId) return;
        $scope.report 		= utils.findById($scope.reportlist, $scope.currentReportId);
        $scope.activeIndex 	= utils.checkIfExist($scope.workspaces, $scope.currentReportId);
		
        var setAllInactive = function() {
            angular.forEach($scope.workspaces, function(workspace) {
                workspace.active = false;
            });
        };
		$scope.parentmenu	= $scope.report.cat.text;
		
        if ($scope.activeIndex != null) {
            setAllInactive();
            $scope.workspaces[$scope.activeIndex].active = true;
        } else {
            var workObj = {};
            workObj.id = $scope.currentReportId;
            workObj.parentmenu = $scope.report.cat.text;
            workObj.name = $scope.report.cat.text + " / " + $scope.report.subcat.text;
            workObj.active = true;
            
            workObj.urlStoreTable = $scope.report.subcat.storeTemplate;
            
            //for unclaimed order report
            if($scope.report.subcat.id == '16') {
            	workObj.urlfilterPage = $scope.report.subcat.storeTemplate;
            	$scope.showFilterOption = false;
            } else {
            	workObj.urlfilterPage = $scope.report.subcat.filterTemplate;
            }
            workObj.dataJson = $scope.report.subcat.dataJson;
            try {
                workObj.machinelistJson         = $scope.report.subcat.machinelistJson;
                workObj.locationlistJson        = $scope.report.subcat.locationlistJson;
                workObj.storeTemplateEdiupc     = $scope.report.subcat.storeTemplateEdiupc;
                workObj.storeTemplateDefault    = $scope.report.subcat.storeTemplate;
                workObj.dataJsonEdiupc          = $scope.report.subcat.dataJsonEdiupc;
                workObj.vendorlistJson          = $scope.report.subcat.vendorlistJson;
                workObj.dataJsonItemDetail      = $scope.report.subcat.dataJsonItemDetail;
                workObj.royaltyVendorlistJson   = $scope.report.subcat.royaltyVendorlistJson;
                workObj.viewSectionListJson     = $scope.report.subcat.viewSectionDetail; //For exception report
                workObj.viewReasonListJson      = $scope.report.subcat.getReasonList; //For exception report
                workObj.dataJsonByEmp           = $scope.report.subcat.dataJsonByEmp; //For exception report
                workObj.storeTemplateEnvlp      = $scope.report.subcat.storeTemplate; //For exception report
                workObj.storeTemplateEmpl       = $scope.report.subcat.storeEmplTemplate; //For exception report
                workObj.filterCriteria          = $scope.report.subcat.filterCriteria; // for print sign
                workObj.pofVendorlistJson       = $scope.report.subcat.pofVendorlistJson; //for vendor cost calculation
                workObj.completionStatusJson    = $scope.report.subcat.completionStatusListJson; //for vendor cost calculation
                workObj.exportToCSVJson         = $scope.report.subcat.exportToCSVJson; // for silver canister report file upload
                workObj.customerDetailJson      = $scope.report.subcat.customerDetailJson; // for unclaimed order report popup
                workObj.pofVndrCalcSuccessJson  = $scope.report.subcat.pofSuccessJson; //for vendor cost validation
                workObj.filterCriteriaJson      = $scope.report.subcat.filterCriteriaJson;	//For SIMS Retail Block
                workObj.cannedlistJson          = $scope.report.subcat.cannedlistJson; //for canner report
                workObj.totalRevJson            = $scope.report.subcat.totalRevJson; //for canner report
                
                workObj.kioskmap = {}; 
                workObj.showTab  = true;
                
                var filterCriteria = $scope.report.subcat.filterCriteria;
                if(filterCriteria && $scope.currentReportId == '3') {
                	$http.get(filterCriteria+'?header='+JSON.stringify(kioskMessageHeader)).then(function(response){
                    	workObj.kioskFilterOptions = response.data.data;
                    	angular.forEach(response.data.data,function(val){
                    		workObj.kioskmap [val.CODE_ID] = val.DECODE ;
                		});
                	}, function(response) {
      					if(response.status == 401 || response.status == 302) {
      						window.location.reload();
      					}
      				});
                }
                var royaltyVendorlistJson = $scope.report.subcat.royaltyVendorlistJson;
                if(royaltyVendorlistJson) {
                	 $http.get(royaltyVendorlistJson).then(function(response){
	                	workObj.royaltyVendorNameList = response.data.vendor;
                	 }, function(response) {
       					if(response.status == 401 || response.status == 302) {
       						window.location.reload();
       					}
       				 });
                } 
               
                if($scope.report.subcat.filterDataUrl){
                	$http.get($scope.report.subcat.filterDataUrl).then(function(response){
                    	workObj.defaultFilters = response.data; 
                	}, function(response) {
      					if(response.status == 401 || response.status == 302) {
      						window.location.reload();
      					}
      				});
                }
            } catch (e) {
                //console.log(e);
            }
            $scope.workspaces.push(workObj);
           
        }
        $scope.removeTab = function(index) {
        	var workSpaceLen = $scope.workspaces.length;
        	var workSpace = $scope.workspaces;
            if (workSpaceLen == 1) {
                alert("Can not delete all tabs.");
                return;
            }
            var tempArray = angular.copy(workSpace);
           
            var changeIndex = true;
            if (!$scope.workspaces[index].active) changeIndex = false;
            $scope.workspaces.splice(index, 1);
            
            $state.go($state.current, $stateParams, { reload: true, inherit: false, notify: true });
            if (changeIndex) {
                $scope.setActive($scope.workspaces[0].id);
            }
        }
    });

    $scope.setActive = function(id) {
        $location.path("/report/" + id);
    }
}]);

myApp.controller("retailBlockCntrl", ['$scope', '$stateParams', '$state', 'utils', '$window','$http',
                                      function($scope, $stateParams, $state, utils, $window, $http) {
      $scope.reportId = $stateParams.reportId;
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
    			  $scope.defaultRetailBlock = retailBlocks[retail].priceLevel;
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
      		$scope.retailBlockValue = retailBlockObj.priceLevel;
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
          $state.go("report.retailBlock");
          };
          
          $scope.cancel = function(e) {
          	$scope.goToReport(e);
          }
      }
  ]);

myApp.controller("reportDetailCtrl", ['$scope', '$stateParams', '$state', function($scope, $stateParams, $state) {
    $scope.$parent.currentReportId = $stateParams.reportId;
    $scope.$parent.lookupData      = $stateParams.lookupData;
}]);

myApp.controller("reportPLUCtrl", ['$scope', '$stateParams', '$state', function($scope, $stateParams, $state) {
    $scope.$parent.currentReportId = $stateParams.reportId;
    //$state.current.url = '/plu'; //URL for PLU popup can be provided from here
}]);

myApp.controller("reportLookupCtrl", ['$scope', '$stateParams', '$state', function($scope, $stateParams, $state) {
    $scope.$parent.currentReportId = $stateParams.reportId;
   // $state.current.url = '/lookup'; //URL for print sign lookup popup can be provided from here
}]);

myApp.controller("singleWorkspaceCtrl", ['$scope', '$state', '$stateParams', '$http', '$modal', '$filter', 'utils','$timeout', '$window', function($scope, $state, $stateParams, $http, $modal, $filter, utils,$timeout, $window) {
    //No need to execute any code if url for response not set 
    if (!$scope.workspace.dataJson)
        return;
    $scope.format = 'MM/dd/yyyy';
	$scope.machineFilterDefault = {};
	$scope.machineFilter = [];
	$scope.payOnFilterDefault = {};
	$scope.payOnFilter = [];
	$scope.royaltyrepFilterDefault	= {};
	$scope.royaltyrepFilter = [];
	$scope.showErrorContainer = false;
	$scope.showSucessContainer = false;
	$scope.showNoDataFound = false;
	$scope.itemCount = 5;
	$scope.index = 1;
	var storeIndex;
	$scope.workspace.plu = {};
	$scope.showStoreHeader = true;
	
	$scope.printSignFilterDefault = {};//for print sign
	$scope.printSignFilter 		  = [];//for print sign
	
	$scope.exceptionReportDefault = {};//for exception report
	$scope.exceptionReport 		  = [];//for exception report
	$scope.licenseContentDataDefault = {};//for license content report
	$scope.licenseContentData     = {};//for license content report
	$scope.showErrorMessage       = false; //added to show error message	
	$scope.errorMessageText       = []; //error message content
	$scope.successMessage         = ''; //for success response
	$scope.exceptionReportByEnvelope = false;//for exception report
	$scope.exceptionReportByEmployee = false;//for exception report
	$scope.showTableData             = false;//for exception report
	$scope.completionStatus          = true; // for silver canister report
	$scope.locationStatus            = true; // for silver canister report
	
	$scope.unclaimedOrderReportDefault = {};//for unclaimed order report
	$scope.unclaimedOrderReport 		  = [];//for unclaimed order report
	
	$scope.silverCanisterReportDefault = {};//for silver canister report
	$scope.silverCanisterReport 		  = [];//for silver canister report
	
	$scope.orderProperty = false; // for column sorting
	$scope.columnName    = ''; // for column sorting
	$scope.reverseSorting = false;
	$scope.showLoader 	= false;
	
	$scope.orderByField = 'VendorUpdSilverCanisterDetails.CanisterChangeDate';
	$scope.reverseSort = false;
	$scope.dateErrorMessage = '';
	$scope.storeNumberAddress = '';
	
	
	$scope.silverCanChngdReportDefault = {};//for silver canister changed screen report
	$scope.silverCanChngdReport 		  = [];//for silver canister changed screen report
	
	$scope.payOnFilterVendCostDefault = {};//for pay on fulfillment vendor cost validation
	$scope.payOnFilterVendCost 		  = [];//for pay on fulfillment vendor cost validation
	
	$scope.simsBlockFilterDefault = {};//for SIMS Retail Block
	$scope.simsBlockFilter 		  = [];//for SIMS Retail Block
	$scope.selectedStores = true;
	
	$scope.cannedReportDefault = {};//for canned report
	$scope.cannedReport 	   = [];//for canned report
	
	$scope.pofHelpText = "EDIUPC is a required field. Please enter a valid EDIUPC number";
	
	var lateEvlpMessageHeader = getMessageHeader("lateEnvelopeReport", "lateEnvelope");
	/* KIOKS report message header */
	var kioskMessageHeader = getMessageHeader("kioskReportDetails", "kiosk");
	
	/* Message header for Machine Down Time report */
    var machineMessageHeader = getMessageHeader("MachineDowntimeReportDetails", "Machine");
	
	/* Message header for Print Sign report */
    var printSignsMessageHeader = getMessageHeader("PrintSignsFilterReportDetails", "PrintSigns");
    
	/* Message header for License Content report */
	var licenseContentMessageHeader = getMessageHeader("LicenseContentFilterReportDetails", "LicenseContent");
      
    /* message header for PayOnFulFillment report */
    var pofMessageHeader = getMessageHeader("PayOnFulFillment", "pof");
	
	/* PM By WIC report message header 
	 */
	var pmBYWICMessageHeader = getMessageHeader("pmByWICreport", "pmReport");
	
	/*
	 * Royalty report message header
	 */
	var royaltyReportMessageHeader = getMessageHeader("royaltyReport", "royalty");
	
	/*
	 * Daily PLU message header
	 */
	var dailyPLUMessageHeader = getMessageHeader("dailyPLUreport", "dailyPLU");
	
	/* Message header for exception report */

    var exceptionMessageHeader = getMessageHeader("exceptionReport", "exception");
	
	/* Unclaimed Order report message header */
	var unclaimedOrderMessageHeader = getMessageHeader("unclaimedOrderReportDetails", "unclaimedOrder");
	

	/* Message header for silver canister report */
	var silverCanMessageHeader = getMessageHeader("silverCanReport", "silverCan");
	
	/* Message header for silver canister changed screen report */
	var silverCanChangedScreenMessageHeader = getMessageHeader("silverCanChngdScrn", "silverCanChngScrn");

	 /* message header for PayOnFulFillment report */
    var pofVCMessageHeader = getMessageHeader("PayOnFulFillmentVendCost", "pofvc");
    
    /* message header for PayOnFulFillment report */
    var cannedMessageHeader = getMessageHeader("CannedReport", "canned");
    
	//Hide the loading as there is no data table initially
    $timeout(function() {
		$scope.hideLoading($scope.workspace.id);
    }, 100); 

    /*$http.get($scope.workspace.dataJson).then(function(response) {
        $scope.workspace.data = response.data;
        
    });*/
	if ($scope.workspace.vendorlistJson) { 
		$scope.payOnFilterDefault.vendor = 'Select';
		$scope.payOnFilter	= angular.copy($scope.payOnFilterDefault);
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
	if ($scope.workspace.machinelistJson) {
		$http.post($scope.workspace.machinelistJson,{storeNbr: '',messageHeader:machineMessageHeader}).then(function(response) {
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
	
	if ($scope.workspace.cannedlistJson) {
		$http.get($scope.workspace.cannedlistJson).then(function(response) {
	            $scope.workspace.orderList = response.data.orderType;
	            $scope.workspace.inputList = response.data.inputChannel;
	            
	            $scope.cannedReport.orderTypeName = $scope.workspace.orderList[0].orderTypeName;
	    		$scope.cannedReport.orderTypeId   = $scope.workspace.orderList[0].orderId;
	    		
	    		$scope.cannedReport.inputChannelName = $scope.workspace.inputList[2].inputChannelName;
	    		$scope.cannedReport.inputChannelId   = $scope.workspace.inputList[2].inputChannelId;

	    });
	}

    if ($scope.workspace.vendorlistJson) { 
        $http.post($scope.workspace.vendorlistJson).then(function(response) {
            $scope.workspace.vendorList = response.data.vendor;
            $scope.workspace.uniqueVendorList = utils.getUniqueVendorName($scope.workspace.vendorList);
        }, function(response) {
			if(response.status == 401 || response.status == 302) {
				window.location.reload();
			}
		});
    }
    //For vendor cost calculation
    if($scope.workspace.pofVendorlistJson) {
    	$http.post($scope.workspace.pofVendorlistJson).then(function(response) {
            $scope.workspace.pofVendorNameList = response.data.vendor;
    	}, function(response) {
			if(response.status == 401 || response.status == 302) {
				window.location.reload();
			}
		});
    }
    
  //For vendor cost calculation
    if($scope.workspace.completionStatusJson) {
    	$http.post($scope.workspace.completionStatusJson).then(function(response) {
            $scope.workspace.completionStatus = response.data.completionStatus;
    	}, function(response) {
			if(response.status == 401 || response.status == 302) {
				window.location.reload();
			}
		});
    }
    
    
    
  //For exception report
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
    	}, function(response) {
			if(response.status == 401 || response.status == 302) {
				window.location.reload();
			}
		});
    }
    
   
    if(!$scope.workspace.filtertypePay) {
        $scope.payOnFilterDefault.filtertypePay = 'ediupc';
        $scope.payOnFilterDefault.vendorDesc = 'Select';
        $scope.payOnFilterDefault.startDate = '';
        $scope.payOnFilterDefault.endDate = '';
        $scope.payOnFilterVendCostDefault.vendorDesc = 'Select';
        $scope.payOnFilterVendCostDefault.pendingApprovalList ={};
        $scope.payOnFilterVendCostDefault.checkBox            ={};
        $scope.payOnFilterVendCostDefault.selectedRowArray    =[];
		$scope.machineFilterDefault.location   = 'Chain';
		$scope.machineFilterDefault.locationId = '4';
		
		$scope.machineFilterDefault.machine    = 'All';
		$scope.machineFilterDefault.machineId = '';
		
		//$scope.machineFilterDefault.startDate  = new Date();
		//$scope.machineFilterDefault.endDate    = new Date();
		getDefaultDate($scope.machineFilterDefault); //set default date for machine downtime
		$scope.endMacMaxDate = $scope.machineFilterDefault.endDate; // set end max date as yesterday's date
		$scope.machineFilterDefault.sortColumnName = 'ID';
		$scope.machineFilterDefault.sortOrder = 'ASC';
		$scope.royaltyrepFilterDefault.vendor = 'Select';
		$scope.machineFilter = angular.copy($scope.machineFilterDefault);
		$scope.payOnFilter = angular.copy($scope.payOnFilterDefault);
		
		$scope.payOnFilter.startDate = new Date();
        $scope.payOnFilter.endDate = new Date();
        
		$scope.royaltyrepFilter = angular.copy($scope.royaltyrepFilterDefault);
		
		$scope.exceptionReportDefault.viewSection = 'Select';//for exception report
		$scope.exceptionReportDefault.exceptionStatus   = 'All';//for exception report
		$scope.exceptionReportDefault.reasonType      = 'All';//for exception report
		
		$scope.exceptionReportDefault.envelopeEnteredDate     = new Date();//for exception report
		$scope.exceptionReportDefault.startDate = new Date();//for exception report
		$scope.exceptionReportDefault.endDate   = new Date();//for exception report
		
		$scope.exceptionReportDefault.sortColumnName = 'envNumber';
		$scope.exceptionReportDefault.sortOrder = 'ASC';
		
		$scope.licenseContentDataDefault.startDate = new Date();
        $scope.licenseContentDataDefault.endDate = new Date();
        
        $scope.printSignFilterDefault.startDate = new Date();
        $scope.printSignFilterDefault.endDate = new Date();
		
		$scope.exceptionReport = angular.copy($scope.exceptionReportDefault);//for exception report
		$scope.licenseContentData = angular.copy($scope.licenseContentDataDefault);//for license content report
		$scope.printSignFilter = angular.copy($scope.printSignFilterDefault);//for print sign report
		
		$scope.unclaimedOrderReport = angular.copy($scope.unclaimedOrderReportDefault);//for unclaimed order report
		
		$scope.silverCanisterReport = angular.copy($scope.silverCanisterReportDefault);
		
		$scope.silverCanisterReport.location = 'Chain';
		$scope.silverCanisterReport.status = 'In progress';
		//$scope.silverCanisterReport.startDate = new Date();//for silver canister report
		//$scope.silverCanisterReport.endDate   = new Date();//for silver canister report
		//$scope.payOnFilter.vendor = data;
		
		$scope.silverCanChngdReportDefault.fileName = '';
		$scope.silverCanChngdReport = angular.copy($scope.silverCanChngdReportDefault);
		$scope.payOnFilterVendCost = angular.copy($scope.payOnFilterVendCostDefault);
		
		// To set default date for PM by WIC, Royalty and Plu report
		$scope.workspace.pmWICFilter = {};
		$scope.workspace.pmWICFilter.startDate = new Date();
		$scope.workspace.pmWICFilter.endDate = new Date();
		
		$scope.workspace.royaltyrepFilter = {};
		$scope.workspace.royaltyrepFilter.startDate = new Date();
		$scope.workspace.royaltyrepFilter.endDate = new Date();
		
		$scope.workspace.plu = {};
		$scope.workspace.plu.startDate = new Date();
		$scope.workspace.plu.endDate = new Date();
		$scope.workspace.plulist = [];
		
		//canned Report
		var defaultCannedDate = new Date();
		defaultCannedDate.setDate(defaultCannedDate.getDate() - 30);
		$scope.cannedReportDefault.startDate = defaultCannedDate;//for canned report
		$scope.cannedReportDefault.endDate   = new Date();//for canned report
		$scope.cannedReport = angular.copy($scope.cannedReportDefault);
		
		$scope.simsBlockFilterDefault.locationType   = 'Chain';
		$scope.simsBlockFilterDefault.locationId = '4';
		$scope.simsBlockFilter = angular.copy($scope.simsBlockFilterDefault); //For SIMS Retail Block 
		$scope.selectedStoreNumberArray = []; //For SIMS Retail Block 
		$scope.simsBlockFilter.xlsFileName = "";
		$scope.simsBlockFilter.number = "";
		
		$scope.colSortOrder = {};

	}
    
    //For unclaimed order report
    if($scope.workspace.id == '16') {
    	$scope.unclaimedOrderReport.storeNumber    = '50001';
    	$scope.unclaimedOrderReport.sortColumnName = 'ID';
    	$scope.unclaimedOrderReport.sortOrder      = 'ASC';
    	$scope.unclaimedOrderReport.currentPageNo  = '1';
    	$http.post($scope.workspace.dataJson,{filter: $scope.unclaimedOrderReport,messageHeader:unclaimedOrderMessageHeader}).then(function(response) {
            $scope.workspace.data = response.data;
            
            if(response.data.unclaimedBeanList.length > 0) {
            	var totalRow = response.data.totalRecord;
    			$scope.totalPageCount = Math.ceil(totalRow / 5);
                $scope.showTableData = true;
                $scope.showNoDataFound = false;
            } else {
            	$scope.showNoDataFound = true;
            }
        });
    }
    
    $scope.$watch('machineFilter.storeId', function() { 
		var storeNo = $scope.machineFilter.storeId;
		if(!storeNo) return
		if (storeNo.match(/^[0-9]*$/) == null) { //checks for non-numeric characters
			$scope.machineFilter.storeId = '';
			if(storeNo.match( /\d+/g ) !== null) { //checks for alpha-numeric character
				$scope.machineFilter.storeId = storeNo.match( /\d+/g )[0];
			}
		}
		if(storeNo.length > 7) {
			$scope.machineFilter.storeId = storeNo.substring(0, 7);
		}
		return '';
    });
    
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
    
    $scope.$watch('payOnFilter.storeNumber', function() { 
		var storeNo = $scope.payOnFilter.storeNumber;
		if(!storeNo) return
		if (storeNo.match(/^[0-9]*$/) == null) { //checks for non-numeric characters
			$scope.payOnFilter.storeNumber = '';
			if(storeNo.match( /\d+/g ) !== null) { //checks for alpha-numeric character
				$scope.payOnFilter.storeNumber = storeNo.match( /\d+/g )[0];
			}
		}
		if(storeNo.length > 5) {
			$scope.payOnFilter.storeNumber = storeNo.substring(0, 5);
		}
		return '';
    });
    
    $scope.$watch('payOnFilter.ediupcValue', function() { 
		var ediupcValue = $scope.payOnFilter.ediupcValue;
		if(!ediupcValue) return
		if (ediupcValue.match(/^[0-9]*$/) == null) { //checks for non-numeric characters
			$scope.payOnFilter.ediupcValue = '';
			if(ediupcValue.match( /\d+/g ) !== null) { //checks for alpha-numeric character
				$scope.payOnFilter.ediupcValue = ediupcValue.match( /\d+/g )[0];
			}
		}
		/*if(storeNo.length > 7) {
			$scope.machineFilter.storeId = storeNo.substring(0, 7);
		}*/
		return '';
    });
    
  //canned report
    $scope.$watch('workspace.orderTypeObj', function() { 
    	var orderObj = $scope.workspace.orderTypeObj;
    	if(orderObj) {
    		orderObj = JSON.parse(orderObj);
    		$scope.cannedReport.orderTypeName = orderObj.orderTypeName;
    		$scope.cannedReport.orderTypeId   = orderObj.orderId;
    	}
    	if(orderObj === "") {
    		$scope.cannedReport.orderTypeName = $scope.workspace.orderList[0].orderTypeName;
    		$scope.cannedReport.orderTypeId   = $scope.workspace.orderList[0].orderId;
    	}
    });
    
  //canned report
    $scope.$watch('workspace.inputChannelObj', function() { 
    	var inputChannelObj = $scope.workspace.inputChannelObj;
    	if(inputChannelObj) {
    		inputChannelObj = JSON.parse(inputChannelObj);
    		$scope.cannedReport.inputChannelName = inputChannelObj.inputChannelName;
    		$scope.cannedReport.inputChannelId   = inputChannelObj.inputChannelId;
    	}
    	if(inputChannelObj === "") {
    		$scope.cannedReport.inputChannelName = $scope.workspace.inputList[2].inputChannelName;
    		$scope.cannedReport.inputChannelId   = $scope.workspace.inputList[2].inputChannelId;
    	}
    });
    
    $scope.$watch('silverCanisterReport.number', function() { 
		var storeNo = $scope.silverCanisterReport.number;
		if(!storeNo) return
		if (storeNo.match(/^[0-9]*$/) == null) { //checks for non-numeric characters
			$scope.silverCanisterReport.number = '';
			if(storeNo.match( /\d+/g ) !== null) { //checks for alpha-numeric character
				$scope.silverCanisterReport.number = storeNo.match( /\d+/g )[0];
			}
		}
		if(storeNo.length > 5) {
			$scope.silverCanisterReport.number = storeNo.substring(0, 5);
		}
		return '';
    });
    
    $scope.$watch('workspace.macLocationObj', function() { 
    	var locationObj = $scope.workspace.macLocationObj;
    	if(locationObj) {
    		$scope.machineFilter.locationId = locationObj.id;
    		$scope.machineFilter.location   = locationObj.text;
    		$scope.showLocation = true;
    	}
    	if(locationObj == null) {
    		$scope.machineFilter.location = 'Chain';
    		$scope.machineFilter.locationId = '4';
    		$scope.showLocation = false;
    	}
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
    
    $scope.$watch('workspace.locationObj', function() { 
    	var locationObj = $scope.workspace.locationObj;
    	if(locationObj) {
    		$scope.silverCanisterReport.location   = locationObj.text;
    		$scope.locationStatus = false;
    	}
    	if(locationObj === null) {
    		$scope.silverCanisterReport.location   = 'Chain';
    		$scope.locationStatus = true;
    		$scope.silverCanisterReport.number = '';
    	}
    });
    
    $scope.$watch('silverCanisterReport.status', function() { 
    	var completionObj = $scope.silverCanisterReport.status;
    	if(completionObj) {
    		if(completionObj == 'Completed') {
    			$scope.completionStatus = false;
    			if(!$scope.silverCanisterReport.startDate) {
    				$scope.silverCanisterReport.startDate = new Date();
    			}
    			if(!$scope.silverCanisterReport.endDate) {
    				$scope.silverCanisterReport.endDate = new Date();
    			}
    		} else {
    			$scope.completionStatus = true;
    			$scope.silverCanisterReport.startDate = '';
    			$scope.silverCanisterReport.endDate   = '';
    		}
    	}
    });
    
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
    
    $scope.$watch('workspace.vendorObj', function() { 
    	var vendorObj = $scope.workspace.vendorObj;
    	if(vendorObj) {
    		$scope.payOnFilterVendCost.vendorDesc = vendorObj.description;
    		var vendorIdArray = utils.getVendorIdArray($scope.workspace.vendorList, vendorObj.description);
    		$scope.payOnFilterVendCost.vendorName = vendorIdArray.toString();
    	}
    	if(vendorObj == null) {
    		$scope.payOnFilterVendCost.vendorName = "";
    		$scope.payOnFilterVendCost.vendorDesc = "";
    	}
    });
    
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
    
    
    //For pof - vendor cost calculation, store number validation
    $scope.$watch('payOnFilterVendCost.storeNumber', function() { 
		var storeNo = $scope.payOnFilterVendCost.storeNumber;
		if(!storeNo) return
		if (storeNo.match(/^[0-9]*$/) == null) { //checks for non-numeric characters
			$scope.payOnFilterVendCost.storeNumber = '';
			if(storeNo.match( /\d+/g ) !== null) { //checks for alpha-numeric character
				$scope.payOnFilterVendCost.storeNumber = storeNo.match( /\d+/g )[0];
			}
		}
		if(storeNo.length > 5) {
			$scope.payOnFilterVendCost.storeNumber = storeNo.substring(0, 5);
		}
		return '';
    });
    $scope.sortFields = {
        column: '',
        descending: false
    };    
    $scope.changeSorting = function(column) {

       var sort = $scope.sortFields;
 
       if (sort.column == column) {
           sort.descending = !sort.descending;
       } else {
          sort.column = column;
          sort.descending = false;
       }
    };
    
    $scope.setFiles = function(element) {
        $scope.$apply(function($scope) {
        	$scope.hideErrorMessage();
            //console.log('files:', element.files);
        	$scope.showTableData = false;
	        $scope.file = element.files[0];
            $scope.silverCanChngdReport.fileName = $scope.file.name;
            /*for (var i = 0; i < element.files.length; i++) {
                $scope.files.push(element.files[i])
            }*/
        });
    };
   
    
    $scope.uploadFile = function() {
    	var file = $scope.file;
    	
    	var errorTextMap = {
				'name': 'Vendor name',
				'fileName'  : '*.csv file'
		}
		var errorMessage  = " Please provide values for ";
		var errorCategory = "";
		
		
		angular.forEach(["name", "fileName"], function (input) {
            if (!$scope.silverCanChngdReport[input]) {  
            	var showError = true;
            	
            	if(errorCategory !== "" && showError === true) {
            		errorCategory = errorCategory + ", ";
            	}
        		if(showError === true) {
        			errorCategory = errorCategory + errorTextMap[input] + ' ';
                	$scope.showErrorContainer = true;
        		}
        	
            }
        });
		
		$scope.errorMessage = errorMessage + errorCategory;
		
		if(errorCategory == "") {
			var fd = new FormData();
	    	fd.append("file", file);
	    	fd.append("vendorName", $scope.silverCanChngdReport.name);
	    	
	    	var oReq = new XMLHttpRequest();
	    	oReq.open("POST", $scope.workspace.dataJson, true);
	    	oReq.onload = function(oEvent) {
				if (oReq.status == 200) {
					var responseData = JSON.parse(oReq.response);
					$scope.silverCanChngdReport.dataList = responseData;
					$scope.workspace.data = createTableDataForPagination(0,4,responseData.filter);
					$scope.workspace.errorString = responseData.errorDetails.errorString;
					
					$timeout(function() {
						var dataLength = responseData.filter.length;
						if(dataLength == 0) {
							$scope.showNoDataFound = true;
						} else {
							$scope.totalPageCount = Math.ceil(dataLength/5);
							$scope.showNoDataFound = false;
						}
						$scope.showTableData = true;
			        }, 50); // delay 250 ms
					
				} else {
					if(oReq.status == 401 || oReq.status == 302) {
  						window.location.reload();
  					}
				}
	    	};
	    	oReq.send(fd);
		} else {
			$scope.showErrorMessage = true;
			$scope.hideLoading($scope.workspace.id);
			$scope.showTableData = false;
		}
    }
    
    $scope.changeCustomPagination = function() {
    	var currentIndex = $scope.currentIndex;
    	var startIndex = (currentIndex - 1)*5;
    	var endIndex = startIndex + 4;
    	var data = $scope.silverCanChngdReport.dataList.filter;
    	
    	$scope.workspace.data = createTableDataForPagination(startIndex,endIndex,data);
    	$scope.showTableData = true;
    };

    var createTableDataForPagination = function(startIndex, endIndex, data) {
    	var maxData = 5;
    	var dataLength = (data.length-1);
    	var endIndex = dataLength <= endIndex ? dataLength : endIndex;
    	
    	var finalArray = [];
    	for(var i = startIndex;i <= endIndex; i++) {
    		finalArray.push(data[i]);
    	}
    	return finalArray;
    };
    
    $scope.$watch('printSignLookupSearch ', function() {
    	$scope.printSignFilter.searchResult = $scope.printSignLookupSearch.signName;
    });
    
	$scope.tempKioskFilter = {
		 "criteria": '',//$scope.workspace.kioskFilterOptions[0],
	"operator":'=',
	"value": ""
	   }
	   $scope.workspace.kioskFilters=[];
	   $scope.workspace.printExportFilter=[];
	   $scope.workspace.showKioskButtonOption = true;
	   
	   $scope.workspace.addKioskCriteria = function($event) {
	       $event.preventDefault();
	       $event.stopPropagation();
	       //check if any filter is empty
	   $scope.workspace.kioskFilters.push($scope.tempKioskFilter);
	   $scope.tempKioskFilter = {
	       "criteria": '',//$scope.workspace.kioskFilterOptions[0],
	   "operator":'=',
	   "value": ""
	       };
	   };
	   
	   $scope.$watch('tempKioskFilter.value',function(){
		   	if($scope.tempKioskFilter.value == '' && $scope.workspace.kioskFilters.length == 0){
		   		$scope.workspace.showKioskButtonOption =true;
		   	}else{
		   		$scope.workspace.showKioskButtonOption =false;
		   	}
	   },true);
	   
	   $scope.$watch('workspace.kioskFilters.length',function(){
		   $scope.prepareString($scope.workspace.kioskFilters);
	   },true);
	   
	   $scope.$watch('workspace.printExportFilter.length',function(){
		   $scope.prepareString($scope.workspace.printExportFilter);
	   },true);
	   
	   $scope.prepareString = function(list){
		   $scope.workspace.kioskReadableString = "";
		   angular.forEach(list,function(obj){
			   if(obj.criteria != ''){
			   $scope.workspace.kioskReadableString += $scope.workspace.kioskmap[obj.criteria] + " "+ obj.operator + " " + obj.value +" , ";
			   }
	   		});
		   $scope.workspace.kioskReadableString = $scope.workspace.kioskReadableString.substr(0, $scope.workspace.kioskReadableString.length-2);
	   };
	   
    $scope.workspace.validatePmReport = function($event, data, filterType) {
    	$event.preventDefault();
    	$event.stopPropagation();
    	$scope.showErrorContainer = false;
    	$scope.errorMessage = "";
    	var errorMessage  = " cannot be empty. ";
    	var error = "";
		var validationErrorMessage = "";
		var inputStatus = false;
		var validationError = false;
		var delimiter = "";
    	// check if any filter is empty
    	if (angular.isObject(data)) {
    		if (angular.isUndefined(data.startDate) || data.startDate == null) {
    			error = "Start Date";
    			inputStatus = true;
    			delimiter = ", ";
    		}if (angular.isUndefined(data.endDate) || data.endDate == null) {
    			error = error + delimiter +"End Date";
    			inputStatus = true;
    			delimiter = ", ";
    		}if(filterType == "royaltyrepFilter" && angular.isUndefined(data.vendorName)){
    			error = error + delimiter +"vendor name";
    			inputStatus = true;
    			delimiter = ", ";
    		}
    		
    		if (angular.isUndefined(data.emailIds) || data.emailIds == "") {
    			error = error + delimiter +"Email ID";
    			inputStatus = true;
    			delimiter = ", ";
    		} else {
    			var arr = data.emailIds.split(',');
    			var str = [];
    			var j=0;
    			for(var i=0;i<arr.length;i++){
    				var username =arr[i].replace(/^[ ]+|[ ]+$/g,'');
    				if( !(/^[a-zA-Z0-9]+\.[a-zA-Z0-9]+$/.test(username))){
    					if(/\s/.test(username)){
    						validationErrorMessage = "One or More Email ID entered is invalid.";
    					}
    					else {
    						validationErrorMessage = "Email ID should be in proper format."
    					}
    					validationError = true;
    					delimiter = ", ";
    				}
    				if(!validationError){
    				for(var k=0 ; k<str.length;k++){
    					if(str[k].toLowerCase()==(username.toLowerCase())){
    						validationErrorMessage ="One or more Email ID is repeated." ;
    						validationError = true;
    						delimiter = ", ";
    					}
    				}
    				str[j]= arr[i];
    				j++;
    				}
    				
    			}
    		}
    		
    		if(inputStatus){
    			errorMessage = 	error +errorMessage;
    		}
    		if( !inputStatus && data.startDate > (new Date()) ){
    			validationErrorMessage = "Start date Should be today or less" + delimiter + validationErrorMessage ;
    			validationError = true;
    		}
    		 else if ( !inputStatus && data.endDate < data.startDate) {
    			 validationErrorMessage = "Start Date should be less then End Date" + delimiter + validationErrorMessage ;
    			validationError = true;
    		} else if (!inputStatus && data.endDate > (new Date())) {
    			validationErrorMessage = "End Date Should be today or less" + delimiter + validationErrorMessage ;
    			validationError = true;
    		} else if(!inputStatus){
    			var datetime1 = data.startDate.getTime();
        		var datetime2 = data.endDate.getTime();
        		var difference = datetime2-datetime1;
        		if((difference/1000) > 7948800){
        			validationErrorMessage =  "Time Period is more than 93 days" + delimiter + validationErrorMessage ;
        			validationError = true;
        		}
    		}
    	} else {
    		validationErrorMessage = "Start Date, End Date, Email ID" + errorMessage;
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
    };
    
    $scope.hideMessage = function() {
		$scope.showErrorContainer = false;
		$scope.showSucessContainer = false;
	};
	
    $scope.workspace.resetKioskCriteria = function($event) {
    	$event.preventDefault();
        $event.stopPropagation();
        $scope.workspace.kioskFilters = [];
        $scope.tempKioskFilter = {
                "criteria": '',//$scope.workspace.kioskFilterOptions[0],
                "operator":'=',
                "value": ""
       }
       //$scope.workspace.kioskFilters.push($scope.tempKioskFilter)
       /*$scope.updateData(null,'kioskFilter');*/
       $scope.workspace.showKioskButtonOption=true;
       $scope.workspace.kioskReadableString = "";
    };
    
    /*$scope.$watchCollection('startIndex', function () {
        $scope.index = $scope.startIndex;
    });*/ 
    /*filter data in Machine Kiosk ENDS*/
	$scope.changeStorePagination = function(type,index, storeNumber,operation) {
		
		if(type == 'kiosk') {
			var pagination = angular.copy($scope.workspace.kioskFiltersDefault);
			pagination.currrentPage = $scope.currentIndex;
			$scope.showLoading($scope.workspace.id);
			$http.post($scope.workspace.dataJson,{filter: pagination,currentPage : $scope.currentIndex,messageHeader:kioskMessageHeader}).then(function(response) {
				$scope.workspace.data = response.data;
				$scope.workspace.data.currrentPage = $scope.workspace.data.currentPage
				console.log($scope.workspace.data)
				$scope.hideLoading($scope.workspace.id);
			}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
		}else if(type == 'machine') {
			if($scope.$$childTail.currentIndex == '') { return; }
			$scope.showLoading($scope.workspace.id);
			var pagination = angular.copy($scope.machineFilter);
			var currrentPage = $scope.workspace.data.store[index].currrentPage;
			pagination.storeIndex = $scope.index;
			pagination.storeId = storeNumber;
			pagination.isPagination = true;
			//pagination.sortColumnName = $scope.columnName;
			if($scope.$$childHead.currentIndex) {
				pagination.currrentPage = $scope.$$childHead.currentIndex;
			} else if($scope.$$childTail.currentIndex) {
				pagination.currrentPage = $scope.$$childTail.currentIndex;
			}
			
			//($scope.orderProperty == true) ? pagination.sortOrder = 'DESC' : pagination.sortOrder = 'ASC';
			storeIndex = index;
			$http.post($scope.workspace.dataJson,{filter: pagination, messageHeader:machineMessageHeader}).then(function(response) {
				//$scope.workspace.data = response.data;
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
					$scope.workspace.data.store[k].chain = false;
					if($scope.machineFilter.locationId == '3') {
						$scope.workspace.data.store[k].regionNumber = false;
						$scope.workspace.data.store[k].districtNumber = false;
					}
					
					if($scope.machineFilter.locationId == '2') {
						$scope.workspace.data.store[k].regionNumber = false;
					}
					if($scope.machineFilter.locationId == '4') {
						$scope.workspace.data.store[k].chain = true;
					}
				}						
			
				/*** region and district show / hide ***/
				var storeData    = $scope.workspace.data.store;
				var currentStore = storeData[storeIndex];
				
				var currntRegion   = currentStore.regionNumber;
				var currntDistrict = currentStore.districtNumber;
				
				for(var indx = 0; indx < storeIndex; indx++) {
					var prevDistrict = storeData[indx].districtNumber;
					var prevRegion   = storeData[indx].regionNumber;
					
					if(parseInt(prevDistrict) == parseInt(currntDistrict)) {
						$scope.workspace.data.store[storeIndex].districtNumber = false;
					}
					if(parseInt(prevRegion) == parseInt(currntRegion)) {
						$scope.workspace.data.store[storeIndex].regionNumber = false;
					}
				}
				$scope.hideLoading($scope.workspace.id);
			}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
		}else if(type == 'orders') {
			if($scope.$$childTail.currentIndex == '') { return; }
			var pagination = angular.copy($scope.payOnFilterDefault);
			pagination.currrentPage = $scope.workspace.data.currentPage;
			var dataJson = '';
			if(pagination.filtertypePay == 'ediupc') {
				dataJson	= $scope.workspace.dataJsonEdiupc;
			}else if(pagination.filtertypePay == 'default') {
				dataJson	= $scope.workspace.dataJson;
			}
			$scope.showLoading($scope.workspace.id);
			$http.get(dataJson,{params: pagination}).then(function(response) {
				$scope.workspace.data = response.data;
				$scope.hideLoading($scope.workspace.id);
			}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
		} else if(type == 'exceptionReport') {
			var dataURL = '';
			var tempURL = '';
			var filterData = {};
			var order = storeNumber;
			var maxPageCount = $scope.totalPageCount;
			index = $scope.currentIndex;
			
			if($scope.exceptionReport.viewSection == 'Exception Report - by Envelope') {
				dataURL = $scope.workspace.dataJson;
				tempURL = $scope.workspace.storeTemplateEnvlp;
				filterData = angular.copy($scope.exceptionReport);
				filterData.currentPageNo = index;
				$scope.index = index;
			} else {
				dataURL = $scope.workspace.dataJsonByEmp;
				tempURL = $scope.workspace.storeTemplateEmpl;					
				filterData.startDate = $scope.exceptionReport.startDate;
				filterData.endDate = $scope.exceptionReport.endDate;
			}
			
			
			$http.post(dataURL,{messageHeader : exceptionMessageHeader, filter : filterData}).then(function(response) {
				$scope.workspace.data = response.data;
				$scope.workspace.urlStoreTable = tempURL;
				$scope.hideLoading($scope.workspace.id);
				var dataList = [];
				
				if($scope.exceptionReport.viewSection.match(/by Envelope/)) {
					dataList = $scope.workspace.data.repByEnvList;
				} else if($scope.exceptionReport.viewSection.match(/by Employee/)) {
					dataList = $scope.workspace.data.empResponseList;
				}				
			});
		} else if(type == 'unclaimedOrderReport') {
			if($scope.currentIndex == '') { return; }
			$scope.showLoading($scope.workspace.id);
			var pagination = {};
			pagination.currrentPage = $scope.currentIndex;;
			
			$scope.showLoading($scope.workspace.id);
			$http.get($scope.workspace.dataJson,{params: pagination}).then(function(response) {
				$scope.workspace.data = response.data;
				$scope.hideLoading($scope.workspace.id);
			}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
		} else if(type == 'pof') {
			if($scope.$$childTail.currentIndex == '') { return; }
			$scope.showLoading($scope.workspace.id);
			$scope.payOnFilter.currrentPage = $scope.$$childTail.currentIndex;
    		$http.post($scope.workspace.dataJson,{filter: $scope.payOnFilter, messageHeader:pofMessageHeader}).then(function(response) {
				$scope.workspace.data = response.data;
				//scope.workspace.urlStoreTable = $scope.workspace.storeTemplateDefault;
				$scope.showErrorMessage = false;
				$scope.showTableData = true;
				$scope.hideLoading($scope.workspace.id);
    		}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
		} else if(type == 'silverCan'){
			if($scope.$$childTail.currentIndex == '') { return; }
			$scope.showLoading($scope.workspace.id);
			var filterData = angular.copy($scope.silverCanisterReport);
			filterData.pageNo = $scope.$$childTail.currentIndex;
			if(filterData.status == "In progress") {
				filterData.startDate = '';
				filterData.endDate   = '';
			} else {
				filterData.startDate = $filter('date')($scope.silverCanisterReport.startDate, "dd-MMM-yy");
				filterData.endDate   = $filter('date')($scope.silverCanisterReport.endDate, "dd-MMM-yy");
			}
			
    		$http.post($scope.workspace.dataJson,{filter: filterData, messageHeader:silverCanMessageHeader}).then(function(response) {
    			$scope.workspace.data = response.data;
				$scope.showErrorMessage = false;
				$scope.showTableData = true;
				$scope.hideLoading($scope.workspace.id);
    		}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
		} else if(type == 'pofVendCost') {
			if($scope.$$childTail.currentIndex == '') { return; }
			$scope.showLoading($scope.workspace.id);
			$scope.payOnFilterVendCost.currrentPage = $scope.$$childTail.currentIndex;
			$http.post($scope.workspace.dataJson,{messageHeader : pofVCMessageHeader, filter : $scope.payOnFilterVendCost}).then(function(response) {
				$scope.workspace.data = response.data.data;
				var dataLen = response.data.data.length;
				if(dataLen > 0) {
					$scope.showTableData = true;
					for(var k = 0;k < dataLen;k++) {
						var indData = $scope.workspace.data[k];
						$scope.payOnFilterVendCost.checkBox[indData.vcId] = {};
						$scope.payOnFilterVendCost.checkBox[indData.vcId] = angular.copy(indData);
						$scope.payOnFilterVendCost.checkBox[indData.vcId].checkedStatus = false;
						$scope.payOnFilterVendCost.checkBox[indData.vcId].envNumber     = indData.envNumber;
						$scope.payOnFilterVendCost.checkBox[indData.vcId].approvedCost  = '';
						$scope.payOnFilterVendCost.checkBox[indData.vcId].completionStatus = true;
					}
					//console.log($scope.payOnFilterVendCost.checkBox);
				}	
				$scope.hideLoading($scope.workspace.id);
			}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
		} else if(type = "cannedFilter") {
			if($scope.$$childTail.currentIndex == '') { 
				$scope.hideLoading($scope.workspace.id);
				return; 
			}
			var pagination = $scope.$$childTail.currentIndex;
			if(pagination != "") {
				var filterData = angular.copy($scope.cannedReport);
				$scope.cannedReport.currrentPage = $scope.$$childTail.currentIndex;
				filterData.currrentPage = $scope.$$childTail.currentIndex;
				filterData.startDate = $filter('date')($scope.cannedReport.startDate, "yyyy-MM-dd");
				filterData.endDate   = $filter('date')($scope.cannedReport.endDate, "yyyy-MM-dd");
				$http.post($scope.workspace.dataJson,{messageHeader : cannedMessageHeader, filter : filterData}).then(function(response) {
					$scope.workspace.data = response.data;
					$scope.hideLoading($scope.workspace.id);
				}, function(response) {
					if(response.status == 401 || response.status == 302) {
						window.location.reload();
					}
				});
			} else {
				$scope.hideLoading($scope.workspace.id);
			}
		}
	};
	
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
    	$scope.$state.go('report.retailBlock.item', {reportId: $scope.currentReportId,simsBlockValue: simsBlockValue,retailBlockArray: retailBlockArray, prevRetailBlock: prevRetailBlock });
    };
    
    $scope.updateRetailBlock = function(e) {
    	e.preventDefault();
    	$scope.hideErrorMessage();
    	var simsBlockValue = $scope.selectedStoreNumberArray;
    	var retailBlockArray = $scope.workspace.simBlockList;
    	var prevRetailBlock = $scope.simsBlockFilter.retailBlock;
    	$scope.$state.go('report.retailBlock.item', {reportId: $scope.currentReportId,simsBlockValue: simsBlockValue,retailBlockArray: retailBlockArray, prevRetailBlock: prevRetailBlock });
    }
    
    // for SIMS Retail Block
    $scope.updateSimsData = function() {
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
        		var requiredFieldArray = ["retailBlock", "number"];
        	}
    	}
		
  		var errorCat = $scope.validateRequiredField(requiredFieldArray,$scope.simsBlockFilter);
		
  		if(errorCat != '') {
			$scope.errorMessageText.push(errorCat);
		}
		
		if($scope.errorMessageText.length == 0) {
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
	
	/*--------------- Date format and min / max date --------------*/
	$scope.open = function($event, ele) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.hideErrorMessage();
        $scope.workspace.openedEnd = false;
        $scope.workspace.openedStart = false;
        /*$scope.showErrorMessage = false;*/
        
        if (ele == 'start') {
        	$scope.workspace.openedStart = true;
        	//angular.element( document.querySelectorAll('input[type="text"].form-control.start + ul.dropdown-menu table thead tr:nth-child(1) th:nth-child(2)') ).attr('colspan',5);
        }
        if (ele == 'end') {
        	$scope.workspace.openedEnd = true;
	      //  angular.element( document.querySelectorAll('input[type="text"].form-control.end + ul.dropdown-menu table thead tr:nth-child(1) th:nth-child(2)') ).attr('colspan',5);
        }
    };
    
    $scope.format = 'MM/dd/yyyy';
   
    $scope.endMaxDate = $scope.endMaxDate ? null : new Date();
    
	$scope.toggleMin = function() {
		$scope.minDate = $scope.minDate ? null : new Date();
	};
	$scope.showWeeks = "false";
	
	$scope.setStartMinDate = function() {
		var defaultMinDate = new Date();
		defaultMinDate.setDate(defaultMinDate.getDate() - 390);
		return defaultMinDate;
	}
	
	$scope.startMinDate = $scope.setStartMinDate();
	
	$scope.toggleMin();

	/*--------------- Date format and min / max date --------------*/
	
	$scope.hideErrorMessage = function() {
		$scope.showErrorMessage   = false;
		$scope.showErrorContainer = false;
		$scope.showNoDataFound    = false;
		$scope.showSuccessMessage = false;
        $scope.showSucessContainer = false;
        $scope.showErrorMsgForApprovedCost = false;
	}
	
	$scope.toggled = function(status) {
		if(status == true) {
			angular.element( document.querySelector('.exception-report-section') ).css('z-index', '10');
			angular.element( document.querySelector('.exceptionReport .row:nth-child(1) .col-md-3:nth-child(1) .form-control') ).css('z-index','0');
		} else {
			//angular.element( document.querySelector('.exception-report-section') ).css('z-index', '0');
			angular.element( document.querySelector('.exceptionReport .row:nth-child(1) .col-md-3:nth-child(1) .form-control') ).css('z-index','');
		}
	};
	
	/*print kiosh report without using fragment */
	 $scope.printKioskRpt = function() {
		 
		 var obj = {
				 currentPage : $scope.workspace.data.currentPage,
				 filter : $scope.workspace.printExportFilter
		 }
		 sessionStorage.setItem('kioskPrint', JSON.stringify(obj));
		 window.open('/PhotoOmniWeb/res/template/report/kiosk/kiosk-report-print.html'); 
	 }
	
	 
	
	$scope.exportKioskRptToExcel = function(){
		var objParam = {
				currentPage : 1,
				filter : $scope.workspace.printExportFilter,
				messageHeader : kioskMessageHeader
		};
		 var url = "/PhotoOmniAdminWeb/report/kiosk-export?param="+JSON.stringify(objParam);
		 var wind = window.open(url);
		 wind.focus();
	}
	
	var reportMachineObjParam = {
			filter : $scope.machineFilter
	}; 
	
	$scope.exportMachineRptToExcel = function(a){
		reportMachineObjParam.filter.storeNumber=a;
		var url = "/PhotoOmniAdminWeb/machines/machine-export?machineFilter="+JSON.stringify(reportMachineObjParam);
		 console.log(url)
		 var wind = window.open(url);
		 wind.focus();
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
	
	$scope.printDiv = function (divName) {
	    var indicatorArr = document.getElementsByClassName('indicator');
	    for(var eachInd in indicatorArr) {
	    	if(indicatorArr[eachInd].style) {
	    		indicatorArr[eachInd].style.display="none";
	    	}
	    }
	    
	    var profitColumn = angular.element( document.querySelectorAll('.profit-col') );
	    var totalRevColumn = angular.element( document.querySelectorAll('.total-rev') );
	    
	    if($scope.cannedReport.showProfitCol === false) {
	    	profitColumn.css('display','none');
	    } else {
	    	profitColumn.css('display','');
	    	totalRevColumn.css('display','none');
	    }
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
	    for(var eachInd in indicatorArr) {
	    	if(indicatorArr[eachInd].style) {
	    		indicatorArr[eachInd].style.display="block";
	    	}
	    }
	   /* if($scope.cannedReport.showProfitCol === false) {
	    	profitColumn.css('display','');
	    } else {
	    	profitColumn.css('display','');
	    	totalRevColumn.css('display','');
	    }*/
	    return true;
	}
	
	//Export to CSV for Silver canister report
	
	$scope.exportSilverCanRptToExcel = function(type){
		var type = type;
		var filterData = {};
		if(type != 'changedReport'){
			filterData.filter = angular.copy($scope.silverCanisterReport);
			filterData.filter.startDate = $filter('date')($scope.silverCanisterReport.startDate, "dd-MMM-yy");
			filterData.filter.endDate   = $filter('date')($scope.silverCanisterReport.endDate, "dd-MMM-yy");
			var url = $scope.workspace.exportToCSVJson + "?silverCanisterFilter="+JSON.stringify(filterData);
			console.log(url)
			var wind = window.open(url);
			wind.focus();
	    }
		if(type == 'changedReport') {
			filterData = $scope.silverCanChngdReport.dataList.filter;
			$http.post($scope.workspace.exportToCSVJson,{filter: filterData, messageHeader:silverCanMessageHeader}).then(function(response) {
				var csvFile = response.data;
                
                var blob = new Blob([csvFile], { type: 'text/csv;charset=utf-8;' });
                if (navigator.msSaveBlob) { // IE 10+
                    navigator.msSaveBlob(blob, "silver_canister_change_report.csv");
                } else {
                    var link = document.createElement("a");
                    if (link.download !== undefined) { // feature detection
                        // Browsers that support HTML5 download attribute
                        var url = URL.createObjectURL(blob);
                        link.setAttribute("href", url);
                        link.setAttribute("download", "silver_canister_change_report.csv");
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
	}
	
	//Export to CSV for pay on fulfilment report
	
    $scope.setLocation = function(data, id, e) {
        e.preventDefault();

        //$scope.workspace.data.location = data;
		$scope.machineFilter.location = data;
		//alert($scope.machineFilter.startDate);
		//alert("1");
		$scope.machineFilter.locationId = id;
		if(data=='Chain') {
			$scope.showLocation = false;
		} else {
			//$scope.machineFilter.storeId = id;
			$scope.showLocation = true;
		}
    };
    
    $scope.setMachine = function(data, id, e) {
        e.preventDefault();

        //$scope.workspace.data.machine = data;
		$scope.machineFilter.machine = data;
		$scope.machineFilter.machineId = id;
	    //alert($scope.temp);
    };
    
    //for exception report
    $scope.setSection = function(data, id, e) {
    	e.preventDefault();
    	$scope.showErrorMessage = false;
    	$scope.exceptionReportByEnvelope = false;
    	$scope.exceptionReportByEmployee = false;
    	if(id == 1) {
			$scope.exceptionReportByEnvelope = true;
		} else if(id == 2){
			$scope.exceptionReportByEmployee = true;
		}
    	$scope.exceptionReport.viewSection = data;
    	$scope.showTableData = false;
    	//console.log($scope.exceptionReport.viewSection);
    }
    //for exception report
    $scope.setException = function(data, e) {
    	e.preventDefault();
    	$scope.exceptionReport.exceptionStatus = data;
    }
    //for exception report
    $scope.setReason = function(data, e) {
    	e.preventDefault();
    	$scope.exceptionReport.reasonType = data;
    }
    
    $scope.setVendor = function(data, e, filterType) {
    	 e.preventDefault();
    	 $scope.hideErrorMessage();
    	 if(filterType == 'payOnFilterVendCost') {
    		 $scope.payOnFilterVendCost.vendorName = data.sysVendorId;
             $scope.payOnFilterVendCost.vendorDesc = data.description;
    	 } else {
    		 $scope.payOnFilter.vendor = data.sysVendorId;
             $scope.payOnFilter.vendorDesc = data.description;
    	 }         
    };
    
  //for print sign report
    $scope.lookup = function(e) {
    	e.preventDefault();
    	$scope.hideErrorMessage();
    	var lookupData = $scope.printSignFilter.lookupData;
    	/*if(lookupData == '') {
    		$scope.showErrorMessage = true;
    		$scope.errorMessageText = 'Please provide value';
    	} else {*/
    		$scope.$state.go('report.lookup.data', {reportId: $scope.currentReportId,lookupData: lookupData });
    	/*}*/
    }
    
    //sort facility
    $scope.sort = function(filterType, columnName, storeNumber, index) {
    	var filterData = '';
    	filterData = prepareSortData(filterType, columnName, storeNumber, index);
    	$scope.showLoading($scope.workspace.id);
    	var storeIndex = index;
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
					$scope.workspace.data.store[k].chain = false;
					if($scope.machineFilter.locationId == '3') {
						$scope.workspace.data.store[k].regionNumber = false;
						$scope.workspace.data.store[k].districtNumber = false;
					}
					
					if($scope.machineFilter.locationId == '2') {
						$scope.workspace.data.store[k].regionNumber = false;
					}
					if($scope.machineFilter.locationId == '4') {
						$scope.workspace.data.store[k].chain = true;
					}
				}	
				$scope.resetSortIcon(columnName,$scope.colSortOrder[index]);
    		} else {
    			$scope.workspace.data = response.data;
    			$scope.resetSortIcon(columnName,$scope.colSortOrder);
    		} 					
			
			$scope.workspace.urlStoreTable = $scope.workspace.storeTemplateDefault;
			$scope.hideLoading($scope.workspace.id);
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
    	} else if(filterType == 'exceptionReport') {
    		filterData = angular.copy($scope.exceptionReport);
    		filterData.currentPageNo = $scope.currentIndex;
    	}
    	 else if(filterType == 'unclaimedOrderReport') {
 			filterData.currrentPage = $scope.currentIndex;
 		} else if(filterType == 'silverCan') {
 			filterData = angular.copy($scope.silverCanisterReport);
 			filterData.startDate = $filter('date')($scope.silverCanisterReport.startDate, "dd-MMM-yy");
			filterData.endDate   = $filter('date')($scope.silverCanisterReport.endDate, "dd-MMM-yy");
			filterData.pageNo = $scope.workspace.data.currentPage;
 		} else if(filterType == 'cannedFilter') {
 			filterData = angular.copy($scope.cannedReport);
 			filterData.startDate = $filter('date')($scope.cannedReport.startDate, "yyyy-MM-dd");
			filterData.endDate   = $filter('date')($scope.cannedReport.endDate, "yyyy-MM-dd");
 		}
    	
    	if($scope.columnName == columnName) {
    		$scope.orderProperty = !$scope.orderProperty;
    	} else {
    		$scope.columnName    = columnName
    		$scope.orderProperty = false;
    	}
    	($scope.orderProperty == true) ? filterData.sortOrder = 'DESC' : filterData.sortOrder = 'ASC';
    	filterData.sortColumnName = columnName;
    	
    	if(filterType == 'machineFilter'){
    		$scope.colSortOrder[index][columnName].order = $scope.orderProperty;
        	$scope.colSortOrder[index][columnName].firstLoad = false;
        	$scope.machineFilter.sortOrder = filterData.sortOrder;
        	$scope.machineFilter.sortColumnName = filterData.sortColumnName;
    	} else if(filterType == 'exceptionReport') {
    		$scope.exceptionReport.sortOrder = filterData.sortOrder;
        	$scope.exceptionReport.sortColumnName = filterData.sortColumnName;
    	}
    	 else if(filterType == 'unclaimedOrderReport') {
 			$scope.unclaimedOrderReport.sortOrder = filterData.sortOrder;
        	$scope.unclaimedOrderReport.sortColumnName = filterData.sortColumnName;
 		} else if(filterType == 'silverCan') {
 			$scope.silverCanisterReport.sortOrder = filterData.sortOrder;
        	$scope.silverCanisterReport.sortColumnName = filterData.sortColumnName;
 		} else if(filterType == 'cannedFilter') {
         	$scope.cannedReport.sortOrder = filterData.sortOrder;
 			$scope.cannedReport.sortColumnName = filterData.sortColumnName;
  		}
    	
    	if(filterType !== 'machineFilter') {
    		$scope.colSortOrder[columnName].order = $scope.orderProperty;
        	$scope.colSortOrder[columnName].firstLoad = false;
    	}
    	return filterData;
    };
    
    $scope.$on('resetKiosk',function(){
		 $scope.updateData(null,'kioskFilter');
	})

	 $scope.setRoyaltyVendorList = function(data, id, e) {
        e.preventDefault();
        if($scope.workspace.royaltyrepFilter == undefined){
        	$scope.workspace.royaltyrepFilter = {};
        }
        $scope.workspace.royaltyrepFilter.vendorName = data;
		$scope.royaltyrepFilter.vendor = data;
		$scope.royaltyrepFilter.sysVendorId = id;
    };
    
    $scope.$watch('payOnFilter.filtertypePay', function() {
    	$scope.showTableData = false;
    	$scope.payOnFilter.ediupcValue = '';
    	$scope.payOnFilter.vendorDesc = 'Select';
    	$scope.payOnFilter.startDate = '';
    	$scope.payOnFilter.endDate = '';
    	$scope.payOnFilter.storeNumber = '';
    	$scope.workspace.vendorObjPof = null;
    	
    	if($scope.payOnFilter.filtertypePay != 'default') {
    		$scope.ediUPCStatus = true;
    	} else {
    		$scope.ediUPCStatus = false;
    	}
    	$scope.showTableData = false;
		$scope.showTable     = false;
    });
    
    
    //vendor cost calculation
    $scope.selectedRowToApprove = function(index) {
    	var selectionStatus = $scope.payOnFilterVendCost.checkBox[index].checkedStatus;
    	var approvalList = $scope.payOnFilterVendCost.selectedRowArray;
    	
    	var isPresent = utils.checkForValue(approvalList, index);
    	if(selectionStatus) {
    		$scope.payOnFilterVendCost.checkBox[index].completionStatus = false;
    		if(isPresent == null) {
    			approvalList.push(index);
    		}
    	} else {
    		$scope.payOnFilterVendCost.checkBox[index].completionStatus = true;
    		$scope.payOnFilterVendCost.checkBox[index].approvedCost = '';
    		if(isPresent != null) {
    			approvalList.splice(isPresent,1);
    		}
    	}
    	//console.log($scope.payOnFilterVendCost.selectedRowArray);
    };
    
    $scope.getPendingApprovalList = function() {
    	var selectedRowArray = $scope.payOnFilterVendCost.selectedRowArray;
    	var arrayLength = selectedRowArray.length;
    	for(var i = 0; i < arrayLength; i++) {
    		var index = selectedRowArray[i];
    		var filterData = angular.copy($scope.payOnFilterVendCost.checkBox[index]);
        	filterData.approvedCost = $scope.payOnFilterVendCost.checkBox[index].approvedCost;
        	filterData.vcId = index;
        	filterData.envNumber = $scope.payOnFilterVendCost.checkBox[index].envNumber;
    		$scope.payOnFilterVendCost.pendingApprovalList[index] = {};
    		$scope.payOnFilterVendCost.pendingApprovalList[index] = filterData;
    	}
    };
    
    //vendor cost calculation
    $scope.saveDataForPOFVC = function() {
    	$scope.getPendingApprovalList();
    	var approveList = $scope.payOnFilterVendCost.pendingApprovalList;
    	var data = [];
    	var errorCat = '';
    	for(var appObj in approveList) {
    		var approvedCost = approveList[appObj].approvedCost;
    		var pattern = /^[0-9\.]+$/;
    		var patternForDecimal = /^[0-9]+(\.[0-9]{1,2})?$/;
    		var zeroCount = (approvedCost.match(new RegExp("0", "g")) || []).length;
			var charCount = approvedCost.length;
			approvedCost = approvedCost.trim();
    		
    		if (!pattern.test(approvedCost)) { //checks for non-numeric characters
    			errorCat = "Approved cost should only contain numeric character"; 
    			if(approvedCost < 0 || approvedCost > 999999.99)
        		{
        			errorCat = "Approved cost range should be between 0 to 999999.99";
        			$scope.errorMessageText = errorCat;
        		}
    			$scope.errorMessageText = errorCat;
    		}
    		else if(!patternForDecimal.test(approvedCost)){
    			errorCat = "Approved cost should only contain maximum two decimal number";
    			$scope.errorMessageText = errorCat;
    		}
    		else if(approvedCost < 0 || approvedCost > 999999.99) {
    			errorCat = "Approved cost range should be between 0 to 999999.99";
    			$scope.errorMessageText = errorCat;
    		} else if(charCount > 1 && zeroCount == charCount) {
    			errorCat = "Approved cost should be a valid input";
    			$scope.errorMessageText = errorCat;
    		} else {
    			approvedCost = approvedCost.trim();
    			approvedCost = Number(approvedCost).toString();
        		$scope.payOnFilterVendCost.checkBox[appObj].approvedCost = approvedCost;
        		//console.info(approvedCost);
    			delete approveList[appObj].checkedStatus;
        		delete approveList[appObj].totalRecord;
        		delete approveList[appObj].completionStatus;
        		data.push(approveList[appObj]);
    		}
    	}
    	
    	if(errorCat == "") {
    		$scope.showLoading($scope.workspace.id);
    		$http.post($scope.workspace.pofVndrCalcSuccessJson,{currentPage: '1', data: data, messageHeader:pofMessageHeader}).then(function(response) {
				var response = response.data.vendorCostReportMsg;
	    		$scope.showSuccessMessage = response.status;
				$scope.showErrorMessage = !$scope.showSuccessMessage;
				$scope.successMessage = response.message;
				$scope.hideLoading($scope.workspace.id);
    		}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
    	} else {
    		$scope.showSuccessMessage = false;
			$scope.showErrorMsgForApprovedCost = true;
    	}
    	
    };
    
    $scope.exportCannedRptToExcel=function() {
    	var filterData = {};
		filterData.filter = angular.copy($scope.cannedReport);
		
		filterData.filter.startDate = $filter('date')($scope.cannedReport.startDate, "yyyy-MM-dd");
		filterData.filter.endDate   = $filter('date')($scope.cannedReport.endDate, "yyyy-MM-dd");
		
		var url = $scope.workspace.exportToCSVJson + "?cannedFilter="+JSON.stringify(filterData);
		console.log(url)
		var wind = window.open(url);
		wind.focus();
    }
    
    $scope.updateData = function(e,filterType) {
    	/*alert(filterType);*/
        //if($scope.workspace.filtertypePay){
    	$scope.errorMessageText = [];
    	var endMaxDateValue = $filter('date')($scope.endMaxDate, "MM-dd-yyyy");
    	var errorMessageArray = [];
		if(filterType == 'payOnFilter'){
		  	$scope.payOnFilterDefault =  angular.copy($scope.payOnFilter);
			//modified to show notifications on error
		  	if($scope.payOnFilter.filtertypePay == 'ediupc'){
	      		var requiredDataPresent = $scope.payOnFilter.ediupcValue;
	      		if(!$scope.payOnFilter.ediupcValue.match(/^[0-9]+$/)) {
	      			requiredDataPresent = false;
	      		}
	      	} else {
	      		var requiredFieldArray = ['startDate', 'endDate'];
	      		var errorCat = $scope.validateRequiredField(requiredFieldArray,$scope.payOnFilter);
	      		var requiredDataPresent = $scope.payOnFilter.startDate && $scope.payOnFilter.endDate;
	      		
	      		var fieldStDate  = angular.element( document.querySelectorAll('.payon input[type="text"].form-control.start') ).val();
	      		var fieldEndDate = angular.element( document.querySelectorAll('.payon input[type="text"].form-control.end') ).val();
	      		
	      		var validationStDate1  = $filter('date')($scope.payOnFilter.startDate, "MM/dd/yyyy"); 
      			var validationEndDate1 = $filter('date')($scope.payOnFilter.endDate, "MM/dd/yyyy"); 
      			
	      		if(errorCat == '') {
	      			if((validationStDate1 && validationEndDate1) && (fieldStDate !== validationStDate1 || fieldEndDate !== validationEndDate1)) {
	      				requiredDataPresent = false;
	      			}
	      			var dateDifferenc = $scope.dateDifference($scope.payOnFilter.startDate, $scope.payOnFilter.endDate);
					if(dateDifferenc < 0) {
						requiredDataPresent = false;
					}
					var dateDifference1 = $scope.dateDifference($scope.payOnFilter.startDate,$scope.endMaxDate);
					var dateDifference2 = $scope.dateDifference($scope.payOnFilter.endDate,$scope.endMaxDate);
					if((dateDifference1 < 0) || (dateDifference2 < 0)) {
		      			requiredDataPresent = false;
		      		}
					
					var dateDifference = $scope.dateDifference($scope.payOnFilter.startDate, $scope.payOnFilter.endDate);
					if(dateDifference > 270) {
						requiredDataPresent = false;
					}
				} else {
					requiredDataPresent = false;
				}
	      	}
          	if(requiredDataPresent) { //true if start date, end date and store number are not blank
          		$scope.showLoading($scope.workspace.id);
          		$scope.showErrorMessage = false;
          		$scope.payOnFilter.currrentPage = '1';
          		$scope.showTableData = false;
          		if($scope.payOnFilter.vendorDesc.toLowerCase() == 'select') {
					$scope.payOnFilter.vendor = '';
				}
				if(!$scope.payOnFilter.storeNumber) {
					$scope.payOnFilter.storeNumber = '';
					$scope.showStoreHeader = false;
				} else {
					$scope.showStoreHeader = true;
				}
				var filterData = angular.copy($scope.payOnFilter);
          		filterData.startDate = $filter('date')($scope.payOnFilter.startDate, "yyyy-MM-dd");
          		filterData.endDate = $filter('date')($scope.payOnFilter.endDate, "yyyy-MM-dd");
          		$http.post($scope.workspace.dataJson,{filter: filterData, messageHeader:pofMessageHeader}).then(function(response) {
  					$scope.workspace.data = response.data;
  					
  					if($scope.payOnFilter.filtertypePay == 'ediupc'){
  						$scope.workspace.urlStoreTable = $scope.workspace.storeTemplateEdiupc;
  						var dataList = response.data.productList;
  					} else {
  						$scope.workspace.urlStoreTable = $scope.workspace.storeTemplateDefault;
  						var dataList = response.data.repByPayOnFulfillmentList;
  					}
  					$scope.startIndex = '1';
  					
  					if(!dataList || dataList.length == 0) {
  						$scope.showNoDataFound = true;
  					} else {
  						$scope.showNoDataFound = false;
  						$scope.showTableData = true;
  	  					$scope.showTable     = true;
  	  					$scope.totalPageCount = Math.ceil(response.data.totalRecord/5);
  					}
  					$timeout(function(){
  						$scope.hideLoading($scope.workspace.id);
  					},50)
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
          			errorMessage = $scope.validateDate($scope.payOnFilter, endMaxDateValue, '270 days');
          		} 
          		$scope.errorMessageText.push(errorMessage);
          	}
         	
		}else if(filterType == 'machineFilter'){
			//alert('hello');
			var requiredFieldArray = ["startDate", "endDate", "storeId"];
			if($scope.machineFilter["location"] == 'Chain') {
				requiredFieldArray = ["startDate", "endDate"];
			}
      		var errorCat = $scope.validateRequiredField(requiredFieldArray,$scope.machineFilter);
			
      		if(errorCat != '') {
					$scope.errorMessageText.push(errorCat);
  			}
			
			$scope.showNoDataKiosk = false;
			$scope.validateDate($scope.machineFilter, endMaxDateValue, '390 days');
			
			
			if($scope.errorMessageText.length == 0) {
				$scope.showLoading($scope.workspace.id);
				$scope.showErrorContainer = false;
				$scope.machineFilterDefault =  angular.copy($scope.machineFilter);
		        $scope.machineFilter.currrentPage=1;
		        $scope.machineFilter.isPagination = false;
		        $scope.machineFilter.sortColumnName = 'ID';
		        $scope.machineFilter.sortOrder = 'DESC';
		       
		        if(!$scope.machineFilter.storeId) {
		        	$scope.machineFilter.storeId = "";
		        }
				$http.post($scope.workspace.dataJson,{filter: $scope.machineFilter, messageHeader:machineMessageHeader}).then(function(response) {
					$scope.workspace.data = response.data;
					var store = $scope.workspace.data.store;
					
					$scope.showNoDataFound = false;
					$scope.showLoading($scope.workspace.id);
					var storeLength = store.length;
					$scope.hideLoading($scope.workspace.id);
					if(storeLength > 0) {
						$scope.workspace.urlStoreTable = $scope.workspace.storeTemplateDefault;
						$scope.hideLoading($scope.workspace.id);
						$scope.showTableData = true;//for pagination
						for(var k = 0; k < storeLength; k++) {
							$scope.colSortOrder[k] = {};
							$scope.workspace.data.store[k].chain = false;
							var totalPageCount = Math.ceil(store[k].totalRecord/5);
							$scope.workspace.data.store[k].totalPageCount = totalPageCount;
							if($scope.machineFilter.locationId == '3') {
								$scope.workspace.data.store[k].regionNumber = false;
								$scope.workspace.data.store[k].districtNumber = false;
							}
							
							if($scope.machineFilter.locationId == '2') {
								$scope.workspace.data.store[k].regionNumber = false;
							}
							if($scope.machineFilter.locationId == '4') {
								$scope.workspace.data.store[k].chain = true;
							}
							
							//For sorting
							var rowData = store[k].data[0];
							for(var eachCol in rowData) {
								$scope.colSortOrder[k][eachCol] = {};
								$scope.colSortOrder[k][eachCol].order = false;
								$scope.colSortOrder[k][eachCol].firstLoad = true;
							}
						}						
					} else {
						$scope.showTableData   = false;
						$scope.showNoDataFound = true;
					}
					$timeout(function(){
						$scope.hideLoading($scope.workspace.id);
  					},50)
				}, function(response) {
  					if(response.status == 401 || response.status == 302) {
  						window.location.reload();
  					}
  				});
			} else {
				$scope.showErrorContainer = true;
				$scope.showTableData = false;
				$scope.showNoDataFound = false;
			}
		}else if(filterType == 'kioskFilter'){
			$scope.showLoading($scope.workspace.id);
			if(($scope.tempKioskFilter.length != 0) && ($scope.tempKioskFilter.value != "")) {
				$scope.workspace.kioskFilters.push($scope.tempKioskFilter);
			}
			$scope.workspace.kioskFiltersDefault = angular.copy($scope.workspace.kioskFilters);
			delete $scope.workspace.kioskFilters[0].$$hashKey;
			$scope.showTableData = false; //for pagination
			$scope.workspace.printExportFilter = [];
			$http.post($scope.workspace.dataJson,{filter: $scope.workspace.kioskFilters,currentPage : 1,messageHeader:kioskMessageHeader}).then(function(response) {
				if(response.status == 200) {
					$scope.workspace.data = response.data;
					$scope.workspace.urlStoreTable = $scope.workspace.storeTemplateDefault;
					$scope.hideLoading($scope.workspace.id);
					$scope.showTableData = true;//for pagination
					$scope.workspace.showKioskButtonOption = true;
				}
			}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
			$timeout(function(){
				$scope.workspace.printExportFilter = angular.copy($scope.workspace.kioskFilters);
				$scope.workspace.kioskFilters = [];
			},250)
		}else if(filterType == 'payOnFulfillmentFilter'){
			$scope.showLoading($scope.workspace.id);
			$scope.workspace.kioskFiltersDefault = angular.copy($scope.workspace.kioskFilters);
			$http.get($scope.workspace.dataJson,{params: $scope.workspace.kioskFilters}).then(function(response) {
				$scope.workspace.data = response.data;
				$scope.workspace.urlStoreTable = $scope.workspace.storeTemplateDefault;
				//$scope.hideLoading($scope.workspace.id);
			}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
		}else if (filterType == 'pmWICFilter') {
			$scope.showSucessContainer = false;
			$scope.workspace.data = {};
			if($scope.workspace.validatePmReport(e, $scope.workspace.pmWICFilter, filterType)){ 
				$scope.showLoading($scope.workspace.id);
				$scope.workspace.pmWICFilter.startDate = dateConverter($scope.workspace.pmWICFilter.startDate);
				$scope.workspace.pmWICFilter.endDate = dateConverter($scope.workspace.pmWICFilter.endDate);
				$http
						.post(
								$scope.workspace.dataJson,
								{
									messageHeader: pmBYWICMessageHeader,
									filter : $scope.workspace.pmWICFilter
									
								})
						.then(
								function(response) {
									$scope.workspace.data = response.data;
									$scope.showSucessContainer = true;
									$scope.workspace.pmWICFilter = {};
									$scope.workspace.pmWICFilter.startDate = new Date();
									$scope.workspace.pmWICFilter.endDate = new Date();
									$timeout(function(){
										$scope.hideLoading($scope.workspace.id);
				  					},10)
								}, function(response) {
				  					if(response.status == 401 || response.status == 302) {
				  						window.location.reload();
				  					}
				  				});
			}
		}else if (filterType == 'royaltyrepFilter') {
			$scope.showSucessContainer = false;
			$scope.workspace.data = {};
			if($scope.workspace.validatePmReport(e, $scope.workspace.royaltyrepFilter, filterType)){
				$scope.showLoading($scope.workspace.id);
				$scope.workspace.royaltyrepFilter.startDate = dateConverter($scope.workspace.royaltyrepFilter.startDate);
				$scope.workspace.royaltyrepFilter.endDate = dateConverter($scope.workspace.royaltyrepFilter.endDate);
				$http.post($scope.workspace.dataJson,{messageHeader : royaltyReportMessageHeader, filter : $scope.workspace.royaltyrepFilter}).then(function(response) {
					$scope.workspace.data = response.data;
					$scope.showSucessContainer = true;
					$scope.workspace.royaltyrepFilter = {};
					$scope.workspace.royaltyrepFilter.startDate = new Date();
					$scope.workspace.royaltyrepFilter.endDate = new Date();
					$scope.royaltyrepFilter = angular.copy($scope.royaltyrepFilterDefault);
					$scope.hideLoading($scope.workspace.id);
				}, function(response) {
  					if(response.status == 401 || response.status == 302) {
  						window.location.reload();
  					}
  				});
			} 
		}
		
		else if (filterType == 'plu') {
			$scope.showSucessContainer = false;
			if($scope.workspace.validatePmReport(e, $scope.workspace.plu, filterType)){
				$scope.showLoading($scope.workspace.id);
				var str = "";
				$scope.workspace.plu.startDate = dateConverter($scope.workspace.plu.startDate);
				$scope.workspace.plu.endDate = dateConverter($scope.workspace.plu.endDate);
					if($scope.workspace.data.rowid.length > 0){
					var strplunumber = "";
						 for (var i = 0; i < $scope.workspace.data.rowid.length; i++) {
					            var entity = $scope.workspace.data.rowid[i];
					            strplunumber += entity.val +", ";
					        }
						 $scope.workspace.plu.pluNumber = strplunumber.replace(/,\s*$/, "");
					}else{
						$scope.workspace.plu.pluNumber = "all";
					}
				
				var obj = {
				  messageHeader : dailyPLUMessageHeader,
				  pluJsonFilters : $scope.workspace.plu
				};
				$http.post("/PhotoOmniOMSWeb/orders/submit/plu_rpt_req",obj).then(function(response) {
					$scope.workspace.data = response.data;
					$scope.showSucessContainer = true;
					$scope.hideLoading($scope.workspace.id);
				}, function(response) {
  					if(response.status == 401 || response.status == 302) {
  						window.location.reload();
  					}
  				});
				$scope.workspace.plu = {};
				$scope.workspace.plu.startDate = new Date();
				$scope.workspace.plu.endDate = new Date();
				$scope.workspace.plulist = [];
			} 
			} else if(filterType == 'exceptionReport') {
				if($scope.exceptionReport.viewSection !== 'Select') {
					$scope.showErrorMessage = false;
					$scope.showTableData = false;
					$scope.showLoading($scope.workspace.id);
					$scope.exceptionReportDefault =  angular.copy($scope.exceptionReport);
					var dataURL = '';
					var tempURL = '';
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
					}
					filterData.currentPageNo = '1';
					filterData.storeNumber = '50001';
					var errorTextMap = {
							'startDate': 'Start date in `MM-DD-YYYY` format',
							'endDate'  : 'End date in `MM-DD-YYYY` format'
					}
					var errorMessage  = " Please provide values for ";
					var errorCategory = "";
					
					
					angular.forEach(["startDate", "endDate"], function (input) {
		                if (!$scope.exceptionReport[input] || $scope.exceptionReport[input] == 'Select') {  
		                	var showError = true;
		                	if(errorCategory !== "" && showError === true) {
		                		errorCategory = errorCategory + ", ";
		                	}
		            		if(showError === true) {
		            			errorCategory = errorCategory + errorTextMap[input] + ' ';
		                    	$scope.showErrorContainer = true;
		            		}	            	
		                }
		            });
					
					$scope.errorMessageText = errorMessage + errorCategory;
					
					if(errorCategory == "") {
						$scope.exceptionReport.storeNumber = '50001'; //store number hardcoded
						$http.post(dataURL,{messageHeader : exceptionMessageHeader, filter : filterData}).then(function(response) {
							$scope.workspace.data = response.data;
							$scope.workspace.urlStoreTable = tempURL;
							$scope.hideLoading($scope.workspace.id);
							var dataList = [];
							
							if($scope.exceptionReport.viewSection.match(/by Envelope/)) {
								dataList = $scope.workspace.data.repByEnvList;
							} else if($scope.exceptionReport.viewSection.match(/by Employee/)) {
								dataList = $scope.workspace.data.empResponseList;
							}
							if(dataList == null) {
								$scope.showTableData = false;
								$scope.showNoDataFound = true;
								$scope.hideLoading($scope.workspace.id);
							} else {
								$scope.showTableData = true;
								var totalRow = dataList[0].totalRows;
								$scope.totalPageCount = Math.ceil(totalRow / 5);
								$scope.workspace.urlStoreTable = tempURL;
								$scope.showNoDataFound = false;
							}
							
						});
					} else {
						$scope.showErrorMessage = true;
						$scope.hideLoading($scope.workspace.id);
						$scope.showTableData = false;
					}
					
				} else {
					$scope.showErrorMessage = true;
					$scope.errorMessageText = "Please select any exception report type";
					$scope.showTableData = false;
				}
			} else if (filterType == 'licenseContentReport') {
				var requiredFieldArray = ["startDate", "endDate", "emailIds"];
	      		var errorCat = $scope.validateRequiredField(requiredFieldArray,$scope.licenseContentData);
				
	      		if(errorCat != '') { $scope.errorMessageText.push(errorCat); }
			
				if($scope.licenseContentData.emailIds) {
					var emailId = $scope.licenseContentData.emailIds;
					var pattern = /^[\w\.\,]+$/;
					var emailError = false;
					if (emailId == '' || !pattern.test(emailId)) {
						errorCategory = 'Email id should be in proper format';
						$scope.errorMessageText.push(errorCategory);
						emailError = true;
					}
					var emailLength = emailId.split('').length;
					var indexOf = emailId.indexOf('.');
					if(!emailError && (indexOf == 0 || indexOf == (emailLength-1))) {
						errorCategory = 'One or more email id entered is invalid';
						$scope.errorMessageText.push(errorCategory);
						emailError = true;
					}
					if(!emailError && (emailId.match(/,,/g) || emailId.match(/\.\./g))) {
						errorCategory = 'One or more email id entered is invalid';
						$scope.errorMessageText.push(errorCategory);
					}
				}
				$scope.validateDate($scope.licenseContentData, endMaxDateValue);
			
			if($scope.errorMessageText.length == 0) {
				$scope.showLoading($scope.workspace.id);
				$scope.licenseContentData.startDate = $filter('date')($scope.licenseContentData.startDate, "yyyy-MM-dd");
				$scope.licenseContentData.endDate   = $filter('date')($scope.licenseContentData.endDate, "yyyy-MM-dd");
				$http.post($scope.workspace.dataJson,{filter: $scope.licenseContentData, messageHeader:licenseContentMessageHeader}).success(function(response) {
					var responseStatus = response.result;
					//$scope.message     = response.message;
					if(responseStatus) { 
						$scope.showSuccessMessage = true;
						$scope.successMessage     = response.message;
						$scope.licenseContentData.startDate = $scope.licenseContentDataDefault.startDate;
						$scope.licenseContentData.endDate   = $scope.licenseContentDataDefault.endDate;
						$scope.licenseContentData.emailIds  = '';				
					} else {
						$scope.errorMessageText = response.message;
						$scope.showErrorMessage   = true;
						$scope.hideLoading($scope.workspace.id);
					}	
					$timeout(function(){
						$scope.hideLoading($scope.workspace.id);
  					},10)
				}, function(response) {
  					if(response.status == 401 || response.status == 302) {
  						window.location.reload();
  					}
  				}).error(function(data, status) {
					$scope.errorMessageText = ' No data found ';
					$scope.showErrorMessage   = true;
				});
				$scope.showErrorMessage = false;
			} else {
				$scope.showErrorMessage = true;
				$scope.hideLoading($scope.workspace.id);
			}
		} else if(filterType == 'printSign') {
				var errorTextMap = {
					'startDate': 'Start Date in `MM-DD-YYYY` format',
					'endDate'  : 'End Date in `MM-DD-YYYY` format',
					'emailIds' : 'Email Id',
					'searchResult' : 'Event Id'
			}
			var errorMessage  = " Please provide values for ";
			var errorCategory = "";
			
			var requiredFieldArray = ["startDate", "endDate", "emailIds", "searchResult"];
      		var errorCat = $scope.validateRequiredField(requiredFieldArray,$scope.printSignFilter);
			
      		if(errorCat != '') { $scope.errorMessageText.push(errorCat); }
      		$scope.validateDate($scope.printSignFilter, endMaxDateValue);
      		
			if($scope.printSignFilter.emailIds) {
				var emailId = $scope.printSignFilter.emailIds;
				var pattern = /^[\w\.\,]+$/;
				var emailError = false;
				if (!emailError && (emailId == '' || !pattern.test(emailId))) {
					errorCategory = 'Email id should be in proper format';
					$scope.errorMessageText.push(errorCategory);
					emailError = true;
				}
				
				var emailLength = emailId.split('').length;
				var indexOf = emailId.indexOf('.');
				if(!emailError && (indexOf == 0 || indexOf == (emailLength-1))) {
					errorCategory = 'One or more email id entered is invalid';
					$scope.errorMessageText.push(errorCategory);
					emailError = true;
				}
				if(!emailError && (emailId.match(/,,/g) || emailId.match(/\.\./g))) {
					errorCategory = 'One or more email id entered is invalid';
					$scope.errorMessageText.push(errorCategory);
					emailError = true;
				}
			}
			
			if($scope.errorMessageText.length == 0) {
				$scope.showLoading($scope.workspace.id);
				var lookupData = $scope.printSignLookupSearch;
				$scope.printSignFilter.eventId = lookupData.signsId;
				var printSignFilterData = angular.copy($scope.printSignFilter);
				printSignFilterData.startDate = $filter('date')($scope.printSignFilter.startDate, "yyyy-MM-dd");
				printSignFilterData.endDate   = $filter('date')($scope.printSignFilter.endDate, "yyyy-MM-dd");
				$http.post($scope.workspace.dataJson,{filter: printSignFilterData, messageHeader:printSignsMessageHeader}).success(function(response) {
					var responseStatus = response.result;
					//$scope.message     = response.message;
					if(responseStatus) { 
						$scope.showSuccessMessage = true;
						$scope.successMessage     = response.message;
						$scope.printSignFilter.startDate = $scope.printSignFilterDefault.startDate;
						$scope.printSignFilter.endDate   = $scope.printSignFilterDefault.endDate;
						$scope.printSignFilter.emailIds  = '';
						$scope.printSignFilter.searchResult = '';
						$scope.printSignFilter.lookupData  = '';	
					} else {
						$scope.errorMessageText = response.message;
						$scope.showErrorMessage   = true;
						$scope.hideLoading($scope.workspace.id);
					}	
					$timeout(function(){
						$scope.hideLoading($scope.workspace.id);
  					},10)
				}, function(response) {
  					if(response.status == 401 || response.status == 302) {
  						window.location.reload();
  					}
  				}).error(function(data, status) {
					$scope.errorMessageText = ' No data found ';
					$scope.showErrorMessage   = true;
				});
				$scope.showErrorMessage = false;
			} else {
				$scope.showErrorMessage = true;
				$scope.hideLoading($scope.workspace.id);
			}
		}else if (filterType == 'lateEnveReport') {
			$scope.showErrorMessage = false;
			$scope.showTableData = false;
			$scope.showLoading($scope.workspace.id);
			$scope.lateEnveDefault =  angular.copy($scope.lateEnveReport);
			var dataURL = '';
			var tempURL = '';
			var filterData = {};					
			dataURL = $scope.workspace.dataJson;
			tempURL = $scope.workspace.storeTemplate;
			$scope.lateEnveReport.startDate = $filter('date')($scope.lateEnveReport.startDate, "dd-MMM-yyyy");
			$scope.lateEnveReport.endDate   = $filter('date')($scope.lateEnveReport.endDate, "dd-MMM-yyyy");
			filterData = angular.copy($scope.lateEnveReport);					
			filterData.currentPageNo = '1';
			var errorTextMap = {
					'startDate': 'Start date in `MM-DD-YYYY` format',
					'endDate'  : 'End date in `MM-DD-YYYY` format'
			}
			var errorMessage  = " Please provide values for ";
			var errorCategory = "";
			
			
			angular.forEach(["startDate", "endDate"], function (input) {
                if (!$scope.exceptionReport[input]) {  
                	var showError = true;
                	if(errorCategory !== "" && showError === true) {
                		errorCategory = errorCategory + ", ";
                	}
            		if(showError === true) {
            			errorCategory = errorCategory + errorTextMap[input] + ' ';
                    	$scope.showErrorContainer = true;
            		}	            	
                }
            });
			
			$scope.errorMessageText = errorMessage + errorCategory;
			
			if(errorCategory == "") {						
				$http.post(dataURL,{messageHeader : lateEvlpMessageHeader, filter : filterData}).then(function(response) {
					$scope.workspace.data = response.data;
					$scope.workspace.urlStoreTable = tempURL;
					$scope.hideLoading($scope.workspace.id);
					var dataList = $scope.workspace.data;
					if(dataList == null) {
						$scope.showTableData = false;
						$scope.showErrorMessage = true;
						$scope.hideLoading($scope.workspace.id);
						$scope.errorMessageText = 'No data available in table';
					} else {
						$scope.showTableData = true;
						//var totalRow = dataList[0].totalRows;
						//$scope.totalPageCount = Math.ceil(totalRow / 5);
					}
					
				});
			} else {
				$scope.showErrorMessage = true;
				$scope.hideLoading($scope.workspace.id);
				$scope.showTableData = false;
			}
		} else if (filterType == 'silverCanisterReport') {
			var validationNumber = $scope.silverCanisterReport.location !== 'Chain';
			var validationDate   = $scope.silverCanisterReport.status !== "In progress";
			
			var requiredFieldArray = [];
      		
			if(validationNumber) {
				requiredFieldArray.push('number');
			}
			if(validationDate) {
				requiredFieldArray.push('startDate');
				requiredFieldArray.push('endDate');
			}
			var errorCat = $scope.validateRequiredField(requiredFieldArray,$scope.silverCanisterReport);
			if(errorCat != '') { $scope.errorMessageText.push(errorCat); }
			
			if(validationDate) {
				$scope.validateDate($scope.silverCanisterReport, endMaxDateValue);
			}
		
			if($scope.errorMessageText.length == 0){
				$scope.showLoading($scope.workspace.id);
				var filterData = angular.copy($scope.silverCanisterReport);
				filterData.pageNo = '1';
				if(filterData.status == "In progress") {
					filterData.startDate = '';
					filterData.endDate   = '';
				} else {
					filterData.startDate = $filter('date')($scope.silverCanisterReport.startDate, "dd-MMM-yy");
					filterData.endDate   = $filter('date')($scope.silverCanisterReport.endDate, "dd-MMM-yy");
				}
				
				filterData.sortColumnName = 'ID';
				filterData.sortOrder      = 'ASC';
				
				$scope.showTableData = false;
				$http.post($scope.workspace.dataJson,{cache: true, messageHeader : silverCanMessageHeader, filter : filterData}).then(function(response) {
					$scope.workspace.data = response.data;
					$scope.workspace.urlStoreTable = $scope.workspace.urlStoreTable;
					var totalRecords = response.data.totalRecords;
					$scope.totalPageCount = Math.ceil(totalRecords / 5);
					var dataList = $scope.workspace.data.SilverCanisterReportRepList;
					if(dataList.length == 0) {
						$scope.showTable = false;
						$scope.showTableData = false;
						/*$scope.showErrorContainer = true;
						$scope.errorMessageText = "No data found";*/
						$scope.showNoDataFound = true;
					} else {
						$scope.showTable = true;
						$scope.showTableData = true;
						$scope.showErrorContainer = false;
						$scope.showNoDataFound = false;
						for(var k = 0; k < dataList.length; k++) {
							//For sorting
							var rowData = dataList[k].SilverCanisterDetails;
							for(var eachCol in rowData) {
								$scope.colSortOrder[eachCol] = {};
								$scope.colSortOrder[eachCol].order = false;
								$scope.colSortOrder[eachCol].firstLoad = true;
							}
						}
					}
					$timeout(function(){
						$scope.hideLoading($scope.workspace.id);
  					},50)
				}, function(response) {
  					if(response.status == 401 || response.status == 302) {
  						window.location.reload();
  					}
  				});
			} else {
				$scope.showErrorContainer = true;
			}
			$scope.endMaxDate = new Date();
			
		} else if (filterType == 'payOnFilterVendCost') {
			/*var errorTextMap = {
					'vendorDesc': 'Vendor Name',
					'storeNumber'  : 'Store Number'
			}
			var errorMessage  = " Please provide values for ";*/
			var errorCategory = "";
			
			/*angular.forEach(["vendorDesc", "storeNumber"], function (input) {
                if (!$scope.payOnFilterVendCost[input] || $scope.payOnFilterVendCost[input] == 'Select') {  
                	var showError = true;
                	if(errorCategory !== "" && showError === true) {
                		errorCategory = errorCategory + ", ";
                	}
            		if(showError === true) {
            			errorCategory = errorCategory + errorTextMap[input] + ' ';
                    	$scope.showErrorContainer = true;
            		}	            	
                }
            });
			
			$scope.errorMessageText = errorMessage + errorCategory;*/
			if(errorCategory == "") {
				$scope.showLoading($scope.workspace.id);
				$scope.payOnFilterVendCost.currrentPage = '1';
				$scope.showTableData = false;
				if($scope.payOnFilterVendCost.vendorDesc.toLowerCase() == 'select') {
					$scope.payOnFilterVendCost.vendorName = '';
				}
				if(!$scope.payOnFilterVendCost.storeNumber) {
					$scope.payOnFilterVendCost.storeNumber = '';
				}
				
				$http.post($scope.workspace.dataJson,{messageHeader : pofVCMessageHeader, filter : $scope.payOnFilterVendCost}).then(function(response) {
					$scope.workspace.data = response.data.data;
					var dataLen = response.data.data.length;
					$scope.showNoDataFound = false;
					if(dataLen > 0) {
						$scope.workspace.urlStoreTable = $scope.workspace.urlStoreTable;
						var totalRecords = $scope.workspace.data.length;
						$scope.totalPageCount = Math.ceil(response.data.totalRecord/5);
						$scope.showTableData = true;
						for(var k = 0;k < dataLen;k++) {
							var indData = $scope.workspace.data[k];
							$scope.payOnFilterVendCost.checkBox[indData.vcId] = {};
							$scope.payOnFilterVendCost.checkBox[indData.vcId] = angular.copy(indData);
							$scope.payOnFilterVendCost.checkBox[indData.vcId].checkedStatus = false;
							$scope.payOnFilterVendCost.checkBox[indData.vcId].envNumber     = indData.envNumber;
							$scope.payOnFilterVendCost.checkBox[indData.vcId].approvedCost  = '';
							$scope.payOnFilterVendCost.checkBox[indData.vcId].completionStatus = true;
						}
						//console.log($scope.payOnFilterVendCost.checkBox);
					} else {
						//$scope.showErrorMessage = true;
						$scope.hideLoading($scope.workspace.id);
						$scope.showTableData = false;
						//$scope.errorMessageText = "table data not found";
						$scope.showNoDataFound = true;
					}
					$timeout(function(){
						$scope.hideLoading($scope.workspace.id);
  					},50)
				}, function(response) {
  					if(response.status == 401 || response.status == 302) {
  						window.location.reload();
  					}
  				});
			} else {
				$scope.showErrorMessage = true;
				$scope.hideLoading($scope.workspace.id);
				$scope.showTableData = false;
			}
		}  else if(filterType == 'cannedFilter') {
			var requiredFieldArray = ["startDate", "endDate"];
      		var errorCat = $scope.validateRequiredField(requiredFieldArray,$scope.cannedReport);
			
      		if(errorCat != '') {
					$scope.errorMessageText.push(errorCat);
  			} else {
  				$scope.validateDate($scope.cannedReport, endMaxDateValue, '30 days');
  			}

			if($scope.errorMessageText.length == 0){
				$scope.showLoading($scope.workspace.id);
				$scope.cannedReport.currrentPage = '1';
				$scope.cannedReport.activeCd = '1';
				$scope.cannedReport.sortOrder = 'ASC';
				$scope.cannedReport.sortColumnName = 'sysProductId';
				var filterData = angular.copy($scope.cannedReport);
				filterData.startDate = $filter('date')($scope.cannedReport.startDate, "yyyy-MM-dd");
				filterData.endDate   = $filter('date')($scope.cannedReport.endDate, "yyyy-MM-dd");
				var orderId = $scope.cannedReport.orderTypeId;
				if(orderId == 'SOP') {
					$scope.cannedReport.showProfitCol = true;
				} else {
					$scope.cannedReport.showProfitCol = false;
				}
				$scope.showTableData = false;
				
				$http.post($scope.workspace.dataJson,{messageHeader : cannedMessageHeader, filter : filterData}).then(function(response) {
					$scope.workspace.data = response.data;
					$scope.workspace.urlStoreTable = $scope.workspace.urlStoreTable;
					var reportArray = $scope.workspace.data.cannedReportDataBeanList;
					$scope.showErrorMessage = false;
					if(reportArray && reportArray.length == 0) {
						$scope.showTable = false;
						$scope.showNoDataFound = true;
					} else {
						$scope.totalPageCount = Math.ceil(response.data.totalRecord/response.data.pageSize);
						$scope.showTableData = true;
						$scope.showNoDataFound = false;
						$scope.showTable = false;
						for(var k = 0; k < reportArray.length; k++) {
							//For sorting
							var rowData = reportArray[k];
							for(var eachCol in rowData) {
								$scope.colSortOrder[eachCol] = {};
								$scope.colSortOrder[eachCol].order = false;
								$scope.colSortOrder[eachCol].firstLoad = true;
							}
						}
					}
					$timeout(function(){
						$scope.hideLoading($scope.workspace.id);
  					},50)
				}, function(response) {
  					if(response.status == 401 || response.status == 302) {
  						window.location.reload();
  					}
  				});
				
				$http.post($scope.workspace.totalRevJson,{messageHeader : cannedMessageHeader, filter : filterData}).then(function(response) {
					$scope.workspace.totalRevData = response.data;
				}, function(response) {
  					if(response.status == 401 || response.status == 302) {
  						window.location.reload();
  					}
  				});
				
			} else {
				$scope.showErrorMessage = true;
				$scope.showTableData = false;
				$scope.showNoDataFound = false;
			}
			
		}
		return;
	};
	
	
	
	if($scope.workspace.data == undefined)
	    $scope.workspace.data = [];
	if($scope.workspace.data.rowid == undefined) { 
		$scope.workspace.data.rowid = [];
	}
	$scope.selected = [];
	$scope.chckedIndexs=[];
    var updateSelected = function (action, id) {
        if (action == 'add' & $scope.selected.indexOf(id) == -1) {$scope.selected.push(id);}
        if (action == 'remove' && $scope.selected.indexOf(id) != -1) {$scope.selected.splice($scope.selected.indexOf(id), 1);}
    }
    
    $scope.addClassName = function() {
    	var className = '';
    	if($scope.workspace.data.data && $scope.workspace.data.data.length < 5) {
			className = 'no-scroll-table';
			if($window.innerWidth < 900) {
				className = 'no-scroll-table-small';
			}
		}
    	return className;
    }

    $scope.updateSelection = function ($event, id, e) {
        var checkbox = $event.target;
        var action = (checkbox.checked ? 'add' : 'remove');
        updateSelected(action, id);
       if (checkbox.checked) {
            $scope.chckedIndexs.push(e);
        }
        else {
            $scope.chckedIndexs.splice($scope.chckedIndexs.indexOf(e), 1);
        }
    };
    $scope.selectAll = function ($event) {
        var checkbox = $event.target;
        var action = (checkbox.checked ? 'add' : 'remove');
        for (var i = 0; i < $scope.workspace.data.rowid.length; i++) {
            var entity = $scope.workspace.data.rowid[i];
            updateSelected(action, entity.id);
        }
        if(checkbox.checked){
        	$scope.chckedIndexs = angular.copy($scope.selected);
        }else{
        	$scope.chckedIndexs =[];
        }
    };
    $scope.getSelectedClass = function (entity) {
        return $scope.isSelected(entity.id) ? 'selected' : '';
    };
    $scope.isSelected = function (id) {
        return $scope.selected.indexOf(id) >= 0;
    };
    $scope.isSelectedAll = function () {
    	if($scope.workspace.data.rowid == undefined) $scope.workspace.data.rowid = {};
        return (($scope.workspace.data.rowid.length > 0) ?  ($scope.selected.length === $scope.workspace.data.rowid.length) : false) ;
    };
    $scope.remove=function(index){
       angular.forEach($scope.chckedIndexs, function (value, index) {
       var index = $scope.workspace.data.rowid.indexOf(value);
       $scope.workspace.data.rowid.splice(index, 1);
       });
       if($scope.workspace.data.rowid.length == 0) {
    	   $scope.workspace.plulist = [];
       }
	   var addtoplulistflag = true;
	   angular.forEach($scope.workspace.data.rowid, function(obj) {
	    	   if(addtoplulistflag) {
	    		   $scope.workspace.plulist = [];
	    	   }
	    	   addtoplulistflag = false;
	    	   $scope.workspace.plulist.push(obj.val);
	   });
       $scope.chckedIndexs = [];
       $scope.selected = [];
    };
    $scope.pluPopup = function(e) {
    	$scope.showErrorContainer = false;		
    	$scope.showSucessContainer = false;
		e.preventDefault();
		$scope.$state.go('report.plu.item', {reportId: $scope.currentReportId});
	};
	$scope.workspace.pluButtonFlag = function(a) {
		if(a == 'sel') {
			if($scope.workspace.data.rowid == undefined)		
				$scope.workspace.data.rowid = [];
			if($scope.workspace.data.rowid.length > 0) {		
			return false;		
			} else {		
			return true;		
			}
		} else {
			if($scope.selected.length > 0) {		
				return false;		
			} else {		
			return true;		
			}
		}
	};
	$scope.$watch('$stateParams.pluObj', function(){
		angular.forEach($stateParams.pluObj, function(obj) {
			var addToArray=true;
			for(var i=0;i<$scope.workspace.plulist.length;i++){
			    if($scope.workspace.plulist[i] ===obj.val){
			        addToArray=false;
			    }
			}
			if(addToArray){
				$scope.workspace.plulist.push(obj.val);
			}
		});
		$stateParams.pluObj = {};
	}, true);
	$scope.$watch('workspace.plulist', function(){
		var rowidFlag = true;
		var j = 1;
		for(var i=0; i<$scope.workspace.plulist.length; i++) {
			if(rowidFlag) {
				$scope.workspace.data.rowid = [];
			}
			rowidFlag = false;
			var obj = {val:$scope.workspace.plulist[i], id: j++};
			$scope.workspace.data.rowid.push(obj);
		}
	}, true);
	/*
	 * DAILY PLU check-box openration and pop ends  
	 */
    $scope.filterToggle = function() {
        if ($scope.showFilter) $scope.showFilter = false;
        else $scope.showFilter = true;
    }
    $scope.differenceInMonths = function (startDate,endDate) { 
    	var dayDifference = $scope.dateDifference(startDate,endDate);
    	var modifiedStDate  = $filter('date')(startDate, "yyyy");
    	var modifiedEndDate = $filter('date')(endDate, "yyyy");
    	var daysCount = 365;
    	
    	var leapYearCount = $scope.checkForLeapYear(parseInt(modifiedStDate), parseInt(modifiedEndDate));
        var totalDayCount = dayDifference - leapYearCount;
        return totalDayCount/daysCount;
    };
    
    $scope.checkForLeapYear = function(yr1, yr2) {
    	var leapYearCount = 0;
    	for ( var i = yr1; i < yr2; i += 1 ) {
    	    if ( new Date( i, 1, 29 ).getMonth() === 1 ) { leapYearCount++; }
    	}
    	return leapYearCount;
    };
    
    $scope.dateDifference = function (startDate,endDate) { 

    	var dt1 = new Date(startDate);
    	var dt2 = new Date(endDate);

    	var date = dateDiffInDays(dt1, dt2);
    	return date;

    };
    
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
   }
   
   $scope.getErrorParam = function(errorMsg) {
	   var errorObjMapper = $scope.errorTextMap();
	   return errorObjMapper[errorMsg];
   }
   
   //error params mapping
   $scope.errorTextMap = function() {
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
   
   //date range validation
   $scope.validateDate = function(filterData, endMaxDateValue, difference) {
	   	var errorMessageText = '';
	   	
	   	var fieldStDate = angular.element( document.querySelector('.tab-content .active input[type="text"].form-control.start') ).val();
  		var fieldEndDate = angular.element( document.querySelector('.tab-content .active input[type="text"].form-control.end') ).val();
  		
		var validationStDate1 = $filter('date')(filterData.startDate, "MM/dd/yyyy"); 
		var validationEndDate1 = $filter('date')(filterData.endDate, "MM/dd/yyyy"); 
		if((validationStDate1 && validationEndDate1) && ((fieldStDate && fieldStDate !== validationStDate1) || (fieldEndDate && fieldEndDate !== validationEndDate1))) {
			if(validationStDate1 && fieldStDate !== validationStDate1) {
				errorMessageText = "Start Date is not a valid date in MM/DD/YYYY format";
				$scope.errorMessageText.push(errorMessageText);
			}
			if(validationEndDate1 && fieldEndDate !== validationEndDate1) {
				errorMessageText = "End Date is not a valid date in MM/DD/YYYY format";
				$scope.errorMessageText.push(errorMessageText);
			}
			return;
		}
		var dateDifference = $scope.dateDifference(filterData.startDate, filterData.endDate);
		
		if((filterData.startDate && filterData.endDate) && dateDifference < 0) {
			errorMessageText = "Start date cannot be greater than end date";
			$scope.errorMessageText.push(errorMessageText);
			return;
		}
		
		var endDateDifference   = $scope.dateDifference(filterData.endDate, $scope.endMaxDate);
		var startDateDifference = $scope.dateDifference(filterData.startDate, $scope.endMaxDate);
		
		if(endDateDifference < 0 && startDateDifference < 0) {
			errorMessageText = "Start Date and End Date cannot be greater than  current date";
			$scope.errorMessageText.push(errorMessageText);
			return;
		}
		
		if(startDateDifference < 0) {
			errorMessageText = "Start Date cannot be greater than current date";
			$scope.errorMessageText.push(errorMessageText);
			return;
		}
		if(endDateDifference < 0) {
			errorMessageText = "End Date cannot be greater than current date";
			$scope.errorMessageText.push(errorMessageText);
			return;
		}
		
		if(difference) {
			var dayDifference = parseInt(difference.split(' ')[0]);
			var daysOrMonths  = difference.split(' ')[1];
			if(daysOrMonths == 'months') {
				var years = $scope.differenceInMonths(filterData.startDate, filterData.endDate);
				var days = Math.floor(years * 12);
			} else if(daysOrMonths == 'days') {
				var days = $scope.dateDifference(filterData.startDate, filterData.endDate);
			}
			
			if(days > dayDifference) { 
				errorMessageText = 'Date range cannot be more than '+difference;
				$scope.errorMessageText.push(errorMessageText); 
				return;
			}
		}
   }
   
 //for by default load report
   var generateReportOnLoad = utils.getReportIdParam($scope.reportIdMappinglist,$scope.workspace.id);
   if(generateReportOnLoad !== '') {
	   $scope.updateData('', generateReportOnLoad);
   }
}]);


myApp.controller("lookupCtrl", ['$scope', '$stateParams', '$state', 'utils', '$window','$http',
    function($scope, $stateParams, $state, utils, $window, $http) {
        $scope.reportId = $stateParams.reportId;
        var lookupData  = $stateParams.lookupData; 
        
        $scope.windowWidth = "innerWidth" in window ? window.innerWidth : document.documentElement.offsetWidth;
        // Change left position based on window width
        angular.element( document.querySelector('.modal-custom-backdrop')).css('left','-12px');
        if($scope.windowWidth > 900) {
        	angular.element( document.querySelector('.modal-custom-backdrop + .spopupOuterContainer')).css('left','35%');
        	angular.element( document.querySelector('.modal-custom-backdrop')).css('height','100vh');
        } else {
        	angular.element( document.querySelector('.modal-custom-backdrop + .spopupOuterContainer')).css('left','25%'); 
        	angular.element( document.querySelector('.modal-custom-backdrop')).css('height','120vh');
        	angular.element( document.querySelector('.modal-custom-backdrop')).css('top','-185px');
        }

        /* Message header for Print Sign report */
        var printSignsMessageHeader = getMessageHeader("PrintSignsFilterReportDetails", "PrintSigns");
       
        angular.element($window).on('keydown', function(e){
            if (e.keyCode == 27) {
                $scope.goToReport(e);
            }
        });
        
        $scope.$watch('workspaces[0].data', function(event) {
        	var event = event;
        	var filterData = {};
        	filterData.eventName = lookupData;
        	var workspaceObj = utils.getWorkspaceObject($scope.workspaces, $scope.reportId);
        	var filterCriteria = workspaceObj.filterCriteria
        		
        	$http.post(filterCriteria, {messageHeader : printSignsMessageHeader, filter: filterData }).then(function(response) {
        		$scope.lookupDataList = response.data.event;
        		if (response.data.event != null 
        				&& response.data.event.length > 0) {
        			$scope.lookupDataError = false;
        		} else {
        			$scope.lookupErrorMessage = ' No data found ';
        			$scope.lookupDataError = true;
        		}
            	
        	}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
        });

        $scope.goToReport = function(e) {
            e.preventDefault();
            $state.go("report.lookup");
        };
        
        $scope.selectData = function(e, data) {
        	e.preventDefault();
        	$scope.$parent.$parent.printSignLookupSearch = data;
        	$scope.goToReport(e);
        }
    }
]);

//for exception report
myApp.controller("unclaimedOrderCtrl", ['$scope', '$stateParams', '$state', 'utils', '$window','$http',
    function($scope, $stateParams, $state, utils, $window, $http) {
        $scope.custId = $stateParams.custId;
        $scope.reportId = $stateParams.reportId;
        var filterData = {};
        filterData.envelopeNbr = $scope.itemId;
        
        /* Message header for unclaimed order report */
         var unclaimedMessageHeader = getMessageHeader("unclaimedReport", "unclaimed");
        
       angular.element($window).on('keydown', function(e){
            if (e.keyCode == 27) {
                $scope.goToReport(e);
            }
        });

        $scope.$watch('workspaces[0].data', function(newValue, oldValue) {
            $scope.activeIndex = utils.checkIfExist($scope.workspaces, $scope.currentReportId);
                        
            $http.post($scope.workspaces[$scope.activeIndex].customerDetailJson, {messageHeader : unclaimedMessageHeader, customerId: $scope.$stateParams.custId }).then(function(response) {
            	$scope.customerDetailList = response.data.unclaimedDataList;
            	
            });
                        
        });
      
        $scope.goToReport = function(e) {
            e.preventDefault();
            $state.go("report.unclaimed");
        };
    }
]);
myApp.controller("reportItemCtrl", ['$scope', '$stateParams', '$state', 'utils', '$window','$http',
    function($scope, $stateParams, $state, utils, $window, $http) {
        $scope.itemId = $stateParams.itemId
        $scope.reportId = $stateParams.reportId;
        var arrayKey    = 'storeDetails';
       
        angular.element($window).on('keydown', function(e){
            if (e.keyCode == 27) {
                $scope.goToReport(e);
            }
        });


        $scope.$watch('workspaces[0].data', function(newValue, oldValue) {
            $scope.activeIndex = utils.checkIfExist($scope.workspaces, $scope.currentReportId);
            if($scope.workspaces[$scope.activeIndex].data.storeDetails) {
                    arrayKey        = 'storeDetails';
                    $scope.itemType = 'storeDetails';
            }else if($scope.workspaces[$scope.activeIndex].data.productDetail) {
                    arrayKey        = 'productDetail';
                    $scope.itemType = 'productDetail';
            }else {
                    arrayKey        = 'data';
                    $scope.itemType = '';
            }
            
            var detailData  = $scope.workspaces[$scope.activeIndex].data[arrayKey];
			for(var i=0;i<detailData.length;i++) {
				if(detailData[i].id == $scope.itemId) {
					 $scope.detail = detailData[i];
				}
			}
        });
      
        $scope.goToReport = function(e) {
            e.preventDefault();
            $state.go("report.detail");
        };

        $scope.getNextItem = function(e,ele){
             var arrayKey = '';
            $scope.activeIndex = utils.checkIfExist($scope.workspaces, $scope.currentReportId);
        
            if($scope.workspaces[$scope.activeIndex].data.storeDetails)
                    arrayKey    = 'storeDetails';
            else if($scope.workspaces[$scope.activeIndex].data.productDetail)
                    arrayKey    = 'productDetail';
            else 
                    arrayKey    = 'data';

            var cItem = utils.getItem(ele,$scope.workspaces[$scope.activeIndex].data[arrayKey],$scope.itemId);
             if(cItem) return cItem;
        }
        $scope.changeItem = function(e,ele){  
            e.preventDefault();
            var cItem = $scope.getNextItem(e,ele);
            if(cItem)
            $state.go("report.detail.item",{itemId:cItem.id});

        };

    }
]);

//for exception report
myApp.controller("reportExceptionItemCtrl", ['$scope', '$stateParams', '$state', 'utils', '$window','$http',
        function($scope, $stateParams, $state, utils, $window, $http) {
            $scope.itemId = $stateParams.itemId
            $scope.reportId = $stateParams.reportId;
            var filterData = {};
            filterData.envelopeNbr = $scope.itemId;
            
            /* Message header for exception report */
            var exceptionMessageHeader = getMessageHeader("exceptionReport", "exception");
            
            var arrayKey    = 'productDetails';
           
            angular.element($window).on('keydown', function(e){
                if (e.keyCode == 27) {
                    $scope.goToReport(e);
                }
            });

            $scope.$watch('workspaces[0].data', function(newValue, oldValue) {
                $scope.activeIndex = utils.checkIfExist($scope.workspaces, $scope.currentReportId);
                if ($scope.workspaces[$scope.activeIndex].dataJsonItemDetail) {
                		arrayKey        = 'productDetails';
                		$scope.itemType = 'productDetails';
                }
                
                
                $http.post($scope.workspaces[$scope.activeIndex].dataJsonItemDetail, {messageHeader : exceptionMessageHeader, filter: filterData }).then(function(response) {
                	$scope.prodDetail      = response.data.envelopNbrHyperLink.productDetails;
                	$scope.envHistDetails  = response.data.envelopNbrHyperLink.envHistoryList;
                	$scope.envelopeNumber  = $scope.itemId;
                });
                
            });
          
            $scope.goToReport = function(e) {
                e.preventDefault();
                $state.go("report.exception");
            };

            
            $scope.changeItem = function(e,ele){  
                e.preventDefault();
                //var cItem = $scope.getNextItem(e,ele);
                /*$http.post($scope.workspaces[$scope.activeIndex].dataJsonItemDetail, {messageHeader : exceptionMessageHeader, filter: filterData }).then(function(response) {
                	$scope.prodDetail      = response.data.envelopNbrHyperLink.productDetails;
                	$scope.envHistDetails  = response.data.envelopNbrHyperLink.envHistoryBean;
                	$scope.envelopeNumber  = $scope.itemId;
                });
                if(cItem)
                	$state.go("report.exception.detail.item",{itemId:cItem.id});*/
                
            };

        }
    ]);

myApp.controller("reportAddToDashboarCtrl", ['$scope', '$stateParams', '$state', '$window', 'utils', '$http', function($scope, $stateParams, $state, $window, utils, $http) {
	$scope.reportId 		= $stateParams.reportId;	
	if($scope.workspaces.length == 0) {
		$state.go("report.detail",{'reportId':$scope.reportId});
		return;
	} 
		
	
	$scope.addDashboardPop	= true;
    $scope.activeIndex 		= utils.checkIfExist($scope.workspaces, $scope.currentReportId);
	$scope.workspace		= angular.copy($scope.workspaces[$scope.activeIndex]);//copy not assign to protect opening datepicker in back page
	$scope.filterPage 		= $scope.workspace.urlfilterPage;
	
	angular.element($window).on('keydown', function(e){
		if (e.keyCode == 27) {
			$scope.goToReport(e);
		}
	});
	$scope.goToReport = function(e) {
		e.preventDefault();
		$state.go("report.detail",{'reportId':$scope.reportId});
	};
	
	/*machine starts */
	if ($scope.workspace.locationlistJson) {
        $http.get($scope.workspace.locationlistJson).then(function(response) {
            $scope.workspace.locationType = response.data.location;
        }, function(response) {
			if(response.status == 401 || response.status == 302) {
				window.location.reload();
			}
		});
    }
	if ($scope.workspace.machinelistJson) {
		$http.get($scope.workspace.machinelistJson).then(function(response) {
            $scope.workspace.machineList = response.data.machine;
		}, function(response) {
			if(response.status == 401 || response.status == 302) {
				window.location.reload();
			}
		});
	}
    if ($scope.workspace.vendorlistJson) { 
        $http.post($scope.workspace.vendorlistJson).then(function(response) {
            $scope.workspace.vendorList = response.data.vendor;
        }, function(response) {
			if(response.status == 401 || response.status == 302) {
				window.location.reload();
			}
		});
    }
	
	//if(!$scope.payOnFilterDefault)
	//	$scope.payOnFilterDefault	= {};
	if(!$scope.machineFilterDefault)
		$scope.machineFilterDefault	= {};
	$scope.machineFilter = [];
	//$scope.payOnFilter = [];
	
	//$scope.payOnFilterDefault.filtertypePay = 'default';
	$scope.machineFilterDefault.location = 'Select';
	$scope.machineFilterDefault.machine = 'Select';
	$scope.machineFilter = angular.copy($scope.machineFilterDefault);
	$scope.payOnFilter = angular.copy($scope.payOnFilterDefault);
	//$scope.payOnFilter.vendor = data;
	
	if(!$scope.royaltyrepFilterDefault)
		$scope.royaltyrepFilterDefault	= {};
	$scope.royaltyrepFilter = [];
	$scope.royaltyrepFilterDefault.vendor = 'Select';
	$scope.royaltyrepFilter = angular.copy($scope.royaltyrepFilterDefault);
	
	$scope.open = function($event, ele) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.workspace.openedEnd = false;
        $scope.workspace.openedStart = false;
        if (ele == 'start') $scope.workspace.openedStart = true;
        if (ele == 'end') $scope.workspace.openedEnd = true;
    }
    $scope.setLocation = function(data, id, e) {
        e.preventDefault();
        //$scope.workspace.data.location = data;
		$scope.machineFilter.location = data;
		
		$scope.machineFilter.locationId = id;
		
    };
 
    $scope.setMachine = function(data, id, e) {
        e.preventDefault();
        //$scope.workspace.data.machine = data;
		$scope.machineFilter.machine = data;
		$scope.machineFilter.machineId = id;
    };
    $scope.setVendor = function(data, e) {
        e.preventDefault();
        $scope.payOnFilter.vendor = data;
    };
	/* machine ends */
	
    $scope.setRoyaltyVendorList = function(data, id, e) {
        e.preventDefault();
        $scope.workspace.royaltyrepFilter.vendorName = data;
		$scope.royaltyrepFilter.vendorName = data;
		$scope.royaltyrepFilter.sysVendorId = id;
    };
    
	$scope.addToDashboard	= function(e,type) {
		e.preventDefault();
		if(type == 'kioskFilter')
			console.log(type, $scope.workspace.kioskFilters);
		else if(type == 'machineFilter')
			console.log(type, $scope.machineFilter);
		
		//call ajax here to add to dashboard
	}
	 
}]);

myApp.controller("licenseContentReportCtrl", ['$scope', '$stateParams', '$state', '$http', function($scope, $stateParams, $state, $http) {
	
	$scope.licenseContent 	  = {};
	$scope.showFilter 		  = true;
	$scope.message            = '';
	$scope.showErrorMessage   = false;
	$scope.showSuccessMessage = false;
	
	$scope.open = function($event, ele) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.licenseContent.openedEnd = false;
        $scope.licenseContent.openedStart = false;
        if (ele == 'start') $scope.licenseContent.openedStart = true;
        if (ele == 'end') $scope.licenseContent.openedEnd = true;
    }
	$scope.filterToggle = function() {
        if ($scope.showFilter) $scope.showFilter = false;
        else $scope.showFilter = true;
    }
	$scope.submitData = function(e) {
		$http.post($scope.workspace.dataJson,{filter: $scope.filterData, messageHeader:licenseContentMessageHeader}).success(function(response) {
			var responseStatus = response.result;
			$scope.message     = response.message;
			if(responseStatus) { 
				$scope.showSuccessMessage = true;
				$scope.showErrorMessage   = false;
				$scope.filterData.startDate = '';
				$scope.filterData.endDate   = '';
				$scope.filterData.emailId   = '';				
			} else {
				$scope.showErrorMessage   = true;
				$scope.showSuccessMessage = false;
			}			
		}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
		}).error(function(data, status) {
			$scope.showErrorMessage = true;
			$scope.message     = data;
		});
	}
}]);

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

/* to generate transaction ID for the KIOSK report */
var transactionID = function guid() {
   function _p8(s) {
       var p = (Math.random().toString()+"000000000").substr(2,8);
       return s ? "-" + p.substr(0,4) + "-" + p.substr(4,4) : p ; 
   }
   return _p8() + _p8(true) + _p8(true) + _p8();
};

/* To generate transaction If for PM BY WIC Report */
var requestTransactionID = function tranId(){ 
	 return  (Math.random().toString()+"000000000").substr(2,8);
};

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

/** PLU pop-up screen **/
myApp.controller('pluCtrl',['$scope', '$stateParams', '$state', '$window', '$http', 'utils', function($scope, $stateParams, $state, $window, $http, utils) {
	
	$scope.workspace = {};
	$scope.reportId = $stateParams.reportId;
	$scope.showtable = false;
	$scope.showDataTD = false;
	$scope.showNoDataTD = false;
	var dailyPLUMessageHeader = getMessageHeader("dailyPLUreport", "dailyPLU");
	$scope.workspace.plu = {};
	$scope.showErrorContainer = false;
	// Get window width
	$scope.windowWidth = "innerWidth" in window ? window.innerWidth : document.documentElement.offsetWidth;
    // Change left position based on window width
    if($scope.windowWidth > 900) {
    	angular.element( document.querySelector('.modal-custom-backdrop + .spopupOuterContainer')).css('left','35%');
    	angular.element( document.querySelector('.modal-custom-backdrop')).css('height','100vh');
    } else {
    	angular.element( document.querySelector('.modal-custom-backdrop + .spopupOuterContainer')).css('left','25%'); 
    	angular.element( document.querySelector('.modal-custom-backdrop')).css('height','120vh');
    	angular.element( document.querySelector('.modal-custom-backdrop')).css('top','-185px');
    }
	
	angular.element($window).on('keydown', function(e){
        if (e.keyCode == 27) {
            $scope.goToReport(e);
        }
    });
	$scope.goToReport = function(e, obj) {
        e.preventDefault();
        $state.go("report.plu",obj);
    };
    $scope.hideMessage = function() {
    	$scope.showErrorContainer = false;
    };
    $scope.getPlU = function(){
    	$scope.showErrorContainer = false;
    	$scope.showTableData = false;
    	$scope.flag = false;
    	if($scope.workspace.plu.PluKey == undefined){
    		$scope.flag = true;
    		$scope.workspace.plu.PluKey = '';
    	}else{
    		//$scope.workspace.plu.PluKey = Number($scope.workspace.plu.PluKey);
    		if(isNaN($scope.workspace.plu.PluKey)){
    			$scope.flag = false;
    		}else{
    			$scope.flag = true;
    		}
    	}
		if(!$scope.flag) {
			$scope.rowId = [];
			$scope.showErrorContainer = true;
			$scope.showTableData = false;
		} else {
			$scope.rowId = [];
    		var obj = {
        			messageHeader : dailyPLUMessageHeader,
    				currentPageReq : 1,
    				pluKey : $scope.workspace.plu.PluKey
        	};
    		$http.post("/PhotoOmniOMSWeb/orders/get_plu_list", obj).then(function(response) {
    			$scope.showtable = true;
    			$scope.showTableData = false;
    			$scope.workspace.data = response.data;
    			if($scope.workspace.data.pluNumber != null && $scope.workspace.data.pluNumber.length > 0){
    				$scope.showNoDataTD = false;
    				$scope.showDataTD = true;
    				$scope.showTableData = true;
    			} else {
    				$scope.showTableData = true;
    				$scope.showNoDataTD = true;
    				$scope.showDataTD = false;
    			}
    		}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
		}
	};
	$scope.changePLUPagination = function() {
		var obj = {
				currentPageReq : $scope.currentIndex,
				pluKey : (($scope.workspace.plu.PluKey == undefined) ? " " : $scope.workspace.plu.PluKey)
		};
		$http.post('/PhotoOmniOMSWeb/orders/get_plu_list',obj).then(function(response) {
			$scope.workspace.data = response.data;
			$scope.workspace.data.currentPage = $scope.workspace.data.currentPage;
		}, function(response) {
			if(response.status == 401 || response.status == 302) {
				window.location.reload();
			}
		});
	};
	var rowId = $scope.rowId = [];
	$scope.enableDisableAddButton = function($event, data) {
		var checkbox = $event.target;
		var action = (checkbox.checked ? 'add' : 'remove');
		updateSelected(action, data);
	};
	var updateSelected = function(action, data) {
		if (action === 'add' && $scope.rowId.indexOf(data) == -1) {
			$scope.rowId.push(data);
		}
		if (action === 'remove' && $scope.rowId.indexOf(data) !== -1) {
			$scope.rowId.splice($scope.rowId.indexOf(data), 1);
		}
	};
	$scope.isSelected = function(data) {
		return $scope.rowId.indexOf(data) >= 0;
	};
	$scope.addPLU = function(e) {
		var workspace = utils.getWorkspaceObject($scope.workspaces, $scope.reportId);
		workspace.data.rowid = [];
	    var i = 1 ;
	    angular.forEach($scope.rowId,function(val){
	    	var obj = {val:val, id: i++};
	    	workspace.data.rowid.push(obj);
	    });
	    $scope.goToReport(e,{pluObj:workspace.data.rowid});
	};
}]);

myApp.controller("SalesReportByProductCtrl", ['$scope', '$stateParams', '$state', '$http', function($scope, $stateParams, $state, $http) {
	$scope.format = 'MM-dd-yyyy';
	$scope.endMaxDate = new Date();
	$scope.workspace.filter = {};
	$scope.workspace.filter.startDate = new Date();
	$scope.workspace.filter.endDate = new Date();
	$scope.saveSalesReportByProduct = function(){
		$scope.workspace.data = [];
		var productCategory = [];
		var productSize = [];
		angular.forEach($scope.workspace.defaultFilters.productCategory,function(obj){
			if(obj.selected){
				productCategory.push(obj.CODE)
			}
		})
		angular.forEach($scope.workspace.defaultFilters.productSizes,function(obj){
			if(obj.selected){
				productSize.push(obj.CODE)
			}
		})
		if($scope.workspace.filter == undefined){
			$scope.workspace.filter = {};
		}
		$scope.workspace.filter.productCategory = productCategory;
		$scope.workspace.filter.productSize = productSize;
		
		var salesReportMessageHeader = getMessageHeader("salesReportByProduct", "salesReport");
		
		 $scope.hideMessage = function() {
				$scope.showErrorContainer = false;
				$scope.showSucessContainer = false;
			};
			$scope.showSucessContainer = false;
		if($scope.validateReportParams($scope.workspace.filter)){
			$scope.workspace.filter.startDate = dateConverter($scope.workspace.filter.startDate);
			$scope.workspace.filter.endDate = dateConverter($scope.workspace.filter.endDate);
		$http.post('/PhotoOmniOMSWeb/orders/submit/sales_by_photo_card_rpt_req',{messageHeader: salesReportMessageHeader,
				filter : $scope.workspace.filter} ).then(function(res){
					
					$scope.workspace.data = res.data;
					$scope.showSucessContainer = true;
					$scope.workspace.filter = {};
					$scope.workspace.filter.startDate = new Date();
					$scope.workspace.filter.endDate = new Date();
					 $scope.unCheckAllProduct($scope.workspace) ;
					 $scope.unCheckAllPrintSize($scope.workspace);
					 $scope.workspace.defaultFilters.selectAllProduct = false;
					 $scope.workspace.defaultFilters.selectAllSize = false;
			
			}, function(response) {
				if(response.status == 401 || response.status == 302) {
					window.location.reload();
				}
			});
		}
		
	}
	
	 $scope.open = function($event, ele,workspace) {
		 $scope.showErrorContainer = false;
	        $event.preventDefault();
	        $scope.showSucessContainer = false;
	        $event.stopPropagation();
	        workspace.openedEnd = false;
	        workspace.openedStart = false;
	        if (ele == 'start') workspace.openedStart = true;
	        if (ele == 'end') workspace.openedEnd = true;
	    };
	 
	 $scope.selectAllSize = function(workspace){
		 angular.forEach(workspace.defaultFilters.productSizes,function(obj){
			 obj.selected = workspace.defaultFilters.selectAllSize;
		 })
	 };
	 
	 
	 $scope.validateReportParams = function( data) {
	    	$scope.showErrorContainer = false;
	    	$scope.errorMessage = "";
	    	var errorMessage  = " cannot be empty. ";
			var validationErrorMessage = "";
			var inputStatus = false;
			var validationError = false;
			var error = "";
			var delimiter = "";
	    	// check if any filter is empty
	    	if (angular.isObject(data)) {
	    		if (angular.isUndefined(data.startDate) || data.startDate == null) {
	    			error = "Start Date";
	    			inputStatus = true;
	    			delimiter = ", ";
	    		}if (angular.isUndefined(data.endDate) || data.endDate == null) {
	    			error = error + delimiter +"End Date" ;
	    			inputStatus = true;
	    			delimiter = ", ";
	    		}if(data.productCategory.length == 0){
	    			error = error + delimiter + "Product Type" ;
	    			inputStatus = true;
	    			delimiter = ", ";
	    		} if(data.productSize.length == 0){
	    			error = error + delimiter + "Print Size";
	    			inputStatus = true;
	    			delimiter = ", ";
	    		}
	    		if (angular.isUndefined(data.emailIds) || data.emailIds == "") {
	    			error = error + delimiter +"Email ID";
	    			inputStatus = true;
	    		} else {
	    			var arr = data.emailIds.split(',');
	    			var str = [];
	    			var j=0;
	    			for(var i=0;i<arr.length;i++){
	    				var username =arr[i].replace(/^[ ]+|[ ]+$/g,'');
	    				if( !(/^[a-zA-Z0-9]+\.[a-zA-Z0-9]+$/.test(username))){
	    					if(/\s/.test(username)){
	    						validationErrorMessage = "One or More Email ID entered is invalid.";
	    					}
	    					else {
	    						validationErrorMessage = "Email ID should be in proper format."
	    					}
	    					validationError = true;
	    					delimiter = ", ";
	    				}
	    				if(!validationError){
	    				for(var k=0 ; k<str.length;k++){
	    					if(str[k].toLowerCase()==(username.toLowerCase())){
	    						validationErrorMessage ="One or more Email ID is repeated." ;
	    						validationError = true;
	    						delimiter = ", ";
	    					}
	    				}
	    				str[j]= arr[i];
	    				j++;
	    				}
	    				
	    			}
	    		}
	    		
	    		if(inputStatus){
	    			errorMessage = 	error +errorMessage;
	    		}
	    		if( !inputStatus && data.startDate > (new Date()) ){
	    			validationErrorMessage = "Start date Should be today or less" + delimiter + validationErrorMessage ;
	    			validationError = true;
	    			delimiter = ", ";
	    		}
	    		 else if ( !inputStatus && data.endDate < data.startDate) {
	    			 validationErrorMessage = "Start Date should be less then End Date" + delimiter + validationErrorMessage ;
	    			validationError = true;
	    			delimiter = ", ";
	    		} else if (!inputStatus && data.endDate > (new Date())) {
	    			validationErrorMessage = "End Date Should be today or less" + delimiter + validationErrorMessage ;
	    			validationError = true;
	    			delimiter = ", ";
	    		} else if(!inputStatus){
	    			var datetime1 = data.startDate.getTime();
	        		var datetime2 = data.endDate.getTime();
	        		var difference = datetime2-datetime1;
	        		if((difference/1000) > 7948800){
	        			validationErrorMessage =  "Time Period is more than 93 days" + delimiter + validationErrorMessage ;
	        			validationError = true;
	        		}
	    		}
	    	} else {
	    		validationErrorMessage = "Start Date, End Date , Email ID" + errorMessage;
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
	    	
	    };
	 $scope.selectAllProduct = function(workspace){
		 angular.forEach(workspace.defaultFilters.productCategory,function(obj){
			 obj.selected = workspace.defaultFilters.selectAllProduct;
		 })
	 } 
	 $scope.unCheckAllProduct = function(workspace){
		 angular.forEach(workspace.defaultFilters.productCategory,function(obj){
			 obj.selected = false;
		 })
	 } 
	 $scope.unCheckAllPrintSize =function(workspace){
		 angular.forEach(workspace.defaultFilters.productSizes,function(obj){
			 obj.selected = false;
		 })
	 };
	 
	
}])

var getMessageHeader = function(commandName, origin){
	var header ={
		 "appID": "PHOTOOMNI",
		 "transactionID": transactionID(),
		 "msgSentTimestamp": messageHeaderCurrentTimestamp(),
		 "commandName":commandName,
		 "commandVersion":"1",
		 "origin":origin
	};
	return header;
};

var getDefaultDate = function(cloneDate) {
	var defaultLateEnvDate   = new Date();
	var defaultLateEnvStDate = new Date();
	defaultLateEnvDate.setDate(defaultLateEnvDate.getDate() - 1);
	defaultLateEnvStDate.setDate(defaultLateEnvStDate.getDate() - 1);
	cloneDate.startDate = defaultLateEnvStDate;
	cloneDate.endDate   = defaultLateEnvDate;
}

//a and b are javascript Date objects (start date and end date)
function dateDiffInDays(a, b) {
	var _MS_PER_DAY = 1000 * 60 * 60 * 24;
	// Discard the time and time-zone information.
	var utc1 = Date.UTC(a.getFullYear(), a.getMonth(), a.getDate());
	var utc2 = Date.UTC(b.getFullYear(), b.getMonth(), b.getDate());

	return Math.floor((utc2 - utc1) / _MS_PER_DAY);
} 


