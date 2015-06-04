app.controller("feederController", function ($scope,feederService){

    var updateFunction = function() {
        feederService.getFeederInfo().then(
            function(data) {
                $scope.feeders = data;
                _.delay(updateFunction,5000);
            }
        );
    };

    updateFunction();
});