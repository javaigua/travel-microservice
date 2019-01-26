# Travel Web Client Application (CaseX01)
A take-home-assignment, by Javier Igua.

## Execution

### Run all with Docker Compose
 1) First you need to mount this project's directory on Docker (Docker -> Preferences... -> File Sharing). 
 2) Then run the following command to start an integrated environment (with the Travel Service API Mock, this Travel Web Client Application, Prometheus and Grafana):
```
docker-compose up &
```

Open a browser at [http://localhost:8081](http://localhost:8081) for Travel Web Client Application interface
Open a browser at [http://localhost:3000](http://localhost:3000) for Grafana monitoring, go to `Dashboards` > `Manage` and click on `JVM(Micrometer)`

### Run independently with Gradle
Run the Simple Travel API mock
```
cd modules/simple-travel-api-mock
./gradlew bootRun
```

Run the Travel Web Client application
```
cd modules/casex01
./gradlew bootRun
```

### Unit tests
```
./gradlew clean :modules:casex01:test
```

## Screen Shots

![CaseX01 Web ScreenShot](etc/ScreenShotWebApp.png?raw=true "CaseX01 Web Interface")

![Grafana ScreenShot](etc/ScreenShotGrafana.png?raw=true "Grafana Web Interface")

![Logging ScreenShot](etc/ScreenShotLogging.png?raw=true "Logging ScreenShot")