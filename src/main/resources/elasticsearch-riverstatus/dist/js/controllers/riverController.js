riverApp.controller("riverController", function ($scope,riverService){

    var updateFunction = function() {
        riverService.getRiverInfo().then(
            function(data) {
                $scope.riverinfo = data;
                _.delay(updateFunction,5000);
            }
        );
    };

    updateFunction();
});