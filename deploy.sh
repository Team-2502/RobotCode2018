#!/bin/bash

COUNT=10

while :
do
	./gradlew deploy && exit
	sleep $COUNT
	COUNT=$COUNT + 10 
done

