package com.cleancode.fitnesse.testablehtml;

import fitnesse.responders.run.SuiteResponder;
import fitnesse.wiki.*;

public class TestableHtml {

    // 중복되는 문자열을 상수로 만들어 사용
    public static final String INCLUDE_SETUP = "!include -setup .";
    public static final String INCLUDE_TEARDOWN = "!include -teardown .";
    static StringBuffer buffer;

    public String testableHtml(PageData pageData, boolean includeSuiteSetup) throws Exception {
        WikiPage wikiPage = pageData.getWikiPage();
        buffer = new StringBuffer();
        boolean isPageData = pageData.hasAttribute("Test"); // if문의 조건을 boolean으로 따로 만들어 짧게 줄인다.

        if (isPageData) {
            isIncludeSuiteSetup(includeSuiteSetup, SuiteResponder.SUITE_SETUP_NAME, wikiPage, INCLUDE_SETUP);
            WikiPage setup = PageCrawlerImpl.getInheritedPage("SetUp", wikiPage);
            isSetupNull(setup, wikiPage, INCLUDE_SETUP);
        }

        buffer.append(pageData.getContent());
        if (isPageData) {
            WikiPage teardown = PageCrawlerImpl.getInheritedPage("TearDown", wikiPage);
            isSetupNull(teardown, wikiPage, INCLUDE_TEARDOWN);
            isIncludeSuiteSetup(includeSuiteSetup, SuiteResponder.SUITE_TEARDOWN_NAME, wikiPage, INCLUDE_TEARDOWN);
        }

        pageData.setContent(buffer.toString());
        return pageData.getHtml();
    }
    //중복되는 구문들을 하나의 메소드로 추출하여 사용
    private static void isIncludeSuiteSetup(boolean includeSuiteSetup, String suiteSetupName, WikiPage wikiPage, String includeSetup) throws Exception {
        if (includeSuiteSetup) {
            WikiPage suiteSetup = PageCrawlerImpl.getInheritedPage(suiteSetupName, wikiPage);
            isSetupNull(suiteSetup, wikiPage, includeSetup);
        }
    }
    //중복되는 구문들을 하나의 메소드로 추출하여 사용
    private static void isSetupNull(WikiPage setup, WikiPage wikiPage, String includeSetup) throws Exception {
        if (setup != null) {
            getPathName(wikiPage, setup, includeSetup);
        }
    }

    //중복되는 구문들을 하나의 메소드로 추출하여 사용
    private static void getPathName(WikiPage wikiPage, WikiPage suiteTeardown, String includeTeardown) throws Exception {
        WikiPagePath pagePath = wikiPage.getPageCrawler().getFullPath(suiteTeardown); // 중복되는 내용이지만 이름이 각기 다르게 설정되어 여러개 사용되고 있던 것을 합쳤다.
        String pathName = PathParser.render(pagePath);
        buffer.append(includeTeardown + pathName + "\n");
    }
}