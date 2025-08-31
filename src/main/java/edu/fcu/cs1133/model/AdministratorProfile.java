package edu.fcu.cs1133.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Administrator_Profiles")
@Getter
@Setter
@NoArgsConstructor
public class AdministratorProfile {
  @Id
  @Column(name = "user_id")
  private Integer userId;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "full_name", nullable = false)
  private String fullName;
}
