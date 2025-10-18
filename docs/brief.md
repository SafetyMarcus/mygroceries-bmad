# Project Brief: My Groceries

## Executive Summary

"My Groceries" is a data visualization tool that allows users to analyze their spending on grocery products and categories. Users can import their own data to visualize key spending habits, such as categories with the highest expenditure. The primary problem this solves is the lack of insight individuals have into their grocery spending patterns. The target market is any individual who wishes to track and understand their grocery expenses. The key value proposition is the tool's ability to empower users by allowing them to use their own data to gain personalized financial insights.

## Problem Statement

In the current economic climate of rising living costs, individuals face significant challenges in managing their grocery expenditures. A key contributor to this difficulty is the inconsistent and non-granular data provided by major grocery retailers in Australia. Product categorizations are often erratic, and detailed spending insights are not readily available to consumers.

This lack of clear, actionable data leads to several negative impacts:
*   **Increased Financial Stress:** Individuals experience anxiety and worry due to the uncertainty surrounding their grocery spending.
*   **Missed Savings Opportunities:** Without a clear understanding of where their money is going, consumers are unable to identify areas where they can cut costs and adjust their purchasing habits effectively.
*   **Ineffective Budgeting:** Existing budgeting tools and manual methods like spreadsheets are often rendered ineffective because the source data from retailers is poor, making it difficult to categorize expenses accurately and gain meaningful insights.

The urgency of this problem is underscored by the increasing financial pressure on households. Providing a tool to bring clarity to grocery spending is no longer a convenience but a necessity for effective household financial management.

## Proposed Solution

"My Groceries" will be a data visualization application designed to run locally on a user's machine. The core concept is to provide users with a suite of powerful, graphed visualizations that break down their grocery spending across multiple dimensions. Users will be to ingest their own transaction data, which the application will analyze to show:

*   Total and average spend per shopping trip.
*   Total and average spend per product category.
*   Total and average spend per individual product.
*   The products with the highest overall spend.

The initial release will be a local-first application, where a server runs on the user's machine, hosts all their data, and serves the web-based front-end. This ensures user privacy and data ownership from day one.

The key differentiator is this "bring-your-own-data" and local-first approach. Unlike other budgeting apps that may rely on unreliable bank feeds or retailer APIs, "My Groceries" empowers users to be the source of truth, bypassing the issue of inconsistent data from external sources.

This solution is poised for success because it directly addresses the core problem—lack of clear data—by putting the user in control. The high-level vision is to evolve this local web application into a cross-platform tool available on both web and mobile, providing users with access to their spending insights wherever they are.

## Target Users

### Primary User Segment: The Budget-Conscious Australian Shopper

*   **Profile:** This segment consists of budget-conscious individuals and families across Australia who are actively seeking to become more financially aware. They are regular shoppers at major supermarkets like Woolworths and Coles. While their technical skills may vary, they are motivated to use tools that can help them manage their finances.
*   **Current Behaviors:** They may currently be using a mix of methods to track spending, from manual spreadsheet entries to general-purpose budgeting apps. However, they consistently struggle to get a clear, detailed picture of their grocery expenses due to the poor quality of data from their shopping providers.
*   **Needs and Pain Points:** Their primary need is for clarity and control over their grocery budget. Their main pain point is the inability to easily see where their money is going within specific grocery categories (e.g., fresh produce, snacks, cleaning supplies), which prevents them from making informed decisions to reduce costs.
*   **Goals:** Their main goal is to gain actionable insights into their spending habits to reduce waste, save money, and feel more in control of their finances amidst the rising cost of living.

## Goals & Success Metrics

### Business Objectives
*   **Validate Core Functionality with a Founder-User:** For the initial release, the primary business objective is to build a functional application that allows the founder to personally use it to gain insights into their own grocery spending. Success is defined by the founder being able to successfully analyze their data and validate the tool's core value proposition.

### User Success Metrics
*   **Rapid Insight into Top Spending:** The user can quickly and easily identify their most expensive grocery categories and individual products without significant manual effort.
*   **Clear Visualization of Spending Trends:** The user can clearly see how their spending in specific categories or on specific products has changed over time.

### Key Performance Indicators (KPIs)
*   **Time to Insight:** Less than 1 minute from launching the app to identifying the top 3 spending categories.
*   **Setup Time:** Less than 5 minutes to import a new data file and see the visualizations update.
*   **Bugs per Session:** Fewer than 1 critical bug encountered per user session.

## MVP Scope

### Core Features (Must Have)
*   **Data Model & Import:** Ability to import user data structured around **Orders**.
    *   An **Order** contains a date, a total cost, and a list of items.
    *   An **Item** links a **Product** with its cost and quantity for that order.
    *   A **Product** has a name and belongs to a **Category**.
*   **Main Dashboard:** The primary application view will display:
    *   A graph of individual **Order** totals over time.
    *   A list of all **Categories**, ranked by their total spend across all time.
*   **Category Drill-Down View:** When a user selects a **Category**, they will see:
    *   A graph showing the total spend for that **Category** within each order over time.
    *   A list of all **Products** within that category, ranked by their total spend.
*   **Product Drill-Down View:** When a user selects a **Product**, they will see a read-only view containing:
    *   A graph showing the individual cost of that **Product** in each order over time.
    *   A list of all **Orders** in which the product was purchased.

