package guru.springframework.sfgrestbrewery.services;

import guru.springframework.sfgrestbrewery.web.model.BeerDto;
import guru.springframework.sfgrestbrewery.web.model.BeerPagedList;
import guru.springframework.sfgrestbrewery.web.model.BeerStyleEnum;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

/**
 * Created by jt on 2019-04-20.
 */
public interface BeerService {
    Mono<BeerPagedList> listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand);

    Mono<BeerPagedList> listBeersR2DBCTemplate(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand);

    Mono<BeerDto> getById(Integer beerId, Boolean showInventoryOnHand);

    Mono<BeerDto> saveNewBeer(BeerDto beerDto);

    Mono<BeerDto> saveNewBeerMono(Mono<BeerDto> beerDtoMono);

    Mono<BeerDto> updateBeer(Integer beerId, BeerDto beerDto);

    Mono<BeerDto> getByUpc(String upc);

    void deleteBeerById(Integer beerId);

    Mono<Void> reactiveDeleteById(Integer beerId);
}
