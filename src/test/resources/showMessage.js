// 创建一个新的 div 元素
let __messageDiv = document.getElementById('__messageDiv');
if (!__messageDiv) {
    __messageDiv = document.createElement('div');
    __messageDiv.id = '__messageDiv';

    // 设置 div 的样式
    __messageDiv.style.position = 'fixed'; // 固定定位
    __messageDiv.style.top = '0'; // 顶部位置
    __messageDiv.style.left = '0'; // 左侧位置
    __messageDiv.style.width = '100%'; // 宽度100%
    __messageDiv.style.height = '150px'; // 设置足够的高度
    __messageDiv.style.backgroundColor = 'rgba(0, 0, 0, 0.7)'; // 背景色，半透明
    __messageDiv.style.color = 'white'; // 文字颜色
    __messageDiv.style.fontSize = '100px'; // 字体大小
    __messageDiv.style.textAlign = 'center'; // 文字居中
    __messageDiv.style.zIndex = '9999'; // 确保在最上层
    __messageDiv.style.lineHeight = '150px'; // 设置行高以垂直居中文本

    // 创建关闭按钮 "X"
    let closeButton = document.createElement('span');
    closeButton.textContent = 'X';
    closeButton.style.position = 'absolute'; // 绝对定位
    closeButton.style.right = '20px'; // 距离右侧10px
    closeButton.style.fontSize = '50px'; // 字体大小
    closeButton.style.color = 'white'; // 文字颜色
    closeButton.style.cursor = 'pointer'; // 鼠标悬停时显示指针
    closeButton.style.zIndex = '10000'; // 确保按钮在 div 之上

    // 将 文本内容 和关闭按钮添加到 div 中
    __messageDiv.textContent = arguments[0]; // 设置 div 的内容
    __messageDiv.appendChild(closeButton); // 将关闭按钮添加到 div

    // 将 div 添加到 body 的最前面
    document.body.insertBefore(__messageDiv, document.body.firstChild);

    // 为关闭按钮绑定点击事件监听器
    closeButton.addEventListener('click', function() {
        // 当点击 "X" 时，从 DOM 中删除 div
        __messageDiv.remove();
    });
} else {
    // 如果 div 已经存在，更新文本内容
    __messageDiv.textContent = arguments[0]; // 更新文本内容
}