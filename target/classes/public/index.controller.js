(function() {
	'use strict';

	angular
		.module('app')
		.controller('indexController', indexController);

	indexController.$inject = ['$scope', '$cookies'];
	function indexController($scope, $cookies) {
		var vm = this;

		updateSignedIn();

		$scope.signOut = function() {
			$cookies.remove('token');
			updateSignedIn();
		}

		$scope.$on('$locationChangeStart', function(event, next, current) {
				updateSignedIn();
		});

		function updateSignedIn() {
			$scope.signedIn = $cookies.get('token') !== undefined;
		}
	}
})();
