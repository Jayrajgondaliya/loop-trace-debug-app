# Loop-trace-debug-app
This Sample Spring Boot application is to reproduce trace stitching to wrong context and create loop like traces.


### What is an Issue?
- On high load, Traces generated started stitching wrongly.

### How to Reproduce ?
- Here, Start application with Java Agent
- Use some performance testing tool to generate high load.
  - Jmeter - 20 threads, 200 Throughput
  - Setup Envoy to listen to 9440 with HTTPS, route request to 9999
    - It can not be reproduced with HTTP directly.
  - Test API : https://localhost:9440/api/greet?name=SomeName&delay=100

**Note: Issue Can not be reproduced with HTTP so, Use HTTPS request (Envoy as proxy and route request to service).**

### Resulting Error: 
![alt text](NormalTrace.png)

![alt text](LoopTrace.png)


## Other findings
1. Only happens after java-agent version - 1.6.2
2. Only happens with Undertow Server (Tested with Tomcat - Didn't happen).
3. Only Happens when HTTPS request 
   - Our Structure : JMeter --->(HTTPS) Request to --> Envoy --> Authentication APP ---> Test APP
4. It is not happening when
   1. suppress strategy to none
       ```
          -Dotel.instrumentation.experimental.span-suppression-strategy=none
       ```
   2. or remove executor instrumentation
       ```
          otel.instrumentation.executor.enabled=false
       ```

    
### How to build:
```
mvn clean install package
```

### How to Start :
```
    java -javaagent:opentelemetry-javaagent-all.jar 
         -Dotel.javaagent.configuration-file=opentelemetry-javaagent.properties 
         -Dotel.traces.exporter=jaeger 
         -Dotel.exporter.jaeger.endpoint=http://<collector>:14250 
         -Dotel.resource.attributes=service.name=TEST-SERVICE
         -Dotel.propagators=tracecontext,baggage,jaeger
         -Dotel.metrics.exporter=none
         -Dotel.logs.exporter=none 
         -jar target/test-service-1.0.0-SNAPSHOT.jar
```
