FROM denvazh/scala

WORKDIR /app

USER root

ADD . .

RUN chmod a+x ./main.scala
RUN chmod a+x ./entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]
