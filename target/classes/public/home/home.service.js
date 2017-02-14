(function() {
	'use strict';

	angular
		.module('app')
		.service('homeService', homeService);

	homeService.$inject = ['$http'];
	function homeService($http) {
		var exports = {
			getTagsAndCounts: getTagsAndCounts,
			getOnePodcast: getOnePodcast,
			getPodcastsAndReviewsAverages: getPodcastsAndReviewsAverages,
			getAllReviews: getAllReviews
		};

		return exports;

		function getTagsAndCounts(max) {
			return $http.get('/api/tag?max=' + (max ? max : ''));
		}

		function getOnePodcast(id) {
			return $http.get('/api/podcast/' + id);
		}

		function getPodcastsAndReviewsAverages(max) {
			return $http.get('/api/review/all/average?max=' + (max ? max : ''));
		}

		function getAllReviews(max) {
			return $http.get('/api/review?max=' + (max ? max : ''));
		}
	}

})();
