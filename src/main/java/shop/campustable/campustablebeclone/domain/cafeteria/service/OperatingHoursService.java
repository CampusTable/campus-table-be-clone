package shop.campustable.campustablebeclone.domain.cafeteria.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.campustable.campustablebeclone.domain.cafeteria.dto.OperatingHoursRequest;
import shop.campustable.campustablebeclone.domain.cafeteria.entity.Cafeteria;
import shop.campustable.campustablebeclone.domain.cafeteria.entity.OperatingHours;
import shop.campustable.campustablebeclone.domain.cafeteria.repository.CafeteriaRepository;
import shop.campustable.campustablebeclone.domain.cafeteria.repository.OperatingHoursRepository;
import shop.campustable.campustablebeclone.global.exception.CustomException;
import shop.campustable.campustablebeclone.global.exception.ErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class OperatingHoursService {

  private final OperatingHoursRepository operatingHoursRepository;
  private final CafeteriaRepository cafeteriaRepository;

  public void saveOperatingHours(Long cafeteriaId,  List<OperatingHoursRequest> requests) {
    Cafeteria cafeteria = cafeteriaRepository.findById(cafeteriaId)
        .orElseThrow(()->{
          log.error("saveOperatingHours : 유효하지 않은 cafeteriaId {}",cafeteriaId);
          return new CustomException(ErrorCode.CAFETERIA_NOT_FOUND);
        });

    operatingHoursRepository.deleteByCafeteria(cafeteria);
    operatingHoursRepository.flush();

    List<OperatingHours> operatingHoursList = requests.stream()
        .map(request -> request.toEntity(cafeteria))
        .toList();

    operatingHoursRepository.saveAll(operatingHoursList);
  }

}
