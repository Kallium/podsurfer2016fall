(function() {
	'use strict';

	angular
		.module('app')
		.controller('uploadController', uploadController);

	uploadController.$inject = ['$scope', '$location', '$timeout', 'uploadService'];
	function uploadController($scope, $location, $timeout, uploadService) {
		var vm = this;

		vm.editing = $location.search().id !== undefined;
		$scope.submitButtonText = vm.editing ? "Update" : "Upload";

		vm.invalidName = false;
		vm.invalidLink = false;
		vm.invalidRelease = false;
		vm.invalidProducer = false;
		vm.invalidLength = false;
		vm.invalidDescription = false;
		vm.invalidTags = false;
		vm.invalidImageUrl = false;

		vm.submitting = false;

		vm.addingEpisode = false;
		vm.addingTag = false;

		vm.uploadError = false;
		vm.uploadErrorMessage = '';

		$scope.thisPodcast = { episodes: [], tags: [] };
		
		$scope.fixEpisodeFocus = function(field) {
			vm.addingEpisode = true;
			$timeout(function() {
				var episode = document.getElementById("episode" + $scope.thisPodcast.episodes.length + field);
				episode.focus();
				episode.setSelectionRange(episode.value.length, episode.value.length);
				vm.addingEpisode = false;
			});
		}
		
		$scope.deleteEpisode = function($index) {
			$scope.thisPodcast.episodes.splice($index, 1);
		}
		
		$scope.fixTagFocus = function() {
			vm.addingTag = true;
			$timeout(function() {
				var tag = document.getElementById("tag" + $scope.thisPodcast.tags.length);
				tag.focus();
				tag.setSelectionRange(tag.value.length, tag.value.length);
				vm.addingTag = false;
			});
		}
		
		$scope.deleteTag = function($index) {
			$scope.thisPodcast.tags.splice($index, 1);
		}
		
		$scope.submit = function(podcast) {
			vm.uploadError = false;
			vm.uploadErrorMessage = '';

			if (!validatePodcast(podcast)) {
				return;
			}

			vm.submitting = true;
			if (vm.editing) {
				uploadService.updatePodcast($location.search().id, podcast)
					.then(function(response) {
						console.log(response.data);
						$location.path('/podcast').search('id', response.data._id);
						vm.submitting = false;
					}, function(reason) {
						vm.uploadErrorMessage = 'Error: ' + reason.data.message;
						vm.uploadError = true;
						console.log(reason);
						vm.submitting = false;
					}
				);
			} else {
				uploadService.createNewPodcast(podcast)
					.then(function(response) {
						console.log(response.data);
						$location.path('/podcast').search('id', response.data._id);
						vm.submitting = false;
					}, function(reason) {
						vm.uploadErrorMessage = 'Error: ' + reason.data.message;
						vm.uploadError = true;
						console.log(reason);
						vm.submitting = false;
					}
				);
			}
		};

		function validatePodcast(podcast) {
			vm.invalidName = podcast.name === undefined || podcast.name === "";
			vm.invalidLink = podcast.link === undefined || podcast.link === "";
			vm.invalidRelease = podcast.release === undefined || podcast.release === "";
			vm.invalidProducer = podcast.producer === undefined || podcast.producer === "";
			vm.invalidLength = podcast.length === undefined || podcast.length === "";
			vm.invalidDescription = podcast.description === undefined || podcast.description === "";
			vm.invalidTags = podcast.tags.length === 0;
			vm.invalidImageUrl = podcast.imageURL === undefined || podcast.imageURL === "";

			return !(vm.invalidName || vm.invalidLink || vm.invalidRelease || vm.invalidProducer
				|| vm.invalidLength || vm.invalidDescription || vm.invalidTags || vm.invalidImageUrl);
		}

		if (vm.editing) {
			uploadService.getPodcast($location.search().id)
				.then(function(response) {
					if (response.data.episodes === undefined) {
						response.data.episodes = [];
					}
					if (response.data.tags === undefined) {
						response.data.tags = [];
					}
					$scope.thisPodcast = angular.copy(response.data);
				}, function(reason) {
					console.log('The call to getPodcast failed');
					$scope.thisPodcast = { episodes: [], tags: [] };
				}
			);
		}
	}
})();
