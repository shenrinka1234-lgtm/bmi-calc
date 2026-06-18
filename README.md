智慧健康日誌與風險評估系統 (BMI-Calc)
本專案為一個基於 Spring Boot 與 H2 Database 的健康管理後端服務，結合自動化決策樹演算法，提供即時的健康風險分析。

🏗️ 系統架構與資料流向
本系統採用分層架構，確保前端、後端服務、ML 決策模組與資料庫間的協作流暢。

各模組協作機制
前端 (Frontend)：作為資料輸入端，透過 JSON 格式將健康數據（睡眠、步數、心情）發送給後端。

後端 (Backend/API)：

Controller: 接收請求並處理業務邏輯。

ML 模組 (Decision Tree Engine)：後端核心邏輯，依據「資訊增益 (Information Gain)」原理進行特徵切分，運算風險等級。

資料庫 (Database)：使用 H2 In-Memory Database，透過 JPA 實現資料持久化。

🚀 核心功能
自動化決策樹風險評估：根據睡眠時數、步數與心情分數，自動分類為高、中、低風險。

自動初始化：系統啟動時自動注入 90 筆種子歷史資料，確保展示資料完整性。

RESTful API：

GET /health-logs: 讀取所有健康日誌紀錄。

POST /health-logs: 新增一筆日誌並自動執行風險評估。

🛠️ 技術實作
Framework: Spring Boot 3.1.0

Database: H2 In-Memory Database

ORM: Spring Data JPA

Build Tool: Maven
```mermaid
graph TD
    A[bmi-calc/ - 專案根目錄] --> B[src/ - 原始碼]
    A --> C[pom.xml - Maven 設定]
    A --> D[Dockerfile - 容器設定]
    B --> E[main/java/com/example/demo/]
    E --> F[DemoApplication.java - 啟動檔]
    E --> G[HelloController.java - 網頁控制器]
