FROM ruby

WORKDIR /app

USER root

ADD . .

RUN chmod a+x ./main.rb
RUN chmod a+x ./entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]
