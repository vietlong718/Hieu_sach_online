# Spring boot, MongoDB, Spring security, JWT, RESTFULL APi

Build Restful API for a card-manage project using Spring Boot, MongoDB

## Steps to Setup

**1. Clone the application**
```bash
git clone https://github.com/NVD-NVD/book-store-online-be.git
```

**2. Run app using Maven**
```bash
mvn spring:boot
```

The app will start running at <http://localhost:8080>

## Explore Restfull API

The app defines following CRUD APIs.

### Authentication
| Method | Url | Description | Sample Valid Request Body |
|--------|-----|-------------|---------------------------|
| POST   | /rest/auth/signup | Sign Up| [JSON](#signup)|
| POST   | /rest/auth/login  | Login| [JSON](#login)
| POST   | /rest/auth/login/google | Login by Google account| [JSON](#logingoogle)   |

### Admin
| Method | Url | Description | Sample Valid Request Body |
|--------|-----|-------------|---------------------------|
| GET    | /rest/user/paging | Get all users with pagination for admin | [JSON](#userpaging) |
| GET    | /rest/user/all    | Get all users for admin | [JSON](#alluser) |
| POST   | /rest/user        | Create new user (used by admin) | [JSON](#postuser) |
| POST   | /rest/user/createnewadmin | Create a new admin account | [JSON](#creatanewadmin) |
| DELETE | /rest/user/{id} | Admin change active account status | [JSON](#deleteuser) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |

### User
| Method | Url | Description | Sample Valid Request Body |
|--------|-----|-------------|---------------------------|
| GET    | /rest/user | Get user info by token | [JSON](#getuser) |
| GET    | /rest/user/{id} | Get user info by id | [JSON](#getuserid) |
| PUT    | /rest/user{id} | User update or change information | [JSON](#putuser) |
| PUT    | /rest/user/avatar/{id} | User change avatar | [JSON](#putavatar) |
| PUT    | /rest/user/password/{id} | User change password | [JSON](#putpassword) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |

### GUEST
| Method | Url | Description | Sample Valid Request Body |
|--------|-----|-------------|---------------------------|
| GET    | /rest/book/paging | Get all book | [JSON](#bookpaging) |
| GET    | /rest/book/{id}   | Get book information| [JSON](#bookinfo) |
| GET    | /rest/book/search| Search book | [JSON](#searchbook) |
| GET    | /rest/category    | Get all category | [JSON](#) |
| GET    | /rest/category/paging    | Get all category | [JSON](#categorypaging) |
| GET    | /rest| | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |
| | | | [JSON](#) |




Test them using postman or any other rest client.
## Sample Valid JSON Request Body

#### <a id="signup"> Sign Up -> /rest/auth/signup</a>
```json
{
  "email": "demo@gmail.com",
  "password": "12345678",
  "firstname": "Zero",
  "lastname": "Zero",
  "gender": "MALE",
  "birthday": "1/1/2020",
  "phone": "0123456789",
  "address": "00, Zero, ZERO"
}
```

#### <a id="login"> Login -> /rest/auth/Login</a>
```json
{
  "email" : "demo@gamil.com",
  "password" : "12345678"
}
```
### <a id="logingoogle"> Login with Google account-> /rest/auth/login/google </a>
```json

```
### 