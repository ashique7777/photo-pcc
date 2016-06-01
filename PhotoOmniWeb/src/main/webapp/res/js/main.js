//it has global scope (all code will execute in strict mode).
"use strict";
if (centralizationReportApp) {
    alert("Error!!! , variable : 'centralizationReportApp' already defined");
}
var centralizationReportApp = {};

centralizationReportApp.hideUrl = false;
centralizationReportApp.urlObj = {
    dashboardUrl: "",
    reportUrl: "",
    reportDetailUrl: "",
    centralAdminDetailUrl: "",
	reportAddToDashboarCtrl: "",
    reportDetailItemUrl: "",
    exceptionReportDetailItemUrl: "",//for exception report
    unclaimedOrderReportItemUrl: "",//for unclaimed order
    omsUrl: "",
    storeUrl: "",
	licenseContentReportUrl: "",
	centralAdminURL: ""
};

if (!centralizationReportApp.hideUrl) {
    centralizationReportApp.urlObj.dashboardUrl = "/dashboard";
    centralizationReportApp.urlObj.reportUrl = "/report";
    centralizationReportApp.urlObj.centralAdminURL = "/centralAdmin";
    centralizationReportApp.urlObj.reportDetailUrl = "/{reportId:[0-9]{1,4}}";
    centralizationReportApp.urlObj.centralAdminDetailUrl = "/{centralAdminId:[0-9]{1,4}}";
    centralizationReportApp.urlObj.reportAddToDashboardUrl = "/addtodashboard/{reportId:[0-9]{1,4}}";
    centralizationReportApp.urlObj.reportDetailItemUrl = "/item/:itemId";
    centralizationReportApp.urlObj.exceptionReportDetailItemUrl = "/item/:itemId";//for exception report
    centralizationReportApp.urlObj.unclaimedOrderReportItemUrl = "/:custId";//for unclaimed order    
    centralizationReportApp.urlObj.omsUrl = "/oms";
    centralizationReportApp.urlObj.storeUrl = "/store";
    centralizationReportApp.urlObj.licenseContentReportUrl = "/licensereport";
}


var myApp = angular.module('walgreenReports',['ui.router' , 'ui.bootstrap'])
    .run(
        ['$rootScope', '$state', '$stateParams',
            function($rootScope, $state, $stateParams) {
                $rootScope.$state = $state;
                $rootScope.$stateParams = $stateParams;
                if(centralizationReportApp.hideUrl)
                    $state.go("dashboard");
            }
        ]
    )
    .config(['$stateProvider', '$urlRouterProvider',
        function($stateProvider, $urlRouterProvider) {
            $stateProvider
                .state("dashboard", {
                    url: centralizationReportApp.urlObj.dashboardUrl,
                    templateUrl: 'res/template/dashboard/dashboard.html',
                    controller: 'DashboardCtrl',
                    resolve: {
                        windowList: ['windowList',
                            function(windowList) {
                                return windowList.all();
                            }
                        ]
                    }
                })
                .state("storeview", {
                    url: centralizationReportApp.urlObj.storeUrl,
                    template: '<small class="muted">StoreView content will be placed under res/template/storeview.html</small>'
                })
                .state("oms", {
                    url: centralizationReportApp.urlObj.omsUrl,
                    template: '<small class="muted">OMS content will be placed under res/template/oms.html</small>'
                })
                .state("centralAdmin", {
                	url: centralizationReportApp.urlObj.centralAdminURL,
                    abstract: true,
                	templateUrl: 'res/template/centralAdmin/centralAdmin.html',
                    controller: 'centralAdminCtrl',
                    resolve: {
                    	adminlist: ['adminlist',
                            function(adminlist) {
                                return adminlist.all();
                            }
                        ]
                    }
                })
                .state('centralAdmin.default', {
                    url: ''/*,
                    template: 'Select a Report to show details'*/
                })
                .state('centralAdmin.detail', {
                    params: {
                    	centralAdminId: ''
                    },
                    url: centralizationReportApp.urlObj.centralAdminDetailUrl,
                    controller: 'centralAdminDetailCtrl',
                    template: '<div ui-view></div>'
                })
                .state('centralAdmin.retailBlock', {
                	params: {
                		centralAdminId: '',
                        simsBlockValue: ''
                    },
                    url: centralizationReportApp.urlObj.centralAdminDetailUrl,
                    controller: 'reportDetailCtrl',
                    template: '<div ui-view></div>'
                })
                .state('centralAdmin.retailBlock.item', {
                	params: {
                		centralAdminId  : '',
                        simsBlockValue  : '',
                        retailBlockArray: '',
                        prevRetailBlock : ''
                    },
                    templateUrl: 'res/template/centralAdmin/masterScreen/SIMSRetailBlock/retail.block.template.html',
                    controller: "retailBlockCntrl"
                })
                .state("report", {
                    url: centralizationReportApp.urlObj.reportUrl,
                    abstract: true,
                    resolve: {
                        reportlist: ['reportlist',
                            function(reportlist) {
                                return reportlist.all();
                            }
                        ],
                        reportIdMappinglist: ['reportIdMapping',
                            function(reportIdMapping) {
                                return reportIdMapping.all();
                            }
                        ]
                    },
                    templateUrl: 'res/template/report/report.html',
                    controller: 'reportMainCtrl'
                })
                .state('report.default', {
                    url: ''/*,
                    template: 'Select a Report to show details'*/
                })
                .state('report.detail', {
                    params: {
                        reportId: ''
                    },
                    url: centralizationReportApp.urlObj.reportDetailUrl,
                    controller: 'reportDetailCtrl',
                    template: '<div ui-view></div>'
                })
				.state('report.addtodashboard', {
                    params: {
                        reportId: ''
                    },
                    url: centralizationReportApp.urlObj.reportAddToDashboardUrl,
                    controller: 'reportAddToDashboarCtrl',
                    templateUrl: 'res/template/report/addto.dashboard.html'
                })
                .state('report.detail.item', {
                    url: centralizationReportApp.urlObj.reportDetailItemUrl,
                    params: {
                        reportId: '',
                        itemId: ''
                    },
                    templateUrl: 'res/template/report/detail.item.html',
                    controller: "reportItemCtrl"
                })
                .state('report.lookup', {//for print sign
                	params: {
                		reportId: ''
                    },
                    url: centralizationReportApp.urlObj.reportDetailUrl,
                    controller: 'reportDetailCtrl',
                    template: '<div ui-view></div>'
                })
                .state('report.lookup.data', {
                    //url: centralizationReportApp.urlObj.printSignLookupUrl,
                    params: {
                        reportId  : '',
                        lookupData: ''
                    },
                    templateUrl: 'res/template/report/detail.lookup.html',
                    controller: "lookupCtrl"
                })
                .state('report.plu', {
                	params: {
                        reportId: '',
                        pluObj: ''
                    },
                    url: centralizationReportApp.urlObj.reportDetailUrl,
                    controller: 'reportDetailCtrl',
                    template: '<div ui-view></div>'
                })
                .state('report.plu.item', {
                    //url: centralizationReportApp.urlObj.pluReportDetailItemUrl,
                    params: {
                        reportId: ''
                    },
                    templateUrl: 'res/template/report/plu/store.plu.template.html',
                    controller: "pluCtrl"
                })
                
            if (!centralizationReportApp.hideUrl)
                $urlRouterProvider.otherwise('/dashboard');
        }
    ])

 myApp.controller("TabsChildController", function($scope, $log) {
});
