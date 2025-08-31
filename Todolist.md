# 開發任務改善清單 (Todolist)

## 階段一：修正核心架構

- [x] **修正資料模型**：修改 `User` 實體，將 `roleId` (`Integer`) 改為與 `Role` 實體的 `@ManyToOne` 關聯。
- [x] **重構權限系統**：
    - [x] 更新 `CustomUserDetailsService` 以從資料庫載入使用者角色對應的所有 `Permission` 作為 `GrantedAuthority`。
    - [x] 將現有 `CourseController` 和 `StudentController` 中的 `@PreAuthorize("hasRole(...)")` 改為更精確的 `@PreAuthorize("hasAuthority('...')")`。

## 階段二：補全資料存取與業務邏輯

- [x] **補全 Repository**：
    - [x] 建立 `CourseRepository.java`
    - [x] 建立 `RoleRepository.java`
    - [x] 建立 `PermissionRepository.java`
    - [x] 建立 `StudentProfileRepository.java`
    - [x] 建立 `TeacherProfileRepository.java`
    - [x] 建立 `AdministratorProfileRepository.java`
- [x] **擴充 Service 業務邏輯**：
    - [x] **`CourseService`**: 新增 `createCourse`, `updateCourse`, `deleteCourse`, `getCourseById`, `getAllCourses` 等方法。
    - [x] **`UserService`**: 建立新的 `UserService.java`，用於管理使用者帳號的 `CRUD` 及角色分配。
    - [x] **`ProfileService`**: 建立新的 `ProfileService.java` 來處理個人資料的查詢與更新。

## 階段三：建構完整 API

- [x] **使用者個人端點 (`/api/users/me`)**
    - [x] `GET /me/profile`: 取得自己的個人資料 (`profile.view.own`)
    - [x] `PUT /me/profile`: 修改自己的個人資料 (`profile.edit.own`)
    - [x] `GET /me/enrollments`: (學生)取得自己的選課紀錄與成績 (`grade.view.own`)

- [x] **課程端點 (`/api/courses`)**
    - [x] `GET /`: 檢視所有課程目錄 (`course.view.catalog`)
    - [x] `GET /{courseId}`: 檢視單一課程詳細資訊 (`course.view.catalog`)
    - [x] `POST /{courseId}/enroll`: (學生)選修此課程 (`course.enroll.self`)
    - [x] `POST /`: (課務)建立新課程 (`course.create`)
    - [x] `PUT /{courseId}`: (課務)修改課程資訊 (`course.update`)
    - [x] `DELETE /{courseId}`: (課務)刪除課程 (`course.delete`)

- [ ] **教師端點 (`/api/teachers`)**
    - [x] `GET /{courseId}/students`: (教師)查看自己課程的修課學生名單 (`grade.view.all`)
    - [x] `PUT /{courseId}/grades`: (教師)為自己課程的學生登錄/修改成績 (`grade.update`)

- [x] **管理端點 (`/api/admin`)**
    - [x] **學生管理**
        - [x] `GET /students`: (學務)查看所有學生資料 (`student.view`)
        - [x] `POST /students`: (學務)建立新學生帳號 (`student.create`)
        - [x] `PUT /students/{userId}`: (學務)修改學生資料 (`student.update`)
        - [x] `DELETE /students/{userId}`: (學務)刪除學生帳號 (`student.delete`)
    - [ ] **教師管理**
        - [x] `GET /teachers`: (課務/人資)查看所有教師資料 (`teacher.view`)
        - [x] `POST /teachers`: (人資)建立新教師帳號 (`teacher.create`)
        - [x] `PUT /teachers/{userId}`: (人資)修改教師資料 (`teacher.update`)
        - [x] `DELETE /teachers/{userId}`: (人資)刪除教師帳號 (`teacher.delete`)
    - [ ] **管理員管理 (僅限超級管理員)**
        - [x] `GET /admins`: 查看所有管理員 (`admin.view` - *建議新增此權限*)
        - [x] `POST /admins`: 建立新管理員 (`admin.create`)
        - [x] `PUT /admins/{userId}`: 修改管理員資料與角色 (`admin.update`)
        - [x] `DELETE /admins/{userId}`: 刪除管理員 (`admin.delete`)
    - [ ] **系統設定 (僅限超級管理員)**
        - [x] `GET /settings`: 取得系統設定 (`system.settings.view` - *建議新增此權限*)
        - [x] `PUT /settings`: 修改系統設定 (`system.settings.edit`)

## 階段四：精煉與優化

- [x] **引入 DTOs**：
    - [x] 建立 `CourseCreationDto.java` 用於建立課程。
    - [x] 建立 `UserCreationDto.java` 用於建立使用者。
    - [x] 建立 `ProfileUpdateDto.java` 用於更新個人資料。
- [x] **完善異常處理**：在 `RestExceptionHandler` 中增加對 `AccessDeniedException` 的處理，返回 `403 Forbidden` 狀態碼。
- [x] **撰寫測試**：為新的 Service 和 Controller 方法撰寫單元測試與整合測試。
