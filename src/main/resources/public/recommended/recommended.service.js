(function() {
  'use strict';

    angular.module('app')
        .service('recommendedService', recommendedService);

    recommendedService.$inject = ['$http', '$cookies'];
    function recommendedService($http, $cookies) {

      var exports = {
    	  getRecommendations: getRecommendations
      };

      var token = "Bearer " + $cookies.get("token");
      
      function getRecommendations() {
    	  return $http.get('/api/recommendation', {
    		  headers: {"Authorization": token}
    	  });
      }

      return exports;
    }
})();
