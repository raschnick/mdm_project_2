# Deployment Guide - Azure Container Instance

## Build JAR
```
./mvnw package
```

## Build & Push Image
```
docker build -t snackbar/mdm_project_2:latest .
docker push snackbar/mdm_project_2:latest
```

## Delete and deploy Azure Container Instance
```
az container delete -g mdm --yes --name urldetector
az container create -g mdm --name urldetector --image snackbar/mdm_project_2:latest --ports 8080 --restart-policy Always --ip-address Public
```
