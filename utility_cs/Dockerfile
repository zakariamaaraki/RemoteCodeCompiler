FROM mono

WORKDIR /app

USER root

ADD . .

RUN chmod a+x ./main.cs
RUN chmod a+x ./entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]
