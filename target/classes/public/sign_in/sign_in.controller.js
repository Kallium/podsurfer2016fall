(function() {
	'use strict';

	angular
		.module('app')
		.controller('signInController', signInController);

	signInController.$inject = ['$scope', '$location', '$cookies', 'signInService'];
	function signInController($scope, $location, $cookies, signInService) {
		var vm = this;

		vm.invalidEmail = false;
		vm.invalidPassword = false;

		vm.submitting = false;

		vm.logInError = false;
		vm.logInErrorMessage = '';

		$scope.submit = function(user) {
			vm.logInError = false;
			vm.logInErrorMessage = '';

			if (validateUser(user)) {
				vm.submitting = true;
				signInService.login(user)
					.then(function(response) {
						console.log(response.data);
						//                                               (1000ms/s * 60s/min * 60min/hr * 5hr) - 60,000ms (one minute)
						$cookies.put('token', response.data.token, { 'expires': new Date(Date.now() + 17940000) });
						console.log('Bearer ' + $cookies.get('token'));
						$location.path('/home');
						vm.submitting = false;
					}, function(reason) {
						vm.logInErrorMessage = 'Error: ' + reason.data.message;
						vm.logInError = true;
						console.log(reason);
						vm.submitting = false;
					}
				);
			}
		}

		function validateUser(user) {
			if (user === undefined) {
				vm.invalidEmail = true;
				vm.invalidPassword = true;
			} else {
				vm.invalidEmail = user.email === undefined || user.email === "";
				vm.invalidPassword = user.password === undefined || user.password === "";
			}

			return !(vm.invalidEmail || vm.invalidPassword);
		}
	}
})();
