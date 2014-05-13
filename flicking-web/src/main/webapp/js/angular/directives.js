'use strict';

/* Directives */

var AppDirectives = angular.module('Flicking.directives', []);

AppDirectives.directive('hboTabs', function() {
    return {
        restrict: 'A',
        link: function(scope, elm, attrs) {
            var jqueryElm = $(elm[0]);
            $(jqueryElm).tabs()
        }
    };
});
