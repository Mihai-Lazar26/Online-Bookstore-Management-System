Online Bookstore Management System
Business Requirements:
1. User Authentication and Authorization
- Users should be able to register, log in, and log out securely.
- Admins should have elevated access for managing books and users.
2. Book Catalog Management
- Users can view a list of available books with details (title, author, price, etc.).
- Admins can add, update, and delete books from the catalog.
3. Shopping Cart Functionality
- Users should be able to add books to their shopping cart and proceed to checkout.
- The system should calculate the total price and handle the purchase.
4. Order Tracking
- Users can track the status of their orders (pending, shipped, delivered, etc.).
- Admins can manage and update order statuses.
5. Review and Rating System
- Users can rate and leave reviews for books they have purchased.
- The system should display average ratings for each book.
6. Book Genre Filters
- Description: Implement filters to categorize books by genres (e.g., fiction, non-fiction, mystery) for easy browsing.
- Goal: Allow users to quickly find books based on their preferred genres, enhancing user experience.
7. User Profile Customization
- Description: Enable users to customize their profiles by adding avatars, setting preferences, or saving favorites.
- Goal: Provide users with a personalized experience and improve engagement on the platform.
Project realized by Lazăr Mihai, group 406
8. Discounts and Promotions
- Description: Implement promotional discounts or coupon codes for special occasions or selected books.
- Goal: Encourage sales by offering discounts, attracting users with special offers and promotions.
9. Simple Search Functionality
- Description: Develop a basic search feature allowing users to search for books by title or author name.
- Goal: Facilitate easy book discovery and navigation within the bookstore.
10. User Feedback Collection
- Description: Implement a feedback system to gather user opinions or suggestions for improvement.
- Goal: Gather valuable insights directly from users to enhance the platform's usability and features.
Main Features for MVP Phase:
1. User Authentication
- Description: Allows users to register and log in securely.
- Endpoint: `/api/auth/register`, `/api/auth/login`
2. Book Catalog Management
- Description: Provides CRUD operations for managing books.
- Endpoints: `/api/books`, `/api/books/{id}`
3. Shopping Cart Functionality
- Description: Enables users to add/remove books from the cart and checkout.
- Endpoints: `/api/cart/add`, `/api/cart/remove`, `/api/cart/checkout`
4. Order Management
- Description: Tracks order status and allows admin to update status.
- Endpoints: `/api/orders`, `/api/orders/{id}/status`
5. Review and Rating System
- Description: Allows users to rate and review purchased books.
- Endpoints: `/api/books/{id}/reviews`, `/api/books/{id}/ratings
