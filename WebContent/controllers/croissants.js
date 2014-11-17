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
		//Recuperation de l'utilisateur en session
		$http.get("/croissants/rest/utilisateurService/getUtilisateurSession")
		.success(function(data) {
			$scope.utilisateur = extractObjectFromData(data);
		})
		.error(function(data, status, headers, config) {
			handleError(data, status, headers, config);
		});
	};

	$scope.init();

}]);