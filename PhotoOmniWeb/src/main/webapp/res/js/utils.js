"use strict";
myApp.factory('utils', function() {
    return {
        // Util for finding an object by its 'id' property among an array
        findById: function findById(a, id) {
            for (var i = 0; i < a.length; i++) {
				if(typeof(a[i].subcat) !== 'undefined') {
					for (var j = 0; j < a[i].subcat.length; j++) {
						if (a[i].subcat[j].id == id) return {
							cat: a[i],
							subcat: a[i].subcat[j]
						}
					}
				}else {
					if (a[i].id == id) return {
							cat: a[i]
					}
				}
            }
            return null;
        },
        checkIfExist: function checkIfExist(array, id) {
            for (var i = 0; i < array.length; i++) {
                if (array[i].id == id) return i;
            }
            return null;
        },
        
        checkForValue: function checkForValue(array, id) {
        	for(var key in array) {
        		var eachObj = array[key];
                if (eachObj == id) return key;
            }
            return null;
        },
        
        getVendorIdArray: function getVendorIdArray(array, vendorName) {
        	var vendorIdArray = [];
        	for(var key in array) {
        		var eachObj = array[key];
                if (eachObj['description'] == vendorName) {
                	vendorIdArray.push(eachObj['sysVendorId']);
                }
            }
            return vendorIdArray;
        },
        
        getUniqueVendorName: function getUniqueVendorName(vendorArray) {
        	if(!vendorArray) { return; }
        	var uniqueVendorArray = [];
        	var vendorArrayLen = vendorArray.length;
            var found;
            var x;
            var y;

	        for (x = 0; x < vendorArrayLen; x++) {
	            found = undefined;
	            for (y = 0; y < uniqueVendorArray.length; y++) {
	                if (vendorArray[x].description === uniqueVendorArray[y].description) {
	                    found = true;
	                    break;
	                }
	            }
	            if (!found) {
	            	uniqueVendorArray.push(vendorArray[x]);
	            }
	        }
	        return uniqueVendorArray;
        },
        
        // Util for returning a random key from a collection that also isn't the current key
        newRandomKey: function newRandomKey(coll, key, currentKey) {
            var randKey;
            do {
                randKey = coll[Math.floor(coll.length * Math.random())][key];
            } while (randKey == currentKey);
            return randKey;
        },
        getItem: function  getItem(ele,workspaceData,cId){
            var returnId = null; var cIndex = null;
            for(var i = 0; i < workspaceData.length;i++){
                if(cId == workspaceData[i].id){
                    if(ele == 'prev')
                        cIndex = i-1;
                    else if(ele == 'next')
                        cIndex = i+1;
                }
            }
             if(cIndex == -1 || cIndex == workspaceData.length)
            return returnId;
            else
                return workspaceData[cIndex];
        },
        /**
         * This method gets the param to call updateData method
         * on load
         * mappingObject : json that contains mapping for parameter
         * id: report id
         */
        getReportIdParam: function  getReportIdParam(mappingObject,id){
        	if(!mappingObject[id]) { return ''; }
        	else { return mappingObject[id]; }
        },
        getWorkspaceObject: function getWorkspaceObject(workspaceObject, id) {
        	for(var eachWrksp in workspaceObject) {
        		if(workspaceObject[eachWrksp].id == id) { return workspaceObject[eachWrksp]; }
        	}
        }
    };
})

myApp.factory('reportCache', function($cacheFactory) {
	var reportData = {};
	reportData.filterData = {};
	reportData.responseData = {};
	return $cacheFactory(reportData);
});

/*** Central Admin Tab Json ***/
myApp.service('adminlist', ['$http', 'utils', function($http, utils) {
    var path = 'res/sampledata/config.centralAdmin.json';
    var adminlist = $http.get(path).then(function(adminlist) {
        return adminlist.data.catagory;
    });

    var service = {};
    service.all = function() {
        return adminlist;
    };
    service.get = function(id) {
        return adminlist.then(function() {
            return utils.findById(adminlist, id);
        });
    };
    return service;
}]);
myApp.service('reportlist', ['$http', 'utils', function($http, utils) {
    var path = 'res/sampledata/config.report.json';
    var reportlist = $http.get(path).then(function(reportlist) {
        return reportlist.data.catagory;
    });

    var service = {};
    service.all = function() {
        return reportlist;
    };
    service.get = function(id) {
        return reportlist.then(function() {
            return utils.findById(reportlist, id);
        })
    };
    return service;
}]);
myApp.service('windowList', ['$http', function($http) {
    var path = appContextPath + '/dashboard/load';
    var windowList = $http.get(path).then(function(windowList) {
        return windowList.data.data;
    });
    
    var service = {};
    service.all = function() {
        return windowList;

    };
    return service;
}]);


/* Store user starts from here */
myApp.service('workspaceList', ['$http', function($http) {
    var path = 'res/sampledata/config.workspace.json';
    var workspaceList = $http.get(path).then(function(workspaceList) {
        return workspaceList.data;
    });
    var service = {};
    service.all = function() {
        return workspaceList;
    };
    return service;
}]);
myApp.service('getWorkspaceTabData', ['$http', function($http) {
	var tabData=[], service	= {};
	service.setTabData	= function(data) {
		tabData.push(data);
	}
	service.getTabData	= function() {
		return tabData;
	}
	return service;
}]);

myApp.service('systemlist', ['$http', 'utils', function($http, utils) {
    var path = 'res/sampledata/config.system.json';
    var systemlist = $http.get(path).then(function(systemlist) {
        return systemlist.data.catagory;
    });

    var service = {};
    service.all = function() {
        return systemlist;
    };
    service.get = function(id) {
        return systemlist.then(function() {
            return utils.findById(systemlist, id);
        })
    };
    return service;
}]);



myApp.service('systemreportlist', ['$http', function($http) {
    var path = 'res/sampledata/system/reports.json';
    var systemreportlist = $http.get(path).then(function(systemreportlist) {
        return systemreportlist.data.reports;
    });

    var service = {};
    service.all = function() {
        return systemreportlist;

    };
    return service;
}]);

myApp.service('reportIdMapping', ['$http', function($http) {
    var path = 'res/sampledata/report.mapping.json';
    var reportIdMappinglist = $http.get(path).then(function(reportIdMappinglist) {
        return reportIdMappinglist.data;
    });

    var service = {};
    service.all = function() {
        return reportIdMappinglist;

    };
    return service;
}]);
