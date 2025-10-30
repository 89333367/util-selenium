# util-selenium
selenium工具

# 添加依赖
```xml
<!-- jdk8用这个 -->
<dependency>
    <groupId>sunyu.util</groupId>
    <artifactId>util-selenium</artifactId>
    <!-- {selenium-java.version}_{util.version}_{jdk.version}_{architecture.version} -->
    <version>4.13.0_1.0_jdk8_x64</version>
    <classifier>shaded</classifier>
</dependency>
```

# 工具初始化与销毁
```java
//工具初始化
SeleniumUtil seleniumUtil = SeleniumUtil.builder().build();

//工具销毁
seleniumUtil.close();
```

# 工具接口
```java
/**
 * 打开一个网站
 *
 * @param url
 */
public void openUrl(String url)

/**
 * 最大化窗口
 */
public void maxWindow()

/**
 * 最小化窗口
 */
public void minWindow()

/**
 * 获取当前窗口url
 *
 * @return
 */
public String getCurrentUrl()

/**
 * 获得网页标题
 *
 * @return
 */
public String getTitle()

/**
 * 获得html文档
 *
 * @param webElement
 * @return
 */
public String getOuterHTML(WebElement webElement)

/**
 * 执行javascript
 *
 * @param script
 * @param args
 */
public void executeJavascript(String script, Object... args)

/**
 * 只保留最新窗口
 */
public void keepLastWindow()

/**
 * 关闭其他窗口
 *
 * @param keepNameOrHandle
 */
public void closeOtherWindow(String keepNameOrHandle)

/**
 * 删除一批元素
 *
 * @param webElements
 */
public void removeWebElements(List<WebElement> webElements)

/**
 * 删除一个元素
 *
 * @param webElement
 */
public void removeWebElement(WebElement webElement)

/**
 * 隐藏一批元素
 *
 * @param webElements
 */
public void hideWebElements(List<WebElement> webElements)

/**
 * 隐藏一个元素
 *
 * @param webElement
 */
public void hideWebElement(WebElement webElement)

/**
 * 等待窗口的数量是否为特定的值
 *
 * @param expectedNumberOfWindows
 * @return
 */
public Boolean waitNumberOfWindowsToBe(int expectedNumberOfWindows)

/**
 * 通过css选择器查找元素
 *
 * @param cssSelector
 * @return
 */
public WebElement findElementByCssSelector(String cssSelector)

/**
 * 通过css选择器查找多个元素
 *
 * @param cssSelector
 * @return
 */
public List<WebElement> findElementsByCssSelector(String cssSelector)

/**
 * 通过css选择一个元素，判断其是否为指定状态
 *
 * @param cssSelector
 * @param selected
 * @return
 */
public Boolean waitElementSelectionStateToBeByCssSelector(String cssSelector, boolean selected)

/**
 * 等待元素存在于dom中
 *
 * @param cssSelector
 * @return
 */
public WebElement waitPresenceOfElementLocatedByCssSelector(String cssSelector)

/**
 * 等待元素存在于dom中
 *
 * @param cssSelector
 * @return
 */
public List<WebElement> waitPresenceOfAllElementsLocatedByCssSelector(String cssSelector)

/**
 * 等待元素存在于dom中并可见
 *
 * @param cssSelector
 * @return
 */
public WebElement waitVisibilityOfElementLocatedByCssSelector(String cssSelector)

/**
 * 等待元素存在于dom中并可见
 *
 * @param cssSelector
 * @return
 */
public List<WebElement> waitVisibilityOfAllElementsLocatedByCssSelector(String cssSelector)

/**
 * 等待直到指定的元素不可见或者从DOM中消失
 *
 * @param cssSelector
 * @return
 */
public Boolean waitInvisibilityOfElementLocatedByCssSelector(String cssSelector)

/**
 * 等待一个元素存在于dom中
 *
 * @param by
 * @return
 */
public WebElement waitPresenceOfElementLocatedBy(By by)

/**
 * 等待元素存在于dom中
 *
 * @param by
 * @return
 */
public List<WebElement> waitPresenceOfAllElementsLocatedBy(By by)

/**
 * 等待一个元素存在于dom中并可见
 *
 * @param by
 * @return
 */
public WebElement waitVisibilityOfElementLocatedBy(By by)

/**
 * 等待元素存在于dom中并可见
 *
 * @param by
 * @return
 */
public List<WebElement> waitVisibilityOfAllElementsLocatedBy(By by)

/**
 * 等待直到指定的元素不可见或者从DOM中消失
 *
 * @param by
 * @return
 */
public Boolean waitInvisibilityOfElementLocatedBy(By by)

/**
 * 等待一个条件成立
 *
 * @param condition
 * @param <V>
 * @return
 */
public <V> V webDriverWaitUntil(Function<? super WebDriver, V> condition)

/**
 * 等待一个条件成立
 *
 * @param waitTime
 * @param condition
 * @param <V>
 * @return
 */
public <V> V webDriverWaitUntil(Duration waitTime, Function<? super WebDriver, V> condition)


```