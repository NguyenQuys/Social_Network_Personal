# Social Network

![SocialNetworkApp Logo](/favicon.ico)

## Description
**SocialNetworkApp** is a social networking platform that allows users to create profiles, connect with friends, post updates, and interact through comments and likes. Built with Spring Boot and MySQL, this application provides a robust backend with a scalable database solution.

## Features
- **User Registration and Authentication:** Secure registration and login functionality with JWT-based authentication.

### Roles:
1. **Users:**
   - **Profile Management:** Users can create, update, and view their profiles.
   - **Friend System:** Send and accept friend requests, view friend lists.
   - **Posts and Comments:** Create posts, comment on posts, and like posts.
   - **Notifications:** Get notified about friend requests, comments, and likes.
   - **Search Functionality:** Search for users and posts.
   - **Report Post:** Users can report posts to admins.
   - **Admin of Groups:** Censor members can join and review posts before they are published.

2. **Admins:**
   - **Review Reports:** Admins can review reports from users and decide to delete or warn users as necessary.
   - **See Statistics:** Admins can view statistics such as the number of posts made and the number of visitors per day over the past 7 days.
   - **Block Users:** Admins can block users from logging in.

### Exited account:
**For users**
1. * Username: quy
2. * Username: nam
3. * Username: viet

**For Admins**
Username: admin

Password for all: 123456Vv

## Installation

### Prerequisites
- [JDK 22](https://jdk.java.net/22) or higher
- [MySQL](https://dev.mysql.com/downloads/)
- [Maven](https://maven.apache.org/)
- [Git](https://github.com/NguyenQuys/Social_Network_Personal)

### Steps
1. **Clone the Repository:**
   ```bash
   git clone https://github.com/NguyenQuys/Social_Network_Personal


2. **Set Up MySQL**
- Create a database named "test".

3. **Import the Database Schema**
- Use the "social_network.sql" file to import the initial data.

4. **Run the Application**
- Navigate to the cloned directory and run the Spring Boot application.

5. **Access the Application"** 
- Open your browser and go to http://localhost:8081 to experience the app.

**Contact:** You can contact me using the information provided on my CV.
