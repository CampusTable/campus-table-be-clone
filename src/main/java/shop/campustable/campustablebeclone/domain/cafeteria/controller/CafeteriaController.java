package shop.campustable.campustablebeclone.domain.cafeteria.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.campustable.campustablebeclone.domain.cafeteria.dto.CafeteriaRequest;
import shop.campustable.campustablebeclone.domain.cafeteria.dto.CafeteriaResponse;
import shop.campustable.campustablebeclone.domain.cafeteria.entity.Cafeteria;
import shop.campustable.campustablebeclone.domain.cafeteria.service.CafeteriaService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CafeteriaController {

  private final CafeteriaService cafeteriaService;

  @PostMapping("/admin/cafeterias")
  public ResponseEntity<CafeteriaResponse> createCafeteria(@RequestBody CafeteriaRequest request) {
      CafeteriaResponse cafeteriaResponse = cafeteriaService.createCafeteria(request);
      return ResponseEntity.status(HttpStatus.CREATED).body(cafeteriaResponse);
  }


}
