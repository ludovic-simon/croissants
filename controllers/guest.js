var guestTemplateApp = angular.module('croissants',
								 [ 'ngRoute',
                                   'ui.select2',
                                   'angular-jquery-ui',
                                   'loginViewController',
                                   'inscriptionViewController',
                                   'guestTemplateController'
                                 ]
);

guestTemplateApp.config(['$routeProvider',
   function($routeProvider) {
   $routeProvider.
     when('/loginView', {
       templateUrl: 'login.html',
       controller: 'LoginViewCtrl'
     }).
     when('/inscriptionView', {
       templateUrl: 'inscriptionView.html',
       controller: 'InscriptionViewCtrl'
     }).
     otherwise({
       redirectTo: '/loginView'
     });
 }]);
 
 var guestTemplateController = angular.module('guestTemplateController', []);
 
guestTemplateController.controller('GuestTemplateCtrl', ['$scope', '$http', '$location', '$route',
function GuestTemplateCtrl($scope, $http, $location, $route) {


	$scope.init = function() {

	};

	$scope.init();

}]);