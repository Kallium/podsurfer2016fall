(function() {
	'use strict';

	angular
		.module('app')
		.service('joinService', joinService);

	joinService.$inject = ['$http'];
	function joinService($http) {
		var exports = {
			createNewUser: createNewUser
		};

		return exports;

		function createNewUser(user) {
			return $http.post('/api/user', user);
		}
	}
})();
