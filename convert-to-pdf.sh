#!/usr/bin/env bash

ORIG_DIR=$(pwd)
TARGET_DIR="event-mailer/src/main/resources/doc"

cd $TARGET_DIR
echo $(pwd)
libreoffice --headless --convert-to pdf *.docx
mv *.pdf ../pdf

cd $ORIG_DIR