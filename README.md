# Finance API

Internal Company Information Management Application

**Group Members:**

|  #  |                                                  Avatar                                                   | Student ID | Full Name             |                     GitHub                      |
| :-: | :-------------------------------------------------------------------------------------------------------: | :--------: | --------------------- | :---------------------------------------------: |
|  1  |     <img src="https://github.com/kaytervn.png" alt="kaytervn" width="50" style="border-radius: 50%;">     |  21110332  | Kiến Đức Trọng        |     [kaytervn](https://github.com/kaytervn)     |
|  2  | <img src="https://github.com/vantrung1109.png" alt="vantrung1109" width="50" style="border-radius: 50%;"> |  21110335  | Nguyễn Trần Văn Trung | [vantrung1109](https://github.com/vantrung1109) |
|  3  | <img src="https://github.com/cong09112003.png" alt="cong09112003" width="50" style="border-radius: 50%;"> |  21110144  | Nguyễn Hữu Công       | [cong09112003](https://github.com/cong09112003) |

# References

### 📄 **API Documentation**

- [Swagger UI](https://api-fm.itz.io.vn/swagger-ui.html)

### 🗂 **Database Diagram**

- [DBDiagram.IO](https://dbdiagram.io/d/Finance-DB-Diagram-6690cbb89939893daebc90f3)

### 🎨 **Figma Design**

- [Figma](https://www.figma.com/design/71tcv3PbOGRxnFtnREKFtu/Finance-Apps)

### 🖌️ **UML Diagram**

- [Draw.IO Viewer](https://viewer.diagrams.net/?tags=%7B%7D&lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&title=FinanceDiagram.drawio#Uhttps%3A%2F%2Fdrive.google.com%2Fuc%3Fid%3D1U1ZYZVYmSbV8B_n3PcLM_Cik31FPRzr3%26export%3Ddownload)

### GitHub Repositories

### 📌 **Web - CMS**

- **Repository:** [GitHub Repository](https://github.com/ITZ-Developers/Finance-CMS)
- **Live Demo:** [CMS Application Link](https://cms-fm.itz.io.vn/)

### 📌 **Mobile - Android**

- **Repository:** [GitHub Repository](https://github.com/ITZ-Developers/Finance-Android)
- **APK File:** [Android Mobile App Link](https://drive.google.com/file/d/1L0VeKf0NoXQzaMzMCwHJHHRCbs5npqN5)

---

# Project Setup and Installation

## Requirements

- Java 11 or higher
- Maven
- MySQL
- IntelliJ IDEA

## Installation

### 1. Clone the Repository

```bash
git clone <HTTP-Git-URL>
```

### 2. Set Up Environment Variables

Update `application-dev.properties` for:

- **Database** connection
- Email **SMTP** settings
- **JWT** signing key
- Server **PORT**

### 3. Configure IntelliJ IDEA

Go to **File** → **Project Structure** → **SDK**, set to **Java 11**

### 4. Prepare Database

Ensure the database is created before running the app

### 5. Build & Run

```bash
mvn clean install
mvn spring-boot:run
```
