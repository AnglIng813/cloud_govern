'use strict';

/**
 * Config for the router
 */
angular.module('app')
	.run(
		['$rootScope', '$state', '$stateParams', '$ocLazyLoad',
			function($rootScope, $state, $stateParams, $ocLazyLoad) {
				$rootScope.$state = $state;
				$rootScope.$stateParams = $stateParams;
			}
		]
	)
	.config(
		['$stateProvider', '$urlRouterProvider', '$httpProvider',
			function($stateProvider, $urlRouterProvider, $httpProvider) {
			$httpProvider.interceptors.push('httpInterceptor');
			
			
				$urlRouterProvider
					.otherwise('/app/traffic-statistics');
				$stateProvider
					.state('app', {
						abstract: true,
						url: '/app',
						templateUrl: 'view/app.html'
					})
					.state('app.traffic-statistics', {
						url: '/traffic-statistics',
						templateUrl: 'view/traffic-statistics.html',
                        resolve: {
                            store: function($ocLazyLoad) {
                                return $ocLazyLoad.load({
                                    files: ["js/controller/traffic-statistics.js"]
                                })
                            }
                        }

					})	
					.state('app.container-statistics', {
						url: '/container-statistics',
						templateUrl: 'view/container-statistics.html',
                        resolve: {
                            store: function($ocLazyLoad) {
                                return $ocLazyLoad.load({
                                    files: ["js/controller/container-statistics.js"]
                                })
                            }
                        }

					})	
					
					.state('app.limit-set', {
						url: '/limit-set',
						templateUrl: 'view/limit-set.html',
                        resolve: {
                            store: function($ocLazyLoad) {
                                return $ocLazyLoad.load({
                                    files: ["js/controller/limit-set.js"]
                                })
                            }
                        }

					})
					
					.state('app.traffic-transfer', {
						url: '/traffic-transfer',
						templateUrl: 'view/traffic-transfer.html',
                        resolve: {
                            store: function($ocLazyLoad) {
                                return $ocLazyLoad.load({
                                    files: ["js/controller/traffic-transfer.js"]
                                })
                            }
                        }

					})	
			}
		]
	);