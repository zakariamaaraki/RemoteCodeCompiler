#!/usr/bin/env bash

images=("gcc" "mono" "golang" "openjdk:11.0.6-jre-slim" "zenika/kotlin" "python:3" "rust" "denvazh/scala" "ruby" "haskell")

# Only for compiled languages
declare -A compilationDockerfilesPaths=(
  ["c"]="c"
  ["cpp"]="cpp"
  ["cs"]="cs"
  ["go"]="go"
  ["haskell"]="hs"
  ["kotlin"]="kt"
  ["rust"]="rs"
  ["scala"]="scala"
  ["java"]="java"
)

# Pull all images before starting the container to make first requests faster
echo "Pulling all images..."
for i in "${images[@]}"
do
  echo "==> $i"
  docker pull "$i"
  if [ $? != 0 ]; then
    echo "!!! Error while pulling images !!!"
    exit 1
  fi
done

echo "Building compilation images..."
for language in "${!compilationDockerfilesPaths[@]}"
do
  echo "==> compiler.$language"
  docker image build -f "dockerfiles/Dockerfile.${compilationDockerfilesPaths[$language]}.compilation" -t "compiler.$language" "dockerfiles"
  if [ $? != 0 ]; then
    echo "!!! Error while building compilation images !!!"
    exit 1
  fi
done

echo "*** End of building images ***"
