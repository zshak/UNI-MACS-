#!/bin/bash

for i in {1..10}; do
    echo "Iteration $i"
    go test -run 2B

done
