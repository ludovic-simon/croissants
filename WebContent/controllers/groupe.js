var groupeViewController = angular.module('groupeViewController', []);
 
groupeViewController.controller('GroupeViewCtrl', ['$scope', '$http', '$location', '$route',
function GroupeViewCtrl($scope, $http, $location, $route) {

	$scope.idGroupe = ($location.search()['idGroupe'] != null)? $location.search()['idGroupe'] : null;

	$scope.groupe = new Object();
	
	 var loadGroupe = function(idGroupe) {
			$http.get('/croissants/rest/cycleService/rechercherGroupeParId?idGroupe='+idGroupe).
			  success(function(data, status, headers, config) {
				  console.log(data);
				  $scope.groupe=data;
			  }).
			  error(function(data, status, headers, config) {
				  handleError(data, status, headers, config);
			  });
	};
	
	$scope.init = function() {
		if(!isNull($scope.idGroupe)) {
			loadGroupe($scope.idGroupe);
		} else {
			$scope.selectedOption = 0;
			
		} 
	};

	$scope.init();

}]);