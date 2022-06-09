FROM zenika/kotlin

WORKDIR /app

USER root

ADD . .

RUN chmod a+x ./main.kt
RUN chmod a+x ./entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]
