function post() {
    console.log("******************************");
    var questionId = $("#question_id").val();
    console.log(questionId);
    //alert(questionId)
    var comment = $("#comment_content").val();
    //alert(comment);
    if(""==comment||comment ==null){
        alert("请填写回复内容");
        return;
    }
    $.ajax({
        type: "POST",
        contentType:"application/json",
        url: "/comment",
        data: JSON.stringify({
            "parentId":questionId,
            "content":comment,
            "type":1 //1代表回复问题
        }),
        success: function (response) {
            if(response.code == 200){
                window.location.reload();
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

function reply(e) {
    console.log("******************************");
    var questionId = e.getAttribute("data-id");
    //var questionId = $("#question_id").val();
    console.log(questionId);
    //alert(questionId)
    var comment = $("#input-"+questionId).val();
    //alert(comment);
    if(""==comment||comment ==null){
        alert("请填写回复内容");
        return;
    }
    $.ajax({
        type: "POST",
        contentType:"application/json",
        url: "/comment",
        data: JSON.stringify({
            "parentId":questionId,
            "content":comment,
            "type":2  //2代表回复评论
        }),
        success: function (response) {
            if(response.code == 200){
                window.location.reload();
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

function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);

    // 获取一下二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        // 折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}

function showSelectTag() {
    $("#select-tag").show();
}

function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    if (previous.indexOf(value) == -1) {
        if (previous) {
            $("#tag").val(previous + ',' + value);
        } else {
            $("#tag").val(value);
        }
    }
}