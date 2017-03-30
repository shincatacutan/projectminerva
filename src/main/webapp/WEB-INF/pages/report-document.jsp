<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="includes/head.jsp" />
<script>
	$(function() {
		disableEnterKey();
		$('#startDate').datepicker({
			autoclose : true,
			todayHighlight : true
		});
		$('#endDate').datepicker({
			autoclose : true,
			todayHighlight : true
		});

		getLobList();
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

				<form class="form-horizontal" action="${pageContext.request.contextPath}/downloadDocReport"
					method="post">
					<fieldset>
						<legend>Generate Document Report</legend>
						<div class="form-group">
							<label for="startDate" class="col-lg-2 control-label">Start
								Date</label>
							<div class="col-lg-4 col-sm-6 date input-group" data-provide="datepicker">
								<input type="text" class="form-control" id="startDate"
									name="startDate" placeholder="Start" required />
								<div class="input-group-addon">
									<span class="glyphicon glyphicon-calendar"></span>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label for="endDate" class="col-lg-2 control-label">End
								Date</label>
							<div class="col-lg-4 col-sm-6  date input-group" data-provide="datepicker">
								<input id="endDate" name="endDate" class="form-control"
									placeholder="End" required />
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
								<button type="submit" class="btn btn-primary" id="add_inq_btn">Generate</button>
							</div>
						</div>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
</div>

<jsp:include page="includes/foot.jsp" />