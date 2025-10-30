package sunyu.util;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.system.SystemUtil;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * selenium自动化测试工具类
 *
 * @author 孙宇
 */
public class SeleniumUtil implements AutoCloseable {
    private final Log log = LogFactory.get();
    private final Config config;

    public static Builder builder() {
        return new Builder();
    }

    private SeleniumUtil(Config config) {
        log.info("[构建{}] 开始", this.getClass().getSimpleName());
        if (config.webDriverManager == null) {
            try {
                log.info("[选择驱动] 尝试选择 chrome 驱动");
                config.webDriverManager = WebDriverManager.chromedriver();
                config.webDriverManager.disableCsp();
                log.info("[配置驱动] 配置 chrome web driver");
                config.webDriverManager.setup();
                ChromeOptions options = new ChromeOptions();
                //options.addArguments("--headless");//使用无头模式,作用是不用打开浏览器
                //options.addArguments("--disable-gpu");//在某些系统上无头模式需要禁用GPU加速
                //options.addArguments("--disable-accelerated-2d-canvas"); // 禁用加速2D Canvas，减少GPU使用
                //options.addArguments("--blink-settings=imagesEnabled=false");// 禁用图片加载，提高性能
                options.addArguments("--disable-extensions");// 禁用扩展和插件，减少资源占用和干扰
                options.addArguments("--disable-dev-shm-usage");// 解决在Docker容器中可能出现的共享内存问题
                options.addArguments("--disable-smooth-scrolling");// 禁用平滑滚动，减少自动滚动问题
                options.addArguments("--window-size=1366,768");// 设置固定窗口大小，避免响应式变化导致的元素定位问题
                options.addArguments("--disable-features=site-per-process");// 禁用站点隔离，减少内存使用
                options.addArguments("--disable-default-apps");// 禁用默认应用，减少启动时间
                options.addArguments("--disable-logging");// 减少日志输出，提高性能
                options.addArguments("--disable-infobars");// 禁用信息栏，避免干扰
                options.addArguments("--disable-notifications");// 禁用通知，避免干扰
                options.addArguments("--disable-web-security"); // 禁用同源策略检查
                options.addArguments("--no-sandbox"); // 禁用沙箱模式，提高性能(注意安全风险)
                options.addArguments("--disable-setuid-sandbox"); // 禁用setuid沙箱，配合--no-sandbox使用
                options.addArguments("--disable-crash-reporter"); // 禁用崩溃报告
                options.addArguments("--disable-in-process-stack-traces"); // 禁用进程内堆栈跟踪
                options.addArguments("--disable-breakpad"); // 禁用断点调试
                options.addArguments("--aggressive-cache-discard"); // 积极丢弃缓存，减少内存使用
                options.addArguments("--disable-ipc-flooding-protection"); // 禁用IPC洪水保护
                options.addArguments("--js-flags=--max-old-space-size=512");// 限制JavaScript引擎内存使用，防止内存溢出
                options.addArguments("--remote-allow-origins=*", "disable-search-engine-choice-screen");//解决 403 出错问题，禁用Chrome浏览器在启动时显示的搜索引擎选择界面
                options.setPageLoadStrategy(PageLoadStrategy.EAGER);// 可选值：NONE (不等待加载), EAGER (DOM就绪即可), NORMAL (等待完全加载)
                options.setExperimentalOption("excludeSwitches",
                        new String[] { "enable-automation", "disable-popup-blocking" });// 禁用浏览器的自动测试软件提示，禁用 Chrome 的弹出窗口拦截功能
                config.webDriver = new ChromeDriver(options);
            } catch (Exception e) {
                config.webDriverManager = null;
                log.warn(e);
            }
        }
        if (config.webDriverManager == null) {
            try {
                log.info("[选择驱动] 尝试选择 edge 驱动");
                config.webDriverManager = WebDriverManager.edgedriver();
                config.webDriverManager.disableCsp();
                log.info("[配置驱动] 配置 edge web driver");
                config.webDriverManager.setup();
                EdgeOptions options = new EdgeOptions();
                options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });// 禁用浏览器的自动测试软件提示
                options.addArguments("--remote-allow-origins=*", "--disable-features=Sync");//解决 403 出错问题，屏蔽Microsoft Edge浏览器在启动时弹出的同步用户配置和个性化设置的提示
                options.setCapability("ms:edgeChromium", true);
                options.setCapability("inPrivate", true);// 使用隐私模式，避免历史记录、cookie等信息的干扰
                config.webDriver = new EdgeDriver(options);
            } catch (Exception e) {
                config.webDriverManager = null;
                log.warn(e);
            }
        }
        if (config.webDriverManager == null) {
            try {
                log.info("[选择驱动] 尝试选择 firefox 驱动");
                config.webDriverManager = WebDriverManager.firefoxdriver();
                config.webDriverManager.disableCsp();
                log.info("[配置驱动] 配置 firefox web driver");
                config.webDriverManager.setup();
                FirefoxOptions options = new FirefoxOptions();
                config.webDriver = new FirefoxDriver(options);
            } catch (Exception e) {
                config.webDriverManager = null;
                log.warn(e);
            }
        }
        log.info("[构建{}] 结束", this.getClass().getSimpleName());

        this.config = config;
    }

    private static class Config {
        private WebDriverManager webDriverManager;
        private WebDriver webDriver;
    }

    public static class Builder {
        private final Config config = new Config();

        public SeleniumUtil build() {
            return new SeleniumUtil(config);
        }
    }

    /**
     * 回收资源
     */
    @Override
    public void close() {
        log.info("[销毁{}] 开始", this.getClass().getSimpleName());
        config.webDriver.quit();
        killDriverProcess();
        log.info("[销毁{}] 结束", this.getClass().getSimpleName());
    }

    /**
     * 资源回收
     */
    private void killDriverProcess() {
        if (SystemUtil.getOsInfo().isWindows()) {
            log.info("[删除残留驱动] 开始");
            RuntimeUtil.exec("taskkill /f /im chromedriver.exe");
            RuntimeUtil.exec("taskkill /f /im msedgedriver.exe");
            RuntimeUtil.exec("taskkill /f /im geckodriver.exe");
            log.info("[删除残留驱动] 结束");
        }
    }

    /**
     * 获取WebDriver
     *
     * @return
     */
    public WebDriver getWebDriver() {
        return config.webDriver;
    }

    /**
     * 打开一个网站
     *
     * @param url
     */
    public void openUrl(String url) {
        config.webDriver.get(url);
    }

    /**
     * 最大化窗口
     */
    public void maxWindow() {
        config.webDriver.manage().window().maximize();
    }

    /**
     * 最小化窗口
     */
    public void minWindow() {
        config.webDriver.manage().window().minimize();
    }

    /**
     * 获取当前窗口url
     *
     * @return
     */
    public String getCurrentUrl() {
        return config.webDriver.getCurrentUrl();
    }

    /**
     * 获得网页标题
     *
     * @return
     */
    public String getTitle() {
        return config.webDriver.getTitle();
    }

    /**
     * 获得html文档
     *
     * @param webElement
     * @return
     */
    public String getOuterHTML(WebElement webElement) {
        return webElement.getAttribute("outerHTML");
    }

    /**
     * 执行javascript
     *
     * @param script
     * @param args
     */
    public void executeJavascript(String script, Object... args) {
        ((JavascriptExecutor) config.webDriver).executeScript(script, args);
    }

    /**
     * 只保留最新窗口
     */
    public void keepLastWindow() {
        closeOtherWindow(CollUtil.getLast(config.webDriver.getWindowHandles()));
    }

    /**
     * 关闭其他窗口
     *
     * @param keepNameOrHandle
     */
    public void closeOtherWindow(String keepNameOrHandle) {
        for (String windowHandle : config.webDriver.getWindowHandles()) {
            if (!windowHandle.equals(keepNameOrHandle)) {
                config.webDriver.switchTo().window(windowHandle).close();
            }
        }
        config.webDriver.switchTo().window(keepNameOrHandle);
    }

    /**
     * 删除一批元素
     *
     * @param webElements
     */
    public void removeWebElements(List<WebElement> webElements) {
        for (WebElement webElement : webElements) {
            removeWebElement(webElement);
        }
    }

    /**
     * 删除一个元素
     *
     * @param webElement
     */
    public void removeWebElement(WebElement webElement) {
        executeJavascript("arguments[0].remove()", webElement);
    }

    /**
     * 隐藏一批元素
     *
     * @param webElements
     */
    public void hideWebElements(List<WebElement> webElements) {
        for (WebElement webElement : webElements) {
            hideWebElement(webElement);
        }
    }

    /**
     * 隐藏一个元素
     *
     * @param webElement
     */
    public void hideWebElement(WebElement webElement) {
        executeJavascript("arguments[0].style.display = 'none'", webElement);
    }

    /**
     * 等待窗口的数量是否为特定的值
     *
     * @param expectedNumberOfWindows
     * @return
     */
    public Boolean waitNumberOfWindowsToBe(int expectedNumberOfWindows) {
        ExpectedCondition<Boolean> booleanExpectedCondition = ExpectedConditions
                .numberOfWindowsToBe(expectedNumberOfWindows);
        return webDriverWaitUntil(booleanExpectedCondition);
    }

    /**
     * 通过css选择器查找元素
     *
     * @param cssSelector
     * @return
     */
    public WebElement findElementByCssSelector(String cssSelector) {
        //定位元素
        By by = By.cssSelector(cssSelector);
        return config.webDriver.findElement(by);
    }

    /**
     * 通过css选择器查找多个元素
     *
     * @param cssSelector
     * @return
     */
    public List<WebElement> findElementsByCssSelector(String cssSelector) {
        //定位元素
        By by = By.cssSelector(cssSelector);
        return config.webDriver.findElements(by);
    }

    /**
     * 通过css选择一个元素，判断其是否为指定状态
     *
     * @param cssSelector
     * @param selected
     * @return
     */
    public Boolean waitElementSelectionStateToBeByCssSelector(String cssSelector, boolean selected) {
        By by = By.cssSelector(cssSelector);
        ExpectedCondition<Boolean> booleanExpectedCondition = ExpectedConditions.elementSelectionStateToBe(by,
                selected);
        return webDriverWaitUntil(booleanExpectedCondition);
    }

    /**
     * 等待元素存在于dom中
     *
     * @param cssSelector
     * @return
     */
    public WebElement waitPresenceOfElementLocatedByCssSelector(String cssSelector) {
        //定位元素
        By by = By.cssSelector(cssSelector);
        return waitPresenceOfElementLocatedBy(by);
    }

    /**
     * 等待元素存在于dom中
     *
     * @param cssSelector
     * @return
     */
    public List<WebElement> waitPresenceOfAllElementsLocatedByCssSelector(String cssSelector) {
        //定位元素
        By by = By.cssSelector(cssSelector);
        return waitPresenceOfAllElementsLocatedBy(by);
    }

    /**
     * 等待元素存在于dom中并可见
     *
     * @param cssSelector
     * @return
     */
    public WebElement waitVisibilityOfElementLocatedByCssSelector(String cssSelector) {
        //定位元素
        By by = By.cssSelector(cssSelector);
        return waitVisibilityOfElementLocatedBy(by);
    }

    /**
     * 等待元素存在于dom中并可见
     *
     * @param cssSelector
     * @return
     */
    public List<WebElement> waitVisibilityOfAllElementsLocatedByCssSelector(String cssSelector) {
        By by = By.cssSelector(cssSelector);
        return waitVisibilityOfAllElementsLocatedBy(by);
    }

    /**
     * 等待直到指定的元素不可见或者从DOM中消失
     *
     * @param cssSelector
     * @return
     */
    public Boolean waitInvisibilityOfElementLocatedByCssSelector(String cssSelector) {
        By by = By.cssSelector(cssSelector);
        return waitInvisibilityOfElementLocatedBy(by);
    }

    /**
     * 等待一个元素存在于dom中
     *
     * @param by
     * @return
     */
    public WebElement waitPresenceOfElementLocatedBy(By by) {
        //等待一个元素存在于DOM中
        ExpectedCondition<WebElement> webElementExpectedCondition = ExpectedConditions.presenceOfElementLocated(by);
        return webDriverWaitUntil(webElementExpectedCondition);
    }

    /**
     * 等待元素存在于dom中
     *
     * @param by
     * @return
     */
    public List<WebElement> waitPresenceOfAllElementsLocatedBy(By by) {
        ExpectedCondition<List<WebElement>> listExpectedCondition = ExpectedConditions
                .presenceOfAllElementsLocatedBy(by);
        return webDriverWaitUntil(listExpectedCondition);
    }

    /**
     * 等待一个元素存在于dom中并可见
     *
     * @param by
     * @return
     */
    public WebElement waitVisibilityOfElementLocatedBy(By by) {
        //等待一个元素存在于DOM中
        ExpectedCondition<WebElement> webElementExpectedCondition = ExpectedConditions.visibilityOfElementLocated(by);
        return webDriverWaitUntil(webElementExpectedCondition);
    }

    /**
     * 等待元素存在于dom中并可见
     *
     * @param by
     * @return
     */
    public List<WebElement> waitVisibilityOfAllElementsLocatedBy(By by) {
        ExpectedCondition<List<WebElement>> listExpectedCondition = ExpectedConditions
                .visibilityOfAllElementsLocatedBy(by);
        return webDriverWaitUntil(listExpectedCondition);
    }

    /**
     * 等待直到指定的元素不可见或者从DOM中消失
     *
     * @param by
     * @return
     */
    public Boolean waitInvisibilityOfElementLocatedBy(By by) {
        ExpectedCondition<Boolean> booleanExpectedCondition = ExpectedConditions.invisibilityOfElementLocated(by);
        return webDriverWaitUntil(booleanExpectedCondition);
    }

    /**
     * 等待一个条件成立
     *
     * @param condition
     * @param <V>
     * @return
     */
    public <V> V webDriverWaitUntil(Function<? super WebDriver, V> condition) {
        return webDriverWaitUntil(Duration.ofDays(1), condition);
    }

    /**
     * 等待一个条件成立
     *
     * @param waitTime
     * @param condition
     * @param <V>
     * @return
     */
    public <V> V webDriverWaitUntil(Duration waitTime, Function<? super WebDriver, V> condition) {
        WebDriverWait webDriverWait = new WebDriverWait(config.webDriver, waitTime);
        //等待条件成立后返回元素引用
        return webDriverWait.until(condition);
    }

}