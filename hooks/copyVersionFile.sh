#!/bin/sh

versionFile="../../version_$(git branch | sed -n -e 's/^\* \(.*\)/\1/p').properties"
cp "$versionFile" '../../build/resources/main/version.properties' || true
