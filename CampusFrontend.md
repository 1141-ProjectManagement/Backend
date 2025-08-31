# 前端 API 需求規格書 (CampusFrontend.md)

本文件基於 `113-Database-CampusFront` Vue.js 專案的實際架構和程式碼分析產生，旨在為後端開發人員提供一份清晰、準確的 API 開發參考。

## 1. 全域需求 (Global Requirements)

### 1.1. API Base URL

前端所有 API 請求的基礎路徑 (baseURL) 設定為 `/api`。

- **開發環境**: `http://localhost:8081/api`

### 1.2. 認證流程 (Authentication Flow)

1.  **發起登入**: 前端透過 `POST /api/auth/login` 提交使用者憑證。
    -   **Request Body**:
        ```json
        {
          "officialId": "string",
          "password": "string"
        }
        ```
2.  **取得 Token**: 前端期望收到包含 `accessToken` 的回應。
    -   **Response Body**:
        ```json
        {
          "accessToken": "your-jwt-token",
          "tokenType": "Bearer"
        }
        ```
3.  **儲存 Token**: 前端會將 `accessToken` 儲存起來。
4.  **發送請求**: 在後續所有需要授權的 API 請求中，前端會自動在 HTTP Header 中加入 `Authorization`。
    -   **Header**: `Authorization: Bearer <your-jwt-token>`

### 1.3. 使用者資訊結構 (User Info Structure)

登入成功後，前端會立即呼叫 `GET /api/users/me/profile` 來獲取當前使用者的詳細資訊。為了讓前端的權限系統 (RBAC) 正常運作，回傳的物件**必須**包含 `role` 和 `permissions` 兩個欄位。

-   **Endpoint**: `GET /api/users/me/profile`
-   **前端用途**: 用於顯示使用者資訊、判斷角色、並根據權限列表動態顯示/隱藏 UI 元素 (如側邊欄連結、操作按鈕)。
-   **期望的 Response Body 結構**:
    ```json
    {
      "userId": 1,
      "officialId": "student123",
      "email": "student@test.com",
      "firstName": "Test",
      "lastName": "User",
      "dateOfBirth": "2000-01-01T00:00:00.000Z", // ISO 8601 格式
      "role": "student", // 使用者角色 (e.g., "student", "teacher", "admin")
      "permissions": [
        "profile.view.own",
        "profile.edit.own",
        "course.view.catalog",
        "course.enroll.self",
        "grade.view.own"
      ]
    }
    ```

### 1.4. 錯誤處理 (Error Handling)

前端在 `src/api/request.js` 中設定了全域錯誤攔截器，能處理 `401` (自動登出), `403` (權限不足提示), `500` (伺服器錯誤提示) 等狀態碼。

---

## 2. API 端點規格 (Endpoint Specifications)

以下是前端各功能頁面實際呼叫的 API 端點及數據需求。

### 2.1. 使用者 (User)

-   **`GET /api/users/me/profile`**
    -   **前端用途**: `ProfileView.vue`, `Layout.vue` - 獲取並顯示當前使用者資訊。
    -   **期望 Response**: 見 `1.3. 使用者資訊結構`。

-   **`PUT /api/users/me/profile`**
    -   **前端用途**: `ProfileView.vue` - 更新使用者個人資料。
    -   **Request Body**:
        ```json
        {
          "firstName": "string",
          "lastName": "string",
          "dateOfBirth": "yyyy-MM-dd" // e.g., "2000-01-01"
        }
        ```

### 2.2. 課程 (Course)

-   **`GET /api/courses`**
    -   **前端用途**: `CourseCatalogView.vue` (學生視角), `CourseManagementView.vue` (管理員視角) - 獲取所有課程列表。
    -   **期望 Response Body**: `Array<Course>`
        ```json
        [
          {
            "id": 1,
            "name": "Introduction to Programming",
            "description": "A beginner's course on programming.",
            "credits": 3,
            "teacher": { // 為了顯示教師姓名，需要 teacher 物件
                "firstName": "John",
                "lastName": "Doe"
            }
          }
        ]
        ```

-   **`POST /api/courses`**
    -   **前端用途**: `CourseManagementView.vue` - 建立新課程。
    -   **Request Body**:
        ```json
        {
          "name": "string",
          "description": "string",
          "credits": number
        }
        ```

-   **`PUT /api/courses/{courseId}`**
    -   **前端用途**: `CourseManagementView.vue` - 更新指定課程。
    -   **Request Body**: 同 `POST /api/courses`。

