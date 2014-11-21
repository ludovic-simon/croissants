var editerGroupeViewController = angular.module('editerGroupeViewController', []);
 
editerGroupeViewController.controller('EditerGroupeViewCtrl', ['$scope', '$http', '$location', '$route',
function EditerGroupeViewCtrl($scope, $http, $location, $route) {

	$scope.idGroupe = ($location.search()['idGroupe'] != null)? $location.search()['idGroupe'] : null;

	$scope.init = function() {
		alert("Param idGroupe = " + $scope.idGroupe);
	};

	$scope.init();

}]);