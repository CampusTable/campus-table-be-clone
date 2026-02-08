package shop.campustable.campustablebeclone.domain.cafeteria.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.campustable.campustablebeclone.domain.cafeteria.dto.CafeteriaRequest;
import shop.campustable.campustablebeclone.domain.cafeteria.dto.CafeteriaResponse;
import shop.campustable.campustablebeclone.domain.cafeteria.entity.Cafeteria;
import shop.campustable.campustablebeclone.domain.cafeteria.repository.CafeteriaRepository;
import shop.campustable.campustablebeclone.global.exception.CustomException;
import shop.campustable.campustablebeclone.global.exception.ErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CafeteriaService {

  private final CafeteriaRepository cafeteriaRepository;

  public CafeteriaResponse createCafeteria(CafeteriaRequest request) {

    cafeteriaRepository.findByName(request.getName())
        .ifPresent(cafeteria -> {
          log.error("createCafeteria: 이미 존재하는 식당입니다.");
          throw new CustomException(ErrorCode.CAFETERIA_ALREADY_EXISTS);
        });

    Cafeteria cafeteria = request.toEntity();
    cafeteriaRepository.save(cafeteria);

    return CafeteriaResponse.from(cafeteria);
  }

  public List<CafeteriaResponse> getAllCafeterias() {

    List<Cafeteria> cafeterias = cafeteriaRepository.findAll();

    return cafeterias.stream()
        .map(CafeteriaResponse::from)
        .toList();
  }

  public CafeteriaResponse getCafeteriaById(Long id) {

    Cafeteria cafeteria = cafeteriaRepository.findById(id)
        .orElseThrow(()->{
          log.error("getCafeteriaById: 유효하지 않은 cafeteriaId");
          return new CustomException(ErrorCode.CAFETERIA_NOT_FOUND);
        });

    return CafeteriaResponse.from(cafeteria);
  }

  public CafeteriaResponse updateCafeteria(CafeteriaRequest request, Long id) {
    Cafeteria cafeteria = cafeteriaRepository.findById(id)
        .orElseThrow(()->{
          log.error("updateCafeteria: 유효하지 않은 cafeteriaId");
          return new CustomException(ErrorCode.CAFETERIA_NOT_FOUND);
        });

    cafeteria.update(request);
    return CafeteriaResponse.from(cafeteria);
  }

}
