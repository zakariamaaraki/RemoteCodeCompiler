FROM haskell

WORKDIR /app

USER root

ADD . .

RUN chmod a+x ./main.hs
RUN chmod a+x ./entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]
