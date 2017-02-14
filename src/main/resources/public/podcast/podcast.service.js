(function() {
	'use strict';

	angular
		.module('app')
		.service('podcastService', podcastService);

	podcastService.$inject = ['$http', '$location', '$cookies'];
	function podcastService($http, $location, $cookies) {

		var exports = {
			getPodcast: getPodcast,
			getReviews: getReviews,
			getMyProfileInfo: getMyProfileInfo,
			updateMyProfileInfo: updateMyProfileInfo,
			createNewReview: createNewReview,
			updateExistingReview: updateExistingReview,
			deleteReview: deleteReview
		};

		function getPodcast() {
			return $http.get('/api/podcast/' + $location.search().id);
		}

		function getReviews() {
			return $http.get('/api/review/' + $location.search().id);
		}

		function getMyProfileInfo() {
			return $http.get('/api/user/me', {
				headers: {'Authorization': "Bearer " + $cookies.get("token")}
			});
		}

		function updateMyProfileInfo(user) {
			return $http.put('/api/user', user, {
				headers: {'Authorization': "Bearer " + $cookies.get("token")}
			});
		}

		function createNewReview(review) {
			return $http.post('/api/review', review, {
				headers: {'Authorization': "Bearer " + $cookies.get("token")}
			});
		}

		function updateExistingReview(id, review) {
			return $http.put('/api/review/' + id, review, {
				headers: {'Authorization': "Bearer " + $cookies.get("token")}
			});
		}

		function deleteReview(id) {
			return $http.delete('/api/review/' + id, {
				headers: {'Authorization': "Bearer " + $cookies.get("token")}
			});
		}

		return exports;
	}
})();
