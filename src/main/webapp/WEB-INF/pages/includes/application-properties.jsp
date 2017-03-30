
<form class="form-horizontal" action="${pageContext.request.contextPath}/updateProperties"
	method="post">
	<fieldset>
		<legend>App Properties</legend>

		<div class="form-group">
			<label for="defaultPassword" class="col-lg-2 control-label">Default
				Password</label>
			<div class="col-lg-8">
				<input type="text" class="form-control"
					id="defaultPassword" name="defaultPassword" placeholder="Default Password"
					required />
			</div>
		</div>

		<div class="form-group">
			<label for="inquiryRecipients" class="col-lg-2 control-label">Inquiry
				Email Recipient</label>
			<div class="col-lg-8">
				<input type="text" class="form-control" id="recipients"
					data-role="tagsinput" name="inquiryRecipients" placeholder="Email"
					required />

			</div>
		</div>
		
		<div class="form-group">
			<label for="newDocRecipients" class="col-lg-2 control-label">New Document
				Email Recipient</label>
			<div class="col-lg-8">
				<input type="text" class="form-control" id="docrecipients"
					data-role="tagsinput" name="newDocRecipients" placeholder="Email"
					required />

			</div>
		</div>
		
		<div class="form-group">
			<label for="appName" class="col-lg-2 control-label">App Name</label>
			<div class="col-lg-8">
				<input type="text" class="form-control"
					id="appName" name="appName" placeholder="App Name"
					required />

			</div>
		</div>
		<div class="form-group">
			<label for="hostName" class="col-lg-2 control-label">Host Name</label>
			<div class="col-lg-8">
				<input type="text" class="form-control"
					id="hostName" name="hostName" placeholder="Host Name"
					required />

			</div>
		</div>
		<div class="form-group">
			<label for="serverPort" class="col-lg-2 control-label">Server Port</label>
			<div class="col-lg-8">
				<input type="text" class="form-control"
					id="serverPort" name="serverPort" placeholder="Server Port"
					required />

			</div>
		</div>
		
		<div class="form-group">
			<label for="emailSender" class="col-lg-2 control-label">Email Sender</label>
			<div class="col-lg-8">
				<input type="text" class="form-control"
					id="emailSender" name="emailSender" placeholder="Email Sender"
					required />
			</div>
		</div>
		
		<div class="form-group">
			<label for="emailIdentity" class="col-lg-2 control-label">Email Identity</label>
			<div class="col-lg-8">
				<input type="text" class="form-control"
					id="emailIdentity" name="emailIdentity" placeholder="Email Identity"
					required />
			</div>
		</div>

		<div class="form-group">
			<div class="col-lg-8 col-lg-offset-2">
				<button type="submit" class="btn btn-primary" id="add_cat_btn">Update</button>
			</div>

		</div>
		
	</fieldset>
</form>