// 检查是否已经存在复选框
let __exportCheckbox = document.getElementById('__exportCheckbox');
if (__exportCheckbox) {
    console.log('复选框已存在，不执行任何操作。');
} else {
    // 步骤1: 找到目标 div 元素
    let div = arguments[0];

    // 步骤2: 创建复选框
    let checkbox = document.createElement('input');
    checkbox.type = 'checkbox';
    checkbox.id = '__exportCheckbox'; // 给复选框一个 id
    // 设置复选框的样式
    checkbox.style.width = '50px';       // 设置宽度
    checkbox.style.height = '50px';      // 设置高度
    checkbox.style.border = '1px solid #000'; // 设置边框
    checkbox.style.backgroundColor = '#f0f0f0'; // 设置背景色
    checkbox.style.boxShadow = '0 0 2px rgba(0,0,0,0.2)'; // 设置阴影
    checkbox.style.cursor = 'pointer';   // 更改鼠标悬停时的光标样式
    checkbox.style.margin = '10px'; // 可以设置外边距，根据需要调整

    // 步骤3: 创建标签
    let label = document.createElement('label');
    label.htmlFor = '__exportCheckbox'; // 关联复选框的 id
    label.textContent = '勾选复选框进行导出操作'; // 设置标签的文本内容
    label.style.cursor = 'pointer';   // 更改鼠标悬停时的光标样式

    // 设置样式
    label.style.fontSize = '20px'; // 放大字体
    label.style.backgroundColor = 'red'; // 背景色为红色
    label.style.color = 'white'; // 设置字体颜色为白色，以便在红色背景上可见
    label.style.padding = '10px'; // 添加一些内边距
    label.style.display = 'inline-block'; // 使 label 表现为块级元素，以便样式生效

    // 步骤4: 将复选框和标签添加到 div 中
    div.appendChild(checkbox);
    div.appendChild(label);
}