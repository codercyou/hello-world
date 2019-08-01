function post() {
    console.log("******************************");
    var questionId = $("#question_id").val();
    console.log(questionId);
    //alert(questionId)
    var comment = $("#comment_content").val();
    console.log(comment);
    $.ajax({
        type: "POST",
        contentType:"application/json",
        url: "/comment",
        data: JSON.stringify({
            "parentId":questionId,
            "content":comment,
            "type":1
        }),
        success: function (response) {
            if(response.code == 200){
                $("#comment_section").hide();
                $("#comment_content_id").text("评论成功");
            }else{
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("http://github.com/login/oauth/authorize?client_id=fb8df772723c425b9d3a&redirect_uri=http://localhost:8081/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
            console.log(response)
        },
        dataType: "json"
    });
}