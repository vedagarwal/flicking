'use strict';

var exampleAppConfig = {
    /* When set to false a query parameter is used to pass on the auth token.
     * This might be desirable if headers don't work correctly in some
     * environments and is still secure when using https. */
    useAuthTokenHeader: true
};

var flickingApp = angular.module('flickingApp', ['flickingApp.services', 'ngRoute', 'ngCookies', 'ui.bootstrap', 'angularFileUpload', 'ngProgress','ngFacebook']);

flickingApp.config(function ($routeProvider, $locationProvider, $httpProvider,$facebookProvider) {

    $routeProvider.when('/', {
        templateUrl: 'views/home.html',
        controller: 'IndexController'
    });
    
    $routeProvider.when('/movie/:id', {
        templateUrl: 'views/movie-detail-complete.html',
        controller: 'IndexMovieDetailController'
    });

    $routeProvider.when('/login', {
        templateUrl: 'views/sign-in.html',
        controller: 'LoginController'
    });

    $routeProvider.when('/register', {
        templateUrl: 'views/sign-up.html',
        controller: 'RegisterController'
    });

    $routeProvider.when('/users', {
        templateUrl: 'views/user-list.html',
        controller: 'UserController'
    });

    $routeProvider.when('/movies', {
        templateUrl: 'views/movie-list.html',
        controller: 'MovieController'
    });

    $routeProvider.when('/addMovie', {
        templateUrl: 'views/add-movie.html',
        controller: 'MovieController'
    });

    $routeProvider.when('/editMovie/:id', {
        templateUrl: 'views/edit-movie.html',
        controller: 'MovieDetailController'
    });

    $routeProvider.when('/addImage/:id', {
        templateUrl: 'views/upload-image.html',
        controller: 'UploadImageController'
    });
    
     $routeProvider.when('/contact', {
        templateUrl: 'views/contact.html'       
    });
    
    $facebookProvider.setAppId('273974442785397');
    
    //$locationProvider.html5Mode(true);


//    $routeProvider.otherwise({
//        templateUrl: 'views/home.html',
//        controller: 'IndexController'
//    });


    $httpProvider.interceptors.push(function ($q, $rootScope, $location,$injector) {
        return {
            'responseError': function (rejection) {
                var ngProgress = $injector.get('ngProgress');                
                ngProgress.complete(); 
                
                var status = rejection.status;
                var config = rejection.config;
                var method = config.method;
                var url = config.url;

                if (status == 401) {
                    $location.path("/login");
                } else {
                    $rootScope.error = method + " on " + url + " failed with status " + status;
                }

                return $q.reject(rejection);
            }
        };
    });

    /* Registers auth token interceptor, auth token is either passed by header or by query parameter
     * as soon as there is an authenticated user */
    $httpProvider.interceptors.push(function ($q, $rootScope, $location, $injector) {
        return {
            'request': function (config) {
                var ngProgress = $injector.get('ngProgress');
                ngProgress.reset();
                ngProgress.start();
                //console.log("Config:" + config);
                var isRestCall = config.url.indexOf("api") > 0;
                //var isRestCall = config.url.contains("/api/");
                //console.log("Is Rest call" + isRestCall);
                if (isRestCall && angular.isDefined($rootScope.authToken)) {
                    var authToken = $rootScope.authToken;
                    //console.log("auth" + authToken);
                    if (exampleAppConfig.useAuthTokenHeader) {
                        config.headers['X-Auth-Token'] = authToken;
                    } else {
                        config.url = config.url + "?token=" + authToken;
                    }
                }
                return config || $q.when(config);
            }
        };
    });
    
    
    $httpProvider.interceptors.push(function ($q, $rootScope, $location, $injector) {
        return {
            'response': function (config) {
                var ngProgress = $injector.get('ngProgress');
                ngProgress.complete();               
                return config || $q.when(config);
            }
        };
    });


});


flickingApp.run(function ($rootScope, $location, $cookieStore, LoginService) {
    
   /* (function(d, s, id) {
  var js, fjs = d.getElementsByTagName(s)[0];
  if (d.getElementById(id)) return;
  js = d.createElement(s); js.id = id;
  js.src = "//connect.facebook.net/en_GB/sdk.js#xfbml=1&appId=273974442785397&version=v2.0";
  fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));*/
    
    (function(){
     // If we've already installed the SDK, we're done
     if (document.getElementById('facebook-jssdk')) {return;}

     // Get the first script element, which we'll use to find the parent node
     var firstScriptElement = document.getElementsByTagName('script')[0];

     // Create a new script element and set its id
     var facebookJS = document.createElement('script'); 
     facebookJS.id = 'facebook-jssdk';

     // Set the new script's source to the source of the Facebook JS SDK
     facebookJS.src = '//connect.facebook.net/en_US/all.js';

     // Insert the Facebook JS SDK into the DOM
     firstScriptElement.parentNode.insertBefore(facebookJS, firstScriptElement);
   }());

    /* Reset error when a new view is loaded */
    $rootScope.$on('$viewContentLoaded', function () {
        delete $rootScope.error;
    });

    $rootScope.hasRole = function (role) {

        if ($rootScope.user === undefined) {
            return false;
        }

        if ($rootScope.user.roles[role] === undefined) {
            return false;
        }

        return $rootScope.user.roles[role];
    };

    $rootScope.logout = function () {
        delete $rootScope.user;
        delete $rootScope.authToken;
        $cookieStore.remove('authToken');
        $location.path("/login");
    };

    /* Try getting valid user from cookie or go to login page */
    var originalPath = $location.path();
    //$location.path("/");
    var authToken = $cookieStore.get('authToken');
    if (authToken !== undefined) {
        $rootScope.authToken = authToken;
        LoginService.get(function (user) {
            $rootScope.user = user;
            $location.path(originalPath);
        });
    }

    $rootScope.initialized = true;
});

flickingApp.directive('match', function () {
    return {
        require: 'ngModel',
        restrict: 'A',
        scope: {
            match: '='
        },
        link: function (scope, elem, attrs, ctrl) {
            scope.$watch(function () {
                return (ctrl.$pristine && angular.isUndefined(ctrl.$modelValue)) || scope.match === ctrl.$modelValue;
            }, function (currentValue) {
                ctrl.$setValidity('match', currentValue);
            });
        }
    };
});