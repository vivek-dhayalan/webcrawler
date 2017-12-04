package org.interview.assignments.wipro.webcrawler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Page {

    @Getter @Setter private String URL;

    @Getter @Setter private Set<String> links;

    @Getter @Setter private Set<String> externalURLs;

    @Getter @Setter private Set<String> internalURLs;

    @Getter @Setter private Set<String> staticURLs;
}
