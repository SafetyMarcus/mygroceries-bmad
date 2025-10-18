# 10. Security

For the MVP, security considerations are minimal due to the local-first nature of the application.

*   **Data at Rest:** Data is stored in a local PostgreSQL database on the user's machine. The security of this data is dependent on the user's own machine security. No encryption at rest will be implemented for the MVP.
*   **Data in Transit:** Communication is between a local client and a local server. While unencrypted HTTP is acceptable for this loopback communication, all external network traffic (if any is added in the future) MUST use HTTPS.
*   **Input Validation:** The server MUST perform validation on all incoming API request data to ensure data integrity and prevent issues like SQL injection (though mitigated by using a framework like Exposed). This is specified in the Acceptance Criteria for the API stories.
*   **Secrets Management:** N/A for MVP, as there are no external services requiring API keys.