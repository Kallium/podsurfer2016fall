(function() {
  'use strict';

    angular.module('app')
      .config(config)

    config.$inject = ['$stateProvider', '$urlRouterProvider'];
    function config($stateProvider, $urlRouterProvider) {

      $urlRouterProvider.otherwise('/home');

      $stateProvider
        .state('about', {
          url: '/about',
          views: {
            '': {
              templateUrl: 'about/about.html',
              controller: 'aboutController',
              controllerAs: 'about'
            },
            'view1@about': {
              templateUrl: 'about/views/about-view1.html',
            },
            'view2@about': {
              templateUrl: 'about/views/about-view2.html',
            },
            'view3@about': {
              templateUrl: 'about/views/about-view3.html',
            },
          }
        })
        .state('account', {
          url: '/account',
          templateUrl: 'account/account.html',
          controller: 'accountController',
          controllerAs: 'account'
        })
        .state('contact', {
          url: '/contact',
          templateUrl: 'contact/contact.html',
          controller: 'contactController',
          controllerAs: 'contact'
        })
        .state('home', {
          url: '/home',
          templateUrl: 'home/home.html',
          controller: 'homeController',
          controllerAs: 'home'
        })
        .state('join', {
          url: '/join',
          templateUrl: 'join/join.html',
          controller: 'joinController',
          controllerAs: 'join'
        })
        .state('podcast', {
          url: '/podcast',
          templateUrl: 'podcast/podcast.html',
          controller: 'podcastController',
          controllerAs: 'podcast'
        })
        .state('recommended', {
          url: '/recommended',
          templateUrl: 'recommended/recommended.html',
          controller: 'recommendedController',
          controllerAs: 'recommended'
        })
        .state('search', {
          url: '/search',
          templateUrl: 'search/search.html',
          controller: 'searchController',
          controllerAs: 'search'
        })
        .state('sign_in', {
          url: '/sign_in',
          templateUrl: 'sign_in/sign_in.html',
          controller: 'signInController',
          controllerAs: 'signIn'
        })
        .state('upload', {
          url: '/upload',
          templateUrl: 'upload/upload.html',
          controller: 'uploadController',
          controllerAs: 'upload'
        });
    };

})();

