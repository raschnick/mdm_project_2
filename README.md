# Deployment Guide

## Build JAR
```
   mvn package
```

## Build image
```
docker build -t snackbar/mdm_project2:latest .
```

## Push image
```
docker push snackbar/mdm_project2:latest
```

## Deploy image - TODO: Azure CLI version
```
create a docker instance on azure and run the image
```
