angular.module('app').controller("container-statistics", function($scope, $http, $state, $rootScope, $timeout) {
	// 配置分页基本参数
	$scope.paginationConf = {
		currentPage: sessionStorage.currentPage || 1,
		totalItems: 8000,
		itemsPerPage: 10,
		pagesLength: 5, //页码
		perPageOptions: [10, 25, 50, 100],
		onChange: function() {}
	};
})