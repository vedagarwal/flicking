'use strict';

/* Services */

var AppServices = angular.module("flickingApp.services", ["ngResource"]);
var baseURL = "http://www.flickingtruth.com/api/";
//var baseURL = "http://localhost:8080/flicking-web/api/";

AppServices.factory('MovieService', function ($resource) {
    return $resource(baseURL+'movies/:id', {id: '@id'},{ save: { method: "POST" }, update: { method: "PUT" },
                                                    getlatest:{url:baseURL+'movies/latest', method: "GET",isArray:true },
                                                getUpcoming:{url:baseURL+'movies/upcoming', method: "GET",isArray:true}
                                                       });  
});

AppServices.factory('ReviewService', function ($resource) {
        return $resource(baseURL+'movies/:movieId/reviews/:id', {movieId:'@movieId',id: '@id'},{ update: {method:'PUT'}});
});

AppServices.factory('CommentService', function ($resource) {
        return $resource(baseURL+'movies/:movieId/comments/:id', {movieId:'@movieId',id: '@id'},{ update: {method:'PUT'}});
});

AppServices.factory('UserService', function($resource) {
        return $resource(baseURL+'users/:id/:action', {id: '@id'},{ 
            save: { method: "POST" },
            update: { method: "PUT" },
            setpassword:{method:"POST",params:{'action':'password'}}
        });	
});

AppServices.factory('LoginService', function($resource) {
	
	return $resource(baseURL+'user/:action', {},
			{
				authenticate: {
					method: 'POST',
					params: {'action' : 'authenticate'},
					headers : {'Content-Type': 'application/x-www-form-urlencoded'}
				},
                register: {
					method: 'POST',
                    params: {'action' : 'register'},
				},
                contact: {
					method: 'POST',
                    params: {'action' : 'contact'},
				},
                forgotpassword: {
					method: 'POST',
                    params: {'action' : 'forgotpassword'},
				}
			}
		);
});