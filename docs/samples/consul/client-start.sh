#!/usr/bin/env bash
consul agent \
    -node=client01 \
    -bind=127.0.0.1 \
    -config-file=/var/www/codelet-cloud/@consul/client.json -ui > /var/www/codelet-cloud/@consul/client-logs/client.log &

