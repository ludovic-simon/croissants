var groupesViewController = angular.module('groupesViewController', []);
 
groupesViewController.controller('GroupesViewCtrl', ['$scope', '$http', '$location', '$route', '$timeout',
function GroupesViewCtrl($scope, $http, $location, $route, $timeout) {

	console.log($scope.utilisateur);
	$scope.constitutionsGroupe = new Object();

	$scope.init = function() {
		$http.get('/croissants/rest/cycleService/rechercherGroupesUtilisateur?idUtilisateur='+$scope.utilisateur.idUtilisateur).
		  success(function(data, status, headers, config) {
			  console.log(data);
			  $scope.constitutionsGroupe = data;
			  $timeout(function(){
				  initDefaultGroup();
			  });
		  }).
		  error(function(data, status, headers, config) {
			  handleError(data, status, headers, config);
		  });
	};

	$scope.init();
	
	//Obligé de passer par jquery pour initialiser les radios bouton, je ne vois pas comment faire autrement.
	var initDefaultGroup = function () {
		$('input').each(function() {
			if($(this).attr('value') == 'true') {
				$(this).attr('checked', '');
			}
		});
	};
	
	$scope.goToGroupe = function(idGroupe) {
		alert("Goto : /croissants/views/croissants.html#/groupe?groupeId="+idGroupe);
		//window.location.href = "/croissants/views/croissants.html#/groupe?groupeId="+idGroupe;
	};
	
	$scope.setDefautGroupe = function(idUtilisateur, idGroupe) {
		alert("Rest to  : /croissants/rest/cycleService/setDefaultGroupe?idUtilisateur="+idUtilisateur+"&idGroupe="+idGroupe);
	};
}]);