(function() {
	'use strict';

	angular
		.module('app')
		.controller('joinController', joinController);

	joinController.$inject = ['$scope', '$location', '$cookies', 'joinService'];
	function joinController($scope, $location, $cookies, joinService) {
		var vm = this;

		vm.invalidName = false;
		vm.invalidEmail = false;
		vm.invalidPassword = false;

		vm.submitting = false;

		vm.joinError = false;
		vm.joinErrorMessage = '';

		$scope.submit = function(user) {
			vm.joinError = false;
			vm.joinErrorMessage = '';

			if (!validateUser(user)) {
				return;
			}

			vm.submitting = true;
			joinService.createNewUser(user)
				.then(function(response) {
					console.log(response.data);
					$cookies.put('token', response.data.token);
					console.log('Bearer ' + $cookies.get('token'));
					$location.path('/home');
					vm.submitting = false;
				}, function(reason) {
					vm.joinErrorMessage = 'Error: ' + reason.data.message;
					vm.joinError = true;
					console.log(reason);
					vm.submitting = false;
				}
			);
		}

		function validateUser(user) {
			if (user === undefined) {
				vm.invalidName = true;
				vm.invalidEmail = true;
				vm.invalidPassword = true;
			} else {
				vm.invalidName = user.name === undefined || user.name === "";
				vm.invalidEmail = user.email === undefined || user.email === "";
				vm.invalidPassword = user.password === undefined || user.password === "";
			}

			return !(vm.invalidName || vm.invalidEmail || vm.invalidPassword);
		}
	}
})();
