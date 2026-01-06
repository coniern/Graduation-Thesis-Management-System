# 毕业论文管理系统 (Graduation Thesis Management System)

项目简介
----
毕业论文管理系统用于高校师生之间的论文选题、指导、进度管理、评审与归档等流程管理。该系统旨在简化毕业论文相关的流程、提高管理效率并提供可追溯的审核记录。

主要功能
----
- 用户与权限管理：管理员、导师(教师)、学生角色及权限控制
- 课题管理：课题发布、申请、确认、撤销
- 指导管理：导师分配、指导记录、阶段性汇报
- 答辩与评审：答辩安排、评审打分、成绩统计
- 文档管理：论文稿件上传、版本控制、文件下载
- 通知与消息：系统通知、站内消息、邮件提醒（可选）
- 报表与导出：统计报表、成绩导出、论文汇总导出

技术选型（示例）
----
> 下面列出常见可选栈，你可以根据实际项目替换或删除不需要的项。

- 后端：Spring Boot / Django / Express.js / ASP.NET Core
- 前端：React / Vue.js / Angular / Thymeleaf（Server-side）
- 数据库：MySQL / PostgreSQL / SQLite
- 缓存：Redis（可选）
- 文件存储：本地文件系统 / AWS S3 / 阿里云 OSS
- 认证：JWT / Session / OAuth2（可选）
- 部署：Docker / Kubernetes / 传统云主机

项目结构（示例）
----
示例目录结构，具体根据项目实际调整：
- backend/            — 后端代码（API）
- frontend/           — 前端代码（SPA）
- docs/               — 文档、需求、设计
- scripts/            — 部署、初始化脚本
- README.md

快速开始
----
以下为通用的「本地开发」步骤示例，请根据实际技术栈替换命令。

1. 克隆仓库
```bash
git clone https://github.com/coniern/Graduation-Thesis-Management-System.git
cd Graduation-Thesis-Management-System
```

2. 后端（示例：Spring Boot + Maven）
```bash
cd backend
# 编辑配置文件（数据库连接等）
mvn clean package
java -jar target/your-app.jar
```

或 Node/Express：
```bash
cd backend
npm install
npm run dev   # 或 npm start
```

3. 前端（示例：Vue/React）
```bash
cd frontend
npm install
npm run dev
# 打包
npm run build
```

配置说明
----
- 请在后端服务根目录下创建环境配置文件，例如 `.env` 或 `application.yml`，配置信息示例：
```
DATABASE_URL=jdbc:mysql://127.0.0.1:3306/thesis_db
DATABASE_USER=root
DATABASE_PASS=your_password
JWT_SECRET=replace_with_strong_secret
FILE_STORAGE_PATH=/path/to/upload
```
- 若使用邮件/通知功能，请配置 SMTP 信息或第三方通知服务。

部署
----
- 推荐使用 Docker 容器化部署，写 Dockerfile 并用 docker-compose 管理数据库、缓存、后端、前端服务。
- 若部署到云服务器，请配置反向代理（如 Nginx），并启用 HTTPS（Let's Encrypt）。

用户角色与使用流程（示例）
----
- 管理员：管理用户、导师账号、系统配置、统计报表。
- 导师：发布课题、管理指导进度、评审学生论文。
- 学生：浏览/申请课题、上传论文、查看指导与评审结果。

开发与贡献
----
欢迎贡献！建议的贡献流程：
1. Fork 本仓库
2. 新建分支：`feature/xxx` 或 `fix/xxx`
3. 提交并推送到你的 Fork
4. 发起 Pull Request，描述改动并关联 issue（如有）

在 PR 中请尽量包含：
- 变更说明
- 测试方式
- 截图（如界面变动）

常见问题 (FAQ)
----
Q: 系统支持多少并发？  
A: 取决于后端实现与部署环境。可通过增加后端副本、使用负载均衡与缓存来提升吞吐。

Q: 如何备份文件与数据库？  
A: 建议使用数据库定时备份（mysqldump / pg_dump）与对象存储（S3/OSS）或定期将上传文件同步到备份存储。

许可证
----
请在此处添加许可证信息（例如 MIT / Apache-2.0 等）。如果尚未决定，常见可选：
- MIT License
- Apache License 2.0

联系方式
----
- 仓库地址: https://github.com/coniern/Graduation-Thesis-Management-System
- 作者: coniern

