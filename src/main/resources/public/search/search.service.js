(function() {
  'use strict';

    angular.module('app')
        .service('searchService', searchService);

    searchService.$inject = ['$http'];
    function searchService($http) {
      var exports = {
			/*getOnePodcast: getOnePodcast,
			getPodcastsAndReviewsAverages: getPodcastsAndReviewsAverages*/
    		getAllPodcasts: getAllPodcasts,
    		getOnePodcastReviewsAverage: getOnePodcastReviewsAverage,
      };
      
      function getAllPodcasts() {
    	  return $http.get('/api/podcast');
      }
      
      function getOnePodcastReviewsAverage(id) {
    	  return $http.get('/api/review/' + id + '/average');
      }
      
	  /*function getOnePodcast(id) {
		  return $http.get('/api/podcast/' + id);
	  }

	  function getPodcastsAndReviewsAverages(max) {
		  return $http.get('/api/review/all/average?max=' + (max ? max : ''));
	  }*/

      return exports;
    }
})();
