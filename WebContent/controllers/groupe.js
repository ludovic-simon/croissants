var groupeViewController = angular.module('groupeViewController', []);
 
groupeViewController.controller('GroupeViewCtrl', ['$scope', '$http', '$location', '$route',
function GroupeViewCtrl($scope, $http, $location, $route) {

	$scope.idGroupe = ($location.search()['idGroupe'] != null)? $location.search()['idGroupe'] : null;

	$scope.tours = new Object();
	$scope.constitutionGroupe = new Object();
	
	 var loadConstitution = function(idUtilisateur, idGroupe) {
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
		//Chargement de la constitution groupe
		var idUtilisateur = getUtilisateurFromCookies();
		loadConstitution(idUtilisateur, $scope.idGroupe);
		
		//Chargement du cycle en cours
		if(!isNull($scope.idGroupe)) {
			loadCycleEnCours($scope.idGroupe);
		} else {
			alert("Error pas de groupe!");
		} 
	};

	$scope.init();

}]);