### Out of Scope for MVP
*   **User Accounts:** There will be no user registration, login, or multi-user functionality.
*   **Data Modification:** The application will only visualize data. There will be no features for editing or deleting imported data from within the app.
*   **Multiple Data Formats:** The initial version will only support a single, predefined data import format. It will not support different formats from various supermarkets.
*   **Predictive Features:** There will be no AI-powered suggestions, spending predictions, or automated insights.

### MVP Success Criteria
The MVP will be considered a success if the founder-user can import their own grocery data and use the defined dashboard, category, and product views to successfully identify their main areas of spending and track spending trends over time, thereby validating the core value proposition of the tool.

## Post-MVP Vision

### Phase 2 Features
*   **Automated Data Import:** Introduce the ability for users to connect directly to their grocery store accounts (e.g., Coles) for automated scraping and formatting of their shopping data.
*   **Data Management & Customization:** Implement features allowing users to edit or delete imported data and create their own custom product categories.
*   **Smart Budgeting Assistance:** Introduce a feature that provides a "suggested ordering schedule" to help users balance their budget by spreading high-cost item purchases over time.

### Long-term Vision
The long-term vision is to become the indispensable tool for managing and optimizing a household's grocery budget. The application will evolve from a reactive analysis tool to a proactive shopping assistant that helps users decide where to shop and what to buy by suggesting the most cost-effective stores and alternative products.

### Expansion Opportunities
*   **Smart Home Integration:** Integrate with platforms like Home Assistant to connect with smart home devices.
*   **Premium Hosted Version:** Offer a paid, fully-hosted version as a premium option, while keeping the self-hosted version free.

## Technical Considerations

### Platform Requirements
*   **Target Platforms:** The application will be built for Web Browsers, Android, and iOS from a single codebase.
*   **Browser/OS Support:** The application will support the last two major OS versions for both Android and iOS. For the web, it will support modern, evergreen browsers.
*   **Performance & UI:** The application should feel responsive and performant on all target platforms and adhere to a modern Material Design aesthetic.

### Technology Preferences
*   **Core Framework:** The entire project will be developed in a single, shared Kotlin codebase using Kotlin Multiplatform.
*   **Frontend:** The UI for all platforms will be built with Compose for Multiplatform, utilizing modern Material Design components.
*   **Backend & Hosting:** For the MVP, the backend server logic will be part of the same Kotlin Multiplatform project and will run locally on the user's machine.

### Architecture Considerations
*   **Repository Structure:** The project will be a single monorepo following the standard Kotlin Multiplatform structure.
*   **Client-Server Communication:** Ktor will be used for both the local server and the clients.
*   **Data Storage:** Data will be stored in a local PostgreSQL database.
*   **Security:** Data will be kept within the application's sandboxed directory on the host OS.

## Constraints & Assumptions

### Constraints
*   **Budget:** There is no formal budget allocated for the MVP.
*   **Timeline:** As a personal project, the timeline is flexible with no fixed deadline.
*   **Resources:** The project will be undertaken by a sole developer.

### Validated Points
*   **Core Data Pipeline:** The technical approach for the application itself has been validated. It is confirmed that the system can serve data from a local PostgreSQL database via a Ktor server to a Kotlin Multiplatform client.

### Key Assumptions
*   **Data Source Pre-processing:** We assume that the external data-sourcing process (scraping and cleaning) can effectively use a Large Language Model (LLM) to turn inconsistent product names into meaningful, consistent categories *before* the data is ingested into this application. The quality of the insights provided by this application is directly dependent on the success of this external pre-processing step.
*   **Scraping Viability:** We assume that the external data scraping method will remain viable and will not be blocked or significantly altered by the retailer.

## Risks & Open Questions

### Key Risks
*   **Data Source Brittleness:** The external scraping script could break if the retailer changes its website, halting the flow of new data.
*   **Poor Categorization Quality:** The external LLM process might fail to produce accurate or meaningful categories, leading to useless insights in the application.
*   **User Onboarding Complexity:** The requirement to set up a local PostgreSQL database might be a significant technical barrier for future users.

### Open Questions
*   **LLM Choice and Cost:** Which specific LLM will be used for the external categorization process, and what are the potential costs and rate limits?
*   **Database Setup Automation:** How can the PostgreSQL setup be simplified for future users? Should we consider containers (Docker) or a simpler embedded database (SQLite)?
*   **JSON Ingestion Schema Definition:** What is the exact JSON schema for the data the application will ingest? This needs to be strictly defined.

### Areas Needing Further Research
*   **LLM Categorization Performance:** Research and benchmark different LLMs and prompting strategies to find the most accurate and cost-effective solution for product categorization.
*   **Database Distribution Strategy:** Investigate the best cross-platform methods for bundling and managing a local PostgreSQL instance, or evaluate alternatives like SQLite for simplicity.

## Next Steps

### Immediate Actions
1.  **Define JSON Schema:** Formally document the exact JSON schema for the data the application will ingest.

### PM Handoff
This Project Brief provides the full context for My Groceries. Please start in 'PRD Generation Mode', review the brief thoroughly to work with the user to create the PRD section by section as the template indicates, asking for any necessary clarification or suggesting improvements.