function del(delUrl){
    $.ajax({
        url: delUrl,
        type: 'DELETE',
        success: function (results) {
            location.reload();
        }
    });
}