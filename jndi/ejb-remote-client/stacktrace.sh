#!/bin/bash

target="$1"

bmfile="src/main/resources/$target.btm"

[ ! -e "$bmfile" ] && echo -e "$bmfile does not exist!" && exit 1

cmd="bmjava -l $bmfile"

if [ -z "${target##*jndi*}" ] ;then
  cmd="$cmd -Djndi=true "
fi
if [ -z "${target##*remote*}" ] ;then
  cmd="$cmd -Dremote=true "
fi

jarfile=`ls target/|grep with-dependencies`
cmd="$cmd -jar target/$jarfile"
echo -e "$cmd"
$cmd

