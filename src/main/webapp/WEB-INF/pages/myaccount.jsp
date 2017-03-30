<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="includes/head.jsp" />
<script src="<c:url value ="/resources/js/account.js " />"></script>
<div class="container-fluid text-center main-container">
	<div class="row content">
		<div class="col-sm-1"></div>

		<div class="col-sm-10 text-left">
			<c:choose>
				<c:when test="${not empty (message)}">
					<div class="alert alert-dismissible alert-custom">
						<button type="button" class="close" data-dismiss="alert">&times;</button>
						<c:out value="${message}" />
					</div>
				</c:when>
			</c:choose>
			<div class="well bs-component">
				<form class="form-horizontal">
					<fieldset>
						<legend>My Account</legend>
						<div class="form-group">
							<label class="col-lg-2 control-label">Username</label>
							<div class="col-lg-10">
								<input type="text" class="form-control" disabled="disabled"
									value="<c:out value="${employee.username }" />">
							</div>
						</div>

						<div class="form-group">
							<label for="inputAuthor" class="col-lg-2 control-label">Name</label>
							<div class="col-lg-10">
								<input type="text" class="form-control" disabled="disabled"
									value="<c:out value="${employee.lastName }"/>, <c:out value="${employee.firstName }" />">
							</div>
						</div>

						<div class="form-group">
							<label for="inputAuthor" class="col-lg-2 control-label">Employee
								ID</label>
							<div class="col-lg-10">
								<input type="text" class="form-control" disabled="disabled"
									value="<c:out value="${employee.empid }"/>" />
							</div>
						</div>

						<div class="form-group">
							<label for="inputAuthor" class="col-lg-2 control-label">Email
								Address</label>
							<div class="col-lg-10">
								<input type="text" class="form-control" disabled="disabled"
									value="<c:out value="${employee.email }"/>" />
							</div>
						</div>

						<div class="form-group">
							<label for="inputAuthor" class="col-lg-2 control-label">Roles</label>
							<div class="col-lg-10">

								<c:forEach items="${employee.userRole}" var="role">
									<input type="text" class="form-control" disabled="disabled"
										value="<c:out value="${role.role }"/>" />
								</c:forEach>

							</div>
						</div>
						<div class="form-group">
							<div class="col-lg-10 col-lg-offset-2">
								<button class="btn btn-primary" id="change_password"
									data-toggle="modal">Change Password</button>
							</div>
						</div>
					</fieldset>
				</form>
			</div>

			<!-- Modal -->
			<div class="modal fade" id="viewModal" tabindex="-1" role="dialog"
				aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<!-- Modal Header -->
						<div class="modal-header btn-info">
							<button type="button" class="close" data-dismiss="modal">
								<span aria-hidden="true">&times;</span> <span class="sr-only">Close</span>
							</button>
							<h4 class="modal-title" id="myModalLabel">Change Password</h4>
						</div>

						<!-- Modal Body -->
						<div class="modal-body">

							<form class="form-horizontal" id="new-pass-form" method="post"
								action="${pageContext.request.contextPath}/changePassword">
								<div class="form-group">
									<label class="col-sm-4 control-label" for="newPassString">New
										Password</label>
									<div class="col-sm-8">
										<input type="password" class="form-control" id="author_pop"
											placeholder="New Password" name="newPassString" required />
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-4 control-label" for="repeatPass">Retype
										Password</label>
									<div class="col-sm-8">
										<input type="password" class="form-control" id="date_pop"
											placeholder="New Password" name="repeatPass" required />
									</div>
								</div>


							</form>
						</div>

						<!-- Modal Footer -->
						<div class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal">Exit</button>
							<button type="button" class="btn btn-primary" id="save_pass">Save</button>
						</div>
					</div>

				</div>

			</div>
		</div>
	</div>
</div>
<jsp:include page="includes/foot.jsp" />