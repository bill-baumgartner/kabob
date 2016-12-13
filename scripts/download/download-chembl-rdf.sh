#!/bin/bash
# ChEMBL is availabe as RDF. This script downloads the ChEMBL turtle RDF files.
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

mkdir -p $TARGET_DIR
cd $TARGET_DIR

wget -r -nd -nc -l1 --no-parent -A.gz ftp://ftp.ebi.ac.uk/pub/databases/chembl/ChEMBL-RDF/latest/

# remove an extraneous line break on lines 16185-6
gunzip -c chembl_22.1_cellline.ttl.gz | sed 's/6286$/6286> ;/' | sed '/^> ;/d' | gzip -c > chembl_22.1_cellline.FIXED.ttl.gz
rm chembl_22.1_cellline.ttl.gz
