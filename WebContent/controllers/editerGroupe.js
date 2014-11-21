var editerGroupeViewController = angular.module('editerGroupeViewController', []);
 
editerGroupeViewController.controller('EditerGroupeViewCtrl', ['$scope', '$http', '$location', '$route',
function EditerGroupeViewCtrl($scope, $http, $location, $route) {

	$scope.idGroupe = ($location.search()['idGroupe'] != null)? $location.search()['idGroupe'] : null;

	$scope.groupe = new Object();
	
	$scope.nouveauGroupe = true;
	
	 $scope.jours =
		    [
		        { id: 1, nom: "Lundi" },
		        { id: 2, nom: "Mardi" }, 
		        { id: 3, nom: "Mercredi" },
		        { id: 4, nom: "Jeudi" },
		        { id: 5, nom: "Vendredi" },
		        { id: 6, nom: "Samedi" },
		        { id: 7, nom: "Dimanche" },
		    ];        
	
	$scope.init = function() {
		if(!isNull($scope.idGroupe)) {
			loadGroupe();
		} else {
			
		}
	};

	$scope.init();
	
	var loadGroupe = new function() {
		/*$http.get('/croissants/rest/cycleService/rechercherGroupesUtilisateur?idUtilisateur='+$scope.utilisateur.idUtilisateur).
		  success(function(data, status, headers, config) {
			  console.log(data);
			  $scope.constitutionsGroupe = data;
			  $timeout(function(){
				  initDefaultGroup();
			  });
		  }).
		  error(function(data, status, headers, config) {
			  handleError(data, status, headers, config);
		  });*/
	};
	
	$scope.editerGroupe = function() {
		if($scope.nouveauGroupe) {
			if(isGroupValid()) {
				$http.get('/croissants/rest/cycleService/creerGroupe?idUtilisateur='+$scope.utilisateur.idUtilisateur+"&nomGroupe="+$scope.groupe.nom).
				  success(function(data, status, headers, config) {
					  alert('Success creation groupe');
					  console.log(data);
				  }).
				  error(function(data, status, headers, config) {
					  handleError(data, status, headers, config);
				  });
				}
		}
	};
	
	var isGroupValid = function () {
		var isOkay = true;
		if($scope.groupe.nom == null) {
			alert('alert error nom null!');
			isOkay = false;
		}
		return isOkay;
	}
	
	
	
}]);