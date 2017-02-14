(function() {
  'use strict';

    angular.module('app')
        .controller('contactController', contactController);

    contactController.$inject = ['$http', 'contactService'];
    function contactController($http, contactService) {
      var vm = this;


      contactService.getMentors()
      .then(function(response) {
        vm.mentors = response.data;
      }, function(reason) {
        console.log('The call to /mentors failed');
        vm.mentors = [];
      });

      
    }
})();
