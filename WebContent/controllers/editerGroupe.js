var editerGroupeViewController = angular.module('editerGroupeViewController', []);
 
editerGroupeViewController.controller('EditerGroupeViewCtrl', ['$scope', '$http', '$location', '$route',
function EditerGroupeViewCtrl($scope, $http, $location, $route) {

	$scope.idGroupe = ($location.search()['idGroupe'] != null)? $location.search()['idGroupe'] : null;

	
	$scope.titrePage = 'Nouveau';
	$scope.groupe = new Object();
	$scope.spinnerClass = '';
	
	$scope.jours =
		    [
		        { id: "0", nom: "Lundi" },
		        { id: "1", nom: "Mardi" }, 
		        { id: "2", nom: "Mercredi" },
		        { id: "3", nom: "Jeudi" },
		        { id: "4", nom: "Vendredi" },
		        { id: "5", nom: "Samedi" },
		        { id: "6", nom: "Dimanche" },
		    ];      
	//on met vendredi par défaut.
	$scope.selectedOption = $scope.jours[4];
		
	 var loadGroupe = function(idGroupe) {
			$http.get('/croissants/rest/cycleService/rechercherGroupeParId?idGroupe='+idGroupe).
			  success(function(data, status, headers, config) {
				  console.log(data);
				  $scope.groupe=data;
				  $scope.selectedOption = $scope.jours[data.jourOccurence];
			  }).
			  error(function(data, status, headers, config) {
				  handleError(data, status, headers, config);
			  });
	};
	
	
	 
	$scope.init = function() {
		if(!isNull($scope.idGroupe)) {
			$scope.titrePage = 'Editer';
			loadGroupe($scope.idGroupe);
		}
	};

	$scope.init();
	
	
	
	$scope.editerGroupe = function() {
		if(isGroupValid()) {
			$scope.spinnerClass = 'active';
			$scope.groupe.jourOccurence= $scope.selectedOption.id;
			$http.post('/croissants/rest/cycleService/editerGroupe?idUtilisateur='+$scope.utilisateur.idUtilisateur, $scope.groupe ).
			  success(function(data, status, headers, config) {
				  console.log(data);
				  $scope.spinnerClass = '';
				  showActionFeedback("Groupe créé.");
				  window.location.href = "/croissants/views/croissants.html#/groupesView";
			  }).
			  error(function(data, status, headers, config) {
				  $scope.spinnerClass = '';
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