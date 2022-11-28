package com.cleancode.fitnesse.testablehtml;

import fitnesse.responders.run.SuiteResponder;
import fitnesse.wiki.*;

public class TestableHtml {
    public static final String INCLUDE_SETUP = "!include -setup .";
    public static final String INCLUDE_TEARDOWN = "!include -teardown .";
    public static final String SET_UP = "SetUp";
    public static final String TEAR_DOWN = "TearDown";
    static StringBuffer buffer;


    public String testableHtml(PageData pageData, boolean includeSuiteSetup) throws Exception {
        WikiPage wikiPage = pageData.getWikiPage();
        buffer = new StringBuffer();

        if (pageData.hasAttribute("Test")) {
            if (includeSuiteSetup) {
                WikiPage suiteSetup = PageCrawlerImpl.getInheritedPage(SuiteResponder.SUITE_SETUP_NAME, wikiPage);
                if (suiteSetup != null) {
                    WikiPagePath pagePath = wikiPage.getPageCrawler().getFullPath(suiteSetup);
                    String pagePathName = PathParser.render(pagePath);
                    buffer.append(INCLUDE_SETUP).append(pagePathName).append("\n");
                }
            }
            WikiPage setup = PageCrawlerImpl.getInheritedPage(SET_UP, wikiPage);
            if (setup != null) {
                WikiPagePath setupPath = wikiPage.getPageCrawler().getFullPath(setup);
                String setupPathName = PathParser.render(setupPath);
                buffer.append(INCLUDE_SETUP).append(setupPathName).append("\n");
            }
        }

        buffer.append(pageData.getContent());
        if (pageData.hasAttribute("Test")) {
            WikiPage teardown = PageCrawlerImpl.getInheritedPage(TEAR_DOWN, wikiPage);
            if (teardown != null) {
                WikiPagePath tearDownPath = wikiPage.getPageCrawler().getFullPath(teardown);
                String tearDownPathName = PathParser.render(tearDownPath);
                buffer.append( INCLUDE_TEARDOWN ).append(tearDownPathName).append("\n");
            }
            if (includeSuiteSetup) {
                WikiPage suiteTeardown = PageCrawlerImpl.getInheritedPage(SuiteResponder.SUITE_TEARDOWN_NAME, wikiPage);
                if (suiteTeardown != null) {
                    WikiPagePath pagePath = wikiPage.getPageCrawler().getFullPath(suiteTeardown);
                    String pagePathName = PathParser.render(pagePath);
                    buffer.append( INCLUDE_TEARDOWN ).append(pagePathName).append("\n");
                }
            }
        }

        pageData.setContent(buffer.toString());
        return pageData.getHtml();
    }
}