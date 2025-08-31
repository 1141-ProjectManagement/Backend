# 權限實作核對清單 (Permission Implementation Checklist)

本文檔旨在核對系統中各角色的權限是否已根據 `Permission.md` 的規格正確實作。

---

## 核對結果

| 角色 (Role) | 權限代碼 (Permission) | 已實作 (Implemented) | 備註 (Notes) |
| :--- | :--- | :--- | :--- |
| **Student (學生)** | | |
| | `profile.view.own` | [x] | | 
| | `profile.edit.own` | [x] | | 
| | `course.view.catalog` | [x] | | 
| | `course.enroll.self` | [x] | | 
| | `grade.view.own` | [x] | | 
| **Teacher (教師)** | | |
| | `profile.view.own` | [x] | | 
| | `profile.edit.own` | [x] | | 
| | `course.view.catalog` | [x] | | 
| | `grade.update` | [x] | | 
| | `grade.view.all` | [x] | | 
| **Student Affairs Admin (學務管理員)** | | |
| | `profile.view.own` | [x] | | 
| | `profile.edit.own` | [x] | | 
| | `student.create` | [x] | | 
| | `student.view` | [x] | | 
| | `student.update` | [x] | | 
| | `student.delete` | [x] | | 
| | `enrollment.manage` | [ ] | **未實作** | 
| | `grade.view.all` | [x] | | 
| **Course Affairs Admin (課務管理員)** | | |
| | `profile.view.own` | [x] | | 
| | `profile.edit.own` | [x] | | 
| | `teacher.view` | [x] | | 
| | `course.create` | [x] | | 
| | `course.update` | [x] | | 
| | `course.delete` | [x] | | 
| | `course.assign` | [ ] | **未實作** | 
| **Teacher HR Admin (人資管理員)** | | |
| | `profile.view.own` | [x] | | 
| | `profile.edit.own` | [x] | | 
| | `teacher.create` | [x] | | 
| | `teacher.view` | [x] | | 
| | `teacher.update` | [x] | | 
| | `teacher.delete` | [x] | | 
| **Super Administrator (超級管理員)** | | |
| | `admin.create` | [x] | | 
| | `admin.update` | [x] | | 
| | `admin.delete` | [x] | | 
| | `system.settings.view` | [x] | 新增的權限 | 
| | `system.settings.edit` | [x] | | 
