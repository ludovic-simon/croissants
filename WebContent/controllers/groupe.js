var groupeViewController = angular.module('groupeViewController', []);
 
groupeViewController.controller('GroupeViewCtrl', ['$scope', '$http', '$location', '$route',
function GroupeViewCtrl($scope, $http, $location, $route) {

	$scope.idGroupe = ($location.search()['idGroupe'] != null)? $location.search()['idGroupe'] : null;

	// Tous les tours du groupe courant.
	$scope.tours = new Object();
	// La constitution du groupe courant.
	$scope.constitutionGroupe = new Object();
	//Toutes les constitutions groupes associées au groupe courant.
	$scope.constitutionsGroupe = new Object();
	
	 var loadConstitutionUtilisateurCourant = function(idUtilisateur, idGroupe) {
			$http.get('/croissants/rest/cycleService/rechercherConstitutionGroupe?idUtilisateur=' + idUtilisateur + '&idGroupe='+idGroupe).
			  success(function(data, status, headers, config) {				 
				  $scope.constitutionGroupe = data;
				  console.log("Constitution groupe : ");
				  console.log( $scope.constitutionGroupe);
			  }).
			  error(function(data, status, headers, config) {
				  handleError(data, status, headers, config);
			  });
	};
	
	 var rechercherConstitutionsGroupe = function(idGroupe) {
		 $http.get('/croissants/rest/cycleService/rechercherUtilisateursGroupe?idGroupe='+idGroupe).
		  success(function(data, status, headers, config) {				 
			  $scope.constitutionsGroupe = data;
			  console.log("Constitutions groupe : ");
			  console.log( $scope.constitutionsGroupe);
		  }).
		  error(function(data, status, headers, config) {
			  handleError(data, status, headers, config);
		  });
	 }
	
	 var loadCycleEnCours = function(idGroupe) {
			$http.get('/croissants/rest/cycleService/rechercherCycleEnCours?idGroupe='+idGroupe).
			  success(function(data, status, headers, config) {	
				  console.log("Tours : ");
				  $scope.tours = data;
				  console.log($scope.tours);
			  }).
			  error(function(data, status, headers, config) {
				  handleError(data, status, headers, config);
			  });
	};
	
	$scope.init = function() {
		//Chargement de la constitution groupe pour  l'utilisateur courant
		var idUtilisateur = getUtilisateurFromCookies();
		loadConstitutionUtilisateurCourant(idUtilisateur, $scope.idGroupe);
		
		//Chargement de toutes les constitutions liées à ce groupes, 
		//afin d'en extraire les différents utilisateurs
		rechercherConstitutionsGroupe($scope.idGroupe);
		
		//Chargement du cycle en cours
		if(!isNull($scope.idGroupe)) {
			loadCycleEnCours($scope.idGroupe);
		} else {
			alert("Error pas de groupe!");
		} 
	};

	$scope.init();
	
	$scope.calculerCycle = function(idGroupe) {
		$http.post('/croissants/rest/cycleService/calculerProchainCycle?idGroupe='+ idGroupe).
		  success(function(data, status, headers, config) {
			  showActionFeedback(data);
			  loadCycleEnCours(idGroupe);
		  }).
		  error(function(data, status, headers, config) {
			  handleError(data, status, headers, config);
		  });
	}
	
	$scope.setAdministrateur = function(constit) {
		$http.post('/croissants/rest/cycleService/affecterDroitAdministrateur?idUtilisateur='+constit.idUtilisateur+"&idGroupe="+ $scope.idGroupe + "&admin=" +constit.admin ).
		  success(function(data, status, headers, config) {
			  console.log("Succès du passage en admininstrateur");
		  }).
		  error(function(data, status, headers, config) {
			  handleError(data, status, headers, config);
		  });
		
	}
	
	var notYetImplemented = function() {
		alert("Pas encore fait...!");
	} 
	
	$scope.monterTour = function(tour) {
		notYetImplemented();
	}

	$scope.descendreTour = function(tour) {
		notYetImplemented();
	}
	
	$scope.annulerTour = function(tour) {
		notYetImplemented();
	}

}]);