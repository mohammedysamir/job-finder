package com.jobfinder.finder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity(name = "verification_token")
@NoArgsConstructor
@Setter
@Getter
public class VerificationTokenEntity {

  public VerificationTokenEntity(String token, UserEntity user, long tokenTtlInHours) {
    this.token = token;
    this.user = user;
    this.expiryDate = new Date(System.currentTimeMillis() + tokenTtlInHours);
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String token;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserEntity user;

  private Date expiryDate;

}
