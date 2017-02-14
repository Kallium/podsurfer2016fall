(function() {
	'use strict';

	angular
		.module('app')
		.controller('homeController', homeController);

	homeController.$inject = ['homeService'];
	function homeController(homeService) {
		var vm = this;

		vm.tagsAndCounts = [];
		vm.podcastsAndReviewsAverages = [];
		vm.reviews = [];
		vm.recommendations = [];

		homeService.getTagsAndCounts(5)
			.then(function(response) {
				vm.tagsAndCounts = response.data;

			}, function(reason) {
				console.log(reason);
				vm.tagsAndCounts = [];
			}
		);
		
		homeService.getRecommendations()
			.then(function(response) {
				vm.recommendations = response.data;
				console.log(vm.recommendations);
			}, function(reason) {
				console.log(reason);
				vm.recommendations = [];
			}
		);

		homeService.getAllReviews(5)
			.then(function(response) {
				vm.reviews = response.data;

			}, function(reason) {
				console.log(reason);
				vm.reviews = [];
			}
		);
	}

})();
