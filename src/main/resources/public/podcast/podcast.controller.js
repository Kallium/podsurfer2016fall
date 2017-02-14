(function() {
	'use strict';

	angular
		.module('app')
		.controller('podcastController', podcastController)
		.directive('episodePlayer', function($timeout) {
			function link($scope, element) {
				function secondsToFormattedTime(seconds) {
					var hrs = ~~(seconds / 3600);
					var mins = ~~((seconds % 3600) / 60);
					var secs = ~~(seconds % 60);
		
					var formattedTime = '';
					if (hrs > 0) {
						formattedTime += hrs + ':' + (mins > 9 ? '' : '0');
					}
					formattedTime += (mins > 0 ? mins : '0') + ':' + (secs > 9 ? '' : '0');
					formattedTime += secs;
		
					return formattedTime;
				}

				element.on('timeupdate', function() {
					var timeLeft = secondsToFormattedTime(this.dataset.episodeTime - this.currentTime);
					$scope.updateEpisodeTimeLeft(this.dataset.episodeIndex, timeLeft);
				});
				
				element.on('ended', function() {
					document.getElementById('audioForEpisode' + this.dataset.episodeNumber + 'Icon').className = 'fa fa-play';
					//var timeLeft = secondsToFormattedTime(this.seekable.end(0));
					$scope.updateEpisodeTimeLeft(this.dataset.episodeIndex, this.dataset.episodeLength /*timeLeft*/);
				});

				$scope.formattedTimeToSeconds = function(formattedTime) {
					var seconds;
					var parts = formattedTime.split(':');
					switch (parts.length) {
					case 3:
						seconds = (3600 * parseInt(parts[0], 10)) + (60 * parseInt(parts[1], 10)) + parseInt(parts[2], 10);
						break;
					case 2:
						seconds = (60 * parseInt(parts[0], 10)) + parseInt(parts[1], 10);
						break;
					}

					return seconds;
				}
			}
			
			return {
				link: link,
				restrict: 'C',
			};
		})
		.directive('convertToNumber', function() {
			function link($scope, element, attrs, ngModel) {
				ngModel.$parsers.push(function(val) {
					return (val === null || val === undefined) ? val : parseInt(val, 10);
				});

				ngModel.$formatters.push(function(val) {
					return (val === null || val === undefined) ? val : val.toString();
				});
			}

			return {
				require: 'ngModel',
				link: link,
			};
		})
		.filter("trustUrl", ['$sce', function($sce) {
			return function(audioUrl) {
				return $sce.trustAsResourceUrl(audioUrl);
			}
		}]);

	podcastController.$inject = ['$scope', '$location', '$anchorScroll', '$timeout', '$q', 'podcastService'];
	function podcastController($scope, $location, $anchorScroll, $timeout, $q, podcastService) {
		var vm = this;

		vm.thisPodcast = {};
		vm.user = {};
<<<<<<< HEAD
		
		var shareUrlParts = $location.absUrl().split('#');
		$scope.shareUrl = shareUrlParts[0] + '#' + shareUrlParts[1];
=======
		vm.bookmarking = false;
>>>>>>> refs/remotes/origin/master

		$scope.userReview = { podcast: $location.search().id, episode: null, spoilers: false };
		vm.invalidRating = false;
		vm.invalidName = false;
		vm.invalidReview = false;
		vm.submittingReview = false;
		vm.reviewError = false;
		vm.reviewErrorMessage = '';

		var shareUrlParts = $location.absUrl().split('#');
		$scope.shareUrl = encodeURIComponent(shareUrlParts[0] + '#' + shareUrlParts[1]);

		vm.reviews = [];
		vm.episodesReviewCount = [];
		vm.reviewsForEpisode = [];
		vm.selectedReviewId = $location.hash();

		$anchorScroll.yOffset = function() {
			return document.getElementById('navbar').clientHeight + 20;
		};
		vm.scrollToReviewsForEpisode = function(number) {
			//$location.hash('reviewsForEpisode' + episode);
			$anchorScroll('reviewsFor' + number);
		};
		vm.scrollToWrite = function(number) {
			//$location.hash('reviewsForEpisode' + episode);
			$scope.userReview.episode = number;
			$anchorScroll('write');
			document.getElementById('writeFocus').focus();
		};

		var bookmarkIndex = -1;
		$scope.bookmark = bookmarkIndex === -1 ? 'Bookmark' : 'Unbookmark';
		vm.bookmark = function() {
			vm.bookmarking = true;
			if (bookmarkIndex === -1) {
				vm.user.bookmarks.push($location.search().id);
			} else {
				vm.user.bookmarks.splice(bookmarkIndex, 1);
			}
			podcastService.updateMyProfileInfo(vm.user)
				.then(function(response) {
					vm.user = response.data;
					bookmarkIndex = vm.user.bookmarks.indexOf($location.search().id);
					$scope.bookmark = bookmarkIndex === -1 ? 'Bookmark' : 'Unbookmark';
					vm.bookmarking = false;
				}, function(reason) {
					console.log('The call to updateBookmarks failed');
					if (bookmarkIndex === -1) {
						vm.user.bookmarks.splice(vm.user.bookmarks.length, 1);
					} else {
						vm.user.bookmarks.push($location.search().id);
					}
					vm.bookmarking = false;
				}
			);
                };

		vm.edit = function() {
			$location.path('/upload').search('id', $location.search().id);
		};

		vm.toggleAudioForEpisode = function(number) {
			var audio = document.getElementById('audioForEpisode' + number);
			if (audio.paused) {
				audio.play();
				document.getElementById('audioForEpisode' + number + 'Icon').className = 'fa fa-pause';
			} else {
				audio.pause();
				document.getElementById('audioForEpisode' + number + 'Icon').className = 'fa fa-play';
			}
		};
		$scope.updateEpisodeTimeLeft = function(episodeIndex, timeLeft) {
			$timeout(function() {
				vm.thisPodcast.episodes[episodeIndex].length = timeLeft;
			});
		};

		function validateReview(review) {
			vm.invalidRating = review.rating === undefined || review.rating === null;
			vm.invalidName = review.name === undefined || review.name === "";
			vm.invalidReview = review.review === undefined || review.review === "";

			return !(vm.invalidRating || vm.invalidName || vm.invalidReview);
		}
		if ($scope.signedIn) {
			podcastService.getMyProfileInfo()
				.then(function(response) {
					vm.user = response.data;
					bookmarkIndex = vm.user.bookmarks.indexOf($location.search().id);
					$scope.bookmark = bookmarkIndex === -1 ? 'Bookmark' : 'Unbookmark';
				}, function(reason) {
					console.log('The call to getMyProfileInfo failed');
					vm.user = {};
				}
			);

		}
		$scope.submit = function(review) {
			vm.reviewError = false;
			vm.reviewErrorMessage = '';

			if (!validateReview(review)) {
				return;
			}

			vm.submittingReview = true;
			if (review._id === undefined) {
				podcastService.createNewReview(review)
					.then(function(response) {
						vm.submittingReview = false;
						vm.selectedReviewId = 'review' + response.data._id;
						if (response.data.episode !== undefined) {
							vm.reviewsForEpisode[response.data.episode].reviews.push(response.data);
						} else {
							vm.reviewsForEpisode[0].reviews.push(response.data);
						}
						$scope.userReview = { podcast: $location.search().id, episode: null, spoilers: false };
						$timeout(function() {
							$anchorScroll('review' + response.data._id);
						});
					}, function(reason) {
						console.log('The call to createNewReview failed');
						vm.submittingReview = false;
						vm.reviewError = true;
						vm.reviewErrorMessage = 'Error: ' + reason.data.message;
					}
				);
			} else {
				var $index = review.$index;
				var _id = review._id;
				delete review.$index;
				delete review._id;
				podcastService.updateExistingReview(_id, review)
					.then(function(response) {
						vm.submittingReview = false;
						vm.selectedReviewId = 'review' + response.data._id;
						if (response.data.episode !== undefined) {
							vm.reviewsForEpisode[response.data.episode].reviews[$index] = response.data;
						} else {
							vm.reviewsForEpisode[0].reviews[$index] = response.data;
						}
						$scope.userReview = { podcast: $location.search().id, episode: null, spoilers: false };
						$timeout(function() {
							$anchorScroll('review' + response.data._id);
						});
					}, function(reason) {
						console.log('The call to updateExistingReview failed');
						vm.submittingReview = false;
						vm.reviewError = true;
						vm.reviewErrorMessage = 'Error: ' + reason.data.message;
					}
				);
			}
		};
		$scope.deleteReview = function(review, $index) {
			podcastService.deleteReview(review._id)
				.then(function(response) {
					if (review.episode !== undefined) {
						vm.reviewsForEpisode[review.episode].reviews.splice($index, 1);
					} else {
						vm.reviewsForEpisode[0].reviews.splice($index, 1);
					}
					console.log("Deleted review " + review._id + " at " + $index);
				}, function(reason) {
					console.log('The call to deleteReview failed');
				}
			);
		};
		$scope.editReview = function(review, $index) {
			$scope.userReview.$index = $index;
			$scope.userReview._id = review._id;
			$scope.userReview.name = review.name;
			$scope.userReview.rating = review.rating;
			$scope.userReview.review = review.review;
			$scope.userReview.spoilers = review.spoilers;
			vm.scrollToWrite(review.episode);
		};

		var loadPodcastPromise = loadPodcast();
		function loadPodcast() {
			return $q(function(resolve, reject) {
				podcastService.getPodcast()
					.then(function(response) {
						console.log('Resolved getPodcast');
						response.data.release = new Date(response.data.release).toLocaleDateString();
						vm.thisPodcast = response.data;
						resolve(vm.thisPodcast);
					}, function(reason) {
						console.log('The call to thisPodcast failed');
						vm.thisPodcast = {};
						reject(vm.thisPodcast);
					}
				);
			});
		}

		var loadReviewsPromise = loadReviews();
		function loadReviews() {
			return $q(function(resolve, reject) {
				podcastService.getReviews()
					.then(function(response) {
						console.log('Resolved getReviews');
						vm.reviews = response.data;
				  
						for (var review in vm.reviews) {
							review = vm.reviews[review];
							console.log(review);
							if (vm.episodesReviewCount[review.episode] === undefined) {
								vm.episodesReviewCount[review.episode] = 1;
							} else {
								vm.episodesReviewCount[review.episode]++;
							}
						}
						console.log(vm.episodesReviewCount);

						resolve(vm.reviews);
					}, function(reason) {
						console.log('The call to reviews failed');
						vm.reviews = [];
						reject(vm.reviews);
					}
				);
			});
		}
		
		$q.all([loadPodcastPromise, loadReviewsPromise])
			.then(function(responses) {
				vm.reviewsForEpisode[0] = { heading: 'The Podcast', number: 0, reviews: [] };
				for (var episode in vm.thisPodcast.episodes) {
					episode = vm.thisPodcast.episodes[episode];

					vm.reviewsForEpisode[episode.number] = { heading: 'Episode ' + episode.number + ': ' + episode.name, number: episode.number, reviews: [] };
				}
				for (var review in vm.reviews) {
					review = vm.reviews[review];

					if (review.episode !== undefined) {
						if (vm.reviewsForEpisode[review.episode] !== undefined) {
							vm.reviewsForEpisode[review.episode].reviews.push(review);
						}
					} else {
						vm.reviewsForEpisode[0].reviews.push(review);
					}
				}

				console.log('Promises resolved');
				$timeout(function() {
					$anchorScroll();
				});
			}, function(reasons) {
				vm.reviewsForEpisode = [];
				console.log(reasons);
			}
		);
			
		(function(d, s, id) {
		  var js, fjs = d.getElementsByTagName(s)[0];
		  if (d.getElementById(id)) return;
		  js = d.createElement(s); js.id = id;
		  js.src = "//connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.8&appId=824794047623314";
		  fjs.parentNode.insertBefore(js, fjs);
		}(document, 'script', 'facebook-jssdk'));
	}
})();
