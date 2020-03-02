var app = angular.module('app');

app.controller("mycontroller", function($scope, $http, $state, $rootScope, $stateParams, $timeout, $interval) {
	/*数据交互请求头*/
	$rootScope.postCfg = {
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'
		},
		transformRequest: function(data) {
			return $.param(data);
		}
	};
	/*数据交互请求头*/
	$rootScope.postCfgJson = {
		headers: {
			'Content-Type': 'application/json;charset=utf-8'
		}
	};

	//侧边栏点击状态
	$rootScope.liActive = sessionStorage.active || 1
	$rootScope.active = function(a) {
		$rootScope.liActive = a
		sessionStorage.active = a
	}

	//页面切换动作
	$rootScope.startPage = function() {
		setTimeout(function() {
			$("#tablelist").addClass("animated fadeInLeft")
			var a = $(".left-side-menu").width()
			if(a == 180) {
				$("#tablelist").css({
					"padding-left": 200 + "px"
				})
				$("#footer p").css({
					"margin-left": "240px"
				})
			} else if(a == 60) {
				$("#tablelist").css({
					"padding-left": 70 + "px"
				})
				$("#footer p").css({
					"margin-left": "100px"
				})
			}
		}, 50)
		setTimeout(function() {
			$("#tablelist").removeClass("animated fadeInLeft")
		}, 1000)
	}

	//路由切换
	$rootScope.$on('$stateChangeSuccess', function() {
		$rootScope.startPage()
	})
});

//拦截器
app.factory('httpInterceptor', ['$q', '$rootScope', function($q, $rootScope) {
	var httpInterceptor = {
		'responseError': function(response) {
			if(response.status === 401) {

			} else if(response.status === 403) {

			} else if(response.status === 303) {

			} else if(response.status === 404) {
				$rootScope.uiAlert.alertFail("失败", "错误原因404,错误接口:" + response.config.url);
			} else if(response.status === 500) {
				$rootScope.uiAlert.alertFail("失败", "错误原因500,错误接口:" + response.config.url);
			}
			return $q.reject(response);
		},
		'response': function(response) { //响应拦截
			//这里可以对所有的响应的进行处理
			return response;
		},
		'request': function(config) { //请求拦截
			//这里可以对所有的请求进行处理
			return config;
		},
		'requestError': function(config) { //请求错误拦截
			//这里可以对所有的请求错误进行处理
			return $q.reject(config);
		}
	}
	return httpInterceptor;
}]);