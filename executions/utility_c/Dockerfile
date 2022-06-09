FROM gcc

WORKDIR /app

USER root

ADD . .

RUN chmod a+x ./main.c
RUN chmod a+x ./entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]
