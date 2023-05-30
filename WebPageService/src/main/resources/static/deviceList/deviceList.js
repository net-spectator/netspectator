angular.module('app').controller('deviceListController', function ($rootScope, $scope, $http, $localStorage) {

    $scope.loadDeviceList = function () {
        $http.get('http://localhost:8181/core/devices/v1/getAll').then(function (response) {
            $scope.DeviceDTO = response.data;
        });
    };
    $scope.loadDeviceList();
});

