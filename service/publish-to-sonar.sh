#!/bin/bash

# Create test directory to avoid errors
mkdir -p ./de.raphaelmuesseler.financer.shared/src/test/
mkdir -p ./de.raphaelmuesseler.financer.client/src/test/


# Determine current branch
BRANCH_NAME=$(git symbolic-ref --short -q HEAD)
echo "Current branch: ${BRANCH_NAME}"

# Checking whether current branch is a Pull Request
if [[ ${BRANCH_NAME} == PR-* ]] ; then
    PR_NUMBER="${BRANCH_NAME//PR-/}"
    echo "Pull Request number: ${PR_NUMBER}"
    mvn sonar:sonar -P sonar -Dsonar.projectKey=financer -Dsonar.github.pullRequest=${PR_NUMBER}
else
    mvn sonar:sonar -P sonar -Dsonar.projectKey=financer -Dsonar.branch.name=${BRANCH_NAME}
fi