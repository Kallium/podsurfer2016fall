(function() {
  'use strict';

    angular.module('app')
        .controller('accountController', accountController);

    accountController.$inject = ['$http', '$scope', '$sce', '$timeout', 'accountService'];
    function accountController($http, $scope, $sce, $timeout, accountService) {
      var vm = this;
      
      $scope.editing = false;
      $scope.canSave = true;
      
      $scope.tab = 1;
      
      accountService.getPodcasts()
      .then(function(response) {
    	  vm.podcasts = response.data;
    	  console.log(vm.podcasts);
    	  accountService.getAccountInfo()
          .then(function(response) {
        	  vm.accountInfo = response.data;
        	  if(vm.accountInfo.interests == null) {
        		  vm.accountInfo.interests = [];
        	  }
        	  if(vm.accountInfo.bookmarks == null) {
        		  vm.accountInfo.bookmarks = [];
        	  }
        	  vm.accountBookmarks = [];
        	  console.log(vm.accountInfo.bookmarks);
        	  console.log(vm.podcasts);
        	  for(var i in vm.accountInfo.bookmarks) {
        		  console.log("Bookmark: " + vm.accountInfo.bookmarks[i]);
        		  for(var j in vm.podcasts) {
        			  console.log("Checking podcast: " + vm.podcasts[j].name + " with id: " + vm.podcasts[j]._id);
        			  if(vm.podcasts[j]._id == vm.accountInfo.bookmarks[i]) {
        				  console.log("Pushing " + vm.podcasts[j].name);
        				  vm.podcasts[j].release = new Date(vm.podcasts[j].release).toLocaleDateString();
        				  vm.accountBookmarks.push(vm.podcasts[j]);
        			  }
        		  }
        	  }
        	  
        	  console.log(response.data);
        	  console.log(vm.accountBookmarks);
          }, function(reason) {
        	  console.log('The call to accountInfo failed');
        	  console.log(reason)
        	  vm.accountInfo = {};
          });
    	  
    	  accountService.getMyReviews()
          .then(function(response) {
        	  vm.reviews = response.data;
        	  for(var i in vm.reviews) {
        		  (function(i) {
	        		  accountService.getPodcastById(vm.reviews[i].podcast)
	        		  .then(function(response) {
	        			  vm.reviews[i].podcast = response.data;
	        			  vm.reviews[i].podcast.release = new Date(vm.reviews[i].podcast.release).toLocaleDateString();
	        		  }, function(reason){
	        			  console.log("The call to getPodcastById failed");
	        			  console.log(reason);
	        		  });
        		  })(i);
        	  }
        	  console.log(vm.reviews);
          }, function(reason) {
        	  console.log("The call to getMyReviews failed");
        	  console.log(reason);
          });
    	  
      }, function(reason) {
    	  console.log('The call to podcasts failed');
    	  vm.podcasts = [];
      });
      
      
      
      accountService.getRecommendations()
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
      
      $scope.setTab = function(newTab) {
    	  $scope.tab = newTab;
      };
      
      $scope.isSet = function(tabNum) {
    	  return $scope.tab === tabNum;
      };
      
      $scope.fixInterestFocus = function() {
			vm.addingField = true;
			$timeout(function() {
				var field = document.getElementById("interest" + vm.accountInfo.interests.length);
				field.focus();
				field.setSelectionRange(field.value.length, field.value.length);
				vm.addingField = false;
			});
      }
      
      $scope.deleteBookmark = function($index) {
    	  vm.accountInfo.bookmarks.splice(vm.accountInfo.bookmarks.indexOf(vm.accountBookmarks[$index]._id), 1);
    	  vm.accountBookmarks.splice($index, 1);
      }
      
      $scope.fixBookmarkFocus = function() {
			vm.addingField = true;
			$timeout(function() {
				var field = document.getElementById("bookmark" + vm.accountBookmarks.length);
				field.focus();
				field.setSelectionRange(field.value.length, field.value.length);
				vm.addingField = false;
			});
    }
    
    $scope.deleteInterest = function($index) {
  	  vm.accountInfo.interests.splice($index, 1);
    }
    
    $scope.checkBookmarkName = function($index) {
    	  var found = false;
    	  var field = document.getElementById("bookmarkErrorMessage" + $index);
	  	  //only add valid bookmarks
	  	  for(var i in vm.podcasts) {
	  		  if(vm.podcasts[i].name == vm.accountBookmarks[$index].name) {
	  			  found = true;
	  			  //don't add duplicate bookmarks
	  			  if(vm.accountBookmarks.indexOf(vm.podcasts[i]) < 0) {
	  				  field.innerHTML = "";
	  				  $scope.canSave = true;
	  			  }
	  			  else {
	  				  field.innerHTML = "This bookmark already exists";
	  				  $scope.canSave = false;
	  			  }
	  		  }
	  	  }
	  	  if(!found) {
	  		  field.innerHTML = "Podcast not found";
	  		  $scope.canSave = false;
	  	  }
    }
    
    $scope.checkInterest = function($index) {
    	var field = document.getElementById("interestErrorMessage" + $index);
    	if(vm.accountInfo.interests.indexOf(vm.accountInfo.interests[$index]) != $index) {
    		field.innerHTML = "This is already an interest";
    		$scope.canSave = false;
    	}
    	else {
    		field.innerHTML = "";
    		$scope.canSave = true;
	  	}
    }
    
    $scope.saveChanges = function() {
    	
    	//make sure accountBookmarks lines up with bookmark id's
    	vm.accountInfo.bookmarks = [];
    	for(var i in vm.accountBookmarks) {
    		console.log(vm.accountBookmarks[i].name);
    		for(var j in vm.podcasts) {
    			if(vm.accountBookmarks[i].name == vm.podcasts[j].name) {
    				vm.accountBookmarks[i] = vm.podcasts[j];
    				vm.accountInfo.bookmarks[i] = vm.accountBookmarks[i]._id;
    				console.log(vm.accountInfo.bookmarks[i]);
    			}
    		}
    	}
    	
    	var data = aggregateData();
    	console.log(data);
		accountService.updateAccountInfo(data)
		.then(function(response) {
			vm.accountInfo = response.data;
			console.log(response.data);
		}, function(reason) {
			console.log('The call to updateAccountInfo failed');
			vm.accountInfo = {};
		});
		$scope.editing = false;
    }
      
      function aggregateData() {
    	  var data = {};
		  data.interests = vm.accountInfo.interests;
		  data.name = vm.accountInfo.name;
		  data.bookmarks = vm.accountInfo.bookmarks;
		  return data;
      }
      
    }
})();
