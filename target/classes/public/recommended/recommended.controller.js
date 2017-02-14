(function() {
  'use strict';

    angular.module('app')
        .controller('recommendedController', recommendedController);

    recommendedController.$inject = ['$http', 'recommendedService'];
    function recommendedController($http, recommendedService) {
      var vm = this;


      recommendedService.getMentors()
      .then(function(response) {
        vm.mentors = response.data;
      }, function(reason) {
        console.log('The call to /mentors failed');
        vm.mentors = [];
      });

      
    }
})();
