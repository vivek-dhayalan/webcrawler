package org.interview.assignments.wipro.webcrawler;

import lombok.extern.slf4j.Slf4j;
import org.interview.assignments.wipro.webcrawler.model.Page;
import org.interview.assignments.wipro.webcrawler.services.WebPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import javax.annotation.PostConstruct;
import java.io.IOException;


@SpringBootApplication(scanBasePackages = {"org.interview.assignments.wipro"})
@EnableConfigurationProperties
@EnableDiscoveryClient
@Slf4j
public class WebcrawlerApplication {

    @Autowired
    private WebPageService webPageService;

	public static void main(String[] args) {
		SpringApplication.run(WebcrawlerApplication.class, args);
	}

	@PostConstruct
    public void getLinks(){

        try {
            log.info("POST CONSTRUCT");
            Page parsedPage = webPageService.getPageDetails("http://wiprodigital.com/");
            log.info("page is parsend");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
