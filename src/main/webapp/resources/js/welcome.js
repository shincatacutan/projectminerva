/**
 * 
 */

$(function() {
	getUser();
	
});

var getUser = function() {
//	var network = new ActiveXObject("WScript.Network");
//	var networkId = network.UserName;
	var networkId = "scatacut";
	$("#username").val(networkId)
	$("#userForm").submit();

}
