if (document.querySelector('[lay-type="export"]')) {
    return;
}
layui.use(function () {
    let util = layui.util;
    // 自定义固定条
    util.fixbar({
        bars: [{
            type: 'export',
            content: '导出',
            style: 'font-size: 21px; background-color: #FF5722;'
        }],
        on: { // 任意事件 --  v2.8.0 新增
            mouseenter: function (type) {
                if (type == 'export') {
                    layer.tips('导出数据', this, {
                        tips: 4,
                        fixed: true
                    });
                }
            },
            mouseleave: function (type) {
                layer.closeAll('tips');
            }
        },
        // 点击事件
        click: function (type) {
            if (type == 'export') {
                // 创建一个新的script元素
                let script = document.createElement('script');

                // 设置script元素的属性
                script.id = "selenium_export";

                // 获取文档的head元素，将script元素添加到head中
                document.getElementsByTagName('head')[0].appendChild(script);
            }
        }
    });
});