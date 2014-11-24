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
			loadGroupe($scope.idGroupe);
			if(!isNull( $scope.groupe)) {
				$scope.nouveauGroupe = false;
			}
		} else {
			
		}
	};

	$scope.init();
	
	var loadGroupe = function(idGroupe) {
		$http.get('/croissants/rest/cycleService/rechercherGroupeParId?idGroupe='+idGroupe).
		  success(function(data, status, headers, config) {
			  $scope.groupe=data;
		  }).
		  error(function(data, status, headers, config) {
			  handleError(data, status, headers, config);
		  });
	};
	
	$scope.editerGroupe = function() {
		console.log($scope.groupe);
		if(isGroupValid()) {
			$http.post('/croissants/rest/cycleService/editerGroupe?idUtilisateur='+$scope.utilisateur.idUtilisateur, $scope.groupe ).
			  success(function(data, status, headers, config) {
				  alert('Success creation groupe');
				  console.log(data);
			  }).
			  error(function(data, status, headers, config) {
				  handleError(data, status, headers, config);
			  });
			}
	};
	
	var isGroupValid = function () {
		var isOkay = true;
		
		//on supprime tous les messages d'erreurs précédents.
		$('.format-error-message').remove();
		
		$('#nomGroupe').removeClass("format-ok-message");
		$('#nomGroupe').removeClass("format-error-message-input");
		
		if($scope.groupe.nom == null) {
			var parentDiv = $('#nomGroupe').closest('.form-group');
			displayFormatError(parentDiv, 'Le nom du groupe ne peut être vide');
			isOkay = false;
		} else {
			displayFormatOk($('#nomGroupe').parent());
		}
		return isOkay;
	}
	
	var displayFormatOk = function(parentDiv) {
		parentDiv.children('input').addClass("format-ok-message");
	}
	
	var displayFormatError = function(parentDiv, message) {
		parentDiv.append('<div class=\'col-sm-offset-4 col-sm-8 format-error-message\'>' + message + '</div>');
		parentDiv.children('input').addClass("format-error-message-input");
	}
	
}]);