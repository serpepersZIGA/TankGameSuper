#!/usr/bin/env bash
#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

set -e

# Get dir
DIRNAME="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
APP_BASE_NAME="$(basename "$0")"
APP_HOME="$DIRNAME"

# JVM
DEFAULT_JVM_OPTS=(-Xmx64m -Xms64m)

# Searching java
if [[ -n "$JAVA_HOME" ]]; then
  JAVA_EXE="$JAVA_HOME/bin/java"
  if [[ ! -x "$JAVA_EXE" ]]; then
    echo ""
    echo "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME"
    echo ""
    echo "Please set the JAVA_HOME variable in your environment to match the"
    echo "location of your Java installation."
    exit 1
  fi
else
  JAVA_EXE=$(command -v java || true)
  if [[ -z "$JAVA_EXE" ]]; then
    echo ""
    echo "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH."
    echo ""
    echo "Please set the JAVA_HOME variable in your environment to match the"
    echo "location of your Java installation."
    exit 1
  fi
fi

# gradle-wrapper.jar path
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

# Gradle Wrapper
exec "$JAVA_EXE" "${DEFAULT_JVM_OPTS[@]}" $JAVA_OPTS $GRADLE_OPTS -Dorg.gradle.appname="$APP_BASE_NAME" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
