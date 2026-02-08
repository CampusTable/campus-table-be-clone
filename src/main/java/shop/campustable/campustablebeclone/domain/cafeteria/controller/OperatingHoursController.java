package shop.campustable.campustablebeclone.domain.cafeteria.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.campustable.campustablebeclone.domain.cafeteria.dto.OperatingHoursRequest;
import shop.campustable.campustablebeclone.domain.cafeteria.service.OperatingHoursService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OperatingHoursController {

  private final OperatingHoursService operatingHoursService;

  @PostMapping("/admin/cafeterias/{cafeteria-id}/operating-hours")
  public ResponseEntity<Void> saveOperatingHours(
      @PathVariable(name = "cafeteria-id")Long cafeteriaId,
      @RequestBody List<@Valid OperatingHoursRequest> requests) {

    operatingHoursService.saveOperatingHours(cafeteriaId, requests);
    return ResponseEntity.ok().build();
  }

}
