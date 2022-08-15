package guru.springframework.sfgrestbrewery.services;

import guru.springframework.sfgrestbrewery.domain.Beer;
import guru.springframework.sfgrestbrewery.repositories.BeerRepository;
import guru.springframework.sfgrestbrewery.web.mappers.BeerMapper;
import guru.springframework.sfgrestbrewery.web.model.BeerDto;
import guru.springframework.sfgrestbrewery.web.model.BeerPagedList;
import guru.springframework.sfgrestbrewery.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.empty;
import static org.springframework.data.relational.core.query.Query.query;

/**
 * Created by jt on 2019-04-20.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;
    private final R2dbcEntityTemplate template;

    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false ")
    @Override
    public Mono<BeerPagedList> listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand) {

        Flux<Beer> pageFlux;
        if (StringUtils.hasText(beerName) && StringUtils.hasLength(beerName) && beerStyle != null) {
            //search both
            pageFlux = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle, pageRequest);
        } else if (StringUtils.hasText(beerName) && StringUtils.hasLength(beerName) && beerStyle == null) {
            //search beer_service name
            pageFlux = beerRepository.findAllByBeerName(beerName, pageRequest);
        } else if (!StringUtils.hasText(beerName) && !StringUtils.hasLength(beerName) && beerStyle != null) {
            //search beer_service style
            pageFlux = beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
        } else {
            pageFlux = beerRepository.findBeerBy(pageRequest);
        }

        if (showInventoryOnHand){
            return pageFlux.map(beerMapper::beerToBeerDtoWithInventory)
                    .collect(Collectors.toList())
                    .map(beerDtos -> new BeerPagedList(beerDtos, PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize()), beerDtos.size()));
        } else {
            return pageFlux.map(beerMapper::beerToBeerDto)
                    .collect(Collectors.toList())
                    .map(beerDtos -> new BeerPagedList(beerDtos, PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize()), beerDtos.size()));
        }
    }

    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false ")
    public Mono<BeerPagedList> listBeersR2DBCTemplate(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand) {

        Query query = null;
        if (StringUtils.hasText(beerName) && StringUtils.hasLength(beerName) && beerStyle != null) {
            //search both
            query = query(where("beerName").is(beerName).and("beerStyle").is(beerStyle));
        } else if (StringUtils.hasText(beerName) && StringUtils.hasLength(beerName) && beerStyle == null) {
            //search beer_service name
            query = query(where("beerName").is(beerName));
        } else if (!StringUtils.hasText(beerName) && !StringUtils.hasLength(beerName) && beerStyle != null) {
            //search beer_service style
            query = query(where("beerStyle").is(beerStyle));
        } else {
            query = empty();
        }

        if (showInventoryOnHand){
            return template.select(Beer.class)
                    .matching(query.with(pageRequest))
                    .all()
                    .map(beerMapper::beerToBeerDtoWithInventory)
                    .collect(Collectors.toList())
                    .map(beerDtos -> new BeerPagedList(beerDtos, PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize()), beerDtos.size()));
        } else {
            return template.select(Beer.class)
                    .matching(query.with(pageRequest))
                    .all()
                    .map(beerMapper::beerToBeerDto)
                    .collect(Collectors.toList())
                    .map(beerDtos -> new BeerPagedList(beerDtos, PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize()), beerDtos.size()));
        }
    }

    @Cacheable(cacheNames = "beerCache", key = "#beerId", condition = "#showInventoryOnHand == false ")
    @Override
    public Mono<BeerDto> getById(Integer beerId, Boolean showInventoryOnHand) {
        if (showInventoryOnHand) {
            return beerRepository.findById(beerId).map(beerMapper::beerToBeerDtoWithInventory);
        } else {
            return beerRepository.findById(beerId).map(beerMapper::beerToBeerDto);
        }
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
//        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beerDto)));
        return null;
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
//        Beer beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);
//
//        beer.setBeerName(beerDto.getBeerName());
//        beer.setBeerStyle(BeerStyleEnum.PILSNER.valueOf(beerDto.getBeerStyle()));
//        beer.setPrice(beerDto.getPrice());
//        beer.setUpc(beerDto.getUpc());
//
//        return beerMapper.beerToBeerDto(beerRepository.save(beer));
        return null;
    }

    @Cacheable(cacheNames = "beerUpcCache")
    @Override
    public Mono<BeerDto> getByUpc(String upc) {
        return beerRepository.findByUpc(upc).map(beerMapper::beerToBeerDto);
    }

    @Override
    public void deleteBeerById(Integer beerId) {
        beerRepository.deleteById(beerId);
    }
}
