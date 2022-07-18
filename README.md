# exercicioREST

## Run 
```
sudo mvn spring-boot:run
```

## Run (specify port)

```shell 
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9000
```

## Endpoints 

| Type   | Endpoint            | Parameters |
|--------|---------------------|------------|
| GET    | devices/all         | -          |
| GET    | devices/listAddress | -          |
| POST   | devices/add         | -          |
| POST   | devices/addList     | -          |
| DELETE | devices/delete/{id} | id         |


## JSON objects used

