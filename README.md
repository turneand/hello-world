# Introduction

Sample web crawler program

# How to build and run:

To build on the command line:
```
   mvn clean install
```

To execute, locate the directory containing "apjt-core-1.0-SNAPSHOT.jar" and run:
```
   java -jar apjt-core-1.0-SNAPSHOT.jar << URL >>
```

Where << URL >> is the url to parse, you should then get output that eventually ends up like:
```
   INTERNAL - http://localhost:54410/index.html
      img - http://localhost:54410/image2.jpg
      INTERNAL - http://localhost:54410/file1.html
      img - http://localhost:54410/image1.jpg
      img - http://localhost:54410/image3.jpg
      INTERNAL - http://localhost:54410/nested/file2.html
        a - http://www.google.co.uk
        img - http://localhost:54410/nested/nestedimg.jpg
      INTERNAL - ERROR[404] - http://localhost:54410/file3.html
```

INTERNAL indicates a relative internal link, "a" indicates an external resource, "img" indicates an image, etc (other tags containing a "src" attribute are also supported).

# Three main components to question:
   1. Write a crawler        - I believe this is the main functionality being requested, so although 3rd party solutions are available, I have opted to implement a basic version myself
   1. Write a webpage parser - Parsing html is surprisingly difficult, so I have utilised "jsoup" 3rd party library for this functionality
   1. Print out results      - Simple outputting to console has been implemented due to time constraints, rather than a graphical model

# Time spent:
   1. approximately 15 minutes was spent with "prep", reading question, determining an html parser, etc
   1. following days a little over two hours was spent on actual coding, but additional time was spent writing this documentation
   1. did already have a "template" maven project that runs a build - but needed to add the shade plugin separately - so not much time spent there

# Missed functionality due to time constraints and tradeoffs
   1. Needs additional tests around the parsing of the html pages
   1. Launcher needs more functionality that can be tested
   1. Need to better handle the distinction between internal and external links, as support is brittle
   1. Should support maximum nested pages, rather than maximum number of pages, as the output would then be a bit more logical
   1. Should allow the maxPages and/or maxDepth to be passed in as parameter
   1. Handling of pages should be executed in parallel - code has been mostly written to support parallelisation, but the launcher does not use it
   1. Rendering of the "site map" should be in a better form, graphical output, xml site, etc
   1. Input to the program should be validated
   1. Should act like a better behaved robot
   1. Documentation should be improved
   1. Unit tests are a little heavy-weight due to the jetty server wiring as a TestRule (although doesn't add too much overhead) - implemented something similar recently for cxf testing, so knew how easy it is although means the app is tested a lot more production-like, the main reason for it was to simplify the jsoup code and wiring, to remove too many conditional parsing types
   1. Used jsoup for the html parsing, this was the first time I've ever come across jsoup, so the API usage is mainly based upon their examples - if for a real app would prefer to spend more time understanding that area
   1. Model implemented was separate parse and render, so will be holding more resources in memory, but have implemented it such that a page hopefully won't be parsed more than once
   1. Simple in-memory cache of the pages was used, rather than a store that could output to disk, therefore would not scale well

   
