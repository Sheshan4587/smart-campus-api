# smart-campus-api

## Overview
This project is a RESTful API built using JAX-RS to manage a Smart Campus system. It handles the management of rooms, sensors (e.g., CO2, Temperature), and records sensor readings using an in-memory, thread-safe data store.

## Video Demonstration 🎥
**[Click here to watch the Video Demonstration](https://drive.google.com/drive/folders/1BbMvneV9fIYu3Mruv91OUDABxk436JRFE)**

## GitHub Link: https://github.com/Sheshan4587/smart-campus-api

## Technologies Used
* **Java**
* **JAX-RS** (Jakarta RESTful Web Services)
* **Server:** GlassFish / Tomcat
* **Testing:** Postman

## How to Run
1. Clone the repository
    bashgit clone https://github.com/YOUR_USERNAME/smart-campus-api.git
2. Open in NetBeans

File → Open Project → select the SmartCampus folder
NetBeans will recognise it as a Maven project

3. Build the project

Right-click project → Clean and Build
    Maven will download all dependencies automatically

4. Configure Tomcat in NetBeans

Go to Services tab → Servers → Add Server → Apache Tomcat
    Point it to your Tomcat installation directory

5. Run the project

Right-click project → Run
    Tomcat will start and deploy the WAR automatically

6. API is now available at:
    http://localhost:8080/SmartCampus/api/v1

## API Endpoints

| Method | Path                                | Description                                | Status Codes                |
|--------|-------------------------------------|--------------------------------------------|-----------------------------|
| GET    | /api/v1                             | Discovery — API metadata and HATEOAS links | 200                         |
| GET    | /api/v1/rooms                       | List all rooms                             | 200                         |
| POST   | /api/v1/rooms                       | Create a new room                          | 201, 400, 409               |
| GET    | /api/v1/rooms/{roomId}              | Get a specific room                        | 200, 404                    |
| DELETE | /api/v1/rooms/{roomId}              | Delete a room (blocked if sensors present) | 204, 404, 409               |
| GET    | /api/v1/sensors                     | List all sensors (optional `?type=` filter)| 200                         |
| POST   | /api/v1/sensors                     | Register a new sensor                      | 201, 400, 409, 422          |
| GET    | /api/v1/sensors/{sensorId}          | Get a specific sensor                      | 200, 404                    |
| GET    | /api/v1/sensors/{sensorId}/readings | Get all readings for a sensor              | 200, 404                    |
| POST   | /api/v1/sensors/{sensorId}/readings | Add a new reading                          | 201, 400, 403, 404          |

## Sample curl Commands
 
### 1. Discovery endpoint
```bash
curl -X GET http://localhost:8080/SmartCampus/api/v1 \
  -H "Accept: application/json"
```
 
### 2. Create a room
```bash
curl -X POST http://localhost:8080/SmartCampus/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"LIB-301","name":"Library Quiet Study","capacity":50}'
```
 
### 3. Create a sensor
```bash
curl -X POST http://localhost:8080/SmartCampus/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","currentValue":22.5,"roomId":"LIB-301"}'
```
 
### 4. Filter sensors by type
```bash
curl -X GET "http://localhost:8080/SmartCampus/api/v1/sensors?type=CO2" \
  -H "Accept: application/json"
```
 
### 5. Post a reading to a sensor
```bash
curl -X POST http://localhost:8080/SmartCampus/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value":25.3}'
```
 
### 6. Get all readings for a sensor
```bash
curl -X GET http://localhost:8080/SmartCampus/api/v1/sensors/TEMP-001/readings \
  -H "Accept: application/json"
```
 
### 7. Attempt to delete a room that has sensors — expect 409
```bash
curl -X DELETE http://localhost:8080/SmartCampus/api/v1/rooms/LIB-301 \
  -H "Accept: application/json"
```
 
### 8. Register a sensor with a non-existent roomId — expect 422
```bash
curl -X POST http://localhost:8080/SmartCampus/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"HUM-001","type":"Humidity","status":"ACTIVE","currentValue":55.0,"roomId":"FAKE-999"}'

## Conceptual Report

/* --- Q1 --- */
By default, JAX‑RS creates a new resource instance for every incoming request (not a singleton).  
If data were stored in instance variables, it would vanish after each request.  
To prevent data loss, shared in‑memory structures must be centralized in a Singleton `DataStore`.  
Since servers handle concurrent requests, these structures must be synchronized (e.g., `Collections.synchronizedMap`) to avoid race conditions and corruption.  

/* --- Q2 --- */
Hypermedia links embedded in responses allow clients to discover API functionality dynamically.  
Instead of relying on static documentation, clients follow links provided by the API.  
This decouples front‑end logic from back‑end routing, reduces maintenance effort, and ensures clients adapt automatically if endpoints change.  

/* --- Q3 --- */
Returning only IDs minimizes payload size and saves bandwidth, which is efficient for large lists.  
However, clients must issue additional requests to fetch details, increasing HTTP traffic.  
Returning full objects consumes more bandwidth upfront but reduces round trips, making it more convenient when clients need complete data immediately.  

/* --- Q4 --- */
DELETE is idempotent in this design.  
The first DELETE removes the resource and returns `204 No Content`.  
Subsequent identical DELETE requests return `404 Not Found` since the resource no longer exists.  
The system’s state remains unchanged after the first deletion, satisfying idempotency.  

/* --- Q5 --- */
When `@Consumes(MediaType.APPLICATION_JSON)` is used, JAX‑RS validates the request’s `Content-Type`.  
If the client sends data in another format (e.g., `text/plain` or `application/xml`), the framework rejects it before reaching application code and responds with `415 Unsupported Media Type`.  

/* --- Q6 --- */
Path parameters are intended for strict hierarchies or unique resources (e.g., `/rooms/123`).  
Query parameters are optional and composable, making them ideal for filtering or searching collections (e.g., `?type=Temperature&status=Active`).  
This flexibility makes query parameters superior for search and filtering use cases.  

/* --- Q7 --- */
Sub‑Resource Locators delegate nested resource logic to separate classes (e.g., `SensorReadingResource`).  
This follows the Single Responsibility Principle, avoids “God Classes,” and keeps APIs modular.  
It makes large APIs easier to maintain, test, and extend by isolating functionality into smaller, focused classes.  

/* --- Q8 --- */
`404 Not Found` indicates the endpoint itself doesn’t exist.  
`422 Unprocessable Entity` is more accurate when the endpoint exists and the JSON syntax is valid, but the payload contains invalid semantics (e.g., referencing a non‑existent `roomId`).  
This distinction improves clarity for clients.  

/* --- Q9 --- */
Exposing stack traces leaks sensitive information such as package names, file paths, frameworks, and library versions.  
Attackers can use this data to identify known vulnerabilities (CVEs) and exploit them.  
Preventing stack trace exposure is critical to avoid information leakage.  

/* --- Q10 --- */
Using JAX‑RS filters centralizes cross‑cutting concerns like logging.  
This adheres to the DRY principle, avoids cluttering resource methods with repetitive `Logger.info()` calls, and ensures consistent logging across all endpoints.  
Updates to logging format or policy can be made in one place instead of hundreds of methods.