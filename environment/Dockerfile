FROM docker

USER root

WORKDIR /build

RUN apk add bash

ADD dockerfiles dockerfiles

ADD build.sh build.sh
RUN chmod a+x build.sh

ENTRYPOINT ["./build.sh"]