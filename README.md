## 📂 Project Structure

```mermaid
graph TD
  A[Core]

  A --> B[api-gateway | Spring Cloud Gateway - API routing & security]

  A --> C[api-service | Backend API services]
  C --> D[admin | API key, IP whitelist, scope & rate limit management]
  C --> E[auth | JWT-based authentication & authorization]

  A --> F[shared | Common DTOs, utilities, and constants]

  A --> G[web-service | Web-based applications]
  G --> H[admin-dashboard | Next.js admin dashboard frontend]

  A --> I[worker | Background worker services]
  I --> J[ConsumerJob | Async jobs - PDF downloader via RabbitMQ]
