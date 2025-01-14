var selenium_msg = arguments[0];
var selenium_msg_seconds = arguments[1];
if (!selenium_msg_seconds) {
    selenium_msg_seconds = 10;
}
layui.use(function () {
    let layer = layui.layer;
    layer.msg(selenium_msg, { icon: 0, offset: 'rt', anim: 'slideDown', time: 1000 * selenium_msg_seconds }, function () {
        // layer.msg('提示框关闭后的回调');
    });
});