package sunyu.util;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TestUtil {
    Log log = LogFactory.get();

    @Test
    void t001() {
        SeleniumUtil seleniumUtil = SeleniumUtil.builder().build();
        WebDriver webDriver = seleniumUtil.getWebDriver();

        //访问网站
        seleniumUtil.openUrl("https://td.sxwhkj.com/Account/BuTCapitalGongS");
        //最大化窗口
        seleniumUtil.maxWindow();

        //等待页面加载完毕
        seleniumUtil.waitPresenceOfElementLocatedByCssSelector(".footer");

        log.info("请选择省");

        //等待窗口数量为2
        seleniumUtil.waitNumberOfWindowsToBe(2);

        //只保留最后的窗口
        seleniumUtil.keepLastWindow();

        log.info("{}", seleniumUtil.getTitle());

        int pageType;//记录页面类型
        WebElement webElement = null;
        while (true) {
            try {
                webElement = seleniumUtil.findElementByCssSelector("div.operate-btn");
                pageType = 1;
                break;
            } catch (Exception e) {
                log.warn("未找到 1 类页面");
            }
            try {
                webElement = seleniumUtil.findElementByCssSelector("div.queryarea_footer>div");
                pageType = 2;
                break;
            } catch (Exception e) {
                log.warn("未找到 2 类页面");
            }
            try {
                webElement = seleniumUtil.findElementByCssSelector("div.ser>form");
                pageType = 3;
                break;
            } catch (Exception e) {
                log.warn("未找到 3 类页面");
            }
            ThreadUtil.sleep(1000);
        }
        log.info("找到 {} 类页面", pageType);

        //向页面注入复选框
        String script = ResourceUtil.readUtf8Str("checkbox.js");
        seleniumUtil.executeJavascript(script, webElement);

        ThreadUtil.sleep(1000 * 10);
        seleniumUtil.close();
    }
}
