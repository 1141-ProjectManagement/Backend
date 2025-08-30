package edu.fcu.cs1133.repository;

import edu.fcu.cs1133.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  /**
   * 根據官方 ID（學號/工號）查詢使用者。
   * 方法名從 findByUsername 更改為 findByOfficialId，
   * Spring Data JPA 會自動為您實現正確的查詢。
   *
   * @param officialId 官方 ID
   * @return 包含 User 的 Optional 物件
   */
  Optional<User> findByOfficialId(String officialId); // <--- 關鍵變更

  boolean existsByOfficialId(String officialId); // <--- 建議新增

  boolean existsByEmail(String email);
}