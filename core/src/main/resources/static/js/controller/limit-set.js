angular.module('app').controller("limit-set", function($scope, $http, $state, $rootScope, $timeout) {
  $scope.getP = 1;
  $scope.newP = 1;

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