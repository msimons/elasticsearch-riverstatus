var app = angular.module("app", ['ngRoute','ngResource','angular-lodash','relativeDate']);

//Do configuration and routing here

app.config(function($routeProvider){
    console.log($routeProvider);
    $routeProvider
        .when("/",{
            controller: "feederController",
            templateUrl: "js/views/list.html"
        });

    $routeProvider.otherwise({"redirectTo": "/"});  //.otherwise("/"); //does not work
});