-   **`DELETE /api/courses/{courseId}`**
    -   **前端用途**: `CourseManagementView.vue` - 刪除指定課程。

### 2.3. 教師 (Teacher)

-   **`GET /api/teachers/{courseId}/students`**
    -   **前端用途**: `GradeManagementView.vue` - 獲取指定課程的修課學生列表，以利成績登錄。
    -   **期望 Response Body**: `Array<Student>`
        ```json
        [
          {
            "id": 101,
            "officialId": "S12345",
            "firstName": "Jane",
            "lastName": "Smith",
            "email": "jane.smith@test.com",
            "grade": "A+" // 學生在該課程的當前成績
          }
        ]
        ```

-   **`PUT /api/teachers/{courseId}/grades`**
    -   **前端用途**: `GradeManagementView.vue` - 批次更新指定課程的學生成績。
    -   **Request Body**:
        ```json
        {
          "grades": [
            { "studentId": 101, "grade": "A" },
            { "studentId": 102, "grade": "B-" }
          ]
        }
        ```

### 2.4. 管理 (Admin)

-   **`GET /api/admin/users`**
    -   **前端用途**: `StudentManagementView.vue`, `TeacherManagementView.vue`, `AdminManagementView.vue` - 獲取所有使用者列表，前端會自行根據 `role` 欄位進行篩選顯示。
    -   **期望 Response Body**: `Array<User>` (應包含所有類型的使用者)
        ```json
        [
          {
            "id": 1, "officialId": "S001", "firstName": "Peter", "lastName": "Pan", "email": "peter@test.com", "role": "student"
          },
          {
            "id": 2, "officialId": "T001", "firstName": "Mary", "lastName": "Jane", "email": "mary@test.com", "role": "teacher"
          }
        ]
        ```

-   **`POST /api/admin/users`**
    -   **前端用途**: 用於建立學生、教師或管理員。
    -   **Request Body**: 前端會根據建立的角色，在 payload 中附加 `role` 欄位。
        ```json
        {
          "officialId": "string",
          "email": "string",
          "firstName": "string",
          "lastName": "string",
          "password": "string",
          "role": "student" // or "teacher", "admin"
        }
        ```

-   **`PUT /api/admin/users/{userId}`**
    -   **前端用途**: 更新使用者資料。
    -   **Request Body**: (不包含密碼和角色)
        ```json
        {
          "officialId": "string",
          "email": "string",
          "firstName": "string",
          "lastName": "string"
        }
        ```

-   **`DELETE /api/admin/users/{userId}`**
    -   **前端用途**: 刪除使用者。

---

## 3. 待辦 API 需求 (Pending API Requirements)

以下是根據 `COUPLING_ANALYSIS.md` 和前端 UI/UX 規劃，目前**缺少**且前端**急需**的 API 端點。

1.  **`GET /api/teachers/my-courses`**
    -   **需求**: 獲取當前登入**教師**自己所開的課程列表。
    -   **前端用途**: `TeacherCoursesView.vue` 頁面需要此 API 來展示課程列表。
    -   **期望 Response**: `Array<Course>` (結構同 `2.2 GET /api/courses`)

2.  **`POST /api/courses/{courseId}/enrollments`**
    -   **需求**: **學生**選修指定課程。
    -   **前端用途**: `CourseCatalogView.vue` 中的 "Enroll" 按鈕功能。
    -   **權限**: `course.enroll.self`

3.  **`DELETE /api/courses/{courseId}/enrollments`**
    -   **需求**: **學生**退選指定課程。
    -   **前端用途**: `CourseCatalogView.vue` 中的 "Drop" 按鈕功能。
    -   **權限**: `course.enroll.self`

4.  **`GET /api/students/me/grades`**
    -   **需求**: 獲取當前登入**學生**自己的所有課程成績。
    -   **前端用途**: `MyGradesView.vue` 頁面需要此 API 來展示成績單。
    -   **權限**: `grade.view.own`
    -   **期望 Response**: `Array<GradeInfo>`
        ```json
        [
          {
            "courseName": "Introduction to Programming",
            "teacherName": "Dr. Smith",
            "credits": 3,
            "grade": "A+"
          },
          {
            "courseName": "Data Structures",
            "teacherName": "Dr. Jones",
            "credits": 4,
            "grade": "B"
          }
        ]
        ```

5.  **管理員功能 API**
    -   **需求**: 需要後端實現對應 `enrollment.manage` 和 `course.assign` 權限的管理 API，具體端點可由後端定義。
