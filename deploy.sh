#!/bin/bash

COUNT=3

while :
do
	./gradlew deploy && exit
	sleep $COUNT
	COUNT=$COUNT + 2
done

