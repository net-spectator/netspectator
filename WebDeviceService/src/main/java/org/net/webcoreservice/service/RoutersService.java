package org.net.webcoreservice.service;

import lombok.RequiredArgsConstructor;
import org.net.webcoreservice.dto.RouterDto;
import org.net.webcoreservice.entities.Routers;
import org.net.webcoreservice.repository.RoutersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoutersService {

    private final RoutersRepository routersRepository;

    public Optional<Routers> findById(Long id) {
        return routersRepository.findById(id);
    }

    public List<Routers> findAll() {
        return routersRepository.findAll();
    }

    public void deleteById(Long id) {
        routersRepository.deleteById(id);
    }

    public void createNewRouter(RouterDto routerDto) {
        Routers routers = new Routers();
        routers.setRouterTitle(routerDto.getTitle());
        routers.setRouterMacAddress(routerDto.getMac());
        routers.setRouterIpAddress(routerDto.getIp());
        routersRepository.save(routers);
    }


}
