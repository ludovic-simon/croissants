var rejoindreGroupeViewController = angular.module('rejoindreGroupeViewController', []);
 
rejoindreGroupeViewController.controller('RejoindreGroupeViewCtrl', ['$scope', '$http', '$location', '$route',
function RejoindreGroupeViewCtrl($scope, $http, $location, $route) {

	$scope.groupeToJoin = new Object();
	$scope.spinnerClass = '';
	$scope.errorMessage = '';
	
	$scope.init = function() {

	};

	$scope.init();
	
	$scope.rejoindreGroupe = function() {
		if(isRejoindreGroupeValid()) {
			$scope.spinnerClass = 'active';
			$http.post('/croissants/rest/cycleService/rejoindreGroupe?idUtilisateur='+$scope.utilisateur.idUtilisateur+"&jeton="+$scope.groupeToJoin.jeton+"&motDePasse="+$scope.groupeToJoin.password ).
			  success(function(data, status, headers, config) {
				  console.log(data);
				  $scope.spinnerClass = '';
				  showActionFeedback("Le groupe a bien été rejoint !");
				  window.location.href = "/croissants/views/croissants.html#/groupesView";
			  }).
			  error(function(data, status, headers, config) {
				  $scope.spinnerClass = '';
				  $scope.errorMessage = data;
				  //handleError(data, status, headers, config);
			  });
		}
		
	}

	/* Validation de la sasie du formulaire */
	var isRejoindreGroupeValid = function () {
		var isOkay = true;
		
		//on supprime tous les messages d'erreurs précédents.
		$('.format-error-message').remove();
		
		
		if(isNull($scope.groupeToJoin.jeton)) {
			var mailDiv = $('#jetonGroupe').closest('.form-group');
			displayFormatError(mailDiv, 'Le jeton ne peut pas être vide');
			isOkay = false;
		} 
		if(isNull($scope.groupeToJoin.password)) { 
			var passDiv = $('#passwordGroupe').closest('.form-group');
			displayFormatError(passDiv, 'Le mot de passe ne peut pas être vide');
			isOkay = false;
		}
		return isOkay;
	};
	
	var displayFormatOk = function(parentDiv) {
		parentDiv.children('input').addClass("format-ok-message");
	}
	
	var displayFormatError = function(parentDiv, message) {
		parentDiv.append('<div class=\'col-sm-offset-4 col-sm-8 format-error-message\'>' + message + '</div>');
		parentDiv.children('input').addClass("format-error-message-input");
	}
	
}]);