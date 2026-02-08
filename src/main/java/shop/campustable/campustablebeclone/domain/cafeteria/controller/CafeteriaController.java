package shop.campustable.campustablebeclone.domain.cafeteria.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
      CafeteriaResponse response = cafeteriaService.createCafeteria(request);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/cafeterias")
  public ResponseEntity<List<CafeteriaResponse>> getAllCafeterias() {
    List<CafeteriaResponse> response = cafeteriaService.getAllCafeterias();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/cafeterias/{cafeteria-id}")
  public ResponseEntity<CafeteriaResponse> getCafeteriaById(@PathVariable("cafeteria-id") Long cafeteriaId) {
    CafeteriaResponse response = cafeteriaService.getCafeteriaById(cafeteriaId);
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/admin/cafeterias/{cafeteria-id}")
  public ResponseEntity<CafeteriaResponse> updateCafeteria(@RequestBody CafeteriaRequest request,@PathVariable("cafeteria-id") Long cafeteriaId) {
    CafeteriaResponse response = cafeteriaService.updateCafeteria(request, cafeteriaId);
    return ResponseEntity.ok(response);
  }

}
