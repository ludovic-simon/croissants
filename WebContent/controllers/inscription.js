var inscriptionViewController = angular.module('inscriptionViewController', []);
 
inscriptionViewController.controller('InscriptionViewCtrl', ['$scope', '$http', '$location', '$route',
function InscriptionViewCtrl($scope, $http, $location, $route) {


	$scope.init = function() {

	};

	$scope.init();
	
	$scope.user = new Object();
	$scope.motDePasseBis;
	$scope.spinnerClass = '';
	
	$scope.creerUtilisateur = function () {
	
		if(isUserValid()) {
			$scope.spinnerClass = 'active';
			console.log($scope.user);
			$http.post('/croissants/rest/utilisateurService/creerUtilisateur', $scope.user).
			  success(function(data, status, headers, config) {
				  alert("Success ! Data : "+ data);
				   $scope.spinnerClass = '';
				   window.location.href = "/croissants/views/guest.html#/loginView";
			  }).
			  error(function(data, status, headers, config) {
				  $scope.spinnerClass = '';
				  handleError(data, status, headers, config);
			  });
		}
		
	}
	
	var isUserValid = function () {
		var ok = true;
		//on supprime tous les messages d'erreurs précédents.
		$('.format-error-message').remove();
		
		//on supprime tous les elements "ok" et "erreurs" des inputs
		$('input').each(function() {
			$(this).removeClass("format-ok-message");
			$(this).removeClass("format-error-message-input");
		});
		
		var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		if( $scope.user.email == null || ! re.test($scope.user.email)) {
			var mailDiv = $('#mailInput').closest('.form-group');
			displayFormatError(mailDiv, 'Format invalide');
			ok = false;
		} else {
			displayFormatOk( $('#mailInput').parent());
		}
		
		//validité du mot de passe		
		if(isNull($scope.user.motDePasse) || $scope.user.motDePasse.length < 5 ) {
			var parentDiv = $('#inputPassword').closest('.form-group');
			displayFormatError(parentDiv, 'Les mots de passes doivent faire 5 caractères minimum');
			ok = false;
		} else if(isNull($scope.motDePasseBis) || $scope.motDePasseBis.length < 5) {
			var parentDiv = $('#inputPassword2').closest('.form-group');
			displayFormatError(parentDiv, 'Les mots de passes doivent faire 5 caractères minimum');
			ok = false;
		} else if(!angular.equals($scope.user.motDePasse, $scope.motDePasseBis)) {
			var parentDiv = $('#inputPassword2').closest('.form-group');
			displayFormatError(parentDiv, 'Mots de passe différents');
			ok = false;
		} else {
			displayFormatOk($('#inputPassword').parent());
			displayFormatOk($('#inputPassword2').parent());
		}
		
		//validé du nom
		if($scope.user.nom == null) {
			var parentDiv = $('#inputNom').closest('.form-group');
			displayFormatError(parentDiv, 'Format invalide');
			ok = false;
		} else {
			displayFormatOk($('#inputNom').parent());
		}
		return ok;	
	}
	
	var displayFormatOk = function(parentDiv) {
		parentDiv.children('input').addClass("format-ok-message");
	}
	
	var displayFormatError = function(parentDiv, message) {
		parentDiv.append('<div class=\'col-sm-offset-4 col-sm-8 format-error-message\'>' + message + '</div>');
		parentDiv.children('input').addClass("format-error-message-input");
	}
}]);


