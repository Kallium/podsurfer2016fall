(function() {
	'use strict';

	angular
		.module('app')
		.service('signInService', signInService);

	signInService.$inject = ['$http'];
	function signInService($http) {
		var exports = {
			login: login
		};

		return exports;

		function login(user) {
			return $http.post('/auth/local', user);
		}
	}
})();
