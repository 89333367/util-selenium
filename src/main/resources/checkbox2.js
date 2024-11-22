// 检查是否已经存在复选框
let existingCheckbox = document.getElementById('exportCheckbox');
if (existingCheckbox) {
    console.log('复选框已存在，不执行任何操作。');
} else {
    // 步骤1: 找到目标 div 元素
    let queryareaFooter = document.querySelector('.queryarea_footer');
    let div = queryareaFooter.querySelector('div');

    // 步骤2: 创建复选框
    let checkbox = document.createElement('input');
    checkbox.type = 'checkbox';
    checkbox.id = 'exportCheckbox'; // 给复选框一个 id

    // 步骤3: 创建标签
    let label = document.createElement('label');
    label.htmlFor = 'exportCheckbox'; // 关联复选框的 id
    label.textContent = '导出'; // 设置标签的文本内容

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