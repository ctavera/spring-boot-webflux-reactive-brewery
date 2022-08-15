package guru.springframework.sfgrestbrewery.repositories;


import guru.springframework.sfgrestbrewery.domain.Beer;
import guru.springframework.sfgrestbrewery.web.model.BeerStyleEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface BeerRepository extends ReactiveCrudRepository<Beer, Integer> {

    Mono<Beer> findByUpc(String upc);

    Flux<Beer> findAllByBeerName(String beerName, Pageable pageable);

    Flux<Beer> findAllByBeerStyle(BeerStyleEnum beerStyle, Pageable pageable);

    Flux<Beer> findAllByBeerNameAndBeerStyle(String beerName, BeerStyleEnum beerStyle, Pageable pageable);

    Flux<Beer> findBeerBy(Pageable pageable);
}
