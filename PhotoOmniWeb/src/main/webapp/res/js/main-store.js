//it has global scope (all code will execute in strict mode).
"use strict";
if (centralizationReportApp) {
    alert("Error!!! , variable : 'centralizationReportApp' already defined");
}
var centralizationReportApp = {};

centralizationReportApp.hideUrl = false;
centralizationReportApp.urlObj = {
    workspaceUrl: "",
	workspaceDetailUrl: "",
	orderDetailsUrl: "",
	envelopeDetailsUrl: "",
	envelopePopupUrl: "",
	systemManagementUrl: "",
	systemReportsUrl: "",
	systemReportsDetailsUrl: ""
};

if (!centralizationReportApp.hideUrl) {
    centralizationReportApp.urlObj.workspaceUrl = "/workspace";
	centralizationReportApp.urlObj.workspaceDetailsUrl = "/:itemType/:itemId";
	centralizationReportApp.urlObj.orderDetailsUrl = "/order-details/:envNo";
	centralizationReportApp.urlObj.envelopeDetailsUrl = "/envelope/:envId";
	centralizationReportApp.urlObj.envelopePopupUrl = "/details/:envId";
	
	centralizationReportApp.urlObj.orderManagementUrl = "/order-management";
	centralizationReportApp.urlObj.systemManagementUrl = "/system-management";
	centralizationReportApp.urlObj.systemReportsUrl = "/reports";
	centralizationReportApp.urlObj.systemReportsDetailsUrl = "/{reportId:[0-9]{1,4}}";
	centralizationReportApp.urlObj.dashboardUrl = "/dashboard";
}

var myApp = angular.module('walgreenReports',['ui.router' , 'ui.bootstrap'])
    .run(
        ['$rootScope', '$state', '$stateParams',
            function($rootScope, $state, $stateParams) {
                $rootScope.$state = $state;
                $rootScope.$stateParams = $stateParams;
                if(centralizationReportApp.hideUrl)
                    $state.go("workspace");
            }
        ]
    )
    .config(['$stateProvider', '$urlRouterProvider',
        function($stateProvider, $urlRouterProvider) {
            $stateProvider
                .state("workspace", {
                    url: centralizationReportApp.urlObj.workspaceUrl,
                    templateUrl: 'res/template/workspace/workspace.html',
                    controller: 'workspaceCtrl',
                    resolve: {
                        workspaceList: ['workspaceList',
                            function(workspaceList) {
                                return workspaceList.all();
                            }
                        ]
                    }
                })
				.state('orderdetails', {
                    params: {
						envNo:''
                    },
                    url: centralizationReportApp.urlObj.orderDetailsUrl,
                    controller: 'orderDetailsCtrl',
                    templateUrl: 'res/template/workspace/order.details.html'
                })
				.state('orderdetails.envelope', {
                    params: {
						envId:''
                    },
                    url: centralizationReportApp.urlObj.envelopeDetailsUrl,
                    controller: 'envelopeDetailsCtrl',
                    templateUrl: 'res/template/workspace/envelope.details.html'
                })
				.state('orderdetails.details', {
                    params: {
						envId:''
                    },
                    url: centralizationReportApp.urlObj.envelopePopupUrl,
                    controller: 'envelopePopCtrl',
                    templateUrl: 'res/template/workspace/envelope.popup.html'
                })
				.state('workspace.details', {
                    params: {
						itemType:'',
                        itemId: ''
                    },
                    url: centralizationReportApp.urlObj.workspaceDetailsUrl,
                    controller: 'workspaceDetailsCtrl',
                    templateUrl: 'res/template/workspace/tab.item.details.html'
                })
				.state('orderManagement', {
                   url: centralizationReportApp.urlObj.orderManagementUrl,
                   template: '<small class="muted">View content will be placed under res/template/order.management.html</small>'
                })
				.state('systemManagement', {
                   url: centralizationReportApp.urlObj.systemManagementUrl,
                   controller: 'systemManagementCtrl',
                   templateUrl: 'res/template/system/system.management.html'
                })
				.state("reports", {
                    url: centralizationReportApp.urlObj.systemReportsUrl,
					controller: 'systemReportsCtrl',
                    templateUrl: 'res/template/system/report/system.reports.html',
					resolve: {
                        reportlist: ['systemreportlist',
                            function(systemreportlist) {
                                return systemreportlist.all();
                            }
                        ]
                    }
                })
				.state('reports.detail', {
                    url: centralizationReportApp.urlObj.systemReportsDetailsUrl,
					params: {
                        reportId: ''
                    },
                    controller: 'systemReportsDetailsCtrl',
                     template: '<div ui-view></div>'
                })
				.state('dashboard', {
                   url: centralizationReportApp.urlObj.dashboardUrl,
                   template: '<small class="muted">View content will be placed under res/template/store-dashboard/dashboard.html</small>'
                })
				
                
            if (!centralizationReportApp.hideUrl)
                $urlRouterProvider.otherwise('/workspace');
        }
    ])
	
	///////////////////////////////
				/*.state('orderdetails.envelope.details', {
                    params: {
						envId:''
                    },
                    url: centralizationReportApp.urlObj.envelopePopupUrl,
                    controller: 'envelopePopCtrl',
                    templateUrl: 'res/template/workspace/envelope.popup.html'
                })*/
