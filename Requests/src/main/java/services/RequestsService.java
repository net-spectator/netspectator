package services;

import entities.Requests;
import entities.RequestsDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import repositories.RequestsRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestsService {
    private final RequestsRepository requestsRepository;


//    public List<Product> findAll (Specification<Product> spec, int page) {
//        return productRepository.findAll();
    public Page<Requests> findAll (Specification<Requests> spec, int page) {
        int sizePage = 10;
        return requestsRepository.findAll(spec, PageRequest.of(page, sizePage));
//        return productRepository.findAll(spec, PageRequest.of(page, sizePage));
    }

    public Optional<Requests> findById(Long id) {
        return requestsRepository.findById(id);
    }

    public void  deleteById(Long id) {
        requestsRepository.deleteById(id);
    }

    public Requests createNewRequests (RequestsDto productDto){

//        product.setPrice(productDto.getPrice());
//        product.setTitle(productDto.getTitle());
//        product.setCategory(category);
        requestsRepository.save(req);
        return requests;
    }
}
