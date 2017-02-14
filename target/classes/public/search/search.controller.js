(function() {
  'use strict';

    angular.module('app')
        .controller('searchController', searchController);

    searchController.$inject = ['$scope', '$location', 'searchService'];
    function searchController($scope, $location, searchService) {
      var vm = this;
      vm.nameSorted = false;
      vm.producerSorted = false;
      vm.avgSorted = false;
      vm.descriptionSorted = false;
      vm.podcastsAndReviewsAverages = [];
      
      $scope.relevantPodcasts = [];
      
      searchService.getAllPodcasts()
      	.then(function(response) {
      		var error = false;
      		var count = response.data.length;
      		for (var podcastAndReviewsAverage in response.data) {
      			podcastAndReviewsAverage = response.data[podcastAndReviewsAverage];
      		
      			(function(podcast) {
      				searchService.getOnePodcastReviewsAverage(podcast._id)
      				.then(function(response) {
      					if (!error) {
      						//podcast.average = response.data.average;
      						podcast.id = podcast._id;
      						podcast.average = isNaN(response.data.average) ? 'No Reviews' : response.data.average + '/5';
      						vm.podcastsAndReviewsAverages.push(podcast);
      						//vm.podcastsAndReviewsAverages.push({ name: response.data.name, average: average, description: response.data.description, id: response.data._id, producer: response.data.producer, tag: response.data.tags });
      						
      						count--;
      						if (count == 0) {
      							if($location.search().type == "tag") {
      								$scope.searchTag($location.search().q);
      							}
      						}
      					} else {
      						vm.podcastsAndReviewsAverages = [];
      					}
      				}, function(reason) {
      					console.log(reason);
						error = true;
						vm.podcastsAndReviewsAverages = [];
      				});
      			})(podcastAndReviewsAverage);
      		}
      		
      		$scope.relevantPodcasts = vm.podcastsAndReviewsAverages;
      		
      	}, function(reason) {
      		console.log(reason);
			vm.podcastsAndReviewsAverages = [];
      	});
      
      $scope.searchName = function(searchItem) {
    	  if(searchItem != null && searchItem != ''){
        	  $scope.relevantPodcasts = [];
        	  
        	  for(var i = 0; i < vm.podcastsAndReviewsAverages.length; i++) {
        		  if(vm.podcastsAndReviewsAverages[i].name.toLowerCase().indexOf(searchItem.toLowerCase()) >= 0) {
        			  console.log(vm.podcastsAndReviewsAverages[i].name);
        			  $scope.relevantPodcasts.push(vm.podcastsAndReviewsAverages[i]);
        		  }
        	  }
    	  }else{
    		  $scope.relevantPodcasts = vm.podcastsAndReviewsAverages;
    	  }
    	  
    	  vm.nameSorted = false;
    	  vm.producerSorted = false;
    	  vm.avgSorted = false;
    	  vm.descriptionSorted = false;
      }
      
      $scope.searchDescription = function(searchItem) {
    	  if(searchItem != null && searchItem != ''){
        	  $scope.relevantPodcasts = [];
        	  
        	  for(var i = 0; i < vm.podcastsAndReviewsAverages.length; i++) {
        		  if(vm.podcastsAndReviewsAverages[i].description.toLowerCase().indexOf(searchItem.toLowerCase()) >= 0) {
        			  console.log(vm.podcastsAndReviewsAverages[i].description);
        			  $scope.relevantPodcasts.push(vm.podcastsAndReviewsAverages[i]);
        		  }
        	  }
    	  }else{
    		  $scope.relevantPodcasts = vm.podcastsAndReviewsAverages;
    	  }
    	  
    	  vm.nameSorted = false;
    	  vm.producerSorted = false;
    	  vm.avgSorted = false;
    	  vm.descriptionSorted = false;
      }
      
      $scope.searchProducer = function(searchItem) {
    	  if(searchItem != null && searchItem != ''){
        	  $scope.relevantPodcasts = [];
        	  
        	  for(var i = 0; i < vm.podcastsAndReviewsAverages.length; i++) {
        		  if(vm.podcastsAndReviewsAverages[i].producer && vm.podcastsAndReviewsAverages[i].producer.toLowerCase().indexOf(searchItem.toLowerCase()) >= 0) {
        			  console.log(vm.podcastsAndReviewsAverages[i].producer);
        			  $scope.relevantPodcasts.push(vm.podcastsAndReviewsAverages[i]);
        		  }
        	  }
    	  }else{
    		  $scope.relevantPodcasts = vm.podcastsAndReviewsAverages;
    	  }
    	  
    	  vm.nameSorted = false;
    	  vm.producerSorted = false;
    	  vm.avgSorted = false;
    	  vm.descriptionSorted = false;
      }
      
      $scope.searchTag = function(searchItem){
    	  if(searchItem != null && searchItem != ''){
        	  $scope.relevantPodcasts = [];
        	  
        	  for(var i = 0; i < vm.podcastsAndReviewsAverages.length; i++) {
        		  for(var x = 0; vm.podcastsAndReviewsAverages[i].tags && x < vm.podcastsAndReviewsAverages[i].tags.length; x++){
        			  if(vm.podcastsAndReviewsAverages[i].tags[x] && vm.podcastsAndReviewsAverages[i].tags[x].toLowerCase().indexOf(searchItem.toLowerCase()) >= 0) {
            			  console.log(vm.podcastsAndReviewsAverages[i].producer);
            			  $scope.relevantPodcasts.push(vm.podcastsAndReviewsAverages[i]);
            			  x = vm.podcastsAndReviewsAverages[i].tags.length;
            		  }
        		  }
        	  }
    	  }else{
    		  $scope.relevantPodcasts = vm.podcastsAndReviewsAverages;
    	  }
    	  
    	  vm.nameSorted = false;
    	  vm.producerSorted = false;
    	  vm.avgSorted = false;
    	  vm.descriptionSorted = false;
      }
      
      $scope.formatNumber = function(i) {
    	  if(i == 'No Reviews'){
    		  return 'No Reviews';
    	  }else{
    		i = parseFloat(i);
    	    return Math.round(i * 100)/100 + '/5'; 
    	  }
      }
      
      $scope.sortName = function(){
    	  if(vm.nameSorted){
    		  //REVERSE LIST
    		  $scope.relevantPodcasts.reverse();
    	  }else{
    		  vm.nameSorted = true;
    		  var swapped;
    		  do{
    			  swapped = false;
    			  for(var i = 0; i < $scope.relevantPodcasts.length - 1; i++){
    				  if($scope.relevantPodcasts[i].name.toLowerCase() > $scope.relevantPodcasts[i+1].name.toLowerCase()){
    					  var temp = $scope.relevantPodcasts[i];
    					  $scope.relevantPodcasts[i] = $scope.relevantPodcasts[i + 1];
    					  $scope.relevantPodcasts[i+1] = temp;
    					  swapped = true;
    				  }
    			  }
    		  }while(swapped)
    	  }
    	  
    	  vm.producerSorted = false;
    	  vm.avgSorted = false;
    	  vm.descriptionSorted = false;
      }
      
      $scope.sortProducer = function(){
    	  if(vm.producerSorted){
    		  //REVERSE LIST
    		  $scope.relevantPodcasts.reverse();
    	  }else{
    		  vm.producerSorted = true;
    		  var swapped;
    		  do{
    			  swapped = false;
    			  for(var i = 0; i < $scope.relevantPodcasts.length - 1; i++){
    				  if($scope.relevantPodcasts[i].producer.toLowerCase() > $scope.relevantPodcasts[i+1].producer.toLowerCase()){
    					  var temp = $scope.relevantPodcasts[i];
    					  $scope.relevantPodcasts[i] = $scope.relevantPodcasts[i + 1];
    					  $scope.relevantPodcasts[i+1] = temp;
    					  swapped = true;
    				  }
    			  }
    		  }while(swapped)
    	  }
    	  
    	  vm.nameSorted = false;
    	  vm.avgSorted = false;
    	  vm.descriptionSorted = false;
      }
      
      $scope.sortAverage = function(){
    	  if(vm.avgSorted){
    		  //REVERSE LIST
    		  $scope.relevantPodcasts.reverse();
    	  }else{
    		  vm.avgSorted = true;
    		  var swapped;
    		  do{
    			  swapped = false;
    			  for(var i = 0; i < $scope.relevantPodcasts.length - 1; i++){
    				  if($scope.relevantPodcasts[i].average != 'No Reviews'){
    					  if($scope.relevantPodcasts[i+1].average == 'No Reviews'){
            				  var temp = $scope.relevantPodcasts[i];
            				  $scope.relevantPodcasts[i] = $scope.relevantPodcasts[i + 1];
            				  $scope.relevantPodcasts[i+1] = temp;
            				  swapped = true;
    					  }else{
    						  if(parseFloat($scope.relevantPodcasts[i].average) > parseFloat($scope.relevantPodcasts[i+1].average)){
            					  var temp = $scope.relevantPodcasts[i];
            					  $scope.relevantPodcasts[i] = $scope.relevantPodcasts[i + 1];
            					  $scope.relevantPodcasts[i+1] = temp;
            					  swapped = true;
            				  }
    					  }
    				  }
    			  }
    		  }while(swapped)
    			  
    		  $scope.relevantPodcasts.reverse();
    	  }
    	  
    	  vm.nameSorted = false;
    	  vm.producerSorted = false;
    	  vm.descriptionSorted = false;
      }
      
      $scope.sortDescription = function(){
    	  if(vm.producerSorted){
    		  //REVERSE LIST
    		  $scope.relevantPodcasts.reverse();
    	  }else{
    		  vm.descriptionSorted = true;
    		  var swapped;
    		  do{
    			  swapped = false;
    			  for(var i = 0; i < $scope.relevantPodcasts.length - 1; i++){
    				  if($scope.relevantPodcasts[i].description.toLowerCase() > $scope.relevantPodcasts[i+1].description.toLowerCase()){
    					  var temp = $scope.relevantPodcasts[i];
    					  $scope.relevantPodcasts[i] = $scope.relevantPodcasts[i + 1];
    					  $scope.relevantPodcasts[i+1] = temp;
    					  swapped = true;
    				  }
    			  }
    		  }while(swapped)
    	  }
    	  
    	  vm.nameSorted = false;
    	  vm.producerSorted = false;
    	  vm.avgSorted = false;
      }
      
    }
})();
