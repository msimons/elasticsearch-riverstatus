var riverApp = angular.module("riverApp", ['ngRoute','ngResource','angular-lodash','relativeDate']);

//Do configuration and routing here

riverApp.config(function($routeProvider){
    console.log($routeProvider);
    $routeProvider
        .when("/",{
            controller: "riverController",
            templateUrl: "js/views/riverList.html"
        });

    $routeProvider.otherwise({"redirectTo": "/"});  //.otherwise("/"); //does not work
});