<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="includes/head.jsp" />
<script src="<c:url value ="/resources/js/inquiry-common.js " />"></script>
<script src="<c:url value ="/resources/js/inquiry.js" />"></script>
<script>
	var hasDirtyBit = false;
</script>
<div class="container-fluid text-center main-container">
	<div class="row content">
		<div class="col-sm-1"></div>
		<div class="col-sm-10 text-left">
			<div class="well bs-component">

				<ul class="nav nav-tabs">
					<li class="active"><a aria-expanded="true" href="#myinq"
						data-toggle="tab">My Inquiries</a></li>
					<li class=""><a aria-expanded="false" href="#allinq"
						data-toggle="tab">All Inquiries</a></li>

				</ul>
				<div id="myTabContent" class="tab-content">
					<div class="tab-pane fade active in" id="myinq">
						<h3 class="well-title">My Inquiries</h3>
						<hr class="well-hr" />
						<div id="inqTableSpace">
							<table class="display" id="inquiry_grid"></table>
						</div>

						<input type="button" id="view_btn" value="View"
							class="btn btn-primary" data-toggle="modal" />
						<c:if test="${isInquiryAdmin==true}">
							<input type="button" id="reply_btn" class="btn btn-info"
								value="Reply" data-toggle="modal" />
						</c:if>
					</div>
					<div class="tab-pane fade" id="allinq">
						<form class="form-horizontal">
							<fieldset>
								<legend>Search Inquiries</legend>
								<div class="form-group">
									<label for="startDate" class="col-lg-2 control-label">Start
										Date</label>
									<div class="col-lg-4 col-sm-6 date input-group"
										data-provide="datepicker">
										<input type="text" class="form-control" id="startDate"
											name="startDate" placeholder="Start" />
										<div class="input-group-addon">
											<span class="glyphicon glyphicon-calendar"></span>
										</div>
									</div>
								</div>

								<div class="form-group">
									<label for="endDate" class="col-lg-2 control-label">End
										Date</label>
									<div class="col-lg-4 col-sm-6  date input-group"
										data-provide="datepicker">
										<input id="endDate" name="endDate" class="form-control"
											placeholder="End" />
										<div class="input-group-addon">
											<span class="glyphicon glyphicon-calendar"></span>
										</div>
									</div>
								</div>

								<div class="form-group">
									<label for="addCat_in" class="col-lg-2 control-label">Line
										of Business</label>
									<div class="col-lg-8">
										<select name="lineOfBus" id="lineOfBus"
											class="input-group bootstrap-tagsinput">
											<option></option>
										</select>
									</div>
								</div>
								
								
								<div class="form-group">
									<div class="col-lg-10 col-lg-offset-2">
										<button type="reset" class="btn btn-default">Reset</button>
										<button class="btn btn-primary" id="src_inq_btn">Search</button>
									</div>
								</div>
							</fieldset>
						</form>
						<div id="inqTableSpace2">
							<table class="display" id="src_inquiry_grid"></table>
						</div>
						<input type="button" id="view_btn_2" value="View"
							class="btn btn-primary" data-toggle="modal" />
						<c:if test="${isInquiryAdmin==true}">
							<input type="button" id="reply_btn_2" class="btn btn-info"
								value="Reply" data-toggle="modal" />
						</c:if>
					</div>
				</div>
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
							<h4 class="modal-title" id="myModalLabel">Modal title</h4>
						</div>

						<!-- Modal Body -->
						<div class="modal-body">

							<form class="form-horizontal" role="form" id="inquiry-view-form">
								<c:if test="${isInquiryAdmin==true}">
									<div class="btn-group" id="actions-group">
										<a href="#" class="btn btn-info">Actions</a> <a href="#"
											class="btn btn-info dropdown-toggle" data-toggle="dropdown"><span
											class="caret"></span></a>
										<ul class="dropdown-menu">
											<li><a href="#" id="action-setclosed">Set as Closed</a></li>
											<li class="divider"></li>
											<li><a href="#" id="action-delete">Delete</a></li>
											<li class="divider"></li>
											<li><a href="#" id="action-reassign">Assign to SME</a></li>
										</ul>
									</div>

								</c:if>
								<br />
								<div class="form-group">
									<label class="col-sm-2 control-label" for="author_pop">Author</label>
									<div class="col-sm-10">
										<input type=text class="form-control" id="author_pop"
											placeholder="Author" readonly="readonly" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label" for="date_pop">Date
										Submitted</label>
									<div class="col-sm-10">
										<input type="text" class="form-control" id="date_pop"
											placeholder="Date Submitted" readonly="readonly" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label" for="inquiry_pop">Inquiry</label>
									<div class="col-sm-10">
										<textarea class="form-control" id="inquiry_pop" rows="3"
											placeholder="Inquiry" readonly="readonly"></textarea>
									</div>
								</div>
								<div class="form-group">
									<label for="assigedSme" class="col-lg-2 control-label">Assigned SME</label>
									<div class="col-lg-8">
										<input id="assignedSme" name="assignedSme" class="form-control"
											placeholder="SME" readonly="readonly" />
									</div>
								</div>
								<div class="form-group" id="attach_grp">
									<label class="col-sm-2 control-label"></label>
									<div class="col-sm-10">
										<a href="#" id="attach">View Attachment<span
											class="glyphicon glyphicon-download-alt"></span>
										</a>
									</div>
								</div>
								<div class="well">
									<div class="form-group reply-div">
										<label class="col-sm-2 control-label" for="reply_pop">Reply</label>
										<div class="col-sm-10">
											<textarea class="form-control" name="reply" id="reply_pop" rows="2"
												placeholder="Reply" required></textarea>
											<span id="availableCharSpan"><span id="availableChar">500</span>
												characters remaining</span>
										</div>

									</div>

									<div class="form-group reply-div">
										<label for="selectedFile" class="col-sm-2 control-label"></label>
										<div class="col-sm-10">
											<span class="btn btn-file btn-info">Add Attachment:<br /> <input
												type="file" name="selectedFile" class="btn btn-default"
												id="selectedFile" required /></span> <input type="hidden"
												class="form-control" id="fileName" name="fileName"
												placeholder="File name" readonly="readonly" /><br /><br />
											<input type="hidden" class="form-control" id="inquiryId"
													name="inquiryId" readonly="readonly" /> <br />
											<button type="button" class="btn btn-primary"
												id="save_inq_pop">Add</button>
											<button type="reset" class="btn btn-default">Clear</button>
										</div>
									</div>

									<div class="form-group">
										<label class="col-sm-2 control-label">Reply History</label>
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
													<tr>
														<td>Column content</td>
														<td>Column content</td>
														<td>Column content</td>
														<td>Column content</td>
													</tr>

												</tbody>
											</table>
										</div>
									</div>
								</div>
							</form>
						</div>

						<!-- Modal Footer -->
						<div class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal">Exit</button>

							<button id="close_inq_pop" type="button" class="btn btn-info">Set
								as Closed</button>
						</div>
					</div>
				</div>
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
				<h4 id="myModalLabel">Assign to SME</h4>
			</div>

			<!-- Modal Body -->
			<div class="modal-body">

				<form action="${pageContext.request.contextPath}/updateInqLOB"
					method="POST" class="form-horizontal" role="form">

					<div class="form-group">
						<label class="col-sm-2 control-label" for="author_pop">Select</label>
						<div class="col-sm-10">
							<input type="hidden" id="id_change" name="inqToChange" /> <select
								class="input-group bootstrap-tagsinput form-control" id="newLob"
								name="newLob">
								<option>test</option>
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