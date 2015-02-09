var profilViewController = angular.module('profilViewController', []);
 
profilViewController.controller('ProfilViewCtrl', ['$scope', '$http', '$location', '$route',
function ProfilViewCtrl($scope, $http, $location, $route) {
	
//	$scope.utilisateur = getUtilisateurFromCookies();
	$scope.currentPassword = "";
	$scope.newPassword1 = "";
	$scope.newPassword2 = "";
	
	
	$scope.init = function() {
		console.log('init profil!!');
		console.log($scope.utilisateur);
	};
	
	$scope.changerMotDePasse = function () {
		if(isNewPasswordValid()) {
			console.log("MaJ du mot de passe de l'utilisateur : " + $scope.utilisateur);
			$http.post('/croissants/rest/utilisateurService/changerMotDePasse?idUtilisateur='+$scope.utilisateur.idUtilisateur+"&ancienMdp="+$scope.currentPassword+"&nouveauMdp="+$scope.newPassword1).
			  success(function(data, status, headers, config) {
				   showActionFeedback("Mot de passe mis à jour");
				   window.location.href = "/croissants/views/croissants.html";
			  }).
			  error(function(data, status, headers, config) {
				  handleError(data, status, headers, config);
			  });
		}
		
	};
	
	
	var isNewPasswordValid = function () {
		var ok = true;
		//validité du mot de passe		
		if(isNull($scope.newPassword1) || $scope.newPassword1.length < 5 ) {
			var parentDiv = $('#inputPassword').closest('.form-group');
			displayFormatError(parentDiv, 'Les mots de passes doivent faire 5 caractères minimum');
			ok = false;
		} else if(isNull($scope.newPassword2) || $scope.newPassword2.length < 5) {
			var parentDiv = $('#inputPassword2').closest('.form-group');
			displayFormatError(parentDiv, 'Les mots de passes doivent faire 5 caractères minimum');
			ok = false;
		} else if(!angular.equals($scope.newPassword1, $scope.newPassword2)) {
			var parentDiv = $('#inputPassword2').closest('.form-group');
			displayFormatError(parentDiv, 'Mots de passe différents');
			ok = false;
		} else {
			displayFormatOk($('#inputPassword').parent());
			displayFormatOk($('#inputPassword2').parent());
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

	$scope.init();
	
}]);