#!/usr/bin/env bash
echo "---------------- [SERVER] ----------------"
consul members -http-addr=http://127.0.0.1:8930
echo "---------------- [CLIENT] ----------------"
consul members -http-addr=http://127.0.0.1:8940

