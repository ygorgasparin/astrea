var contactService = function($http) {

    var base_url = '/contacts';

    return service = {
        save: save,
        listAll: listAll,
        deleteById: deleteById
    };

    function save(contact) {
       return $http.post(base_url, contact);
    }
    function listAll(config) {
        return $http.get(base_url, config);
    }

    function deleteById(id) {
        return $http.delete(base_url + '/' + id);
    }

}

angular.module('avaliacandidatos').factory("contactService", contactService);