#!/bin/bash
# You have to run this once in order for ant builds to work
set -e

if [ -z "${ANDROID_SDK_ROOT+xxx}" ]; then
	echo "Please define ANDROID_SDK_ROOT to point to the Android SDK"
	exit 1
fi

if [ ! -d "$ANDROID_SDK_ROOT" ]; then
    echo "The directory $ANDROID_SDK_ROOT = ${ANDROID_SDK_ROOT} does not exist."
    exit 1
fi

ANDROID="$ANDROID_SDK_ROOT/tools/android"

command -v "$ANDROID" >/dev/null 2>&1 || { echo >&2 "The $ANDROID tool is not found.  Aborting."; exit 1; }

ANDROID_TARGET=android-16

# Make sure ANDROID_TARGET is installed

$ANDROID update sdk -a -u -t $ANDROID_TARGET

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
APP_ROOT="$( cd $DIR/.. && pwd )"

# Copy the Android Support jar file from the SDK to our libs directory.
mkdir -p $APP_ROOT/libs

cp $ANDROID_SDK_ROOT/extras/android/support/v4/android-support-v4.jar $APP_ROOT/libs

echo "Updating android project files"

$ANDROID update project -p "$APP_ROOT" --target $ANDROID_TARGET
