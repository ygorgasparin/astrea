var contactListController;

contactListController = function($scope, $http) {
	$scope.contacts = [];
	$scope.preDeletedContact = {};

	$scope.init = function() {
		$scope.listAllContacts();
	};
	
	$scope.listAllContacts = function() {

	    console.log("Get contatos");

        $http.get('/contacts').then(function(result) {
            console.log("Sucesso");
            console.log(result);
            $scope.contacts = result.data;
        }, function(result) {
            console.log("Error");
            console.log(result);
        });

	};

	$scope.edit = function(contact) {

	};

	$scope.preDelete = function(contact) {
		$scope.preDeletedContact = contact;
		$('#myModal').modal('show');
	};

	$scope.delete = function() {
		if($scope.preDeletedContact != null) {

		    $http.delete('/contacts/' + $scope.preDeletedContact.id).then(function(result) {
                $scope.listAllContacts();
                $('#myModal').modal('hide');
            }, function(result) {
                console.log("Error");
                console.log(result);
                $('#myModal').modal('hide');
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