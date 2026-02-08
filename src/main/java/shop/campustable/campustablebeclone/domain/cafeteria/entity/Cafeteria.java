package shop.campustable.campustablebeclone.domain.cafeteria.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cafeteria {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cafeteria_id")
  private Long id;

  @Builder.Default
  @OneToMany(mappedBy = "cafeteria",
      cascade = CascadeType.REMOVE,
      orphanRemoval = true
  )
  private List<OperatingHours> operatingHours = new ArrayList<>();

  private String name;

  private String description;

  private String address;

  public void update(String name,String description,String address) {
    if(name != null && !name.isBlank()) {
      this.name = name;
    }
    if(description != null && !description.isBlank()) {
      this.description = description;
    }
    if(address != null && !address.isBlank()) {
      this.address = address;
    }
  }

}
