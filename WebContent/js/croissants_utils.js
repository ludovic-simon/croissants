//STRING

//Ajout de la methode startsWith sur String
if(!String.prototype.startsWith){
    String.prototype.startsWith = function (str) {
        return this.indexOf(str) == 0;
    }
}

//ARRAY
if(!Array.prototype.remove){
	Array.prototype.remove = function (value) {
        var idx = this.indexOf(value);
        if(idx != -1){
        	return this.splice(idx, 1);
        }
        return false;
    }
}

//NULL / EMPTY

function isNull(value){
	return value == null || value == '' || value == 'null' || value == {};
}

function isNotNull(value){
	return !isNull(value);
}

function isEmpty(value){
	return value == null || value == '' || value == 'null' || value == {};
}

function isNotEmpty(value){
	return !isEmpty(value);
}

function isListEmpty(value){
	return value == null || value.length == 0;
}

function isBooleanTrue(value){
	return value == "true" || value == true;
}

function isBooleanFalse(value){
	return !isBooleanTrue();
}

function getEmptyIfNull(value){
	if(value == null){
		return "";
	} else {
		return value;
	}
}

function getEmptyIfNullExpression(value, checkNullExpression){
	if(checkNullExpression == null){
		return "";
	} else {
		return value;
	}
}

//REST / JSON

function extractObjectFromData(data){
	//Cas d'un retour vide
	if(data == null || data == "null" || data == ""){
		return new Object();
	}
	//Cas d'un retour de liste (plus besoin avec Jackson)
	//var firstProp = data[Object.keys(data)[0]];
	//if(Array.isArray(firstProp)){
		//return firstProp;
	//}
	//Cas d'un retour d'un element ou d'une liste d'un element
	return data;
}

function evalListParameterForRequest(paramName, paramListValue){
	var listParameterValue = "";
	var prefix = "";
	for(var i = 0; i < paramListValue.length; i++){
		if(paramListValue[i] != null){
			listParameterValue += prefix + paramName + "=" + paramListValue[i];
		}
		prefix = "&";
	}
	return listParameterValue;
}

//GESTION DES DATES

function getTodayDateDDMMYYYY(){
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!
	var yyyy = today.getFullYear();
	if(dd < 10) {
	    dd = "0" + dd;
	} 
	if(mm < 10) {
	    mm = '0' + mm
	} 
	return dd + '/' + mm + '/' + yyyy;
}

function getTodayDateIso(){
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!
	var yyyy = today.getFullYear();
	if(dd < 10) {
	    dd = "0" + dd;
	} 
	if(mm < 10) {
	    mm = '0' + mm
	} 
	return yyyy + '-' + mm + '-' + dd;
}

function formatDateDDMMYYYYToIso(inputDate){
	if(inputDate == null || inputDate == ""){
		return "";
	}
	var isoDate = inputDate.substring(6,10) + "-" + inputDate.substring(3,5) + "-" + inputDate.substring(0,2);
	return isoDate;
}

function formatDateIsoToDDMMYYYY(inputDate){
	if(inputDate == null || inputDate == ""){
		return "";
	}
	var isoDate = inputDate.substring(8,10) + "/" + inputDate.substring(5,7) + "/" + inputDate.substring(0,4);
	return isoDate;
}

//TIMER

var sdevTimers = {};

function callOnceAfterDelay(timerName, functionToCall, timeMillis){
	var timer = sdevTimers[timerName];
	if(timer != null){
		window.clearTimeout(timer);
	}
	timer = window.setTimeout(functionToCall, 1000);
	sdevTimers[timerName] = timer;
}

//GESTION UI

var uid = 0;

function getNewUid(){
	uid ++;
	return "component_" + uid;
}

function handleError(data, status, headers, config){
	var errorMessage = data;
	if(data.startsWith("Erreur technique")){
		//Gestion de l'erreur technique : affiche "Erreur technique" et log l'erreur en console
		errorMessage = "Erreur technique";
		console.log(data);
	}
	showError(errorMessage);
}

function showDialog(dialogTitle, message){
	var dialogId = getNewUid();
	
	var modalDialog = 
	'<div class="modal fade" id="' + dialogId + '" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">' +
	  '<div class="modal-dialog">' +
	    '<div class="modal-content">' +
		   '<div class="modal-header">' +
	        '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>' +
	        '<h4 class="modal-title" id="myModalLabel">' + dialogTitle + '</h4>' +
	      '</div>' +
	      '<div class="modal-body">' +
	        message +
	      '</div>' +
	      '<div class="modal-footer">' +
	        '<button type="button" class="btn btn-primary" data-dismiss="modal">Fermer</button>' +
	      '</div>' +
	    '</div>' +
	  '</div>' +
	'</div>';
	$("body").append(modalDialog);
	
	$("#" + dialogId).on('hidden.bs.modal', function (e) {
		$("#" + dialogId).remove();
	});
	
	$("#" + dialogId).modal();
	
	return dialogId;
}

function showInfo(message){
	showDialog("Information", message);
}

function showError(message){
	showDialog("Erreur", message);
}

function showWarning(dialogTitle, message, okFunction, functionArgs){
	var dialogId = showDialog(dialogTitle, message);
	
	var additionalButtons = '<button type="button" id="' + dialogId + '_okBtn" class="btn btn-warning"  data-dismiss="modal">OK</button>';
	$("#" +  dialogId).find('.modal-footer').append(additionalButtons);
	
	
	$("#" + dialogId + "_okBtn").bind('click', function() {
		okFunction.apply(this, functionArgs);
	});
}

function showActionFeedback(actionMessage){
	var dialogId = getNewUid();
	$("body").append("<span id=\"sdevActionFeedbackBox" + dialogId + "\" class=\"sdevActionFeedbackBox\">" + actionMessage + "</span>");
	$("#sdevActionFeedbackBox" + dialogId).position({
        of: window,
        my: "right bottom",
        at: "right bottom"
      });
	$("#sdevActionFeedbackBox" + dialogId).effect(
			"fade", null, 4000,
			function(){$(this).remove();}
	);
}


function createCookie(name,value,days) {
    if (days) {
        var date = new Date();
        date.setTime(date.getTime()+(days*24*60*60*1000));
        var expires = "; expires="+date.toGMTString();
    }
    else var expires = "";
    document.cookie = name+"="+value+expires+"; path=/";
}

function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}

function eraseCookie(name) {
    createCookie(name,"",-1);
}

function saveUtilisteurInCookies(idUtilisateur) {
	console.log("SAVE COOKIE : " +idUtilisateur );
	createCookie("idUtilisateur", idUtilisateur);
}

function getUtilisateurFromCookies () {
	return readCookie("idUtilisateur");
}

function eraseUtilisateurFromCookies() {
	eraseCookie("idUtilisateur");
}