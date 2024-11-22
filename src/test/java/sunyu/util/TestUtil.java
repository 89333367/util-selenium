package sunyu.util;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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
        seleniumUtil.executeJavascript(ResourceUtil.readUtf8Str("showMessage.js"), "请选择省");

        //等待窗口数量为2
        seleniumUtil.waitNumberOfWindowsToBe(2);

        //只保留最后的窗口
        seleniumUtil.keepLastWindow();

        log.info("{}", seleniumUtil.getTitle());

        seleniumUtil.executeJavascript(ResourceUtil.readUtf8Str("showMessage.js"), "勾选复选框进行数据导出");

        AtomicInteger pageType = new AtomicInteger(0);//记录页面类型
        AtomicBoolean export = new AtomicBoolean(false);

        //向页面注入复选框
        log.info("注入导出复选框");
        String script = ResourceUtil.readUtf8Str("addCheckbox.js");
        ThreadUtil.execute(() -> {
            while (!export.get()) {
                WebElement searchDiv;
                while (true) {
                    try {
                        searchDiv = seleniumUtil.findElementByCssSelector("div.operate-btn");
                        pageType.set(1);
                        break;
                    } catch (Exception e) {
                        //log.warn("未找到 1 类页面");
                    }
                    try {
                        searchDiv = seleniumUtil.findElementByCssSelector("div.queryarea_footer>div");
                        pageType.set(2);
                        break;
                    } catch (Exception e) {
                        //log.warn("未找到 2 类页面");
                    }
                    try {
                        searchDiv = seleniumUtil.findElementByCssSelector("div.ser>form");
                        pageType.set(3);
                        break;
                    } catch (Exception e) {
                        //log.warn("未找到 3 类页面");
                    }
                    ThreadUtil.sleep(1000);
                }
                //log.info("找到 {} 类页面", pageType.get());
                seleniumUtil.executeJavascript(script, searchDiv);
                ThreadUtil.sleep(1000);
            }
        });

        log.info("等待选中导出复选框");
        seleniumUtil.waitElementSelectionStateToBeByCssSelector("#selenium_exportCheckbox", true);
        log.info("导出复选框被选中，准备导出数据");
        export.set(true);//更改导出标记
        seleniumUtil.executeJavascript(ResourceUtil.readUtf8Str("showMessage.js"), "准备导出数据....");

        log.info("页面类型 {}", pageType.get());
        if (pageType.get() == 1) {
            WebElement dataTable = seleniumUtil.waitVisibilityOfElementLocatedByCssSelector("table");
            Document table = Jsoup.parse(seleniumUtil.getOuterHTML(dataTable));
        } else if (pageType.get() == 2) {

        } else if (pageType.get() == 3) {

        }

        ThreadUtil.sleep(1000 * 10);
        seleniumUtil.close();
    }
}
