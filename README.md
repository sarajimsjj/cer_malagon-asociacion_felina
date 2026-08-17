# 🐱 CER Malagón — Feline Adoption Platform

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

* Local file storage for photos and videos, abstracted behind a single component so it can be swapped for a cloud provider (e.g. Amazon S3) without touching the rest of the application — see [Roadmap](#-roadmap)

### Tools

* Git & GitHub
* IntelliJ IDEA
* Visual Studio Code
* Maven

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
| PostgreSQL|  |  Local file   |
|  Database |  |    storage    |
+-----------+  +---------------+
```

## 🔐 Security

Authentication and authorization are handled on the backend using Spring Security with JSON Web Tokens.

Two administrator roles determine which actions are available: the **principal** administrator can add or edit cats, manage their photos/videos, and invite new administrators, while **standard** administrators can only view and manage adoption requests. All role checks are enforced on the backend, not just hidden in the interface.

Sensitive configuration such as database credentials and the JWT signing secret is handled through environment variables rather than being stored directly in the source code.

## 📸 Image & Video Management

Each cat can have multiple photos and videos, with one photo designated as the main image.

The main photo is used throughout the application as the cat's primary representation, both in the cat listing and as the cover image on its profile.

Media files are currently stored on the server's local disk. The storage logic lives behind a single component, so migrating to a cloud provider later only requires changing that one piece — the rest of the application only ever deals with a URL.

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

🚧 **In development**

This project is actively being developed and new features and improvements are being added progressively.
