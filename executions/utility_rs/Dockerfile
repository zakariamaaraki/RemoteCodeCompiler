FROM rust

WORKDIR /app

USER root

ADD . .

RUN chmod a+x ./main.rs
RUN chmod a+x ./entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]
