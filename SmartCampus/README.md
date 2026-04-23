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
1. Clone this repository to your local machine.
2. Open the project in Apache NetBeans (or your preferred IDE).
3. Clean and Build the project.
4. Run the project on your local server.
5. The API discovery endpoint is accessible at `/api/v1`.

---

## Conceptual Report

### 1.1 Service Architecture & Setup
By default, the JAX-RS runtime instantiates a **new instance** of a Resource class for every single incoming HTTP request; it is not treated as a singleton. This architectural decision means that if we stored our application data in standard instance variables within the resource classes, the data would be lost as soon as the HTTP request finished. To prevent this data loss, we must centralize our in-memory data structures (HashMaps) inside a Singleton class (like the `DataStore` we created). Because web servers handle multiple requests concurrently using multi-threading, simultaneous requests attempting to modify these central Maps could cause data corruption. To prevent this, we wrapped our HashMaps in `Collections.synchronizedMap()`, which locks the maps and ensures only one thread can modify the data at a time, eliminating race conditions.

### 1.2 The "Discovery" Endpoint
Hypermedia as the Engine of Application State (HATEOAS) is a hallmark of advanced RESTful design because it makes the API self-discoverable. Instead of forcing client developers to hardcode URL paths based on static documentation, the API provides dynamic navigation links inside the JSON response itself. This benefits client developers massively because it decouples the front-end code from backend routing. If the server needs to change an endpoint's URL structure in the future, the client code does not break; the client simply follows the new dynamically provided link.

### 2.1 Room Resource Implementation
Returning only IDs heavily conserves network bandwidth and speeds up the initial API response time, making it highly efficient for massive lists. However, it increases client-side processing; if the client needs to display room names, they must execute subsequent `GET` requests for every single ID (known as the "N+1 query problem"), generating high HTTP overhead. Conversely, returning the full room objects consumes more upfront network bandwidth but minimizes HTTP round-trips, allowing the client to render the UI immediately without extra processing or API calls.

### 2.2 Room Deletion & Safety Logic
Yes, the `DELETE` operation in this implementation is idempotent. Idempotency means that making multiple identical requests has the same effect on the server's state as making a single request. If a client mistakenly sends the exact same `DELETE` request for a room multiple times, the first request will remove the room and return a `204 No Content`. The subsequent requests will simply return a `404 Not Found` because the room is already gone. Even though the HTTP status code changes, the *state of the system* remains exactly the same—the room remains deleted. 

### 3.1 Sensor Resource & Integrity
Because the method explicitly uses `@Consumes(MediaType.APPLICATION_JSON)`, JAX-RS strictly enforces the incoming data format. If a client attempts to send data in a different format like `text/plain` or `application/xml`, the JAX-RS framework intercepts the request before it even reaches the Java method. It automatically handles the mismatch by rejecting the payload and returning an HTTP `415 Unsupported Media Type` status code to the client, preventing internal parsing errors.

### 3.2 Filtered Retrieval & Search
Path parameters (like `/type/CO2`) are designed to identify a specific, unique resource or signify a rigid hierarchy. Query parameters are designed to filter, sort, or modify the view of an existing collection. The query parameter approach is considered superior because queries are optional and highly composable. For example, using query parameters makes it trivial to chain multiple filters together (e.g., `?type=CO2&status=ACTIVE`). Attempting to do this with path parameters leads to deep, confusing, and brittle URL routing matrices.

### 4.1 Deep Nesting with Sub-Resources
The Sub-Resource Locator pattern aligns with the Single Responsibility Principle. By using a locator method to return a separate `SensorReadingResource` class, the logic for readings is entirely encapsulated away from the core `SensorResource` logic. In large APIs, defining deep nesting inside a single massive controller creates an unmaintainable "God Class". Delegating sub-paths to their own classes keeps file sizes small, makes the codebase modular, and allows developers to reuse resource classes across different parts of the API.

### 5.2 Dependency Validation (422 Unprocessable Entity)
HTTP `404 Not Found` typically implies that the target URL itself (the endpoint) does not exist on the server. If a client targets `/api/v1/sensors` correctly, returning a `404` is misleading. HTTP `422 Unprocessable Entity` is far more semantically accurate because it tells the client: "The endpoint exists, and the JSON syntax you sent is perfectly valid, but the business semantics inside that payload (such as referencing a non-existent `roomId`) are invalid and cannot be processed".

### 5.4 The Global Safety Net (500)
Exposing internal Java stack traces is a critical cybersecurity risk known as "Information Leakage". A stack trace reveals the exact internal architecture of the application, including internal package names, file paths, and the specific framework being used (e.g., Jersey, Tomcat, GlassFish). An attacker can use this specific versioning information to search for known Common Vulnerabilities and Exposures (CVEs) associated with those exact libraries, granting them a blueprint to craft targeted exploits against the system.

### 5.5 API Request & Response Logging Filters
Using JAX-RS filters for cross-cutting concerns adheres to the DRY (Don't Repeat Yourself) principle. If you manually place `Logger.info()` inside every single resource method, it creates massive code duplication and clutters the core business logic. Furthermore, if the logging format needs to be updated later, a developer would have to manually edit hundreds of individual methods. By utilizing Container Filters, logging is centralized in one single class that intercepts all traffic globally, ensuring 100% automated logging coverage without touching the underlying endpoint code.