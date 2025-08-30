# 開發任務改善清單 (Todolist)

## 階段一：修正核心架構

- [ ] **修正資料模型**：修改 `User` 實體，將 `roleId` (`Integer`) 改為與 `Role` 實體的 `@ManyToOne` 關聯。
- [ ] **重構權限系統**：
    - [ ] 更新 `CustomUserDetailsService` 以從資料庫載入使用者角色對應的所有 `Permission` 作為 `GrantedAuthority`。
    - [ ] 將現有 `CourseController` 和 `StudentController` 中的 `@PreAuthorize("hasRole(...)")` 改為更精確的 `@PreAuthorize("hasAuthority('...')")`。

## 階段二：補全資料存取與業務邏輯

- [ ] **補全 Repository**：
    - [ ] 建立 `CourseRepository.java`
    - [ ] 建立 `RoleRepository.java`
    - [ ] 建立 `PermissionRepository.java`
    - [ ] 建立 `StudentProfileRepository.java`
    - [ ] 建立 `TeacherProfileRepository.java`
    - [ ] 建立 `AdministratorProfileRepository.java`
- [ ] **擴充 Service 業務邏輯**：
    - [ ] **`CourseService`**: 新增 `createCourse`, `updateCourse`, `deleteCourse`, `getCourseById`, `getAllCourses` 等方法。
    - [ ] **`UserService`**: 建立新的 `UserService.java`，用於管理使用者帳號的 `CRUD` 及角色分配。
    - [ ] **`ProfileService`**: 建立新的 `ProfileService.java` 來處理個人資料的查詢與更新。

## 階段三：建構完整 API

- [ ] **擴充 `CourseController`**：加入課程的 `CRUD` 端點 (`GET`, `POST`, `PUT`, `DELETE`)，並以對應權限保護。
- [ ] **建立 `AdminController`**：
    - [ ] 建立 `AdminController.java`。
    - [ ] 新增管理使用者的 API 端點 (建立、查詢、更新、刪除)。
    - [ ] 新增管理角色的 API 端點。
- [ ] **擴充使用者端點**：
    - [ ] 在 `StudentController` 中新增 `GET /me/profile` 和 `PUT /me/profile`，用於查詢和修改學生自己的個人資料。
    - [ ] (未來) 建立 `TeacherController`，提供類似功能。

## 階段四：精煉與優化

- [ ] **引入 DTOs**：
    - [ ] 建立 `CourseCreationDto.java` 用於建立課程。
    - [ ] 建立 `UserCreationDto.java` 用於建立使用者。
    - [ ] 建立 `ProfileUpdateDto.java` 用於更新個人資料。
- [ ] **完善異常處理**：在 `RestExceptionHandler` 中增加對 `AccessDeniedException` 的處理，返回 `403 Forbidden` 狀態碼。
- [ ] **撰寫測試**：為新的 Service 和 Controller 方法撰寫單元測試與整合測試。
