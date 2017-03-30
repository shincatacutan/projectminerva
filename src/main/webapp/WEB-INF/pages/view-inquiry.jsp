<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="includes/head.jsp" />
<script src="<c:url value ="/resources/js/inquiry-common.js " />"></script>
<script>
	$(function() {
		initAddInquiryReply();
		initCloseInquiry();
		disableEnterKey();
		textAreaAction("#reply_pop");
		$("#home")
				.click(
						function() {
							window.location = "${pageContext.request.contextPath}/inquiry/view";
						});

		$("#reassign_btn").on("click", function() {
			$("#changeLOB").modal("show");
			loadSMEs("#newLob");

		})
		$("#selectedFile").change(function() {
			$("#fileName").val($("#selectedFile")[0].files[0]['name']);
		});
		
		
	});
</script>
<div class="container-fluid text-center main-container">
	<div class="row content">
		<div class="col-sm-1"></div>
		<div class="col-sm-10 text-left">
			<c:choose>
				<c:when test="${not empty (message)}">
					<div class="alert alert-dismissible alert-warning">
						<button type="button" class="close" data-dismiss="alert">&times;</button>
						<c:out value="${message}" />
					</div>
				</c:when>
			</c:choose>

			<div class="well bs-component">
				<form class="form-horizontal" role="form" id="inquiry-view-form">
					<fieldset>
						<legend>View Inquiry</legend>
						<div class="form-group">
							<label class="col-sm-2 control-label">Line of Business</label>
							<div class="col-sm-10">

								<input type="text" id="line-of-business" class="form-control"
									readonly
									value="<c:out value='${inquiry.lineOfBusiness.name}'/>" /> <input
									type="hidden" class="form-control" placeholder="ID"
									value="<c:out value='${inquiry.id}'/>" id="inquiryId" name="inquiryId" />
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label" for="author_pop">Author</label>
							<div class="col-sm-10">
								<input type=text class="form-control" placeholder="Author"
									value="<c:out value='${inquiry.createUser.fullName}'/>"
									readonly="readonly" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="date_pop">Date
								Submitted</label>
							<div class="col-sm-10">
								<input type="text" class="form-control"
									placeholder="Date Submitted"
									value="<c:out value='${inquiry.createDate}'/>"
									readonly="readonly" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">Title</label>
							<div class="col-sm-10">
								<input type=text class="form-control" placeholder="Title"
									value="<c:out value='${inquiry.title}'/>" readonly="readonly" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">Inquiry Body</label>
							<div class="col-sm-10">
								<textarea class="form-control" rows="3" placeholder="Inquiry"
									readonly="readonly"><c:out value='${inquiry.body}' /></textarea>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label">Assigned SME</label>
							<div class="col-sm-10">
								<input type=text class="form-control" placeholder="SME"
									value="<c:out value='${inquiry.assignedSME.fullName}'/>" readonly="readonly" />
							</div>
						</div>
						
						<c:if test="${not empty (inquiry.path)}">
							<div class="form-group">
								<label class="col-sm-2 control-label"></label>
								<div class="col-sm-10">
									<a
										href="${pageContext.request.contextPath}/downloadAttach?inqId=${inquiry.id}">View
										Attachment<span class="glyphicon glyphicon-download-alt"></span>
									</a>
								</div>
							</div>
						</c:if>
						<div class="form-group">
							<label class="col-sm-2 control-label">Status</label>
							<div class="col-sm-10">
								<c:choose>
									<c:when test="${inquiry.status==0}">
										<input type=text class="form-control" placeholder="Status"
											value="New" readonly="readonly" />
									</c:when>
									<c:when test="${inquiry.status==1}">
										<input type=text class="form-control" placeholder="Status"
											value="Open" readonly="readonly" />
									</c:when>
									<c:when test="${inquiry.status==2}">
										<input type=text class="form-control" placeholder="Status"
											value="Closed" readonly="readonly" />
									</c:when>
								</c:choose>

							</div>
						</div>
						<div class="panel panel-default">
							<div class="panel-heading">
								<h3 class="panel-title">Inquiry Reply</h3>
							</div>
							<div class="panel-body">
								<c:if test="${inquiry.status!=2}">
									<div class="reply-div">
										<div class="form-group">
											<label class="col-sm-2 control-label" for="reply_pop">Reply</label>
											<div class="col-sm-10">
												<textarea class="form-control" id="reply_pop" rows="4"
													placeholder="Reply" name="reply" required></textarea>
												<span id="availableCharSpan"><span id="availableChar">500</span>
													characters remaining</span> <br />

											</div>
										</div>


										<div class="form-group">
											<label for="selectedFile" class="col-sm-2 control-label"></label>
											<div class="col-sm-10">
												<span class="btn btn-file btn-info" id="replyattach">Add Attachment:<br />
													<input type="file" name="selectedFile"
													class="btn btn-default" id="selectedFile" required /></span> <input
													type="hidden" class="form-control" id="fileName"
													name="fileName" placeholder="File name" readonly="readonly" /><br />
												<input type="hidden" class="form-control" id="inquiryId"
													name="inquiryId" readonly="readonly" /> <br />
												<c:if test="${isInquiryAdmin==true || isUploader==true}">
													<button type="button" class="btn btn-primary"
														id="save_inq_pop">Add</button>
													<button type="reset" class="btn btn-default">Clear</button>
												</c:if>
											</div>
										</div>
									</div>

								</c:if>

								<div class="form-group">
									<label class="col-sm-2 control-label">History</label>
									<div class="col-sm-10">
										<table class="table table-striped table-hover"
											id="reply-table">
											<thead>
												<tr>
													<th>Reply</th>
													<th>Create Date</th>
													<th>Create User</th>
													<th>Attachment</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach var="reply" items="${replies}">
													<tr>
														<td><c:out value='${reply.replyBody}' /></td>
														<td><c:out value='${reply.createDate}' /></td>
														<td><c:out
																value='${reply.createUser.firstName} ${reply.createUser.lastName}' /></td>
														<td><c:if test="${not empty reply.path}">
																<a
																	href="${pageContext.request.contextPath}/downloadRepAttach?repId=${reply.id}"><span
																	class="glyphicon glyphicon-download-alt"></span></a>
															</c:if></td>
													</tr>
												</c:forEach>
											</tbody>

										</table>
									</div>
								</div>
							</div>
						</div>
						<div class="form-group ">
							<div class="col-lg-offset-2 col-lg-10">
								<button id="home" type="button" class="btn btn-primary">
									Inquiry List</button>

								<c:if test="${isInquiryAdmin==true}">
									<c:if test="${inquiry.status!=2}">
										<button id="close_inq_pop" type="button" class="btn btn-info">Set
											as Closed</button>

										<button id="reassign_btn" type="button"
											class="btn btn-success">Assign to SME</button>
									</c:if>
								</c:if>

								<c:if test="${isInquiryAdmin==true || isUploader==true}">
									<button id="delete_inq_pop" type="button"
										class="btn btn-danger">Delete</button>
								</c:if>

							</div>
						</div>
					</fieldset>
				</form>

			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="changeLOB" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<!-- Modal Header -->
			<div class="modal-header btn-info">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span> <span class="sr-only">Close</span>
				</button>
				<h4 id="myModalLabel">Assign To SME</h4>
			</div>

			<!-- Modal Body -->
			<div class="modal-body">

				<form action="${pageContext.request.contextPath}/updateInqLOB"
					method="POST" class="form-horizontal" role="form"
					id="inquiry-view-form">

					<div class="form-group">
						<label class="col-sm-2 control-label" for="author_pop">Select</label>
						<div class="col-sm-10">
							<input type="hidden" value="<c:out value='${inquiry.id}'/>"
								id="id_change" name="inqToChange" /> <select
								class="input-group bootstrap-tagsinput form-control" id="newLob"
								name="newLob">
								<option></option>
							</select>
						</div>
					</div>
					<div class="right-buttons">
						<button type="button" class="btn btn-default" data-dismiss="modal">Exit</button>
						<button id="close_inq_pop" type="submit" class="btn btn-primary">Assign</button>
					</div>
				</form>
			</div>

		</div>
	</div>
</div>

<jsp:include page="includes/foot.jsp" />