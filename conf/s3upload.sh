#/bin/bash
#
#  Script to upload and download SSTable file[s] to a personal Amazon S3 static storage
#

#
# s3cmd is makes transfers to and from Amazon S3 easy
#
# Import S3tools signing key
#
wget -O- -q http://s3tools.org/repo/deb-all/stable/s3tools.key | sudo apt-key add -
#
# Add the repo to sources.list
#
sudo wget -O/etc/apt/sources.list.d/s3tools.list http://s3tools.org/repo/deb-all/stable/s3tools.list
#
#Refresh package cache and install the newest s3cmd
#
sudo apt-get update && sudo apt-get install s3cmd

#
# Configure s3cmd to work with local machine
#
# AWSAccessKeyId = AKIAIPY4VVZYUMMLWQGA
# AWSSecretKey   = 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
# Bucket Info    = s3://scale-userdata
# Region         = Northern California [or] US
# Local Files    = /var/lib/cassandra/data/
#

#
# List the contents of bucket
#
s3cmd ls

#
# Upload some contents into bucket
#
s3cmd put FILE --recursive /var/lib/cassandra/data/ s3://scale-userdata/

#
# Download contents of bucket
#
s3cmd get s3://scale-userdata/ /var/lib/cassandra/data/s3-backup

#
# Sync local data with bucket
#
s3cmd sync /var/lib/cassandra/data/ s3://scale-userdata

#
# Put some contents into bucket
#
s3cmd sync s3://scale-userdata /var/lib/cassandra/data/