$(document).ready(function() {
	$("#results").hide();
	$("#finalResult").hide();
	$("#upload-file-input").on("change", upload);
});
function upload() {
	$
			.ajax({
				url : "/upload",
				type : "POST",
				data : new FormData($("#upload-file-form")[0]),
				enctype : 'multipart/form-data',
				processData : false,
				contentType : false,
				cache : false,
				success : function() {
					$("#results").show();
					$("#file-size").text("OK");
					$("#file-size").removeClass().addClass("badge badge-pill badge-success");
					$("#file-content-type").text("OK");
					$("#file-content-type").removeClass().addClass("badge badge-pill badge-success");
					$("#file-extension").text("OK");
					$("#file-extension").removeClass().addClass("badge badge-pill badge-success");
					$("#file-virus-free").text("OK");
					$("#file-virus-free").removeClass().addClass("badge badge-pill badge-success");
					$("#file-uploaded").text("OK");
					$("#file-uploaded").removeClass().addClass("badge badge-pill badge-success");
					$("#finalResultText").text("File was successfully uploaded.");
					$("#finalResult").removeClass().addClass("alert alert-success");
					$("#finalResult").show();
				},
				error : function(data) {
					$("#results").show();
					$("#finalResultText").text("File was not successfully uploaded.");
					if (data.responseJSON.validFileSize == undefined) {
						$("#file-size").text("OK");
						$("#file-size").removeClass().addClass("badge badge-pill badge-success");
					}
					else if (!data.responseJSON.validFileSize) {
						$("#file-size").text("KO");
						$("#file-size").removeClass().addClass("badge badge-pill badge-danger");
					}
					if (data.responseJSON.validFileExtension == undefined) {
						$("#file-extension").text("?");
						$("#file-extension").removeClass().addClass("badge badge-pill badge-warning");
					} else if (data.responseJSON.validFileExtension) {
						$("#file-extension").text("OK");
						$("#file-extension").removeClass().addClass("badge badge-pill badge-success");
					}
					else {
						$("#file-extension").text("KO");
						$("#file-extension").removeClass().addClass("badge badge-pill badge-danger");
					}
					if (data.responseJSON.virusFree == undefined) {
						$("#file-virus-free").text("?");
						$("#file-virus-free").removeClass().addClass("badge badge-pill badge-warning");
					} else if (data.responseJSON.virusFree) {
						$("#file-virus-free").text("OK");
						$("#file-virus-free").removeClass().addClass("badge badge-pill badge-success");
					}
					else {
						$("#file-virus-free").text("KO");
						$("#file-virus-free").removeClass().addClass("badge badge-pill badge-danger");
					}
					if (data.responseJSON.validContentType == undefined) {
						$("#file-content-type").text("?");
						$("#file-content-type").removeClass().addClass("badge badge-pill badge-warning");
					} else if (data.responseJSON.validContentType) {
						$("#file-content-type").text("OK");
						$("#file-content-type").removeClass().addClass("badge badge-pill badge-success");
					}
					else {
						$("#file-content-type").text("KO");
						$("#file-content-type").removeClass().addClass("badge badge-pill badge-danger");
					}
					if (data.responseJSON.failed) {
						$("#file-uploaded").text("KO");
						$("#file-uploaded").removeClass().addClass("badge badge-pill badge-danger");
					}
					$("#finalResult").removeClass().addClass("alert alert-danger");					
					$("#finalResult").show();
				}
			});
}