<div id="tabs">
	<ul>
		<li><a href="#tabs-1">Search Updates</a></li>
		<li><a href="#tabs-2">Add Updates</a></li>
		<li><a href="#tabs-3">View Inquiry</a></li>
		<li><a href="#tabs-4">Add Inquiry</a></li>
	</ul>
	<div id="tabs-1" class="tab_div">
		<form id="search_tab">
			<fieldset>
				<span class="addSpan"> <label class="addLbl">Category</label>
					<select name="searchCat_in" id="searchCat_in"
					class="bootstrap-tagsinput" required>
						<option></option>
				</select>
				</span> <span class="addSpan"> <label class="addLbl">Uploader</label>
					<input type="text" id="uploader_search" class="bootstrap-tagsinput"></input>
				</span> <span class="addSpan"> <label class="addLbl">Uploader</label>
					<input type="text" id="author_search" class="bootstrap-tagsinput"></input>
				</span> <span class="addSpan"> <label class="addLbl">Date
						Cascaded</label> <input type="text" id="createDt_search"
					class="bootstrap-tagsinput"></input>
				</span>

				<!-- 	<span class="addSpan"> 
					<label class="addLbl">Keyword/s</label>
					<input type="text" id="keywords_search" data-role="tagsinput"/>
				</span> -->

			</fieldset>
			<br class="clearfix" /> <input type="button" id="search_btn"
				value="Filter" /> <input type="reset" />
			<hr class="seperator">

			<div id="tableSpace">
				<table class="display" id="resultGrid"></table>
				<input type="button" id="update_btn" value="Update" /> <input
					type="button" id="delete_btn" value="Delete" />
			</div>
		</form>

	</div>
	<div id="tabs-2" class="tab_div">
		<form id="add_tab">
			<fieldset>
				<div>
					<div class="two_column two_column_left">
						<span class="addSpan"> <label class="addLbl">Category</label>
							<select name="addCat_in" id="addCat_in"
							class="bootstrap-tagsinput" required>
								<option></option>
						</select>
						</span> <span class="addSpan"> <label class="addLbl">Title</label>
							<input type="text" id="title_in" name="title_in"
							class="bootstrap-tagsinput" required />
						</span> <span class="addSpan"> <label class="addLbl">Detailed
								Info</label> <textarea cols="40" rows="5" id="desc_in" name="desc_in"
								class="bootstrap-tagsinput" required></textarea>
						</span> <span class="addSpan"> <label class="addLbl">Date
								Cascaded</label> <input type="text" id="createDt_in" name="createDt_in"
							class="bootstrap-tagsinput" required />
						</span>
					</div>
					<div class="two_column two_column_right">
						<span class="addSpan"> <label class="addLbl">Status</label>
							<select name="status_in" id="status_in"
							class="bootstrap-tagsinput" required>
								<option></option>
								<option>Open</option>
								<option>Pending</option>
								<option>Close</option>
						</select>
						</span> <span class="addSpan"> <label class="addLbl">Link</label>
							<input type="text" id="link_in" name="link_in"
							class="bootstrap-tagsinput" required />
						</span> <span class="addSpan"> <label class="addLbl">Author</label>
							<input type="text" id="author_in" name="author_in"
							class="bootstrap-tagsinput" required />
						</span>
					</div>
					<br class="clearfix" />
				</div>
			</fieldset>
			<br class="clearfix" /> <input type="button" id="add_btn"
				value="Add" /> <input type="reset" />
		</form>
		<div class="dialog-fix">
			<div id="dialog-form" title="Update Details">
				<p class="validateTips">All form fields are required.</p>
				<form>
					<fieldset>
						<span class="addSpan"> <label for="name" class="addLbl">Title</label>
							<input type="text" name="title" id="title_modal" value=""
							class="text ui-widget-content ui-corner-all">
						</span> <span class="addSpan"> <label for="email" class="addLbl">Description</label>
							<textarea name="desc" id="desc_modal"
								class="text ui-widget-content ui-corner-all" rows="3" cols="38"></textarea>
						</span>
						<!-- Allow form submission with keyboard without duplicating the dialog button -->
						<input type="submit" tabindex="-1"
							style="position: absolute; top: -1000px">
					</fieldset>
				</form>
			</div>
		</div>
	</div>
	<div id="tabs-3" class="tab_div">

		<div id="inqTableSpace">
			<table class="display" id="inquiry_grid"></table>
		</div>
		<input type="button" id="view_btn" value="View" />
		<input type="button" id="reply_btn" value="Reply" />

		<div class="reply-dialog">
			<div id="reply-dialog-form" title="Reply to Inquiry">
				<p>
					Sender: <span id="authorName"></span> | 
					Sent Date: <span id="createDate"></span> | 
					Modified By: <span id="modifiedBy"></span> | 
					Modified Date: <span id="modifiedDate"></span>
				</p>
				<h4>Title</h4>
				<span id="inquiryTitle"></span>
				<h4>Body</h4>
				<span id="inquiryBody"></span>
				<hr class="seperator">
				<form>
					<fieldset>
						<span class="addSpan"> <label for="email" class="addLbl">Reply
						</label> <textarea name="reply_modal" id="reply_modal"
								class="text ui-widget-content ui-corner-all" rows="7" cols="50"></textarea>
						</span> <br /> <span class="addSpan"> <label class="addLbl">Status</label>
							<select name="inq_status_in" id="inq_status_in"
							class="bootstrap-tagsinput" required>
								<option></option>
								<option>Open</option>
								<option>Pending</option>
								<option>Close</option>
						</select> <input type="hidden" id="inqId"></input>
						</span>
						<!-- Allow form submission with keyboard without duplicating the dialog button -->
						<input type="submit" tabindex="-1"
							style="position: absolute; top: -1000px">
					</fieldset>
				</form>
			</div>
		</div>
	</div>
	<div id="tabs-4" class="tab_div">
		<form id="view_tab">
			<fieldset>
				<span class="addSpan"> <label class="addLbl">Title</label> <input
					type="text" id="inq_title_in" class="bootstrap-tagsinput"></input>
				</span> <span class="addSpan"> <label class="addLbl">Inquiry</label>
					<textarea cols="40" rows="5" id="inq_in"
						class="bootstrap-tagsinput"></textarea></span>
			</fieldset>
			<br class="clearfix" /> <input type="button" id="add_inq_btn"
				value="Add" /> <input type="reset" />
		</form>

	</div>
</div>