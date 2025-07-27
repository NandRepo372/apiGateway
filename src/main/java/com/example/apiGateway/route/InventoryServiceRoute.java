package com.example.apiGateway.route;

import ch.qos.logback.classic.spi.LoggingEventVO;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class InventoryServiceRoute {

    @Bean
    public RouterFunction<ServerResponse> inventoryRoutes() {
       return GatewayRouterFunctions.route("inventory-service")
               .route(RequestPredicates.path("api/v1/inventory/event/{eventId}"),
                       request -> forwardWithpathVariable(request, "eventId", "http://localhost:8080/api/v1/inventory/event/"))

               .route(RequestPredicates.path("api/v1/inventory/venue/{venueId}"),
                       request -> forwardWithpathVariable(request, "venueId", "http://localhost:8080/api/v1/inventory/venue/"))

               .route(RequestPredicates.PUT("/inventory/event/{eventId}/capacity/{capacity}"),
                       request -> forwardWithpathVariables(request, "eventId", "capacityId","http://localhost:8080/api/v1/inventory/event/"))

               .build();

    }

    private static ServerResponse forwardWithpathVariables(ServerRequest request, String eventId, String capacityId, String s) throws Exception {
        String pathVariable1 = request.pathVariable(eventId);
        String pathVariable2 = request.pathVariable(capacityId);
        String url = s + pathVariable1 + pathVariable2;
        return HandlerFunctions.http(url).handle(request);
    }

    private static ServerResponse forwardWithpathVariable(ServerRequest request, String eventId, String s) throws Exception {
        String pathVariable = request.pathVariable(eventId);
        String url = s + pathVariable;
        return HandlerFunctions.http(url).handle(request);
    }
}
