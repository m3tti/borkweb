from babashka/babashka:1.4.193-SNAPSHOT-alpine

run apk update && apk add openjdk21-jdk git

copy . /app
workdir /app
run bb prepare

entrypoint ["bb", "-m", "core"]

