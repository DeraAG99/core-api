## 📂 Project Structure

```mermaid
graph TD
  A[Core]

  A --> B[api-gateway<br/>Spring Cloud Gateway<br/>API routing & security]

  A --> C[api-service<br/>Backend API services]
  C --> D[admin<br/>API key, IP whitelist,<br/>scope & rate limit management]
  C --> E[auth<br/>JWT-based authentication & authorization]

  A --> F[shared<br/>Common DTOs, utilities,<br/>and constants]

  A --> G[web-service<br/>Web-based applications]
  G --> H[admin-dashboard<br/>Next.js admin dashboard frontend]

  A --> I[worker<br/>Background worker services]
  I --> J[ConsumerJob<br/>Async jobs (e.g., PDF downloader via RabbitMQ)]
