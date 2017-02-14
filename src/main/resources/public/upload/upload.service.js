(function() {
	'use strict';

	angular
		.module('app')
		.service('uploadService', uploadService);

	uploadService.$inject = ['$http', '$cookies'];
	function uploadService($http, $cookies) {

		var config = {
			headers: {
				'Authorization': 'Bearer ' + $cookies.get('token')
			}
		};
		
		var exports = {
			getPodcast: getPodcast,
			createNewPodcast: createNewPodcast,
			updatePodcast: updatePodcast
		};

		function getPodcast(id) {
			return $http.get('/api/podcast/' + id);
		}
		
		function createNewPodcast(podcast) {
			return $http.post('/api/podcast/', podcast, config);
		}
      
		function updatePodcast(id, podcast) {
			return $http.put('/api/podcast/' + id, podcast, config);
		}

		return exports;
	}
})();
