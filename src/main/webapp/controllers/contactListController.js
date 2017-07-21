var contactListController = function($scope, $http, contactService) {
	$scope.contacts = [];
	$scope.preDeletedContact = {};
	$scope.search_filter = "";
	$scope.loading = false;

	$scope.init = function() {
		$scope.listAllContacts();
	};
	
	$scope.listAllContacts = function() {

	    var config = {};
        $scope.loading = true;

	    if($scope.search_filter.trim().length > 0)
	        config.params = {filter:$scope.search_filter};

        contactService.listAll(config).then(function(result) {
            $scope.contacts = result.data;
            $scope.loading = false;
        }, function(result) {
            alert(result.data.message);
            console.log(result.data);
            $scope.loading = false;
        });

	};

	$scope.preDelete = function(contact) {
		$scope.preDeletedContact = contact;
		$('#myModal').modal('show');
	};

	$scope.clearFilter = function() {
	    $scope.search_filter = "";
		$scope.listAllContacts();
	}

	$scope.delete = function() {
		if($scope.preDeletedContact != null) {

		    contactService.deleteById($scope.preDeletedContact.id).then(function(result) {
                $scope.listAllContacts();
                $('#myModal').modal('hide');
            }, function(result) {
                $('#myModal').modal('hide');
                alert(result.data.message);
                console.log(result.data);
            });
		}
	};
};

angular.module('avaliacandidatos').controller("contactListController", contactListController);