<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="includes/head.jsp" />
<script>
	$(function() {
		disableEnterKey();
		getUnsentEmailsByUser();
	
	});
</script>
<script src="<c:url value ="/resources/js/unsent.js " />"></script>
<div class="container-fluid text-center main-container">
	<div class="row content">
		<div class="col-sm-1"></div>

		<div class="col-sm-10 text-left">
		<div class="well bs-component">
			<h4>Unsent Emails</h4>
			<hr class="well-hr" />
			<div id="inqTableSpace">
				<table class="display" id="unsentEmailGrid"></table>
				<input type="button" class="btn btn-primary" id="sendEmail"
					value="Send" />
			</div>
			</div>
		</div>
	</div>

</div>

<jsp:include page="includes/foot.jsp" />
