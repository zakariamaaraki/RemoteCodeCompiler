FROM golang

WORKDIR /app

USER root

ADD . .

RUN chmod a+x ./main.go
RUN chmod a+x ./entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]
