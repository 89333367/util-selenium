if (document.querySelector('#selenium_layui_loaded')) {
    return;
}

// 创建一个新的link元素
let selenium_layui_link = document.createElement('link');

// 设置link元素的属性
selenium_layui_link.href = "//unpkg.com/layui/dist/css/layui.css";
selenium_layui_link.rel = "stylesheet";

// 获取文档的head元素，将link元素添加到head中
document.getElementsByTagName('head')[0].appendChild(selenium_layui_link);

// 创建一个新的script元素
let selenium_layui_script = document.createElement('script');

// 设置script元素的属性
selenium_layui_script.src = "//unpkg.com/layui/dist/layui.js";

// 设置onload事件处理函数，当脚本加载完成后执行
selenium_layui_script.onload = function () {
    // 创建一个新的script元素
    let script = document.createElement('script');

    // 设置script元素的属性
    script.id = "selenium_layui_loaded";

    // 设置script元素的内容
    script.textContent = `
    layui.use(function () {
        let layer = layui.layer;
        layer.msg('layui加载完毕', { icon: 0, offset: 'rt', anim: 'slideDown', time: 1000 }, function () {
            // layer.msg('提示框关闭后的回调');
        });
    });
    `;

    // 获取文档的head元素，将script元素添加到head中
    document.getElementsByTagName('head')[0].appendChild(script);
};

// 获取文档的head元素，将script元素添加到head中
document.getElementsByTagName('head')[0].appendChild(selenium_layui_script);