var loginViewController = angular.module('loginViewController', []);
 
loginViewController.controller('LoginViewCtrl', ['$scope', '$http', '$location', '$route',
function LoginViewCtrl($scope, $http, $location, $route) {

	$scope.email = null;
	$scope.motDePasse = null;
	
	$scope.errorMessage = null;

	$scope.init = function() {

	};

	$scope.connexion = function() {
		$http.get("/croissants/rest/utilisateurService/connecterUtilisateur" + getEmptyIfNullExpression("?email=" + $scope.email, $scope.email) + getEmptyIfNullExpression("&motDePasse=" + $scope.motDePasse, $scope.motDePasse))
		.success(function(data) {
			var utilisateur = extractObjectFromData(data);
			saveUtilisteurInCookies(utilisateur.idUtilisateur);
			//Navigation vers l'appli connectee
			window.location.href = "/croissants/views/croissants.html";
		})
		.error(function(data, status, headers, config) {
			$scope.errorMessage = data;
//			handleError(data, status, headers, config);
		});
	};

	$scope.gotoInscription = function() {
		//Navigation vers l'inscription
		window.location.href = "/croissants/views/guest.html#/inscriptionView";
	};

	$scope.init();

}]);