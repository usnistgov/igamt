angular.module('igl').factory('DatatypeLibraryDocumentSvc', function ($q, $http, $httpBackend, userInfoService) {

    var svc = this;
    var dtLibStruct = function (scope, children) {
        this.id = null;
        this.scope = scope;
        this.sectionDescription = null;
        this.sectionContents = null;
        this.children = children;
    };

    
    
    svc.getDataTypeLibraryDocumentByScopesAndVersion = function (scopes, hl7Version) {
        console.log("datatype-library-document/findByScopesAndVersion scopes=" + scopes + " hl7Version=" + hl7Version);
        var scopesAndVersion = {
            "scopes": scopes,
            "hl7Version": hl7Version
        };
        return $http.post(
            'api/datatype-library-document/findByScopesAndVersion', angular.toJson(scopesAndVersion))
            .then(function (response) {
                console.log("getDataTypeLibraryByScopesAndVersion response size=" + response.data.length);
                return angular.fromJson(response);
            });
    };
    

    svc.getDataTypeLibraryDocumentByScopesAndVersion = function (scopes, hl7Version) {
        console.log("datatype-library-document/findByScopesAndVersion scopes=" + scopes + " hl7Version=" + hl7Version);
        var scopesAndVersion = {
            "scopes": scopes,
            "hl7Version": hl7Version
        };
        return $http.post(
            'api/datatype-library-document/findByScopesAndVersion', angular.toJson(scopesAndVersion))
            .then(function (response) {
                console.log("getDataTypeLibraryByScopesAndVersion response size=" + response.data.length);
                return angular.fromJson(response);
            });
    };
    
    
    svc.getDataTypeLibraryDocumentByScope = function (scope) {
        console.log("datatype-library-document/findByScope scope=" + scope);
        return $http.post(
            'api/datatype-library-document/findByScope', scope)
            .then(function (response) {
                console.log("getDataTypeLibraryByScope response=" + response.data.length);
                return angular.fromJson(response);
            });
    };
    
    svc.getDataTypeLibraryDocumentByScopeForAll = function (scope) {
        console.log("datatype-library-document/findByScopeForAll scope=" + scope);
        return $http.post(
            'api/datatype-library-document/findByScopeForAll', scope)
            .then(function (response) {
                console.log("getDataTypeLibraryByScope response=" + response.data.length);
                return angular.fromJson(response);
            });
    };


    svc.create = function (hl7Version, scope, name, ext) {
        var dtlcw = { "hl7Version": hl7Version,
            "scope": scope,
            "name": name,
            "ext": ext,
            "accountId": userInfoService.getAccountID()};
        return $http.post(
            'api/datatype-library-document/create', dtlcw).then(function (response) {
                return angular.fromJson(response)
            });
    };
    
   svc.delete = function (datatypeLibraryDocumentId) {
    	
        return $http.get(
            'api/datatype-library-document/' + datatypeLibraryDocumentId + '/delete').then(function (response) {
                return angular.fromJson(response.data)
            });
    };
    
    svc.save = function (datatypeLibrary) {
        
        return $http.post(
            'api/datatype-library-document/save', angular.toJson(datatypeLibrary)).then(function (response) {
                return angular.fromJson(response.data)
            });
    };
    
    
    return svc;
});