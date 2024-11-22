package sunyu.util;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

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

        //等待页面加载完毕
        if (seleniumUtil.getCurrentUrl().contains("/gongshi")) {
            seleniumUtil.waitVisibilityOfElementLocatedByCssSelector(".divPage");

            String script = ResourceUtil.readUtf8Str("checkbox1.js");
            seleniumUtil.executeJavascript(script);
        } else if (seleniumUtil.getCurrentUrl().contains("/subsidyOpen")) {
            seleniumUtil.waitVisibilityOfElementLocatedByCssSelector(".pageBox");

            String script = ResourceUtil.readUtf8Str("checkbox2.js");
            seleniumUtil.executeJavascript(script);
        }

        ThreadUtil.sleep(1000 * 10);
        seleniumUtil.close();
    }
}
