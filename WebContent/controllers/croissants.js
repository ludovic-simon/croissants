var croissantsTemplateApp = angular.module('croissants',
								 [ 'ngRoute',
                                   'ui.select2',
                                   'angular-jquery-ui',
                                   'accueilViewController',
                                   'groupeViewController',
                                   'editerGroupeViewController',
                                   'rejoindreGroupeViewController',
                                   'croissantsTemplateController'
                                 ]
);

croissantsTemplateApp.config(['$routeProvider',
   function($routeProvider) {
   $routeProvider.
     when('/accueilView', {
       templateUrl: 'accueil.html',
       controller: 'AccueilViewCtrl'
     }).
     when('/groupeView', {
       templateUrl: 'groupe.html',
       controller: 'GroupeViewCtrl'
     }).
     when('/editerGroupeView', {
       templateUrl: 'editerGroupe.html',
       controller: 'EditerGroupeViewCtrl'
     }).
     when('/rejoindreGroupeView', {
       templateUrl: 'rejoindreGroupe.html',
       controller: 'RejoindreGroupeViewCtrl'
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
			window.location.href = "/croissants/views/guest.html#/loginView";
		});
	};

	$scope.init();

}]);