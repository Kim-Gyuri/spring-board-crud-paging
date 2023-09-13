function updatePostSubmit(){

    var form = $("#updateForm")[0];
    var formData = new FormData(form);

    //data json으로 저장
    var data = {
        "title" : $.trim($("#title").val()),
        "content" : $.trim($("#content").val())
    }
    formData.append("updateDto", new Blob([JSON.stringify(data)], {type: "application/json"})); // "updateDto" 이름으로 게시글 데이터 저장
    var id = $('#id').val();

    $.ajax({
                url : "/api/posts/" + id,
                type : "patch",
                data : formData,
                dataType : 'json',
                contentType : false,
                processData : false,
                success : function(data) {
                    window.location.replace('/posts/'+ id);
                    alert("글이 수정되었습니다.");
                },
                error : function(error){
                    alert(JSON.stringify(error));
                }
    });
}