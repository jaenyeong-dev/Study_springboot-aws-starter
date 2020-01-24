let index = {
    init: function () {
        console.log('call init function');
        let _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
        });
    },
    save: function () {
        console.log('call save function');
        let data = {
            title: $('#title').val(),
            author: $('#author').val(),
            content: $('#content').val()
        };

        $.ajax({
            type: 'post',
            url: '/api/v1/posts',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('글이 등록되었습니다.');
            window.location.href = '/'; // 글 등록 성공시 메인페이지(/)로 이동
        }).fail(function (error) {
            alert(JSON.stringify(error));
        })
    }
};

index.init();
