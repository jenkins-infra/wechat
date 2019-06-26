#!/bin/sh

ls | grep png | xargs -I {} ffmpeg -i {} -vf "scale='min(1248,iw)':-1" {}.png
