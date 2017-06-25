$(".answer-write input[type=submit]").click(addAnswer);

function addAnswer(e){
	e.preventDefault();
	console.log("click!");
	
	var queryString = $(".answer-write").serialize();
	console.log("query: "+queryString);
	
	var url = $(".answer-write").attr("action");
	console.log("url: "+url);
	$.ajax({
			type : 'post',
			url : url,
			data : queryString,
			dataType : 'json',
			error : onError,
			success : onSuccess});
}

function onError(data){
	alert("You need to Sign in.");
}

function onSuccess(data, status){
	console.log(data);
	var answerTemplate = $("#answerTemplate").html();
	var template = answerTemplate.format(data.writer.userId, data.formattedCreateDate, data.contents, data.question.id, data.id);
	$(".qna-comment-slipp-articles").prepend(template);
	
	$(".answer-write textarea").val('');
	$(".qna-comment-count strong").html(data.question.countOfAnswer);
	
}

//$(".link-delete-article").on('click',deleteAnswer);
$('.qna-comment-slipp-articles').on('click','.link-delete-article',deleteAnswer);

function deleteAnswer(e){
	e.preventDefault();
	var deleteBtn = $(this);
	var url = deleteBtn.attr("href");
	console.log(url);
	
	$.ajax({
		type : 'delete',
		url : url,
		dataType : 'json',
		error : function(xhr, status){
			console.log("error");
		},
		success : function(data, status){
			console.log(data);
			if(data.valid){
				var count = $(".qna-comment-count strong").html();
				$(".qna-comment-count strong").html(count-1);
				deleteBtn.closest("article").remove();
			}else{
				alert(data.errorMessage);
			}
		}
	});
}

String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function(match, number) {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};