<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<head>
<title>cerebra</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon"
	href="<c:url value="/resources/optum_favicon.png" />">

<link rel="<c:url value="/resources/css/bootstrap.min.css" />" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/jquery.dataTables.css" />" />
<link href="<c:url value="/resources/css/bootstrap-tagsinput.css" />"
	rel="stylesheet">
<link href="<c:url value="/resources/css/datepicker.css" />"
	rel="stylesheet">

<link href="<c:url value="/resources/css/bootstrap.css" />"
	rel="stylesheet" />
<link href="<c:url value="/resources/css/bootstrap-switch.css" />"
	rel="stylesheet">
<link href="<c:url value="/resources/css/index.css" />" rel="stylesheet">

<script type="text/javascript"
	src="<c:url value ="/resources/js/jquery-1.11.2.js" />"></script>
<script src="<c:url value ="/resources/js/bootstrap.min.js" />"></script>
<script type="text/javascript"
	src="<c:url value ="/resources/js/jquery-validate-min.js" />"></script>
<script type="text/javascript"
	src="<c:url value ="/resources/js/bootstrap-tagsinput.min.js" />"></script>
<script type="text/javascript"
	src="<c:url value ="/resources/js/jquery.dataTables.min.js" />"></script>

<script src="<c:url value ="/resources/js/bootstrap-datepicker.js" />"></script>
<script src="<c:url value ="/resources/js/bootbox.min.js" />"></script>
<script src="<c:url value ="/resources/js/bootstrap-switch.js" />"></script>
<script
	src="<c:url value ="/resources/js/bootstrap-typeahead.min.js" />"></script>
<script src="<c:url value ="/resources/js/jquery.blockUI.js" />"></script>
<script src="<c:url value ="/resources/js/bootstrap-notify.min.js" />"></script>

<script>
	var contextPath = "${pageContext.request.contextPath}";
	var isInquiryAdmin = "${isInquiryAdmin}";
	var isUploader = "${isUploader}";
	var loggedUser = "${employee.firstName}"+" ${employee.lastName}"
	var disableEnterKey = function() {
		$('form').on('keyup keypress', function(e) {
			var keyCode = e.keyCode || e.which;
			if (keyCode === 13) {
				e.preventDefault();
				return false;
			}
		});
	}

	$(document).ajaxStart(function() {
		$.blockUI({
			message : '<h2>Loading...</h2>',
			theme : false,
			baseZ : 3000,
			css : {
				border : '3px solid #772953',
				backgroundColor : '#eee',
				color : '#000'
			}
		})
	}).ajaxStop($.unblockUI);
	jQuery.extend(jQuery.fn, {
		categories : function(data) {
			var input = $(this);
			input.find('option').remove().end();
			input.append($('<option>', ""));
			$.each(data, function(i, data) {
				input.append($('<option>', {
					value : data.directoryName,
					text : data.name,
					id : data.id
				}));
			});
		},
		lineofbusiness : function(data) {
			var input = $(this);
			input.find('option').remove().end();
			input.append($('<option>', ""));
			$.each(data, function(i, data) {
				input.append($('<option>', {
					value : data.id,
					text : data.name,
					id : data.id
				}));
			});
		},
		smes : function(data){
			var input = $(this);
			input.find('option').remove().end();
			input.append($('<option>', ""));
			$.each(data, function(i, data) {
				input.append($('<option>', {
					value : data.username,
					text : data.fullName
				}));
			});
		}

	});

	var showNotifyMsg = function(msg) {
		$.notify({
			// options
			message : msg
		}, {
			// settings
			type : 'danger',
			offset : 60,
			delay : 7000,
			placement : {
				from : "top",
				align : "center"
			}
		});
	}
</script>
</head>
<body>
	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#myNavbar">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="${pageContext.request.contextPath}"><img
					alt="Optum Logo"
					src="<c:url value="/resources/images/OptumRx.png" />" /><span>
						&nbsp;| <span style="color: #EA683B">cerebra</span>
				</span></a>
			</div>

			<c:choose>
				<c:when test="${isLoggedin==true}">
					<div class="collapse navbar-collapse" id="myNavbar">
						<ul class="nav navbar-nav">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false"><span
									class="glyphicon glyphicon-tags"></span>Documents <span
									class="caret"></span> </a>
								<ul class="dropdown-menu" role="menu">
									<li><a
										href="${pageContext.request.contextPath}/library/search"><span
											class="glyphicon glyphicon-search"></span>Search Document</a></li>
									<!-- 	<li class="divider"></li>
								<li><a href="${pageContext.request.contextPath}/library/search-tools"><span
											class="glyphicon glyphicon-th-list"></span>Search
											Tool/Reference</a></li> -->
									<c:if test="${isRoleUploader==true}">
										<li class="divider"></li>
										<li><a
											href="${pageContext.request.contextPath}/library/add"><span
												class="glyphicon glyphicon-upload"></span>Add Document</a></li>
									</c:if>
								</ul></li>

							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false"><span
									class="glyphicon glyphicon-question-sign"></span>Inquiries<span
									class="caret"></span> </a>
								<ul class="dropdown-menu" role="menu">
									<li><a
										href="${pageContext.request.contextPath}/inquiry/view"><span
											class="glyphicon glyphicon-inbox"></span>View Inquiries</a></li>
									<li class="divider"></li>
									<li><a
										href="${pageContext.request.contextPath}/inquiry/add"><span
											class="glyphicon glyphicon-pencil"></span>Add Inquiry</a></li>

								</ul></li>
							<c:if test="${isInquiryAdmin==true || isRoleUploader==true || isAdmin==true}">

								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-expanded="false"><span
										class="glyphicon glyphicon-th-list"></span>Reports<span
										class="caret"></span> </a>
									<ul class="dropdown-menu" role="menu">
										<li><a
											href="${pageContext.request.contextPath}/document/report"><span
												class="glyphicon glyphicon-th-large"></span>Document Report</a></li>
										<li class="divider"></li>
										<li><a
											href="${pageContext.request.contextPath}/inquiry/report"><span
												class="glyphicon glyphicon-th"></span>Inquiry Report</a></li>
									</ul></li>
							</c:if>
							<c:if test="${isAdmin==true}">
								<li class="dropdown"><a
									href="${pageContext.request.contextPath}/admin"><span
										class="glyphicon glyphicon-wrench"></span>Settings</a></li>
							</c:if>
						</ul>
						<ul class="nav navbar-nav navbar-right">
							<li class="dropdown"><div class="btn-group">
									<a href="#" class="btn btn-default"><span
										class="glyphicon glyphicon-user"></span> <c:out
											value="${employee.firstName }" />&nbsp;<c:out
											value="${employee.lastName }" /></a> <a href="#"
										class="btn btn-default dropdown-toggle" data-toggle="dropdown"><span
										class="caret"></span></a>
									<ul class="dropdown-menu">
										<li><a
											href="${pageContext.request.contextPath}/myaccount"><span
												class="glyphicon glyphicon-info-sign"></span>My Account</a></li>
										<li class="divider"></li>
										<li><a
											href="${pageContext.request.contextPath}/myunsent"><span
												class="glyphicon glyphicon-envelope"></span>My Unsent Emails</a></li>
										<li class="divider"></li>
										<li><a href="${pageContext.request.contextPath}/logout"><span
												class="glyphicon glyphicon-log-out"></span>Logout</a></li>
									</ul>
								</div></li>
						</ul>
					</div>

				</c:when>
			</c:choose>
		</div>
	</nav>