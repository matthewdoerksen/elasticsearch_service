# ElasticSearchService
A simple Dropwizard-based project which allows you to get an ElasticSearch machine up and running easily. See https://github.com/matthewdoerksen/elasticsearch_service/issues to see what needs to be implemented as well as bug tracking.

For a walkthrough of this project, its evolution and tutorials, please see http://restfuljavaforbeginners.weebly.com.

Releases (https://github.com/matthewdoerksen/elasticsearch_service/releases/) are tagged (and released) such that they include the incremental changes from past projects. They should follow the convention of major.minor.revision where:

1) Major changes cause breakage to occur between project versions. If this occurs, what breaks should be noted in the release notes (along with any workarounds, if necessary).

2) Minor changes include new features that do not break backwards compatibility (such as new endpoints).

3) Revisions include minor bug fixes, comments, etc. not big enough to be included as a minor change.

Anyone is free to use this project as the starting point of their own. However, I do ask that you return credit to me for the work I have done and send me an email (matthewdoerksen.dev@gmail.com) just so I can stay informed regarding who is using this project (disclosing project inner workings or information about unreleased projects is NOT required).

Project Articles

http://restfuljavaforbeginners.weebly.com/articles.html

Thanks!
Matt Doerksen (matthewdoerksen.dev@gmail.com)



Notes:

This project makes a couple of assumptions regarding your machine setup:

1) That you already have ElasticSearch installed.

2) That you have downloaded the newrelic.jar and placed it in your home directory.
    2.1) You should make sure that the version of the New Relic API (in the client) matches that of the newrelic jar.