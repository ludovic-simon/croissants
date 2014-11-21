var groupeViewController = angular.module('groupeViewController', []);
 
groupeViewController.controller('GroupeViewCtrl', ['$scope', '$http', '$location', '$route',
function GroupeViewCtrl($scope, $http, $location, $route) {

	$scope.idGroupe = ($location.search()['idGroupe'] != null)? $location.search()['idGroupe'] : null;

	$scope.init = function() {

	};

	$scope.init();

}]);