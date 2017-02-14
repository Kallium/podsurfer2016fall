(function() {
	'use strict';

	angular
		.module('app')
		.service('homeService', homeService);

	homeService.$inject = ['$http', '$cookies'];
	function homeService($http, $cookies) {
		var exports = {
			getTagsAndCounts: getTagsAndCounts,
			getOnePodcast: getOnePodcast,
			getPodcastsAndReviewsAverages: getPodcastsAndReviewsAverages,
			getAllReviews: getAllReviews,
			getRecommendations: getRecommendations
		};

		

		var token = "Bearer " + $cookies.get("token");
		
		console.log(token);
		
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
		
		function getRecommendations() {
			if ($cookies.get("token") !== undefined) {
				return $http.get('/api/recommendation', {
					headers: {"Authorization": token}
				});
			} else {
				return $http.get('/api/podcast?max=5');
			}
		}
		
		return exports;
		
	}

})();
