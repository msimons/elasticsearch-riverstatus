app.factory("feederService", function($http){

    return {

        getFeederInfo: function() {
            return $http.get("/_feederstatus/info").then(
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