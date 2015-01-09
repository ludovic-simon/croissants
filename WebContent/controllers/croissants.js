var croissantsTemplateApp = angular.module('croissants',
								 [ 'ngRoute',
                                   'ui.select2',
                                   'angular-jquery-ui',
                                   'xeditable',
                                   'accueilViewController',
                                   'groupesViewController',
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
     when('/groupesView', {
       templateUrl: 'groupes.html',
       controller: 'GroupesViewCtrl'
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
       redirectTo: '/groupesView'
     });
 }]);

croissantsTemplateApp.run(function(editableOptions) {
	  editableOptions.theme = 'bs3'; 
	  editableOptions.mode = 'inline';
});

 
 var croissantsTemplateController = angular.module('croissantsTemplateController', []);
 
croissantsTemplateController.controller('CroissantsTemplateCtrl', ['$scope', '$http', '$location', '$route',
function CroissantsTemplateCtrl($scope, $http, $location, $route) {

	$scope.utilisateur = null;

	$scope.init = function() {
		//On verifie que le cookie contenant l'i utilisateur est toujours valide
		if(isNull(getUtilisateurFromCookies())) {
			console.log("Aucune cookie pour l'utilisateur, redirection vers la page de login")
			window.location.href = "/croissants/views/guest.html#/loginView";
		} else {
			$scope.utilisateur = getUtilisateurFromCookies();
		}
	}
		
	$scope.init();
	
	$scope.seDeconnecter = function() {
		$http.post('/croissants/rest/utilisateurService/seDeconnecter' ).
		  success(function(data, status, headers, config) {
			  eraseUtilisateurFromCookies();
			  window.location.href = "/croissants/views/guest.html#/loginView";
		  }).
		  error(function(data, status, headers, config) {
			  handleError(data, status, headers, config);
		  });
	}

}]);

