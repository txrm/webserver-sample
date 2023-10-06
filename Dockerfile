FROM eclipse-temurin:17-alpine AS base-builder
ARG SBT_VER=1.9.6
RUN apk add --no-cache bash
ENV JAVA_HOME="/usr/lib/jvm/default-jvm/"
ENV PATH=$PATH:${JAVA_HOME}/bin
RUN \
	wget -O sbt-$SBT_VER.tgz https://github.com/sbt/sbt/releases/download/v$SBT_VER/sbt-$SBT_VER.tgz && \
  	tar -xzvf sbt-$SBT_VER.tgz && \
  	rm sbt-$SBT_VER.tgz

ENV PATH=$PATH:/sbt/bin/


FROM base-builder AS sbt-builder
WORKDIR /build
COPY project/plugins.sbt project/
COPY src/ src/
COPY build.sbt .
RUN sbt reload

EXPOSE 8000

ENTRYPOINT ["sbt"]

CMD ["run"]