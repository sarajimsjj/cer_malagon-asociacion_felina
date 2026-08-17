# 🐱 CER Malagón — Feline Adoption Platform

https://cer-malagon-asociacion.netlify.app/

A full-stack web application developed for **CER Malagón**, a feline association, to simplify the management of cats in their care and facilitate the adoption process.

The platform provides administrators with tools to manage cat profiles, health information, photos/videos and adoption status, while offering potential adopters a clear and user-friendly way to browse cats and submit adoption requests.

## 🚀 Features

* 🐱 Cat profiles with detailed information and adoption status
* 📸 Photo and video galleries per cat, with one photo designated as the main image
* 🏠 Adoption status management (available, reserved, adopted, under treatment, urgent) and adoption request handling
* 🔐 Secure authentication with two administrator roles: a **principal** administrator with full access, and **standard** administrators limited to reviewing adoption requests
* 🩺 Health and veterinary information (spaying/neutering, deworming, vaccination, FIV/FeLV test results)
* 📋 Public cat listing, sorted by adoption priority (urgent cases first)
* 🎨 Responsive interface that adapts to mobile screens
* 🔒 Secure handling of user and application data

## 🛠️ Tech Stack

### Frontend

* React
* JavaScript
* HTML5
* CSS3

### Backend

* Java
* Spring Boot
* Spring Security
* REST API

### Database

* PostgreSQL

### Storage

* Amazon S3 for photo and video storage, accessed through the official AWS SDK for Java
* Storage logic isolated behind a single component, so the provider could be swapped later without touching the rest of the application

### Deployment

* **Netlify** — frontend, auto-deployed from the `main` branch on every push
* **Render** — backend, built and run from a multi-stage Dockerfile
* **Neon** — managed PostgreSQL database
* Environment-based configuration throughout (database URL, JWT secret, AWS credentials, allowed CORS origins), so the same codebase runs locally and in production without changes

### Tools

* Git & GitHub
* IntelliJ IDEA
* Visual Studio Code
* Maven
* Docker

### AI-Assisted Development

AI tools were integrated into the development workflow as a coding and problem-solving assistant.

They were used for tasks such as exploring implementation approaches, debugging, refactoring, generating ideas and improving development efficiency.

The architecture, technical decisions and final implementation were reviewed and adapted throughout the development process to ensure that the application met the project's functional and technical requirements.

## 🎯 Project Goals

The main goal of the project is to provide CER Malagón with a digital platform that makes it easier to manage cats available for adoption and present their information to potential adopters.

The project also serves as an opportunity to apply full-stack development practices in a real-world context, including:

* Separation of frontend and backend responsibilities
* RESTful API design
* Authentication and role-based authorization
* Relational database design
* File storage design that stays decoupled from the storage provider
* Deploying a multi-service architecture (separate hosts for frontend, backend, database and file storage) with environment-based configuration
* Responsive UI development
* Component-based frontend architecture
* Maintainable and scalable code

## 🏗️ Architecture

The application follows a separated frontend/backend architecture:

```
   +------------------+
   |  React Frontend  |
   +--------+---------+
            |
            | REST API
            v
   +------------------+
   | Spring Boot API  |
   +--------+---------+
            |
      +-----+-----+
      |           |
      v           v
+-----------+  +---------------+
| PostgreSQL|  |   Amazon S3   |
|  Database |  | (photos/video)|
+-----------+  +---------------+
```

In production, the React frontend is hosted on Netlify, the Spring Boot backend runs as a Docker container on Render, the database is a managed PostgreSQL instance on Neon, and media files live in an Amazon S3 bucket. Each piece is configured entirely through environment variables, so the same codebase runs locally and in production unchanged.

## 🔐 Security

Authentication and authorization are handled on the backend using Spring Security with JSON Web Tokens.

Two administrator roles determine which actions are available: the **principal** administrator can add or edit cats, manage their photos/videos, and invite new administrators, while **standard** administrators can only view and manage adoption requests. All role checks are enforced on the backend, not just hidden in the interface.

Sensitive configuration — database credentials, the JWT signing secret, and AWS credentials — is handled through environment variables rather than being stored directly in the source code. AWS credentials in particular are read by the AWS SDK straight from the environment; they never pass through application code or get logged.

## 📸 Image & Video Management

Each cat can have multiple photos and videos, with one photo designated as the main image.

The main photo is used throughout the application as the cat's primary representation, both in the cat listing and as the cover image on its profile.

Media files are stored in a dedicated Amazon S3 bucket, uploaded and deleted through a single component so the rest of the application only ever deals with a URL — never with where the file actually lives.

The bucket is configured so anyone can *read* an object (needed for a public adoption site), but only the backend can *write* to it: uploading and deleting require the credentials of a dedicated IAM user, scoped to just `PutObject`/`DeleteObject` on that one bucket, which only the backend holds. On top of that, the backend itself only allows the principal administrator to trigger an upload or deletion in the first place.

## 🎨 UX/UI

The interface has been designed with a focus on clarity and ease of use, adapting to both desktop and mobile screens.

The application aims to make important information about each cat immediately understandable, particularly adoption status and health-related information.

The design and user experience have been developed with the needs of both association administrators and potential adopters in mind.

## 🧠 Technical Challenges

Some of the main technical challenges addressed in the project include:

* Designing the relationship between cats, photos/videos and adoption requests
* Implementing role-based authentication and authorization, enforced consistently on the backend
* Managing file uploads (images and videos) with validation by actual content type
* Selecting and switching the main photo for each cat without leaving it briefly unset or duplicated
* Keeping frontend and backend responsibilities clearly separated
* Designing reusable React components
* Handling different cat statuses and health information consistently

## 📌 Project Status

🚀 **Deployed** — frontend on Netlify, backend on Render, database on Neon, media storage on Amazon S3.

The project is live and still actively developed, with new features and improvements added progressively.
