var groupeViewController = angular.module('groupeViewController', []);
 
groupeViewController.controller('GroupeViewCtrl', ['$scope', '$http', '$location', '$route', '$timeout', '$filter',
function GroupeViewCtrl($scope, $http, $location, $route,  $timeout, $filter) {
	

	$scope.idGroupe = ($location.search()['idGroupe'] != null)? $location.search()['idGroupe'] : null;

	// Tous les tours du groupe courant.
	$scope.tours = new Object();
	// La constitution du groupe courant.
	$scope.constitutionGroupe = new Object();
	//En préparation de la création du groupe par ce controleur, on créé un groupe vide.
//	$scope.constitutionGroupe.groupe = new Object();
	//Toutes les constitutions groupes associées au groupe courant.
	$scope.constitutionsGroupe = new Object();
	
	 $scope.jours =
		    [
		        { id: 0, nom: "Lundi" },
		        { id: 1, nom: "Mardi" }, 
		        { id: 2, nom: "Mercredi" },
		        { id: 3, nom: "Jeudi" },
		        { id: 4, nom: "Vendredi" },
		        { id: 5, nom: "Samedi" },
		        { id: 6, nom: "Dimanche" },
		    ];      
	
	
	
	 var loadConstitutionUtilisateurCourant = function(idUtilisateur, idGroupe) {
			$http.get('/croissants/rest/cycleService/rechercherConstitutionGroupe?idUtilisateur=' + idUtilisateur + '&idGroupe='+idGroupe).
			  success(function(data, status, headers, config) {				 
				  $scope.constitutionGroupe = data;
				  $scope.jourCourant.id = $scope.constitutionGroupe.groupe.jourCourant;
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
				  $timeout(function(){
					  $('[data-toggle="tooltip"]').tooltip();
				  });
				  
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
	
	
	$scope.switchTour = function(tourSource, tourCible) {
		$http.post('/croissants/rest/cycleService/deplacerTour?idTourSource='+tourSource.idTour+"&idTourCible="+tourCible.idTour).
		  success(function(data, status, headers, config) {
			  console.log(data);
			  $scope.tours = data;
			  showActionFeedback("Tour échangé!");
		  }).
		  error(function(data, status, headers, config) {
			  handleError(data, status, headers, config);
		  });
	}

	
	$scope.updateGroupe = function() {
		$scope.constitutionGroupe.groupe.jourOccurence =  $scope.jourCourant.id;
		$http.post('/croissants/rest/cycleService/editerGroupe?idUtilisateur='+$scope.utilisateur.idUtilisateur, $scope.constitutionGroupe.groupe ).
		  success(function(data, status, headers, config) {
			  console.log(data);
			  showActionFeedback("Groupe modifié.");
		  }).
		  error(function(data, status, headers, config) {
			  handleError(data, status, headers, config);
		  });
	}
	
	$scope.annulerTour = function(tour) {
		$http.post('/croissants/rest/cycleService/annulerTour?idTour='+tour.idTour+'&messageAnnulation=Annulé par ' +$scope.utilisateur.nom).
		  success(function(data, status, headers, config) {
			  console.log(data);
			  showActionFeedback("Tour annulé.");
			  loadCycleEnCours($scope.idGroupe);
		  }).
		  error(function(data, status, headers, config) {
			  handleError(data, status, headers, config);
		  });
	}
	
	 /* Désactivé car pour le moment on ne veut pas éditer le jour d'occurence. */
	  
	$scope.jourCourant = new Object();
	   
	  $scope.$watch('constitutionGroupe.groupe', function(newVal, oldVal) {
		    if (newVal !== oldVal) {
		      var selected = $filter('filter')($scope.jours, {id: $scope.constitutionGroupe.groupe.jourOccurence});
		      $scope.jourCourant = selected[0];
		      console.log($scope.jourCourant);
		    }
		  });

	  
	  $scope.$watch('jourCourant.id', function(newVal, oldVal) {
		  if (newVal !== oldVal) {
			  console.log($scope.jourCourant.id);
			  var selected = $filter('filter')($scope.jours, {id: $scope.jourCourant.id});
		      $scope.jourCourant = selected[0];
		      $scope.jourCourant.id = selected[0].id;
		      
			  $scope.constitutionGroupe.groupe.jourOccurence = $scope.jourCourant.id;
		  }
	  });
	 
}]);