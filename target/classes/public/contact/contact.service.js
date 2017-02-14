(function() {
  'use strict';

    angular.module('app')
        .service('contactService', contactService);

    contactService.$inject = ['$http'];
    function contactService($http) {

      var exports = {
        getMentors: getMentors
      };

      function getMentors() {
        return $http.get('/mentors');
      }

      return exports;
    }
})();
