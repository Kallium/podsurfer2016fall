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

		homeService.getTagsAndCounts(5)
			.then(function(response) {
				vm.tagsAndCounts = response.data;

			}, function(reason) {
				console.log(reason);
				vm.tagsAndCounts = [];
			}
		);

		homeService.getPodcastsAndReviewsAverages(5)
			.then(function(response) {
				var error = false;
				for (var podcastAndReviewsAverage in response.data) {
					if (error) {
						vm.podcastsAndReviewsAverages = [];
						break;
					}

					podcastAndReviewsAverage = response.data[podcastAndReviewsAverage];
					(function(id, average) {
						homeService.getOnePodcast(id)
							.then(function(response) {
								vm.podcastsAndReviewsAverages.push({ id: id, name: response.data.name, average: average });
							}, function(reason) {
								console.log(reason);
								error = true;
							}
						);
					})(podcastAndReviewsAverage.id, isNaN(podcastAndReviewsAverage.average) ? 'No Reviews' : podcastAndReviewsAverage.average.toFixed(0) + '/5');
				}

			}, function(reason) {
				console.log(reason);
				vm.podcastsAndReviewsAverages = [];
			}
		);

		homeService.getAllReviews(6)
			.then(function(response) {
				vm.reviews = response.data;

			}, function(reason) {
				console.log(reason);
				vm.reviews = [];
			}
		);
	}

})();
