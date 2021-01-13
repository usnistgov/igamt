/**
 * Created by haffo on 9/11/17.
 */
angular.module('igl').controller('ChooseToolCtrl', [ '$rootScope','$scope', '$mdDialog', '$http', '$location','$window', function($rootScope,$scope, $mdDialog, $http, $location, $window) {


$scope.stay = function () {
    //$location.path('/registration');
    $mdDialog.hide(false);
}

$scope.redirect = function(){
    $mdDialog.hide(true);
}

}]);
