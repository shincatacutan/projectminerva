<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="includes/head.jsp" />
<script>
	$(function() {
		disableEnterKey();
		getLobList();
		$("#selectedFile").change(function() {
			$("#fileName").val($("#selectedFile")[0].files[0]['name']);
		});
		$("#inq_in").on('change keyup paste', function() {
			var inq_in = $("#inq_in");
			var remarksLength = inq_in.val().length;
			var maxLength = 500;
			if (remarksLength > maxLength) {
				inq_in.val(inq_in.val().substring(0, maxLength));
			}
			$("#availableChar").html(maxLength - inq_in.val().length);
		});
	});
	var getLobList = function() {
		$.ajax({
			url : "${pageContext.request.contextPath}/getLoBList",
			type : "POST",
			accept : 'application/json',
			success : function(data) {
				$('#lineOfBus').lineofbusiness(data);
			},
			error : function(e) {
				// console.log(e);
			}
		});
	}
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

				<form class="form-horizontal"
					action="${pageContext.request.contextPath}/addInquiry"
					method="post" enctype="multipart/form-data">
					<fieldset>
						<legend>Add Inquiry</legend>
						<div class="form-group">
							<label for="inqTitle" class="col-lg-2 control-label">Title</label>
							<div class="col-lg-10">
								<input type="text" class="form-control" id="inq_title_in"
									name="inqTitle" placeholder="Title" required />
							</div>
						</div>

						<div class="form-group">
							<label for="inqBody" class="col-lg-2 control-label">Inquiry</label>
							<div class="col-lg-10">
								<textarea cols="40" rows="3" id="inq_in" name="inqBody"
									class="form-control" placeholder="Inquiry" required></textarea>
								<span id="availableCharSpan"><span id="availableChar">500</span>
									characters remaining</span>
							</div>
						</div>

						<div class="form-group">
							<label for="addCat_in" class="col-lg-2 control-label">Line
								of Business</label>
							<div class="col-lg-8">
								<select name="lineOfBus" id="lineOfBus"
									class="input-group bootstrap-tagsinput" required="required">
									<option></option>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label for="addCat_in" class="col-lg-2 control-label">Attachment</label>

							<div class="col-lg-8 col-md-8  col-sm-8 col-md-8  col-sm-8">
								<span class="btn btn-file btn-info">Selected:<br /> <input
									type="file" name="selectedFile" class="btn btn-default"
									id="selectedFile" /></span>
							</div>
							
							<div class="col-lg-8 col-md-8  col-sm-8 col-md-8  col-sm-8">
								<input type="hidden" class="form-control" id="fileName"
									name="fileName" placeholder="File name" readonly="readonly" />
							</div>
						</div>

						<div class="form-group">
							<div class="col-lg-10 col-lg-offset-2">
								<button type="reset" class="btn btn-default">Reset</button>
								<button type="submit" class="btn btn-primary" id="add_inq_btn">Add</button>
							</div>
						</div>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
</div>

<jsp:include page="includes/foot.jsp" />