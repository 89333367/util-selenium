// 创建一个新的 div 元素
let selenium_messageDiv = document.getElementById('selenium_messageDiv');
if(selenium_messageDiv){
    selenium_messageDiv.remove();
}
selenium_messageDiv = document.createElement('div');
selenium_messageDiv.id = 'selenium_messageDiv';

// 设置 div 的样式
selenium_messageDiv.style.position = 'fixed'; // 固定定位
selenium_messageDiv.style.top = '0'; // 顶部位置
selenium_messageDiv.style.left = '0'; // 左侧位置
selenium_messageDiv.style.width = '100%'; // 宽度100%
selenium_messageDiv.style.height = '150px'; // 设置足够的高度
selenium_messageDiv.style.backgroundColor = 'rgba(0, 0, 0, 0.7)'; // 背景色，半透明
selenium_messageDiv.style.color = 'white'; // 文字颜色
selenium_messageDiv.style.fontSize = '50px'; // 字体大小
selenium_messageDiv.style.textAlign = 'center'; // 文字居中
selenium_messageDiv.style.zIndex = '9999'; // 确保在最上层
selenium_messageDiv.style.lineHeight = '150px'; // 设置行高以垂直居中文本

// 创建关闭按钮 "X"
let selenium_closeButton = document.createElement('span');
selenium_closeButton.textContent = 'X';
selenium_closeButton.style.position = 'absolute'; // 绝对定位
selenium_closeButton.style.right = '20px'; // 距离右侧10pxw
selenium_closeButton.style.fontSize = '50px'; // 字体大小
selenium_closeButton.style.color = 'white'; // 文字颜色
selenium_closeButton.style.cursor = 'pointer'; // 鼠标悬停时显示指针
selenium_closeButton.style.zIndex = '10000'; // 确保按钮在 div 之上

// 将 文本内容 和关闭按钮添加到 div 中
selenium_messageDiv.textContent = arguments[0]; // 设置 div 的内容
selenium_messageDiv.appendChild(selenium_closeButton); // 将关闭按钮添加到 div

// 将 div 添加到 body 的最前面
document.body.insertBefore(selenium_messageDiv, document.body.firstChild);

// 为关闭按钮绑定点击事件监听器
selenium_closeButton.addEventListener('click', function() {
    // 当点击 "X" 时，从 DOM 中删除 div
    selenium_messageDiv.remove();
});
