var loginViewController = angular.module('loginViewController', []);
 
loginViewController.controller('LoginViewCtrl', ['$scope', '$http', '$location', '$route',
function LoginViewCtrl($scope, $http, $location, $route) {

	$scope.email = null;
	$scope.motDePasse = null;

	$scope.init = function() {

	};

	$scope.connexion = function() {
		$http.get("/croissants/rest/utilisateurService/connecterUtilisateur" + getEmptyIfNullExpression("?email=" + $scope.email, $scope.email) + getEmptyIfNullExpression("&motDePasse=" + $scope.motDePasse, $scope.motDePasse))
		.success(function(data) {
			var utilisateur = extractObjectFromData(data);
			//Navigation vers l'appli connectee
			window.location.href = "/croissants/views/croissants.html";
		})
		.error(function(data, status, headers, config) {
			handleError(data, status, headers, config);
		});
	};

	$scope.gotoInscription = function() {
		//Navigation vers l'inscription
		window.location.href = "/croissants/views/guest.html#/inscriptionView";
	};

	$scope.init();

}]);