
function bDay() {
    return function(contact) {
		if(contact.birthDay==null || contact.birthDay == ""){
			return "";
		} else {
			return contact.birthDay + "/" + contact.birthMonth + "/" + contact.birthYear;
		}
	};
}

angular.module('avaliacandidatos').filter('bDay', bDay)