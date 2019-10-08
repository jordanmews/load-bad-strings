#!/bin/bash

### Skeleton of a run command
#docker run -it --rm mvngatimage java \
#-DYOUR-JVM-ARGS-HERE \
#-cp target/string-tests-1.0-SNAPSHOT.jar io.gatling.app.Gatling -s config.testSim

### example 1 - modifying a body
#docker run -it --rm mvngatimage java \
#-Dpath="http://localhost:3001/user" \
#-Dmethod=POST \
#-Dbody='"{"username":"${custom}", "password":"${custom}"}' \
#-Dtarget=custom \
#-cp target/string-tests-1.0-SNAPSHOT.jar io.gatling.app.Gatling -s config.testSim

### example 2 - modifying a query
#docker run -it --rm mvngatimage java \
#-Dpath="http://localhost:3001/users" \
#-Dmethod=GET \
#-Dquery='?name=${custom}' \
#-Dtarget=custom \
#-cp target/string-tests-1.0-SNAPSHOT.jar io.gatling.app.Gatling -s config.testSim

### example 3 - modifying a query with a pre-empted auth-request
#docker run -it --rm mvngatimage java \
#-Dpath="http://localhost:3001/users" \
#-Dmethod=GET \
#-Dquery='?name=${custom}' \
#-Dtarget=custom \
#-Dauth=true \
#-Dauth_path="http://localhost:3001/sign-in" \
#-Dauth_method=POST \
#-Dauth_body='"{"username":"${custom}", "password":"${custom}"}' \
#-Dauth_query='?username=${custom}&password=${custom}' \
#-Dauth_expRspCode=200 \
#-cp target/string-tests-1.0-SNAPSHOT.jar io.gatling.app.Gatling -s config.testSim


# Reference examples of all args
# Place any or all of these after the `java` and before the `-cp`

# -Dpath="http://localhost:3001/user" \
# -Dmethod=POST \
# -Dbody='"{"username":"${custom}", "password":"${custom}"}' \
# -Dquery='?name=${custom}'
# -Dtarget=custom \
# -DexpRspCode=200 \

# -Dauth=true \
# -Dauth_path="http://localhost:3001/sign-in" \
# -Dauth_method=POST \
# -Dauth_body='"{"username":"${custom}", "password":"${custom}"}' \
# -Dauth_query='?username=${custom}&password=${custom}' \
# -Dauth_expRspCode=200 \

# -Dthreads=100 \
# -DmaxDuration=300 \
# -DrampDuration=0 \
# -cp target/string-tests-1.0-SNAPSHOT.jar io.gatling.app.Gatling -s config.testSim
