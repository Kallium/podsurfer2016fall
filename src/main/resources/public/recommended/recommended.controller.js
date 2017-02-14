(function() {
  'use strict';

    angular.module('app')
        .controller('recommendedController', recommendedController);

    recommendedController.$inject = ['recommendedService'];
    function recommendedController(recommendedService) {
      var vm = this;

      recommendedService.getRecommendations()
		.then(function(response) {
			vm.recommendations = response.data;
			console.log(vm.recommendations);
			for(var i in vm.recommendations) {
				vm.recommendations[i].podcast.release = new Date(vm.recommendations[i].podcast.release).toLocaleDateString();
			}
		}, function(reason) {
			console.log("The call to getRecommendations failed");
				console.log(reason);
		});
      

      
    }
})();
