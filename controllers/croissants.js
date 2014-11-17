var croissantsTemplateApp = angular.module('croissants',
								 [ 'ngRoute',
                                   'ui.select2',
                                   'angular-jquery-ui',
                                   'accueilViewController',
                                   'croissantsTemplateController'
                                 ]
);

croissantsTemplateApp.config(['$routeProvider',
   function($routeProvider) {
   $routeProvider.
     when('/accueilView', {
       templateUrl: 'accueilView.html',
       controller: 'AccueilViewCtrl'
     }).
     otherwise({
       redirectTo: '/accueilView'
     });
 }]);
 
 var croissantsTemplateController = angular.module('croissantsTemplateController', []);
 
croissantsTemplateController.controller('CroissantsTemplateCtrl', ['$scope', '$http', '$location', '$route',
function CroissantsTemplateCtrl($scope, $http, $location, $route) {

	$scope.utilisateur = null;

	$scope.init = function() {

	};

	$scope.init();

}]);