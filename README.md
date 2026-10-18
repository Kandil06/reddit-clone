# Reddit Clone (Spring Boot + React)

A full-stack Reddit-style app: communities (subreddits), posts, nested comments,
upvote/downvote, user profiles with karma, and search.

## Stack
- **Backend**: Spring Boot 3.3 (Java 17), Spring Security + JWT, Spring Data JPA, PostgreSQL
- **Frontend**: React 18 + Vite + React Router, plain CSS (no UI library)

## Project layout
```
reddit-clone/
  backend/    Spring Boot REST API (port 8080)
  frontend/   React SPA (port 5173)
  docker-compose.yml   PostgreSQL for local dev
```

## 1. Start the database
```bash
cd reddit-clone
docker compose up -d
```
This starts Postgres on `localhost:5432` with db `redditclone` / user `postgres` / password `postgres`
(matches `backend/src/main/resources/application.yml`). Edit that file if you use different credentials.

## 2. Run the backend
```bash
cd backend
./mvnw spring-boot:run
```
(No `mvnw` wrapper is included — run `mvn -N io.takari:maven:wrapper` once, or just use your local `mvn`.)
Tables are auto-created on first run (`spring.jpa.hibernate.ddl-auto: update`).

The API listens on `http://localhost:8080/api`. Key endpoints:

| Method | Path                              | Auth | Description                     |
|--------|------------------------------------|------|----------------------------------|
| POST   | /api/auth/register                | no   | Create account, returns JWT      |
| POST   | /api/auth/login                   | no   | Log in, returns JWT              |
| GET    | /api/communities                  | no   | List communities                 |
| POST   | /api/communities                  | yes  | Create a community               |
| POST   | /api/communities/{id}/toggle-membership | yes | Join/leave                 |
| GET    | /api/posts?page=&size=            | no   | Home feed, paginated             |
| GET    | /api/posts/community/{id}         | no   | Posts in a community             |
| POST   | /api/posts                        | yes  | Create a post                    |
| POST   | /api/posts/{id}/vote              | yes  | `{ "voteType": "UP" | "DOWN" }`  |
| GET    | /api/comments/post/{postId}       | no   | Threaded comments for a post     |
| POST   | /api/comments                     | yes  | Add a comment / reply            |
| POST   | /api/comments/{id}/vote           | yes  | Vote on a comment                |
| GET    | /api/users/{username}/profile     | no   | Public profile + karma           |
| GET    | /api/search?q=                    | no   | Search posts/communities/users   |

Send `Authorization: Bearer <token>` on authenticated requests.

## 3. Run the frontend
```bash
cd frontend
npm install
npm run dev
```
Open `http://localhost:5173`. It talks to `VITE_API_URL` (defaults to `http://localhost:8080/api`, see `frontend/.env`).

## Notes / things to harden before shipping
- JWT secret in `application.yml` is a placeholder — replace it and move it to an env var.
- Karma/profile stats currently load all posts/comments into memory to compute counts;
  fine for learning/demo scale, swap for a `SUM`/`COUNT` query before using real data.
- No rate limiting, no email verification, no image upload storage (imageUrl is just a string field).
- Add pagination to the comment thread and community list once they grow.
