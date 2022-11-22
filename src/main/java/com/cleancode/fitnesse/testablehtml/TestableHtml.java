package com.cleancode.fitnesse.testablehtml;

import fitnesse.responders.run.SuiteResponder;
import fitnesse.wiki.*;

public class TestableHtml {
    static StringBuffer buffer;

    public String testableHtml(PageData pageData, boolean includeSuiteSetup) throws Exception {
        WikiPage wikiPage = pageData.getWikiPage();
        buffer = new StringBuffer();

        if (pageData.hasAttribute("Test")) {
            if (includeSuiteSetup) {
                WikiPage suiteSetup = PageCrawlerImpl.getInheritedPage(SuiteResponder.SUITE_SETUP_NAME, wikiPage);
                setUp(suiteSetup, wikiPage);
                //기존에 겹지던 내용을 함수로 빼냈음.
                //문자열도 겹치는 부분은 삭제함.
            }
            WikiPage setup = PageCrawlerImpl.getInheritedPage("SetUp", wikiPage);
            setUp(setup, wikiPage);
        }

        buffer.append(pageData.getContent());
        if (pageData.hasAttribute("Test")) {
            WikiPage teardown = PageCrawlerImpl.getInheritedPage("TearDown", wikiPage);
            teardown(wikiPage, teardown);
            if (includeSuiteSetup) {
                WikiPage suiteTeardown = PageCrawlerImpl.getInheritedPage(SuiteResponder.SUITE_TEARDOWN_NAME, wikiPage);
                teardown(wikiPage, suiteTeardown);
            }
        }

        pageData.setContent(buffer.toString());
        return pageData.getHtml();
    }

    private static void teardown(WikiPage wikiPage, WikiPage teardown) throws Exception {
        if (teardown != null) {
            WikiPagePath tearDownPath = wikiPage.getPageCrawler().getFullPath(teardown);
            String tearDownPathName = PathParser.render(tearDownPath);
            buffer.append("!include -teardown ." + tearDownPathName + "\n");
        }
    }

    private static void setUp(WikiPage setup, WikiPage wikiPage) throws Exception {
        if (setup != null) {
            WikiPagePath pagePath = wikiPage.getPageCrawler().getFullPath(setup);
            String pagePathName = PathParser.render(pagePath);
            buffer.append("!include -setup ." + pagePathName + "\n");
        }
    }
}
