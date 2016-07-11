################################################
# README
################################################

# Build steps/commands (as of July 6, 2016)
# 1) Pull and build the project using Maven from the parent directory
# 2) Set the appropriate environment variables:
#   2.1) export ENVIRONMENT=dev (qa/integ/prod)
#   2.2) export NEW_RELIC_LICENSE_KEY=1234
# 3) docker build -f Dockerfile -t elasticsearch-server-docker-image .
# 4) docker run -p 10000:10000 -e ENV=$ENV -e NEW_RELIC_LICENSE_KEY=$NEW_RELIC_LICENSE_KEY -t elasticsearch-server-docker-image java -javaagent:newrelic.jar -Dnewrelic.environment=$ENV -jar elasticsearch_server.jar server ${ENV}_config.yaml

FROM elasticsearch

# Expose ports.
#   - 9200: HTTP
#   - 9300: transport
# Don't expose 9200/9300 to the outside world; force people that want to interact with
# this service to go through our "proxy" for standardization and security.
# EXPOSE 9200
#EXPOSE 9300

# expose our service to the outside world
EXPOSE 10000

# make the builder pass in the appropriate version number so we choose the correct jar
# leave empty so the build fails (can't find jar) if it's not provided and correct
ARG BUILD_VERSION

# pull the built jar
ADD /service/target/elasticsearch-server-${BUILD_VERSION}.jar elasticsearch_server.jar

# include our newrelic yaml so that we can push stats
ADD newrelic.yml newrelic.yml

# add each of our configs so that we can run in any mode (pending the appropriate environment variable is set
ADD client/src/main/resources/config/modules/elasticsearch_service/dev_config.yaml dev_config.yaml
ADD client/src/main/resources/config/modules/elasticsearch_service/qa_config.yaml qa_config.yaml
ADD client/src/main/resources/config/modules/elasticsearch_service/integ_config.yaml integ_config.yaml
ADD client/src/main/resources/config/modules/elasticsearch_service/prod_config.yaml prod_config.yaml
