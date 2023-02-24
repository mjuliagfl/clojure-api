FROM clojure:openjdk-17-tools-deps-buster

RUN mkdir -p /app
WORKDIR /app

COPY deps.edn /app
RUN clojure -P

COPY . /app

CMD clojure -M -m clojure_api.core