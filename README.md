#### Purpose
- Modify a REST API's body and/or query with strings from the [big list of naughty strings](src/test/scala/data/NaughtyStrings.scala) and check for the expected response code. 
- The list has over 500 strings, which can be slow, so run tests concurrently using the gatling load-test framework. 


#### Usage
1. [./build.sh](build.sh)  
2. Examples in [test.sh](test.sh). Or if building code, [Config.scala](src/test/scala/config/Config.scala) is probably a good starting point.


#### Examples 
##### 1. modifying body params
```
docker run -it --rm mvngatimage java \

-Dpath="http://localhost:3001/user" \
-Dmethod=POST \
-Dbody='"{"username":"${custom}", "password":"${custom}"}' \
-Dtarget=custom \

-cp target/performance-tests-1.0-SNAPSHOT.jar io.gatling.app.Gatling -s config.testSim
```

##### 2. modifying a query
```
docker run -it --rm mvngatimage java \

-Dpath="http://localhost:3001/users" \
-Dmethod=GET \
-Dquery='?name=${custom}' \
-Dtarget=custom \

-cp target/performance-tests-1.0-SNAPSHOT.jar io.gatling.app.Gatling -s config.testSim
```
##### 3. modifying a query preempted by an auth-request
```
docker run -it --rm mvngatimage java \

-Dauth=true \
-Dauth_path="http://localhost:3001/sign-in" \
-Dauth_method=POST \
-Dauth_body='"{"username":"${custom}", "password":"${custom}"}' \
-Dauth_query='?username=${custom}&password=${custom}' \
-Dauth_expRspCode=500 \

-Dpath="http://localhost:3001/users" \
-Dmethod=GET \
-Dquery='?name=${custom}' \
-Dtarget=custom \

-cp target/string-tests-1.0-SNAPSHOT.jar io.gatling.app.Gatling -s config.testSim
```