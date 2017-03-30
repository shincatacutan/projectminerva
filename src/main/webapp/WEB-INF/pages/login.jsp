<%@page session="true"%>
<jsp:include page="includes/head.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="container-fluid text-center main-container">
	<div class="row content">

		<div class="col-sm-3"></div>

		<div class="col-sm-6 text-left">
			<c:choose>
				<c:when test="${not empty (message)}">
				
					<div class="alert alert-dismissible alert-custom">
						<button type="button" class="close" data-dismiss="alert">&times;</button>
						<c:out value="${message}" />
					</div>
				</c:when>
			</c:choose>
			<div class="well bs-component">
				<form class="form-horizontal" action="${pageContext.request.contextPath}/auth"
					method="post">
					<fieldset>
						<legend>Login</legend>
						<div class="form-group">
							<label for="inputUsername" class="col-lg-2 control-label">Username</label>
							<div class="col-lg-10">
								<input type="text" class="form-control" id="inputUsername"
									placeholder="Username" name="username" required>
							</div>
						</div>
						<div class="form-group">
							<label for="inputPassword" class="col-lg-2 control-label">Password</label>
							<div class="col-lg-10">
								<input type="password" class="form-control" id="inputPassword"
									name="password" placeholder="Password" required>

							</div>
						</div>

						<div class="form-group">
							<div class="col-lg-10 col-lg-offset-2">
								<button type="reset" class="btn btn-default">Reset</button>
								<button type="submit" class="btn btn-primary">Login</button>
							</div>
						</div>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
</div>
<jsp:include page="includes/foot.jsp" />