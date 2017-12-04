package org.interview.assignments.wipro.webcrawler.services;

import org.interview.assignments.wipro.webcrawler.model.Page;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


@Service
public class WebPageService {

    public Page getPageDetails(String url) throws IOException {

        Page page = new Page();

        page.setURL(url);
        Document webPage = getPageContent(url);
        Elements linkElements = getLinkElements(webPage);

        page.setLinks(getLinks(linkElements));
        page.getExternalURLs();

        return page;
    }

    private Document getPageContent(String url) throws IOException {

        return Jsoup.connect(url).get();
    }

    private Elements getLinkElements(Document webPage) {

        Elements links = webPage.select("a[href]");
        links.addAll(webPage.select("script[src]"));
        links.addAll(webPage.select("link[href]"));
        links.addAll(webPage.select("img[src]"));
        return links;
    }

    private Set<String> getLinks(Elements linkEelemnts) {
        Set<String> links = new HashSet<>();

        linkEelemnts.forEach(element -> {
            links.add(element.attr("abs:href"));
        });

        return links;
    }
}
