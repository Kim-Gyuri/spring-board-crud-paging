function deletePostSubmit(){

     var id = $('#id').val();

    $.ajax({
                url : "/api/posts/" + id,
                type : "delete",
                enctype : 'multipart/form-data',
                dataType : 'json',
                contentType : false,
                processData : false,
                success : function(data) {
                    window.location.replace('/');
                    alert("글이 삭제되었습니다.");
                },
                error : function(error){
                    alert(JSON.stringify(error));
                }
    });
}