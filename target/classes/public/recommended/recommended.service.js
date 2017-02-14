(function() {
  'use strict';

    angular.module('app')
        .service('recommendedService', recommendedService);

    recommendedService.$inject = ['$http'];
    function recommendedService($http) {

      var exports = {
        getMentors: getMentors
      };

      function getMentors() {
        return $http.get('/mentors');
      }

      return exports;
    }
})();
