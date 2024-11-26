package sunyu.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TestUtil {
    Log log = LogFactory.get();

    @Test
    void t001() {
        SeleniumUtil seleniumUtil = SeleniumUtil.builder().build();

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

        log.debug("{}", seleniumUtil.getTitle());

        AtomicInteger pageType = new AtomicInteger(0);//记录页面类型
        AtomicBoolean exporting = new AtomicBoolean(false);//标记是否正在导出

        //向页面注入复选框
        log.debug("注入导出复选框");
        String script = ResourceUtil.readUtf8Str("addCheckbox.js");
        ThreadUtil.execute(() -> {
            while (!exporting.get()) {
                WebElement searchDiv;
                while (true) {
                    try {
                        searchDiv = seleniumUtil.findElementByCssSelector("div.operate-btn");
                        pageType.set(1);
                        break;
                    } catch (Exception e) {
                    }
                    try {
                        searchDiv = seleniumUtil.findElementByCssSelector("div.queryarea_footer>div");
                        pageType.set(2);
                        break;
                    } catch (Exception e) {
                    }
                    try {
                        searchDiv = seleniumUtil.findElementByCssSelector("div.ser>form");
                        pageType.set(3);
                        break;
                    } catch (Exception e) {
                    }
                    ThreadUtil.sleep(1000);
                }
                //log.info("找到 {} 类页面", pageType.get());
                seleniumUtil.executeJavascript(script, searchDiv);
                seleniumUtil.executeJavascript(ResourceUtil.readUtf8Str("showMessage.js"), "勾选复选框进行数据导出");
                ThreadUtil.sleep(1000);
            }
        });

        log.info("等待选中导出复选框");
        seleniumUtil.waitElementSelectionStateToBeByCssSelector("#selenium_exportCheckbox", true);
        log.info("导出复选框被选中，准备导出数据");
        exporting.set(true);//更改导出标记
        seleniumUtil.executeJavascript(ResourceUtil.readUtf8Str("showMessage.js"), "准备导出数据....");

        log.debug("页面类型 {}", pageType.get());
        List<String> header = new ArrayList<>();
        if (pageType.get() == 1) {
            //共7474条， 第1/499页
            //共7条， 第1/1页
            String s = seleniumUtil.waitVisibilityOfElementLocatedByCssSelector("div.divPage>span").getText();
            int totalPage = Convert.toInt(ReUtil.getGroup1("/(\\d+)页", s));
            log.info("准备导出 {} 页数据", totalPage);
            //获取表头
            for (WebElement th : seleniumUtil.waitVisibilityOfAllElementsLocatedByCssSelector("table[role='c-table']>thead>tr>th")) {
                header.add(th.getText());
            }
            log.debug("{}", CollUtil.join(header, "\t"));
            for (int i = 1; i <= totalPage; i++) {
                if (i > 1) {//大于1页的时候才需要点击页码进行翻页
                    //log.info("翻页到 {}", i);
                    for (WebElement el : seleniumUtil.waitVisibilityOfAllElementsLocatedByCssSelector("div.pagerItem>a")) {
                        if (el.getText().equals(Convert.toStr(i))) {
                            //log.debug("点击翻页 {}", i);
                            seleniumUtil.executeJavascript("arguments[0].click()", el);//点击页码，进行翻页
                            break;
                        }
                    }
                }
                List<WebElement> trs = null;
                while (CollUtil.isEmpty(trs)) {
                    try {
                        trs = seleniumUtil.waitVisibilityOfAllElementsLocatedByCssSelector("table[role='c-table']>tbody>tr");
                    } catch (Exception e) {
                        log.warn("获取数据异常，重试");
                    }
                }
                //log.info("页码 {} 有 {} 行数据", i, trs.size());
                log.info("导出进度 {}/{}", i, totalPage);
                seleniumUtil.executeJavascript(ResourceUtil.readUtf8Str("showMessage.js"), StrUtil.format("导出进度 {}/{}", i, totalPage));
                for (WebElement tr : trs) {
                    List<String> tds = new ArrayList<>();
                    for (WebElement td : tr.findElements(By.cssSelector("td"))) {
                        tds.add(td.getText());
                    }
                    log.debug("{}", CollUtil.join(tds, "\t"));
                }
                seleniumUtil.removeWebElements(trs);//这些数据已导出，删除数据，翻页后会添加新数据
            }
        } else if (pageType.get() == 2) {
            // 共229341条，第1/15290页
            // 共2条，第1/1页
            String s = seleniumUtil.waitVisibilityOfElementLocatedByCssSelector("span.slot_style").getText();
            int totalPage = Convert.toInt(ReUtil.getGroup1("/(\\d+)页", s));
            log.info("准备导出 {} 页数据", totalPage);
            //获取表头
            for (WebElement th : seleniumUtil.waitPresenceOfAllElementsLocatedByCssSelector("table.el-table__header>thead>tr>th")) {
                header.add(th.getText());
            }
            log.debug("{}", CollUtil.join(header, "\t"));
            String preTr0HTML = null;
            for (int i = 1; i <= totalPage; i++) {
                if (i > 1) {//大于1页的时候才需要点击页码进行翻页
                    //log.info("翻页到 {}", i);
                    List<WebElement> els = null;
                    while (CollUtil.isEmpty(els)) {
                        try {
                            els = seleniumUtil.waitPresenceOfAllElementsLocatedByCssSelector("ul.el-pager>li");
                        } catch (Exception e) {
                            log.warn("获取分页条异常，重试");
                        }
                    }
                    for (WebElement el : els) {
                        if (el.getText().equals(Convert.toStr(i))) {
                            //log.debug("点击翻页 {}", i);
                            seleniumUtil.executeJavascript("arguments[0].click()", el);//点击页码，进行翻页
                            break;
                        }
                    }
                }
                List<WebElement> trs = null;
                while (CollUtil.isEmpty(trs)) {
                    try {
                        if (seleniumUtil.findElementByCssSelector("div.code-dialog").isDisplayed()) {
                            log.info("等待输入验证码");
                            seleniumUtil.findElementByCssSelector("input[placeholder='请输入验证码']").sendKeys("");//使其光标在输入框中
                            seleniumUtil.waitInvisibilityOfElementLocatedByCssSelector("div.code-dialog");
                            log.info("验证码输入完毕");
                        }
                        trs = seleniumUtil.waitVisibilityOfAllElementsLocatedByCssSelector("table.el-table__body>tbody>tr");
                        if (preTr0HTML != null && preTr0HTML.equals(seleniumUtil.getOuterHTML(trs.get(0)))) {
                            log.warn("当前获取的数据与上一页数据相同，重新获取");
                            trs = null;
                        } else {
                            preTr0HTML = seleniumUtil.getOuterHTML(trs.get(0));
                        }
                    } catch (Exception e) {
                        log.warn("获取数据异常，重试");
                    }
                }
                //log.info("页码 {} 有 {} 行数据", i, trs.size());
                log.info("导出进度 {}/{}", i, totalPage);
                seleniumUtil.executeJavascript(ResourceUtil.readUtf8Str("showMessage.js"), StrUtil.format("导出进度 {}/{}", i, totalPage));
                for (WebElement tr : trs) {
                    List<String> tds = new ArrayList<>();
                    for (WebElement td : tr.findElements(By.cssSelector("td"))) {
                        tds.add(td.getText());
                    }
                    log.debug("{}", CollUtil.join(tds, "\t"));
                }
            }
        } else if (pageType.get() == 3) {
            seleniumUtil.waitVisibilityOfElementLocatedByCssSelector("div.foot_main>p");//等待footer显示
            int totalPage = 1;
            for (WebElement el : seleniumUtil.findElementsByCssSelector("div.pagerItem>a")) {
                if (el.getText().equals("尾页")) {
                    // /GZBT2021To23/pub/GongShiSearch?pageIndex=2
                    String s = el.getAttribute("href");
                    totalPage = Convert.toInt(ReUtil.getGroup1("pageIndex=(\\d+)", s));
                }
            }
            log.info("准备导出 {} 页数据", totalPage);
            //获取表头
            for (WebElement th : seleniumUtil.waitVisibilityOfAllElementsLocatedByCssSelector("table>tbody>tr>td>table>thead>tr>td")) {
                header.add(th.getText());
            }
            log.debug("{}", CollUtil.join(header, "\t"));
            for (int i = 1; i <= totalPage; i++) {
                if (i > 1) {//大于1页的时候才需要点击页码进行翻页
                    //log.info("翻页到 {}", i);
                    List<WebElement> els = null;
                    while (CollUtil.isEmpty(els)) {
                        try {
                            els = seleniumUtil.waitPresenceOfAllElementsLocatedByCssSelector("div.pagerItem>a");
                        } catch (Exception e) {
                            log.warn("获取分页条异常，重试");
                        }
                    }
                    for (WebElement el : els) {
                        if (el.getText().equals(Convert.toStr(i))) {
                            //log.debug("点击翻页 {}", i);
                            seleniumUtil.executeJavascript("arguments[0].click()", el);//点击页码，进行翻页
                            break;
                        }
                    }
                }
                List<WebElement> trs = null;
                while (CollUtil.isEmpty(trs)) {
                    try {
                        trs = seleniumUtil.waitVisibilityOfAllElementsLocatedByCssSelector("#list-pub>tr");
                    } catch (Exception e) {
                        log.warn("获取数据异常，重试");
                    }
                }
                //log.info("页码 {} 有 {} 行数据", i, trs.size());
                log.info("导出进度 {}/{}", i, totalPage);
                seleniumUtil.executeJavascript(ResourceUtil.readUtf8Str("showMessage.js"), StrUtil.format("导出进度 {}/{}", i, totalPage));
                for (WebElement tr : trs) {
                    List<String> tds = new ArrayList<>();
                    for (WebElement td : tr.findElements(By.cssSelector("td"))) {
                        tds.add(td.getText());
                    }
                    log.debug("{}", CollUtil.join(tds, "\t"));
                }
                seleniumUtil.removeWebElements(trs);//这些数据已导出，删除数据，翻页后会添加新数据
            }
        }
        seleniumUtil.close();
    }

    @Test
    void t002() {
        String s = "共7474条， 第1/499页";
        log.info("{}", ReUtil.getGroup1("第(\\d+)/", s));
        log.info("{}", ReUtil.getGroup1("/(\\d+)页", s));
        s = "共7条， 第1/1页";
        log.info("{}", ReUtil.getGroup1("第(\\d+)/", s));
        log.info("{}", ReUtil.getGroup1("/(\\d+)页", s));
    }

    @Test
    void t003() {
        String s = " 共229341条，第1/15290页 ";
        log.info("{}", ReUtil.getGroup1("第(\\d+)/", s));
        log.info("{}", ReUtil.getGroup1("/(\\d+)页", s));
        s = " 共2条，第1/1页 ";
        log.info("{}", ReUtil.getGroup1("第(\\d+)/", s));
        log.info("{}", ReUtil.getGroup1("/(\\d+)页", s));
    }

    @Test
    void t004() {
        for (int i = 1; i <= 10; i++) {
            log.info("{}", i);
            if (i == 5) {
                i--;
                ThreadUtil.sleep(1000);
                continue;
            }
            ThreadUtil.sleep(1000);
        }
    }

    @Test
    void t005() {
        String s = "/GZBT2021To23/pub/GongShiSearch?pageIndex=2";
        log.info("{}", ReUtil.getGroup1("pageIndex=(\\d+)", s));
    }
}
