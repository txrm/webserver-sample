FROM adoptopenjdk/openjdk17:alpine AS base-builder
ARG SBT_VER=1.9.6
RUN apk add --no-cache bash
ENV JAVA_HOME="/usr/lib/jvm/default-jvm/"
ENV PATH=$PATH:${JAVA_HOME}/bin
RUN \
	wget -O sbt-$SBT_VER.tgz https://github.com/sbt/sbt/releases/download/v$SBT_VERSION/sbt-$SBT_VERSION.tgz && \
  	tar -xzvf sbt-$SBT_VER.tgz && \
  	rm sbt-$SBT_VER.tgz

ENV PATH=$PATH:/sbt/bin/


FROM base-builder AS sbt-builder
WORKDIR /build
COPY lib/ lib/
COPY project/plugins.sbt project/
COPY build.sbt .
RUN sbt assembly

FROM sbt-builder as builder
COPY src/ src/
RUN sbt assembly

FROM adoptopenjdk/openjdk17:alpine-jre  AS base-core
ENV JAVA_HOME="/usr/lib/jvm/default-jvm/"
RUN apk add -v --update ttf-dejavu
ENV PATH=$PATH:${JAVA_HOME}/bin


FROM base-core
WORKDIR /lc-core
COPY --from=builder /build/target/scala-2.13/webserver-sample.jar .
RUN mkdir data/

EXPOSE 8000

CMD [ "java", "-jar", "webserver-sample.jar" ]