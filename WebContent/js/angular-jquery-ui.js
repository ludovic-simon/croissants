var jqueryUiAngularModule = angular.module('angular-jquery-ui', []);

jqueryUiAngularModule.directive('datepicker', function ($parse) {
    return function (scope, element, attrs, controller) {
        var ngModel = $parse(attrs.ngModel);
        $(function(){
            element.addClass("dateField");
        	element.datepicker({
            	dateFormat: 'dd/mm/yy',
            	onSelect:function (dateText, inst) {
                    scope.$apply(function(scope){
                        // Change binded variable
                        ngModel.assign(scope, dateText);
                    });
               }
            });
        });
    }
});