package edu.fcu.cs1133.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "Student_Profiles")
@Getter
@Setter
@NoArgsConstructor
public class StudentProfile {

  @Id
  @Column(name = "user_id")
  private Integer userId;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;
}
