#!/usr/bin/env bash

images=("gcc" "mono" "golang" "openjdk:11.0.6-jre-slim" "zenika/kotlin" "python:3" "rust" "denvazh/scala" "ruby" "haskell")

# Only for compiled languages
declare -A compilationDockerfilesPaths=(
  ["c"]="utility_c"
  ["cpp"]="utility_cpp"
  ["cs"]="utility_cs"
  ["go"]="utility_go"
  ["haskell"]="utility_hs"
  ["kotlin"]="utility_kt"
  ["rust"]="utility_rs"
  ["scala"]="utility_scala"
  ["java"]="utility_java"
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
  docker image build -f "executions/${compilationDockerfilesPaths[$language]}/Dockerfile.compilation" -t "compiler.$language" "executions/${compilationDockerfilesPaths[$language]}"
  if [ $? != 0 ]; then
    echo "!!! Error while building compilation images !!!"
    exit 1
  fi
done

echo "*** End of building images ***"
