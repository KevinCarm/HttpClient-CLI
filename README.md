
# Java CURL

This is a simple java curl example.
Makes HTTP requests via the command line


Instructions.

- Generate the jar file
```bash
  mvn clean package
```

- Execute your jar file
```bash
  java -jar .\target\httpcli-1.0.jar 
```

## Parameters
- u Api URL
- m Method (GET, POST, PUT, DELETE)
- b Body
- h Header

### Example


```bash
  java -jar .\target\httpcli-1.0.jar  -u https://petstore3.swagger.io/api/v3/store/order -m POST -b '{"id":10, "petId":1232, "quantity":9999}' -h Content-Type:application/json -h accept:application/json
```
