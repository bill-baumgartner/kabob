#!/bin/bash
# most downloads are handled in the Java code, however the irefweb downloads sometimes terminate
# prematurely so we use unix utilities to download the irefweb files
#
# NOTE: due to CD'ing in script use absolute file names!!
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

EXPECTED_ARGS=2

if [ $# -ne $EXPECTED_ARGS ]
then
    echo "#NOTE: due to CD'ing in script use absolute file names!!"
    echo "Usage: LOG_FILE TARGET_DOWNLOAD_DIR"
    echo "current usage:"
    echo $@
    exit 1
fi

LOG_FILE=$1
TARGET_DIR=$2

echo "Logging to: $LOG_FILE"
echo "Saving files to: $TARGET_DIR"

URL="http://irefindex.org/download/irefindex/data/archive/release_14.0/psi_mitab/MITAB2.6/9606.mitab.07042015.txt.zip"

exit_code=0
echo "downloading $url"
$DIR/download-and-log.sh $LOG_FILE $TARGET_DIR $URL
e=$?
if [ $e -ne 0 ]; then
    exit $e
fi

# unpack the download
cd $TARGET_DIR && { unzip -o 9606.mitab.07042015.txt.zip ; touch 9606.mitab.04072015.txt.ready ; cd - ; }
