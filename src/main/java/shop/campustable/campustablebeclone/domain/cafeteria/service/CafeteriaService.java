package shop.campustable.campustablebeclone.domain.cafeteria.service;

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

}
