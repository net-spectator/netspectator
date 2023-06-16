package org.net.webcoreservice.converters;

import org.net.webcoreservice.dto.RouterDto;
import org.net.webcoreservice.entities.Routers;
import org.springframework.stereotype.Component;

@Component
public class RouterConverter {

    public RouterDto entityToDto(Routers routers) {
        RouterDto routerDto = new RouterDto();
        routerDto.setId(routers.getId());
        routerDto.setTitle(routers.getRouterTitle());
        routerDto.setMac(routers.getRouterMacAddress());
        routerDto.setIp(routers.getRouterIpAddress());
        return routerDto;
    }
}
