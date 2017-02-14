(function() {
  'use strict';

    angular.module('app')
        .service('accountService', accountService);

    accountService.$inject = ['$http', '$cookies'];
    function accountService($http, $cookies) {

      var exports = {
        getPodcasts: getPodcasts,
        getAccountInfo: getAccountInfo,
        updateAccountInfo: updateAccountInfo,
        getPodcastById: getPodcastById,
        getMyReviews: getMyReviews,
        getRecommendations: getRecommendations
      };

      var token = "Bearer " + $cookies.get("token");
      
      function getPodcasts() {
        return $http.get('/api/podcast');
      }
      
      function getAccountInfo() {
    	  return $http.get('/api/user/me', {
    		  headers: {'Authorization': token}
    	  });
      }
      
      function updateAccountInfo(data) {
    	  return $http.put('/api/user', data, {
    		  headers: {'Authorization': token}
    	  });
      }
      
      function getPodcastById(id) {
    	  return $http.get('/api/podcast/'+id);
      }
      
      function getMyReviews() {
    	  return $http.get('/api/review/mine', {
    		  headers: {"Authorization": token}
    	  });
      }

      function getRecommendations() {
    	  return $http.get('/api/recommendation', {
    		  headers: {"Authorization": token}
    	  });
      }
      
      return exports;
    }
})();
