riverApp.factory("riverService", function($http){

    return {

        getRiverInfo: function() {
            return $http.get("/_riverstatus/info").then(
                //Success
                function(response){
                    return response.data;
                } ,
                // Error
                function(err) {
                    console.log(err);
                    return err;
                }
            );
        }
    };
});