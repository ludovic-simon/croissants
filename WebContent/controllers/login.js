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
			saveUtilisteurInCookies(utilisateur);
			
			//Navigation vers l'appli connectee
			//On recupère les groupes auxquels l'utilisateur est lié afin de le rediriger vers son groupe favori s'il en a un. 
			// Il faudrait peut être faire un service qui renvoie le groupe favori de l'utilisateur directement ?
			$http.get('/croissants/rest/cycleService/rechercherGroupesUtilisateur?idUtilisateur='+utilisateur.idUtilisateur).
			  success(function(data, status, headers, config) {
				  for(var i = 0 ; i < data.length; i++){
					  if(data[i].parDefaut == true) {
						  window.location.href = "/croissants/views/croissants.html#/groupeView?idGroupe="+data[i].idGroupe;
						  return;
					  }
				  }
				  window.location.href = "/croissants/views/croissants.html";
			  }).
			  error(function(data, status, headers, config) {
				  handleError(data, status, headers, config);
			  });
		})
		.error(function(data, status, headers, config) {
			console.log(data);
			$scope.errorMessage = data;
//			handleError(data, status, headers, config);
		});
	};

	$scope.gotoInscription = function() {
		//Navigation vers l'inscription
		window.location.href = "/croissants/views/guest.html#/inscriptionView";
	};
	
	$scope.oubliMdp = function() {
		if(isNull($scope.email)) {
			$scope.errorMessage = "Saisisez votre mail pour recevoir un nouveau mot de passe";
		} else {
			$http.post('/croissants/rest/utilisateurService/motDePassePerdu?email='+ $scope.email).
			  success(function(data, status, headers, config) {
				  showInfo("Un email avec votre nouveau mot de passe vous a été envoyé");
			  }).
			  error(function(data, status, headers, config) {
				  $scope.errorMessage = data;
			  });
		}
	};

	$scope.init();

}]);