(function () {
    angular
        .module('app', ['ngRoute', 'ngStorage'])
        .config(config);

    function config($routeProvider) {
        $routeProvider
            .when('/deviceList', {
                templateUrl: 'deviceList/deviceList.html',
                controller: 'deviceListController'
            });
    }



})();

angular.module('app').controller('indexController', function ($scope, $http, $location, $localStorage) {

});

