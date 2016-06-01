"use strict";
myApp.directive('skwindow', function() {
    return {
        restrict: 'EA',
        templateUrl: "res/template/skwindow.template.html",
        scope: {
            title: '@'
        },
        transclude: true,
        link: function(scope, element, attrs, dropdownCtrl, $http) {
            scope.title = attrs.title;
            scope.windowid = attrs.windowid;
            scope.windowclass = attrs.windowclass;
            scope.windowVisible = true;
            scope.fullscreen = false;
            scope.minimize = false;
            scope.enableRemoveSession = attrs.enableremovesession;
            scope.enableRemovePermanent = attrs.enableremovepermanent;
            scope.minimiseMaximiseWindow = function() {
                if (scope.minimize) scope.minimize = false;
                else scope.minimize = true;
            };
            scope.resizeWindow = function() {
                if (scope.fullscreen) scope.fullscreen = false;
                else scope.fullscreen = true;
            };
            scope.hideWindow = function(windowid) {
                scope.windowVisible = false;
            };
            scope.removeWindow = function(windowid) {
                scope.windowVisible = false;
            };


        }
    };
});

myApp.directive('wgpagination', function() {
    return {
        restrict: 'EA',
        templateUrl: "res/template/wgpagination.template.html",
        scope: {
        	showTableData: '='
        },
        transclude: true,
        link: function(scope, element, attrs, dropdownCtrl, $http) {
            scope.$watch('showTableData', function() {
            	scope.startIndex     = attrs.startIndex;
                scope.totalPageCount = attrs.totalPageCount;
                scope.index = attrs.id;
                
                scope.disabledPrevPagination = true;
                scope.disabledNextPagination = false; 
                scope.$parent.$parent.currentIndex = 1;
                
                if(scope.startIndex == scope.totalPageCount) {
                	scope.disabledNextPagination = true;
                }

                scope.changePagination = function(state) {
                	var currentIndex = scope.startIndex;
                	var maxIndex     = scope.totalPageCount;
                	
                	var tempIndex = currentIndex;
                	
                	if(state == 'prev') {
                		tempIndex--;
                	}
                	if(state == 'next') {
                		tempIndex++;
                	}
                	
                	if(state == 'prev' && currentIndex > '1') {
                		scope.startIndex--;
                		scope.disabledPrevPagination = false;                		
                	}
                    
                	if(state == 'next' && currentIndex < maxIndex) {
                		scope.startIndex++;
                		scope.disabledNextPagination = false;
                	}
                	
                	if(scope.startIndex == '1') {
                		scope.disabledPrevPagination = true;
                	} else {
                		scope.disabledPrevPagination = false;
                	}
                	if(scope.startIndex == maxIndex) {
                		scope.disabledNextPagination = true;
                	} else {
                		scope.disabledNextPagination = false;
                	}  
                	
                	if(tempIndex <= 0 || tempIndex > maxIndex) {
                		scope.$parent.$parent.currentIndex = "";
                	} else {
                		scope.$parent.$parent.currentIndex = scope.startIndex;
                	}
                	
                };
            });
        }
    };
})
