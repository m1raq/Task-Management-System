FROM ubuntu:latest
LABEL authors="miraq"

ENTRYPOINT ["top", "-b"]