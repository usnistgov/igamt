/**
 * Created by haffo on 3/9/16.
 */
'use strict';
angular.module('igl').factory('PcService', ['$rootScope', 'ViewSettings', 'ElementUtils', '$http', '$q', 'userInfoService', function($rootScope, ViewSettings, ElementUtils, $http, $q, userInfoService) {
    var PcService = {

        create: function(pc) {
            var delay = $q.defer();
            // table.accountId = userInfoService.getAccountID();
            $http.post('api/profile-components/create', pc).then(function(response) {
                console.log(response);
                var saved = angular.fromJson(response.data);
                delay.resolve(saved);
                return saved;
            }, function(error) {
                delay.reject(error);
            });
            return delay.promise;
        },
        getPc: function(pcId) {
            var delay = $q.defer();
            $http.get('api/profile-components/' + pcId).then(function(response) {
                console.log("-----------------------");
                console.log(response);
                var pc = angular.fromJson(response.data);
                console.log(pc);
                delay.resolve(pc);
            }, function(error) {
                delay.reject(error);
            });
            return delay.promise;
        },


        findAll: function() {
            var delay = $q.defer();
            $http.post('api/profile-components/findAll').then(function(response) {
                var res = angular.fromJson(response.data);
                delay.resolve(res);
            }, function(error) {
                delay.reject(error);
            });
            return delay.promise;
        },
        addPCs: function(pcId, pcs) {
            var delay = $q.defer();

            $http.post('api/profile-components/' + pcId + '/addMult', pcs).then(function(response) {

                console.log(response);
                var saved = angular.fromJson(response.data);
                delay.resolve(saved);
                return saved;
            }, function(error) {
                delay.reject(error);
            });
            return delay.promise;
        },
        save: function(pcLibId, profileComponent) {
            //$rootScope.saved = false;
            var delay = $q.defer();
            //var changes = angular.toJson([]);
            //var data = angular.fromJson({ "changes": changes, "igDocument": igDocument });
            $http.post('api/profile-components/save/' + pcLibId, profileComponent).then(function(response) {
                var saveResponse = angular.fromJson(response.data);

                delay.resolve(saveResponse);
            }, function(error) {
                delay.reject(error);
                $rootScope.saved = false;
            });
            return delay.promise;
        },
        saveAll: function(profileComponents) {
            //$rootScope.saved = false;
            var delay = $q.defer();
            //var changes = angular.toJson([]);
            //var data = angular.fromJson({ "changes": changes, "igDocument": igDocument });
            $http.post('api/profile-components/saveAll/', profileComponents).then(function(response) {
                var saveResponse = angular.fromJson(response.data);

                delay.resolve(saveResponse);
            }, function(error) {
                delay.reject(error);
                $rootScope.saved = false;
            });
            return delay.promise;
        },
        delete: function(pcLibId, profileComponent) {
            //$rootScope.saved = false;
            var delay = $q.defer();
            //var changes = angular.toJson([]);
            //var data = angular.fromJson({ "changes": changes, "igDocument": igDocument });
            $http.post('api/profile-components/delete/' + pcLibId, profileComponent).then(function(response) {
                var saveResponse = angular.fromJson(response.data);

                delay.resolve(saveResponse);
            }, function(error) {
                delay.reject(error);
                $rootScope.saved = false;
            });
            return delay.promise;
        },

    }
    return PcService;
}]);