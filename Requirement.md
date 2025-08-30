# 校園管理系統後端改善方案

## 總體評價

目前的後端專案已經建立了一個不錯的基礎，特別是在：
*   **專案結構**：遵循了標準的 Spring Boot 分層架構 (Controller, Service, Repository, Model)。
*   **JPA 實體映射**：對於 `User` 與各種 `Profile` (Student, Teacher, Administrator) 之間的一對一關聯，以及 `Enrollment` 的複合主鍵，都使用了正確且高效的 JPA 註解 (`@OneToOne`, `@MapsId`, `@EmbeddedId`)。
*   **認證機制**：整合了 Spring Security 與 JWT，並建立了基本的登入流程 (`AuthController`)。

然而，相較於 `DBDesign` 中定義的完整業務範疇，目前的實作尚處於**初期階段**，許多核心功能和關鍵的關聯設計有待完善。最大的問題在於**權限系統並未真正實作**，且**多數管理功能缺失**。

---

## 主要問題與改善建議

以下我將從幾個關鍵層面，逐一分析其不足之處並提供具體改善方案。

### 1. 模型層 (Model Layer) 的核心缺陷

**問題：**
`User` 實體與 `Role` 實體的關聯是目前設計中最嚴重的缺陷。`User.java` 中僅用一個 `private Integer roleId;` 來儲存角色 ID。這種作法會導致型別不安全、查詢效率低、導航困難且高耦合。

**改善建議：**
必須將其修改為標準的 JPA 多對一 (`@ManyToOne`) 關聯。

```java
// In User.java (Recommended change)
@ManyToOne(fetch = FetchType.LAZY) // 使用 LAZY Fetching 提高效率
@JoinColumn(name = "role_id", nullable = false)
private Role role;

// 移除 get/set roleId，改用 get/set Role
public Role getRole() {
    return role;
}

public void setRole(Role role) {
    this.role = role;
}
```

### 2. 權限管理 (Authorization) 的不完整實作

**問題：**
目前的權限控制非常粗糙，完全沒有利用到 `Permissions` 和 `Role_Permissions` 資料表的設計精髓。權限被寫死為單一的角色名稱，而不是基於詳細定義的操作權限。

**改善建議：**
重構安全設定，使其真正基於 **Permission (權限)** 而非 **Role (角色)**。

**步驟 1: 修改 `CustomUserDetailsService`**
讓它在載入使用者時，一併載入其角色所擁有的所有權限。

```java
// In CustomUserDetailsService.java (Recommended change)
@Override
@Transactional
public UserDetails loadUserByUsername(String officialId) throws UsernameNotFoundException {
    User user = userRepository.findByOfficialId(officialId)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with official_id: " + officialId));

    Set<GrantedAuthority> authorities = user.getRole().getPermissions().stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermissionName()))
            .collect(Collectors.toSet());

    authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName()));

    return new org.springframework.security.core.userdetails.User(
        user.getOfficialId(),
        user.getPasswordHash(),
        user.isActive(),
        true, true, true,
        authorities
    );
}
```

**步驟 2: 更新 Controller 的 `@PreAuthorize`**
將基於角色的檢查，改為基於權限的檢查。

```java
// 推薦的寫法:
@PostMapping("/{courseId}/enroll")
@PreAuthorize("hasAuthority('course.enroll.self')")
public ResponseEntity<?> enrollInCourse(...) { ... }
```

### 3. 功能完整性 (Feature Completeness) 的巨大差距

**問題：**
目前的 Service 和 Controller 層只實作了極少數功能，缺失了 90% 以上的業務。
*   **Repository 層**：缺少了 `CourseRepository`, `RoleRepository`, `PermissionRepository` 等。
*   **Service 層**：缺少使用者、角色、權限、課程、個人資料的管理服務。
*   **Controller 層**：缺少對應的管理 API 端點。

**改善建議：**
系統性地進行功能開發，補全所有 `Repository`，擴充 `Service` 層的業務邏輯，並建立完整的 `Controller` API 端點。

### 4. 程式碼與設計細節

*   **缺少 DTO (Data Transfer Object)**：在接收輸入時應使用 DTO 而非直接使用 Model Entity。
*   **異常處理**：可以做得更全面，例如處理 `AccessDeniedException`。
*   **硬編碼字串**：應避免在程式碼中硬編碼角色名稱等字串，可改用 `Enum` 或常數管理。

---

## 具體行動計畫 (Action Plan)

1.  **修正資料模型**：立刻修改 `User` 實體，將 `roleId` 改為與 `Role` 的 `@ManyToOne` 關聯。
2.  **重構權限系統**：更新 `CustomUserDetailsService` 以載入使用者所有權限，並將 Controller 中的權限檢查改為基於 `hasAuthority`。
3.  **補全資料存取層**：為所有核心實體建立對應的 `JpaRepository` 接口。
4.  **實作核心業務邏輯**：擴充 `CourseService`，並建立 `UserService` 和 `ProfileService`。
5.  **建構完整的 API 端點**：在 `CourseController` 中加入 `CRUD` 端點，並建立新的 `AdminController`。
6.  **精煉與優化**：為 `POST` 和 `PUT` 操作建立 DTO，完善異常處理，並撰寫測試。
