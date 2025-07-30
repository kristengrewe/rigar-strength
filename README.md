# 🏋️ Rigar Strength Workout Tracker App

A web-based workout tracking application designed for Strength & Conditioning programs. This app enables athletes to log their barbell lifts and workouts, while providing coaches and gym owners with tools to monitor member engagement, progress, and program effectiveness.


## 🛠️ Tech Stack

| **Backend**      | **Frontend**   | **Tooling & DevOps**   |
|------------------|----------------|-------------------------|
| Java             | Vue.js         | Git                     |
| Spring Boot      | JavaScript     | IntelliJ IDEA           |
| JDBC             | HTML           | Postman                 |
| SQL              | CSS            | JUnit                   |
| PostgreSQL       |                | Agile methodology       |
| RESTful APIs     |                |                         |


  

## 🚀 MVP Functional Requirements

### Anonymous Users Can:
- Register with a username and password
- Login with credentials
- View a list of barbell lifts
- Select light or dark mode for the application view

### Authenticated **User** Role Can:
- View a list of all personal workouts
- Create, update, and delete personal workouts
- Filter personal workouts by:
  - Specific date or date range
  - Specific lift

### Authenticated **Admin** Role Can:
- View a list of all registered members
- View a list of all member workouts
- Filter member workouts by:
  - Specific date or date range
  - Specific lift
  - Member last name



## 🗃️ Database Design

- **Users Table** (with role-based access control: USER, ADMIN)
- **Person Table** (member details)
- **Lift Table** (standard barbell movements)
- **Person_Lift Table** (join table for person and lift, with detailed workout entries)
<img width="618" height="391" alt="ERD Diagram" src="https://github.com/user-attachments/assets/d9de0190-a8c2-4dc7-9f48-d0b101931a9f" />


## 🧩 API Endpoint Design

| Endpoint                | Method | Query Params                                 | Description                                                                 | Success Code | Error Code(s)         | 🔐 Authentication        |
|-------------------------|--------|----------------------------------------------|-----------------------------------------------------------------------------|--------------|------------------------|---------------------------|
| `/api/register`         | POST   | –                                            | Register a username and password                                            | `201 Created`| `400 Bad Request`, `422 Unprocessable Entity` | ❌ None                 |
| `/api/login`            | POST   | –                                            | Login with username and password                                            | `201 Created`| `401 Unauthorized`, `422 Unprocessable Entity` | ❌ None                 |
| `/api/lifts`            | GET    | –                                            | Get list of all barbell lifts                                               | `200 OK`     | `404 Not Found`         | ❌ None                 |
| `/api/workouts`         | GET    | `lastName`, `date`, `dateRange`, `lift`      | Get all member workouts<br/>*(filter optional by user or lift)*<br/>*(admin can filter by last name)* | `200 OK`     | `403 Forbidden`, `404 Not Found` | ✅ USER / ✅ ADMIN       |
| `/api/workouts`         | POST   | –                                            | Create a workout (authenticated user)                                       | `201 Created`| `401 Unauthorized`, `404 Not Found` | ✅ USER               |
| `/api/workouts/{id}`    | PUT    | –                                            | Update a workout (authenticated user)                                       | `201 Created`| `401 Unauthorized`, `404 Not Found` | ✅ USER               |
| `/api/workouts/{id}`    | DELETE | –                                            | Delete a workout (authenticated user)                                       | `204 No Content` | `402 Payment Required`, `404 Not Found` | ✅ USER         |
| `/api/members`          | GET    | –                                            | View list of members                                                        | `200 OK`     | `403 Forbidden`, `404 Not Found` | ✅ ADMIN              |



## 📸 Screenshots of Application

### 🔐 Login View
<p float="left">
  <img src="https://github.com/user-attachments/assets/98f34344-b711-457c-8979-3cf1e011b155" alt="LoginView - Mobile - Light Mode" width="200" />
</p>

### 🆕 Register View
<p float="left">
  <img src="https://github.com/user-attachments/assets/17712649-74b3-4227-8734-fa66a0db65b5" alt="RegisterView - Desktop - Dark Mode" width="400" />
</p>

### 🏠 Home View
<p float="left">
  <img src="https://github.com/user-attachments/assets/e9fbb086-c0e5-47a6-8f21-961643077327" alt="HomeView - Desktop - Light Mode" width="400" />
  <img src="https://github.com/user-attachments/assets/ccb90f53-5510-42d2-aacd-21fe3889d4da" alt="HomeView - Desktop - Dark Mode" width="400" />
  <img src="https://github.com/user-attachments/assets/b4ae9109-da82-4c4a-947d-620d2bbc6dd9" alt="HomeView - Mobile - Light Mode" width="200" />
</p>

### 🏋️ Workout List View
<p float="left">
  <img src="https://github.com/user-attachments/assets/b97174fb-9072-4f11-b497-969fd6b884a9" alt="WorkoutListView - Desktop - Dark Mode - Admin" width="400" />
  <img src="https://github.com/user-attachments/assets/319b49da-3ec8-4d5a-9102-b7e2429ba619" alt="WorkoutListView - Desktop - Light Mode - Admin" width="400" />
</p>
<p float="left">
  <img src="https://github.com/user-attachments/assets/80e5bc04-b0b9-490d-b0cf-771ca5ba6472" alt="WorkoutListView - Mobile - Dark Mode - Admin" width="200" />
</p>

### 📝 Create Workout View
<p float="left">
  <img src="https://github.com/user-attachments/assets/f686805d-5727-4953-bb64-a1ab8a7b4d4d" alt="CreateWorkoutView - Desktop - Dark Mode" width="400" />
</p>

### 🔄 Update Workout View
<p float="left">
  <img src="https://github.com/user-attachments/assets/8d4159fc-8785-4ab5-846e-463f5bac7be4" alt="UpdateWorkoutView - Desktop - Light Mode" width="400" />
</p>

### 👥 Member View
<p float="left">
  <img src="https://github.com/user-attachments/assets/11825f89-545c-4a62-bf7a-ef1676874523" alt="MemberView - Desktop - Dark Mode" width="400" />
</p>


