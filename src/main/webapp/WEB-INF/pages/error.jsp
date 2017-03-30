<%@page session="true"%>
<jsp:include page="includes/head.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="container-fluid text-center main-container">
	<div class="row content">

		<div class="col-sm-3"></div>

		<div class="col-sm-6 text-left">

			<div class="well bs-component">
				<div class="error_dv">
					<h4>Error Type</h4><br /> <span>${errCode}</span><br />
					<br /> <h4>Error Message</h4><br /> <span>${errMsg}</span>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="includes/foot.jsp" />