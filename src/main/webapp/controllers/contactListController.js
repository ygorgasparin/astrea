var contactListController;

contactListController = function($scope, $http) {
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

        $http.get('/contacts', config).then(function(result) {
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

		    $http.delete('/contacts/' + $scope.preDeletedContact.id).then(function(result) {
                $scope.listAllContacts();
                $('#myModal').modal('hide');
            }, function(result) {
                $('#myModal').modal('hide');
                alert(result.data.message);
                console.log(result.data);
            });
		}
	};

	$scope.bday = function(c) {
		if(c.birthDay==null || c.birthDay == ""){
			return "";
		} else {
			return c.birthDay + "/" + c.birthMonth + "/" + c.birthYear;
		}
	};
};

angular.module('avaliacandidatos').controller("contactListController", contactListController);