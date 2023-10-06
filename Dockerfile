FROM ubuntu:latest
LABEL authors="txrm"

ENTRYPOINT ["top", "-b"]