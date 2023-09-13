function addPostSubmit(){

    var form = $("#addPostForm")[0];
    var formData = new FormData(form);

    //data json으로 저장
    var data = {
        "title" : $.trim($("#title").val()),
        "content" : $.trim($("#content").val()),
        "boardType" : $.trim($("#boardType option:selected").val())
    }
    formData.append("createDto", new Blob([JSON.stringify(data)] , {type: "application/json"}));     //createDto 이름으로 게시글 내용 저장

    $.ajax({
                url : "/api/posts",
                type : "post",
                data : formData,
                dataType : 'json',
                contentType : false,
                processData : false,
                success : function(data) {
                    window.location.replace('/');
                    alert("글이 등록되었습니다.");
                },
                error : function(error){
                    alert(JSON.stringify(error));
                }
    });
}
