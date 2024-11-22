package sunyu.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.system.SystemUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
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

import java.io.Closeable;
import java.io.Serializable;
import java.time.Duration;
import java.util.function.Function;

/**
 * selenium自动化测试工具类
 *
 * @author 孙宇
 */
public class SeleniumUtil implements Serializable, Closeable {
    private final Log log = LogFactory.get();

    private WebDriverManager webDriverManager;
    private WebDriver webDriver;


    /**
     * 资源回收
     */
    private void killDriverProcess() {
        if (SystemUtil.getOsInfo().isWindows()) {
            log.info("删除残留驱动开始");
            RuntimeUtil.exec("taskkill /f /im chromedriver.exe");
            RuntimeUtil.exec("taskkill /f /im msedgedriver.exe");
            RuntimeUtil.exec("taskkill /f /im geckodriver.exe");
            log.info("删除残留驱动结束");
        }
    }

    /**
     * 获取WebDriver
     *
     * @return
     */
    public WebDriver getWebDriver() {
        return webDriver;
    }

    /**
     * 打开一个网站
     *
     * @param url
     */
    public void openUrl(String url) {
        webDriver.get(url);
    }

    /**
     * 最大化窗口
     */
    public void maxWindow() {
        webDriver.manage().window().maximize();
    }

    /**
     * 最小化窗口
     */
    public void minWindow() {
        webDriver.manage().window().minimize();
    }

    /**
     * 获得网页标题
     *
     * @return
     */
    public String getTitle() {
        return webDriver.getTitle();
    }

    /**
     * 只保留最新窗口
     */
    public void keepLastWindow() {
        closeOtherWindow(CollUtil.getLast(webDriver.getWindowHandles()));
    }

    /**
     * 关闭其他窗口
     *
     * @param keepNameOrHandle
     */
    public void closeOtherWindow(String keepNameOrHandle) {
        for (String windowHandle : webDriver.getWindowHandles()) {
            if (!windowHandle.equals(keepNameOrHandle)) {
                webDriver.switchTo().window(windowHandle).close();
            }
        }
        webDriver.switchTo().window(keepNameOrHandle);
    }

    /**
     * 等待窗口的数量是否为特定的值
     *
     * @param expectedNumberOfWindows
     * @return
     */
    public boolean waitNumberOfWindowsToBe(int expectedNumberOfWindows) {
        ExpectedCondition<Boolean> booleanExpectedCondition = ExpectedConditions.numberOfWindowsToBe(expectedNumberOfWindows);
        return webDriverWaitUntil(Duration.ofDays(1), booleanExpectedCondition);
    }

    /**
     * 等待class元素存在于dom中
     *
     * @param className
     * @return
     */
    public WebElement waitPresenceOfElementLocatedByClassName(String className) {
        //定位元素
        By by = By.className(className);
        return waitPresenceOfElementLocatedBy(by);
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
        return webDriverWaitUntil(Duration.ofDays(1), webElementExpectedCondition);
    }

    /**
     * 等待一个条件成立
     *
     * @param waitTime
     * @param isTrue
     * @param <V>
     * @return
     */
    public <V> V webDriverWaitUntil(Duration waitTime, Function<? super WebDriver, V> isTrue) {
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, waitTime);
        //等待条件成立后返回元素引用
        return webDriverWait.until(isTrue);
    }

    private void buildWebDriver() {
        if (webDriverManager == null) {
            try {
                log.info("尝试选择 chrome 驱动");
                webDriverManager = WebDriverManager.chromedriver();
                webDriverManager.disableCsp();
                log.info("配置 chrome web driver");
                webDriverManager.setup();
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});// 禁用浏览器的自动测试软件提示
                options.addArguments("--remote-allow-origins=*");//解决 403 出错问题
                //options.setHeadless(true);//这句话作用是不用打开浏览器
                webDriver = new ChromeDriver(options);
            } catch (Exception e) {
                webDriverManager = null;
                log.warn(e);
            }
        }
        if (webDriverManager == null) {
            try {
                log.info("尝试选择 edge 驱动");
                webDriverManager = WebDriverManager.edgedriver();
                webDriverManager.disableCsp();
                log.info("配置 edge web driver");
                webDriverManager.setup();
                EdgeOptions options = new EdgeOptions();
                options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});// 禁用浏览器的自动测试软件提示
                options.addArguments("--remote-allow-origins=*", "--disable-features=Sync");//解决 403 出错问题，屏蔽Microsoft Edge浏览器在启动时弹出的同步用户配置和个性化设置的提示
                webDriver = new EdgeDriver(options);
            } catch (Exception e) {
                webDriverManager = null;
                log.warn(e);
            }
        }
        if (webDriverManager == null) {
            try {
                log.info("尝试选择 firefox 驱动");
                webDriverManager = WebDriverManager.firefoxdriver();
                webDriverManager.disableCsp();
                log.info("配置 firefox web driver");
                webDriverManager.setup();
                FirefoxOptions options = new FirefoxOptions();
                webDriver = new FirefoxDriver(options);
            } catch (Exception e) {
                webDriverManager = null;
                log.warn(e);
            }
        }
    }


    /**
     * 私有构造，避免外部初始化
     */
    private SeleniumUtil() {
    }

    /**
     * 获得工具类工厂
     *
     * @return
     */
    public static SeleniumUtil builder() {
        return new SeleniumUtil();
    }

    /**
     * 构建工具类
     *
     * @return
     */
    public SeleniumUtil build() {
        log.info("构建工具类开始");
        killDriverProcess();
        buildWebDriver();
        log.info("构建工具类结束");
        return this;
    }


    @Override
    public void close() {
        log.info("销毁工具类开始");
        killDriverProcess();
        webDriver.quit();
        log.info("销毁工具类结束");
    }

}