package controllers;

import entities.Requests;
import entities.RequestsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import services.RequestsService;

@RestController
@RequestMapping("/api/v1/request")
@RequiredArgsConstructor
public class RequestController {
    private final RequestsService requestsService;
    private final RequestController requestsConverter;

//    @GetMapping
//    public List<ProductDto> findAllProducts() {
//        return productService.findAll().stream().map(productConverter::entityToTdo).collect(Collectors.toList());
//    }

    @GetMapping
    public List<RequestsDto> findProducts(
            @RequestParam(required = false, name = "min_price") Integer minPrice,
            @RequestParam(required = false, name = "max_price") Integer maxPrice,
            @RequestParam(required = false, name = "title") String title,
            @RequestParam(defaultValue = "1", name = "p") Integer page
    ) {
        if (page<1) { page  =1; }
        Specification<Requests> spec = requestsService.createSpecByFilters(minPrice, maxPrice, title);
//        return productService.findAll().stream().map(productConverter::entityToTdo).collect(Collectors.toList());
        return requestsService.findAll(spec,page-1).map(requestsConverter::entityToTdo).getContent();
    }


    @GetMapping("/{id}")
    public RequestsDto findProducts(@PathVariable Long id) {
        Requests p = requestsService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Продукт с id: "+id+" не найден"));
//        return new ProductDto(p.getId(),p.getTitle(),p.getPrice(),p.getCategory().getTitle());
        return requestsConverter.entityToTdo(p);
    }

    private RequestsDto entityToTdo(Requests p) {
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProducts(@PathVariable Long id) {
        requestsService.deleteById(id);
    }

    @PostMapping
    public RequestsDto createNewProduct(@RequestBody RequestsDto productDto){
        Requests r = requestsService.createNewRequests(productDto);
        return requestsConverter.entityToTdo(r);
    }
}