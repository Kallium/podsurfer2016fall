(function() {
  'use strict';

    angular.module('app')
      .controller('aboutController', aboutController);

    aboutController.$inject = [];
    function aboutController() {
      var vm = this;
      vm.message = 'About the Project';
    }
})();
