#!/usr/bin/env bash
consul agent \
    -server \
    -bootstrap-expect=1 \
    -node=server01 \
    -bind=127.0.0.1 \
    -config-file=/var/www/codelet-cloud/@consul/server.json -ui > /var/www/codelet-cloud/@consul/server-logs/server.log &

